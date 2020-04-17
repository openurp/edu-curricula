package org.openurp.edu.curricula.admin.web.action

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.model.util.Hierarchicals
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.curricula.model.CourseGroup

class CourseGroupAction extends RestfulAction[CourseGroup] {

	protected override def editSetting(courseGroup: CourseGroup): Unit = {

		var folders = Collections.newBuffer[CourseGroup]
		// 查找可以作为父节点的分组
		val folderBuilder = OqlBuilder.from(classOf[CourseGroup], "courseGroup")
		folderBuilder.orderBy("courseGroup.indexno")
		val rs = entityDao.search(folderBuilder)
		folders ++= rs
		courseGroup.parent foreach { p =>
			if (!folders.contains(p)) folders += p
		}
		folders --= Hierarchicals.getFamily(courseGroup)
		put("parents", folders)
	}

	protected override def removeAndRedirect(entities: Seq[CourseGroup]): View = {
		val parents = Collections.newBuffer[CourseGroup]
		for (courseGroup <- entities) {
			courseGroup.parent foreach { p =>
				p.children -= courseGroup
				parents += p
			}
		}
		entityDao.saveOrUpdate(parents)
		super.removeAndRedirect(entities)
	}


	protected override def saveAndRedirect(courseGroup: CourseGroup): View = {
		val newParentId = getInt("parent.id")
		val indexno = getInt("indexno", 0)
		var parent: CourseGroup = null
		if (newParentId.isDefined) parent = entityDao.get(classOf[CourseGroup], newParentId.get)
		move(courseGroup, parent, indexno)
		entityDao.evict(courseGroup)
		if (null != parent) {
			entityDao.evict(parent)
		}
		redirect("search", "info.save.success")
	}

	def move(courseGroup: CourseGroup, location: CourseGroup, index: Int): Unit = {
		courseGroup.parent foreach { p =>
			if (null == location || p != location) {
				courseGroup.parent = None
				entityDao.saveOrUpdate(courseGroup)
				entityDao.refresh(p)
			}
		}

		val nodes =
			if (null != location) {
				Hierarchicals.move(courseGroup, location, index)
			} else {
				val builder = OqlBuilder.from(classOf[CourseGroup], "courseGroup")
					.where("courseGroup.parent is null")
					.orderBy("courseGroup.indexno")
				Hierarchicals.move(courseGroup, entityDao.search(builder).toBuffer, index)
			}
		entityDao.saveOrUpdate(nodes)

		if (null != location) {
			entityDao.refresh(location)
		}
	}

}
