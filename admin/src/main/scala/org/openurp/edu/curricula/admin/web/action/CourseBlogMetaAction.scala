package org.openurp.edu.curricula.admin.web.action

import java.time.Instant

import org.beangle.commons.collection.{Collections, Order}
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.openurp.edu.base.code.model.CourseType
import org.openurp.edu.curricula.model.{CourseBlogMeta, CourseGroup}

class CourseBlogMetaAction extends AbstractAction[CourseBlogMeta] {

	override def indexSetting(): Unit = {
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

	override def getQueryBuilder: OqlBuilder[CourseBlogMeta] = {
		val builder = OqlBuilder.from(classOf[CourseBlogMeta], "courseBlogMeta")
		get("hasGroup").foreach(a => a match {
			case "0" => builder.where("courseBlogMeta.courseGroup is not null")
			case "1" => builder.where("courseBlogMeta.courseGroup is null")
			case _ =>
		})

		populateConditions(builder)
		get(Order.OrderStr) foreach { orderClause =>
			builder.orderBy(orderClause)
		}
		builder.tailOrder("courseBlogMeta.id")
		builder.limit(getPageLimit)
	}

	def editGroup(): View = {
		//		val a =intIds("courseBlogMeta")
		put("metaIds", get("courseBlogMeta.id"))
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
		val metaIdsString = get("metaIds")
		if (metaIdsString.isEmpty) {
			redirect("search", "error.parameters.needed")
		}
		else {
			val metaIds = Strings.splitToInt(metaIdsString.get)
			val courseGroupMetas = entityDao.find(classOf[CourseBlogMeta], metaIds)
			if (get("courseGroup.id").isEmpty || (!get("courseGroup.id").isEmpty && get("courseGroup.id").get == "")) {
				courseGroupMetas.foreach(meta => {
					meta.author = getUser
					meta.updatedAt = Instant.now()
					meta.courseGroup = None
				})
				entityDao.saveOrUpdate(courseGroupMetas)
			} else {
				getInt("courseGroup.id").foreach(courseGroupId => {
					courseGroupMetas.foreach(meta => {
						meta.author = getUser
						meta.updatedAt = Instant.now()
						meta.courseGroup = Option(entityDao.get(classOf[CourseGroup], courseGroupId))
					})
					entityDao.saveOrUpdate(courseGroupMetas)
				})
			}
			redirect("search", "info.save.success")
		}
	}


}
