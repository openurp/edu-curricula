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
import org.beangle.security.realm.cas.CasConfig
import org.beangle.webmvc.api.annotation.param
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.edu.base.model.Semester
import org.openurp.edu.base.web.ProjectSupport
import org.openurp.edu.course.model._


class IndexAction extends RestfulAction[CourseBlog] with ProjectSupport {

	var casConfig: CasConfig = _

	def nav(): Unit = {
		val departBuilder = OqlBuilder.from(classOf[Department], "department")
		departBuilder.where("department.school=:school", getProject.school)
		departBuilder.where("department.endOn is null")
		departBuilder.where("department.teaching is true")
		departBuilder.orderBy("department.code")
		val departments = entityDao.search(departBuilder)
		put("departments", departments)
		put("firstDepartment", departments.head)
		put("awardLabelTypes", getCodes(classOf[AwardLabelType]))
	}

	override def indexSetting(): Unit = {
		nav()

		put("casConfig", casConfig)
		// 没有父类的分组
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

		val builder = OqlBuilder.from(classOf[Semester], "semester")
			.where("semester.calendar in(:calendars)", getProject.calendars)
		builder.orderBy("semester.code desc")
		put("semesters", entityDao.search(builder))
		put("currentSemester", getCurrentSemester)

		super.indexSetting()
	}

	override def search(): View = {
		val courseblogs = entityDao.search(getQueryBuilder)
		put("courseBlogs", courseblogs)
		put("BlogStatus", BlogStatus)
		forward()
	}

	override def getQueryBuilder: OqlBuilder[CourseBlog] = {
		val builder = OqlBuilder.from(classOf[CourseBlog], "courseBlog")
		builder.where("courseBlog.semester=:semester", getSemester)
		//		builder.where("courseBlog.status =:status", BlogStatus.Published)
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
		get("courseBlog.department.id").foreach(depart => {
			depart match {
				case "else" => builder.where("courseBlog.department.teaching is false ")
				case "" =>
				case _ => builder.where("courseBlog.department.id=:id", depart.toInt)
			}
		})
		builder.limit(getPageLimit)
		builder.orderBy("courseBlog.status desc")
		builder.orderBy("courseBlog.course.code")
	}


	def getSemester: Semester = {
		val semesterString = get("courseBlog.semester.id").orNull
		if (semesterString != null) entityDao.get(classOf[Semester], semesterString.toInt) else getCurrentSemester
	}

	def getCourseGroups(id: Int): Set[CourseGroup] = {
		val courseGroup = entityDao.get(classOf[CourseGroup], id)
		Hierarchicals.getFamily(courseGroup)
	}

	override def info(id: String): View = {
		put("BlogStatus", BlogStatus)
		super.info(id)
	}

	def detail(@param("id") id: String): View = {
		nav()
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
		put("BlogStatus", BlogStatus)
		forward()
	}

	/*
	院系页
	 */
	def courseBlogForDepart(@param("id") id: String): View = {
		nav()

		if (id != "else") {
			put("choosedDepartment", entityDao.get(classOf[Department], id.toInt))
		}
		val builder = OqlBuilder.from(classOf[CourseBlog], "courseBlog")
		builder.where("courseBlog.semester=:semester", getCurrentSemester)
		//		courseBlogBuilder.where("courseBlog.status =:status", BlogStatus.Published)
		builder.where("courseBlog.meta.courseGroup is not null")
		builder.orderBy("courseBlog.course.code")
		id match {
			case "else" => builder.where("courseBlog.department.teaching is false")
			case _ => builder.where("courseBlog.department.id=:id", id.toInt)
		}
		val courseBlogs = entityDao.search(builder).toBuffer
		val courseBlogMap = courseBlogs.groupBy { x => x.meta.get.courseGroup.get }
		put("courseBlogMap", courseBlogMap)
		put("BlogStatus", BlogStatus)
		forward()
	}

	def courseBlogForType(): View = {
		nav()
		// 没有父类的分组
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

		val builder = OqlBuilder.from(classOf[Semester], "semester")
			.where("semester.calendar in(:calendars)", getProject.calendars)
		builder.orderBy("semester.code desc")
		put("semesters", entityDao.search(builder))
		put("currentSemester", getCurrentSemester)
		forward()
	}

	/*
	获奖分类页
	 */
	def awardLabel(): View = {
		nav()
		val builder = getQueryBuilder
		get("labelTypeId").foreach(labelTypeId => {
			builder.where("exists(from courseBlog.awards a where a.awardLabel.labelType.id=:labelTypeId)", labelTypeId.toInt)
			put("labelTypeId", labelTypeId)
			put("labelType", entityDao.get(classOf[AwardLabelType], labelTypeId.toInt))
			val awardLabels = entityDao.findBy(classOf[AwardLabel], "labelType.id", List(labelTypeId.toInt))
			put("awardLabels", awardLabels)
		})
		get("labelId").foreach(labelId => {
			val choosedAwardLabel = entityDao.get(classOf[AwardLabel], labelId.toInt)
			put("choosedAwardLabel", choosedAwardLabel)
			builder.where("exists(from courseBlog.awards a where a.awardLabel.id=:labelId)", labelId.toInt)
			val labelTypeId = choosedAwardLabel.labelType.id
			put("labelTypeId", labelTypeId)
			put("labelType", entityDao.get(classOf[AwardLabelType], labelTypeId.toInt))
			val awardLabels = entityDao.findBy(classOf[AwardLabel], "labelType.id", List(labelTypeId.toInt))
			put("awardLabels", awardLabels)
		})

		val courseblogs = entityDao.search(builder)
		put("courseBlogs", courseblogs)
		put("BlogStatus", BlogStatus)
		forward()
	}


	def awardLabelMap(): View = {
		nav()
		val labelTypeMap = Collections.newMap[AwardLabelType, Seq[CourseBlog]]
		val builder = getQueryBuilder
		val awardLabelTypes = getCodes(classOf[AwardLabelType])
		awardLabelTypes.foreach(labelType => {
			builder.where("exists(from courseBlog.awards a where a.awardLabel.labelType=:labelType)", labelType)
			labelTypeMap.put(labelType, entityDao.search(builder))
		})
		put("labelTypeMap", labelTypeMap)
		put("BlogStatus", BlogStatus)
		forward()
	}


	def getDatas[T <: Entity[_]](clazz: Class[T], courseBlog: CourseBlog): Seq[T] = {
		val builder = OqlBuilder.from(clazz, "aa")
		builder.where("aa.course=:course", courseBlog.course)
		builder.where("aa.semester=:semester", courseBlog.semester)
		//		builder.where("aa.author=:author", courseBlog.author)
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
