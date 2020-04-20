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
package org.openurp.edu.curricula.admin.web.action

import java.io.{File, FileOutputStream}
import java.time.Instant
import java.util.Locale

import javax.servlet.http.Part
import org.beangle.commons.codec.digest.Digests
import org.beangle.commons.collection.{Collections, Order}
import org.beangle.commons.io.{Dirs, IOs}
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.context.Params
import org.beangle.webmvc.api.view.View
import org.openurp.edu.base.model.Semester
import org.openurp.edu.curricula.admin.Constants
import org.openurp.edu.curricula.app.model.{ReviseSetting, ReviseTask}
import org.openurp.edu.curricula.model.{Attachment, BlogStatus, CourseBlog, CourseBlogMeta, CourseGroup, LecturePlan, Syllabus}

class TeacherAction extends AbstractAction[ReviseTask] {

	override def indexSetting(): Unit = {
		val semesterString = get("reviseTask.semester.id").orNull
		val semester = if (semesterString != null) entityDao.get(classOf[Semester], semesterString.toInt) else getCurrentSemester
		put("currentSemester", semester)
		put("project", getProject)
		forward()
	}

	override def getQueryBuilder: OqlBuilder[ReviseTask] = {
		val builder: OqlBuilder[ReviseTask] = OqlBuilder.from(entityName, simpleEntityName)
		populateConditions(builder)
		builder.where("reviseTask.author=:user", getUser)
	}

	override def search(): View = {
		if (getUser != null) {
			val courseBlogMap = Collections.newMap[ReviseTask, CourseBlog]
			val semesterString = get("reviseTask.semester.id").orNull
			val semester = if (semesterString != null) entityDao.get(classOf[Semester], semesterString.toInt) else getCurrentSemester
			val reviseTaskBuilder = OqlBuilder.from(classOf[ReviseTask], "rt")
			reviseTaskBuilder.where("rt.semester=:semester", semester)
			reviseTaskBuilder.where("rt.author=:author", getUser)
			val reviseTasks = entityDao.search(reviseTaskBuilder)
			reviseTasks.foreach(reviseTask => {
				val courseBlogs = getCourseBlogs(reviseTask)
				courseBlogs.foreach(courseBlog => {
					courseBlogMap.put(reviseTask, courseBlog)
				})
			})
			put("courseBlogMap", courseBlogMap)
			val reviseSettingBuilder = OqlBuilder.from(classOf[ReviseSetting], "rs")
			reviseSettingBuilder.where("rs.semester=:semester", semester)
			reviseSettingBuilder.where(":now between rs.beginAt and rs.endAt", Instant.now())
			val reviseSettings = entityDao.search(reviseSettingBuilder)
			if (!reviseSettings.isEmpty) {
				put("reviseSetting", reviseSettings.head)
			}
			put("BlogStatus", BlogStatus)

			val builder = OqlBuilder.from(classOf[CourseBlog], "courseBlog")
			builder.where("courseBlog.author=:author", getUser)
			put("courseBlogs", entityDao.search(builder))
		}
		put("user", getUser)
		super.search()
	}

	override def editSetting(reviseTask: ReviseTask): Unit = {
		val courseBlogs = getCourseBlogs(reviseTask)
		courseBlogs.foreach(courseBlog => {
			put("courseBlog", courseBlog)
			val syllabuses = getDatas(classOf[Syllabus], courseBlog)
			if (!syllabuses.isEmpty) {
				put("syllabuses", syllabuses)
			}
			val lecturePlans = getDatas(classOf[LecturePlan], courseBlog)
			if (!lecturePlans.isEmpty) {
				put("lecturePlans", lecturePlans)
			}
		})
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

		val metas = entityDao.findBy(classOf[CourseBlogMeta], "course", List(reviseTask.course))
		put("meta",metas.head)
		super.editSetting(reviseTask)
	}

