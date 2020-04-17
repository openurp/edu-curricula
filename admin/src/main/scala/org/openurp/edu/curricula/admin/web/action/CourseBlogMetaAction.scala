package org.openurp.edu.curricula.admin.web.action

import java.time.Instant

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.openurp.edu.base.code.model.CourseType
import org.openurp.edu.curricula.model.{CourseBlogMeta, CourseGroup}

class CourseBlogMetaAction extends AbstractAction[CourseBlogMeta] {

	override def indexSetting(): Unit = {
		put("departments", getDeparts)
		put("courseTypes", getCodes(classOf[CourseType]))
		var folders = Collections.newBuffer[CourseGroup]
		// 查找没有子节点的分组
		val folderBuilder = OqlBuilder.from(classOf[CourseGroup], "courseGroup")
		folderBuilder.orderBy("courseGroup.indexno")
		val courseGroups = entityDao.search(folderBuilder)
		courseGroups.foreach(courseGroup => {
			if (courseGroup.children.isEmpty) {
				folders += courseGroup
			}
		})
		put("courseGroups", folders)
		super.indexSetting()
	}

	def editGroup(): View = {
		put("metaIds", intIds("courseBlogMeta"))
		var folders = Collections.newBuffer[CourseGroup]
		// 查找没有子节点的分组
		val folderBuilder = OqlBuilder.from(classOf[CourseGroup], "courseGroup")
		folderBuilder.orderBy("courseGroup.indexno")
		val courseGroups = entityDao.search(folderBuilder)
		courseGroups.foreach(courseGroup => {
			if (courseGroup.children.isEmpty) {
				folders += courseGroup
			}
		})
		put("courseGroups", folders)
		forward()
	}

	def saveGroup(): View = {
		val metaIds = getInt("metaIds").toList
		val courseGroupMetas = entityDao.find(classOf[CourseBlogMeta], metaIds)
		getInt("courseGroup.id").foreach(courseGroupId => {
			courseGroupMetas.foreach(meta => {
				meta.author = getUser
				meta.updatedAt = Instant.now()
				meta.courseGroup = Option(entityDao.get(classOf[CourseGroup], courseGroupId))
			})
		})
		redirect("search", "info.save.success")
	}


}
