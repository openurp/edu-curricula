/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.edu.course.admin.web.action

import java.time.Instant

import org.beangle.commons.collection.{Collections, Order}
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.openurp.edu.base.code.model.CourseType
import org.openurp.edu.course.model.{CourseBlogMeta, CourseGroup}

class CourseBlogMetaAction extends AbstractAction[CourseBlogMeta] {

	override def indexSetting(): Unit = {
		//		put("courseTypes", getCodes(classOf[CourseType]))
		val metaBuilder = OqlBuilder.from(classOf[CourseBlogMeta].getName, "meta")
		metaBuilder.select("distinct meta.course.courseType")
		val courseTypes = entityDao.search(metaBuilder)
		put("courseTypes", courseTypes)
		val builder = OqlBuilder.from(classOf[CourseGroup], "courseGroup")
		builder.orderBy("courseGroup.indexno")
		put("courseGroups", entityDao.search(builder))
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
		put("metaIds", get("courseBlogMeta.id"))
		val builder = OqlBuilder.from(classOf[CourseGroup], "courseGroup")
		builder.orderBy("courseGroup.indexno")
		put("courseGroups", entityDao.search(builder))
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