	override def saveAndRedirect(reviseTask: ReviseTask): View = {
		val courseBlogs = getCourseBlogs(reviseTask)
		val courseBlog = if (!courseBlogs.isEmpty) courseBlogs.head else new CourseBlog
		courseBlog.semester = reviseTask.semester
		courseBlog.course = reviseTask.course
		courseBlog.department = reviseTask.course.department
		courseBlog.author = getUser
		courseBlog.updatedAt = Instant.now()
		get("courseBlog.description").foreach(description => {
			courseBlog.description = description
		})
		courseBlog.enDescription = get("courseBlog.enDescription")
		courseBlog.materials = get("courseBlog.materials")
		courseBlog.website = get("courseBlog.website")

		val courseBlogMeta = entityDao.findBy(classOf[CourseBlogMeta], "course", List(reviseTask.course))
		courseBlogMeta.foreach(meta => {
			courseBlog.meta = Option(meta)
		})

		val path = Constants.AttachmentBase + reviseTask.semester.id.toString + "/" + reviseTask.course.id.toString
		Dirs.on(path).mkdirs()

		//保存syllabus
		val syllabuses = getDatas(classOf[Syllabus], courseBlog)
		val syllabus = if (syllabuses.isEmpty) new Syllabus else syllabuses.head
		syllabus.semester = reviseTask.semester
		syllabus.course = reviseTask.course
		syllabus.locale = Locale.CHINESE
		syllabus.author = getUser
		syllabus.updatedAt = Instant.now()
		if (!getAll("syllabus.attachment").exists(_ == "")) {
			if (null != syllabus.attachment && null != syllabus.attachment.key) {
				val file = new File(path + "/" + syllabus.attachment.key)
				if (file.exists()) file.delete()
			}
			val parts = Params.getAll("syllabus.attachment").asInstanceOf[List[Part]]
			for (part <- parts) {
				val attachment = new Attachment()
				attachment.size = part.getSize.toInt
				val ext = Strings.substringAfterLast(part.getSubmittedFileName, ".")
				attachment.key = "syllabus_" + getUser.id.toString + (if (Strings.isEmpty(ext)) "" else "." + ext)
				attachment.mimeType = "application/pdf"
				attachment.name = part.getSubmittedFileName
				IOs.copy(part.getInputStream, new FileOutputStream(path + "/" + attachment.key))
				syllabus.attachment = attachment
			}
		}
		entityDao.saveOrUpdate(syllabus)

		//lecturePlan
		val lecturePlans = getDatas(classOf[LecturePlan], courseBlog)
		val lecturePlan = if (lecturePlans.isEmpty) new LecturePlan else lecturePlans.head
		lecturePlan.semester = reviseTask.semester
		lecturePlan.course = reviseTask.course
		lecturePlan.locale = Locale.CHINESE
		lecturePlan.author = getUser
		lecturePlan.updatedAt = Instant.now()
		if (!getAll("lecturePlan.attachment").exists(_ == "")) {
			if (null != lecturePlan.attachment && null != lecturePlan.attachment.key) {
				val file = new File(Constants.AttachmentBase + "lecturePlan/" + syllabus.attachment.key)
				if (file.exists()) file.delete()
			}
			val parts = Params.getAll("lecturePlan.attachment").asInstanceOf[List[Part]]
			for (part <- parts) {
				val attachment = new Attachment()
				attachment.size = part.getSize.toInt
				val ext = Strings.substringAfterLast(part.getSubmittedFileName, ".")
				attachment.key = "lecture_plan_" + getUser.id.toString + (if (Strings.isEmpty(ext)) "" else "." + ext)
				attachment.mimeType = "application/pdf"
				attachment.name = part.getSubmittedFileName
				IOs.copy(part.getInputStream, new FileOutputStream(path + "/" + attachment.key))
				lecturePlan.attachment = attachment
			}
		}
		entityDao.saveOrUpdate(lecturePlan)
		entityDao.saveOrUpdate(courseBlog)

		val newCourseBlogs = entityDao.findBy(classOf[CourseBlog], "course", List(reviseTask.course))
		courseBlogMeta.foreach(meta => {
			meta.count = newCourseBlogs.size
			meta.updatedAt = Instant.now()
			meta.author = getUser
		})
		entityDao.saveOrUpdate(courseBlogMeta)
		redirect("search", "&reviseTask.semester.id=" + reviseTask.semester.id,"info.save.success")
	}

	override def remove(): View = {
		val reviseTask = entityDao.get(classOf[ReviseTask], longId("reviseTask"))
		val courseBlogs = getCourseBlogs(reviseTask)
		entityDao.remove(courseBlogs)

		val newCourseBlogs = entityDao.findBy(classOf[CourseBlog], "course", List(reviseTask.course))
		val courseBlogMeta = entityDao.findBy(classOf[CourseBlogMeta], "course", List(reviseTask.course))
		courseBlogMeta.foreach(meta => {
			meta.count = newCourseBlogs.size
			meta.updatedAt = Instant.now()
			meta.author = getUser
		})
		entityDao.saveOrUpdate(courseBlogMeta)
		redirect("search", "&reviseTask.semester.id=" + reviseTask.semester.id,"info.delete.success")
	}


	def getCourseBlogs(reviseTask: ReviseTask): Seq[CourseBlog] = {
		val builder = OqlBuilder.from(classOf[CourseBlog], "courseBlog")
		builder.where("courseBlog.course=:course", reviseTask.course)
		builder.where("courseBlog.semester=:semester", reviseTask.semester)
		reviseTask.author.foreach(author => {
			builder.where("courseBlog.author=:author", author)
		})
		entityDao.search(builder)
	}

	def submit(): View = {
		val reviseTask = entityDao.get(classOf[ReviseTask], longId("reviseTask"))
		val courseBlogs = getCourseBlogs(reviseTask)
		courseBlogs.foreach(courseBlog => {
			courseBlog.status = BlogStatus.Submited
		})
		entityDao.saveOrUpdate(courseBlogs)
		redirect("search", "&reviseTask.semester.id=" + reviseTask.semester.id,"info.save.success")
	}
}
