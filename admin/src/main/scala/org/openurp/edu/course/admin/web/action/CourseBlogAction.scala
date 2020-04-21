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

import java.io.{File, FileOutputStream}
import java.time.Instant
import java.util.Locale

import javax.servlet.http.Part
import org.beangle.commons.collection.{Collections, Order}
import org.beangle.commons.file.digest.Sha1
import org.beangle.commons.io.{Dirs, IOs}
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.context.Params
import org.beangle.webmvc.api.view.View
import org.openurp.edu.base.model.{Course, Semester}
import org.openurp.edu.course.admin.Constants
import org.openurp.edu.course.model._


class CourseBlogAction extends AbstractAction[CourseBlog] {

	override def indexSetting(): Unit = {
		put("currentSemester", getSemester)
		super.indexSetting()
	}

	override def getQueryBuilder: OqlBuilder[CourseBlog] = {
		val builder: OqlBuilder[CourseBlog] = OqlBuilder.from(entityName, simpleEntityName)
		builder.where("courseBlog.semester=:semester", getSemester)
		addDepart(builder, "courseBlog.department")
		populateConditions(builder)
		builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
	}

	def getSemester(): Semester = {
		val semesterString = get("courseBlog.semester.id").orNull
		if (semesterString != null) entityDao.get(classOf[Semester], semesterString.toInt) else getCurrentSemester
	}

	override def info(id: String): View = {
		val courseBlog = entityDao.get(classOf[CourseBlog], id.toLong)
		val syllabuses = getDatas(classOf[Syllabus], courseBlog)
		if (!syllabuses.isEmpty) {
			put("syllabuses", syllabuses)
		}
		val lecturePlans = getDatas(classOf[LecturePlan], courseBlog)
		if (!lecturePlans.isEmpty) {
			put("lecturePlans", lecturePlans)
		}
		super.info(id)
	}

	override def editSetting(courseBlog: CourseBlog): Unit = {
		if (courseBlog.persisted) {
			val syllabuses = getDatas(classOf[Syllabus], courseBlog)
			if (!syllabuses.isEmpty) {
				put("syllabuses", syllabuses)
			}
			val lecturePlans = getDatas(classOf[LecturePlan], courseBlog)
			if (!lecturePlans.isEmpty) {
				put("lecturePlans", lecturePlans)
			}
		}
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
		super.editSetting(courseBlog)
	}

