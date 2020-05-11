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
package org.openurp.edu.course.index.web.action

import org.beangle.commons.collection.{Collections, Order}
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.model.Entity
import org.beangle.data.model.util.Hierarchicals
import org.beangle.webmvc.api.annotation.param
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.edu.base.web.ProjectSupport
import org.openurp.edu.course.model._


class IndexAction extends RestfulAction[CourseBlog] with ProjectSupport {

	override def indexSetting(): Unit = {
		// 没有子节点的分组
		var courseGroups = Collections.newBuffer[CourseGroup]
		val folderBuilder = OqlBuilder.from(classOf[CourseGroup], "courseGroup")
		folderBuilder.orderBy("courseGroup.indexno")
		val rs = entityDao.search(folderBuilder)
		rs.foreach(courseGroup => {
			if (courseGroup.parent.isEmpty) {
				courseGroups += courseGroup
			}
		})
		put("courseGroups", courseGroups)
		val courseBlogBuilder = OqlBuilder.from(classOf[CourseBlog].getName, "courseBlog")
		courseBlogBuilder.where("courseBlog.semester=:semester", getCurrentSemester)
		courseBlogBuilder.where("courseBlog.status =:status", BlogStatus.Published)
		courseBlogBuilder.select("distinct courseBlog.department")
		val departments = entityDao.search(courseBlogBuilder)
		put("departments", departments)
		getInt("courseGroup_child").foreach(childId => {
			val courseGroup_child = entityDao.get(classOf[CourseGroup], childId)
			put("courseGroup_children", courseGroup_child.parent.get.children)
			put("courseGroup_child_children", courseGroup_child.children)
			put("choosedCourseGroup", courseGroup_child.parent)
			put("choosedCourseGroup_child", courseGroup_child)
		})
		getInt("courseBlog.department.id").foreach(departmentId => {
			val department = entityDao.get(classOf[Department], departmentId)
			put("choosedDepartment", department)
		})
		super.indexSetting()
	}

	override def search(): View = {
		val builder = OqlBuilder.from(classOf[CourseBlog], "courseBlog")
		builder.where("courseBlog.semester=:semester", getCurrentSemester)
		builder.where("courseBlog.status =:status", BlogStatus.Published)
		get("nameOrCode").foreach(nameOrCode => {
			builder.where("(courseBlog.course.name like :name or courseBlog.course.code like :code)", '%' + nameOrCode + '%', '%' + nameOrCode + '%')
		})
		val first = getInt("courseGroup")
		val second = getInt("courseGroup_child")
		val third = getInt("courseGroup_child_child")
		var groups = Collections.newSet[CourseGroup]
		if (third != None && third != null) {
			groups ++= getCourseGroups(third.get)
		} else if (second != None && second != null) {
			groups ++= getCourseGroups(second.get)
		} else if (first != None && first != null) {
			groups ++= getCourseGroups(first.get)
		}
		if (!groups.isEmpty) {
			builder.where("courseBlog.meta.courseGroup in :groups", groups)
		}
		populateConditions(builder)
		builder.orderBy(get(Order.OrderStr).orNull)
		val courseblogs = entityDao.search(builder)
		put("courseBlogs", courseblogs)
		forward()
	}

	def getCourseGroups(id: Int): Set[CourseGroup] = {
		val courseGroup = entityDao.get(classOf[CourseGroup], id)
		Hierarchicals.getFamily(courseGroup)
	}

	def detail(@param("id") id: String): View = {
		val courseBlog = entityDao.get(classOf[CourseBlog], id.toLong)
		put("courseBlog", courseBlog)
		val courseBlogs = entityDao.findBy(classOf[CourseBlog], "course", List(courseBlog.course))
		put("courseBlogs", courseBlogs)
		val syllabuses = getDatas(classOf[Syllabus], courseBlog)
		if (!syllabuses.isEmpty) {
			put("syllabuses", syllabuses)
		}
		val lecturePlans = getDatas(classOf[LecturePlan], courseBlog)
		if (!lecturePlans.isEmpty) {
			put("lecturePlans", lecturePlans)
		}
		forward()
	}

	def courseBlogList(@param("id") id: String): View = {
		val courseBlog = entityDao.get(classOf[CourseBlog], id.toLong)
		val courseBlogs = entityDao.findBy(classOf[CourseBlog], "course", List(courseBlog.course))
		put("courseBlogs", courseBlogs)
		forward()
	}

	def getDatas[T <: Entity[_]](clazz: Class[T], courseBlog: CourseBlog): Seq[T] = {
		val builder = OqlBuilder.from(clazz, "aa")
		builder.where("aa.course=:course", courseBlog.course)
		builder.where("aa.semester=:semester", courseBlog.semester)
		builder.where("aa.author=:author", courseBlog.author)
		entityDao.search(builder)
	}

	def childrenAjax(): View = {
		getInt("courseGroupId").foreach(courseGroupId => {
			val courseGroup = entityDao.get(classOf[CourseGroup], courseGroupId)
			val courseGroupChildren = courseGroup.children
			put("courseGroups", courseGroupChildren)
		})
		forward("childrenJSON")
	}

}