	override def saveAndRedirect(courseBlog: CourseBlog): View = {
		val user = getUser
		val course = if (courseBlog.persisted) courseBlog.course else entityDao.findBy(classOf[Course], "code", List(get("courseBlog.course").get)).head
		val semester = if (courseBlog.persisted) courseBlog.semester else entityDao.get(classOf[Semester], intId("courseBlog.semester"))
		if (!courseBlog.persisted) {
			if (duplicate(classOf[CourseBlog].getName, null, Map("semester" -> courseBlog.semester, "author" -> user, "course" -> courseBlog.course))) {
				return redirect("search", "该课程资料存在,请修改课程资料")
			}
		}
		courseBlog.author = user
		courseBlog.course = course
		courseBlog.department = course.department
		courseBlog.status = BlogStatus.Submited

		val courseBlogMeta = entityDao.findBy(classOf[CourseBlogMeta], "course", List(course))
		courseBlogMeta.foreach(meta => {
			courseBlog.meta = Option(meta)
		})

		val path = Constants.AttachmentBase + semester.id.toString
		Dirs.on(path).mkdirs()
		//保存syllabus
		val syllabuses = getDatas(classOf[Syllabus], courseBlog)
		val syllabus = if (syllabuses.isEmpty) new Syllabus else syllabuses.head
		syllabus.semester = semester
		syllabus.course = course
		syllabus.locale = Locale.CHINESE
		syllabus.author = user
		syllabus.updatedAt = Instant.now()
		if (!getAll("syllabus.attachment").exists(_ == "")) {
			if (null != syllabus.attachment && null != syllabus.attachment.key) {
				val file = new File(path + "/" + syllabus.attachment.key)
				if (file.exists()) file.delete()
			}
			val parts = Params.getAll("syllabus.attachment").asInstanceOf[List[Part]]
			for (part <- parts) {
				val syllabusFile = new File(path + "/" + Instant.now().toString)
				IOs.copy(part.getInputStream, new FileOutputStream(syllabusFile))
				val sha = Sha1.digest(syllabusFile)
				val target = new File(path + "/" + sha)
				syllabusFile.renameTo(target)

				val attachment = new Attachment()
				attachment.size = part.getSize.toInt
				attachment.key = "/" + semester.id.toString + "/" + sha
				attachment.mimeType = "application/pdf"
				attachment.name = part.getSubmittedFileName
				//				val syllabusFile = new File(path + "/" + attachment.key)
				//				Encryptor.encrypt(syllabusFile, None, "123", PdfWriter.ALLOW_PRINTING)
				syllabus.attachment = attachment
			}
		}
		entityDao.saveOrUpdate(syllabus)

		//lecturePlan
		val lecturePlans = getDatas(classOf[LecturePlan], courseBlog)
		val lecturePlan = if (lecturePlans.isEmpty) new LecturePlan else lecturePlans.head
		lecturePlan.semester = semester
		lecturePlan.course = course
		lecturePlan.locale = Locale.CHINESE
		lecturePlan.author = user
		lecturePlan.updatedAt = Instant.now()
		if (!getAll("lecturePlan.attachment").exists(_ == "")) {
			if (null != lecturePlan.attachment && null != lecturePlan.attachment.key) {
				val file = new File(path + "/" + lecturePlan.attachment.key)
				if (file.exists()) file.delete()
			}
			val parts = Params.getAll("lecturePlan.attachment").asInstanceOf[List[Part]]
			for (part <- parts) {
				val lecturePlanFile = new File(path + "/" + Instant.now().toString)
				IOs.copy(part.getInputStream, new FileOutputStream(lecturePlanFile))
				val sha = Sha1.digest(lecturePlanFile)
				val target = new File(path + "/" + sha)
				lecturePlanFile.renameTo(target)

				val attachment = new Attachment()
				attachment.size = part.getSize.toInt
				attachment.key = "/" + semester.id.toString + "/" + sha
				attachment.mimeType = "application/pdf"
				attachment.name = part.getSubmittedFileName
				//				attachment.key = Digests.md5Hex(part.getSubmittedFileName + Instant.now().toString) + (if (Strings.isEmpty(ext)) "" else "." + ext)
				//				val lecturePlanFile = new File(path + "/" + attachment.key)
				//				Encryptor.encrypt(lecturePlanFile, None, "123", PdfWriter.ALLOW_PRINTING)
				lecturePlan.attachment = attachment
			}
		}
		entityDao.saveOrUpdate(lecturePlan)
		saveOrUpdate(courseBlog)

		val courseBlogs = entityDao.findBy(classOf[CourseBlog], "course", List(course))
		courseBlogMeta.foreach(meta => {
			meta.count = courseBlogs.size
			meta.updatedAt = Instant.now()
			meta.author = getUser
		})
		entityDao.saveOrUpdate(courseBlogMeta)
		redirect("search", "info.save.success")
	}

	def audit(): View = {
		val courseBlogs = entityDao.find(classOf[CourseBlog], longIds("courseBlog"))
		get("passed").orNull match {
			case "1" => courseBlogs.foreach(courseBlog => {
				if (courseBlog.status != BlogStatus.Draft && courseBlog.status != BlogStatus.Published) {
					courseBlog.status = BlogStatus.Passed
					courseBlog.auditor = Option(getUser)
					courseBlog.auditAt = Option(Instant.now())
				}
			})
			case "0" => courseBlogs.foreach(courseBlog => {
				if (courseBlog.status != BlogStatus.Draft && courseBlog.status != BlogStatus.Published) {
					courseBlog.status = BlogStatus.Unpassed
					courseBlog.auditor = Option(getUser)
					courseBlog.auditAt = Option(Instant.now())
				}
			})
		}
		entityDao.saveOrUpdate(courseBlogs)
		redirect("search", "info.save.success")
	}

	def publish(): View = {
		val courseBlogs = entityDao.find(classOf[CourseBlog], longIds("courseBlog"))
		courseBlogs.foreach(courseBlog => {
			if (courseBlog.status == BlogStatus.Passed) {
				courseBlog.status = BlogStatus.Published
				courseBlog.auditor = Option(getUser)
				courseBlog.auditAt = Option(Instant.now())
			}
		})
		entityDao.saveOrUpdate(courseBlogs)
		redirect("search", "info.save.success")
	}

	override def removeAndRedirect(courseBlogs: Seq[CourseBlog]): View = {
		courseBlogs.foreach(courseBlog => {
			val newCourseBlogs = entityDao.findBy(classOf[CourseBlog], "course", List(courseBlog.course))
			val courseBlogMeta = entityDao.findBy(classOf[CourseBlogMeta], "course", List(courseBlog.course))
			courseBlogMeta.foreach(meta => {
				meta.count = newCourseBlogs.size
				meta.updatedAt = Instant.now()
				meta.author = getUser
			})
			entityDao.saveOrUpdate(courseBlogMeta)
		})
		remove(courseBlogs)
		redirect("search", "info.remove.success")
	}


}
