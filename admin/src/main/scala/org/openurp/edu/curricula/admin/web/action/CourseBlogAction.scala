package org.openurp.edu.curricula.admin.web.action

import java.io.{File, FileOutputStream}
import java.time.Instant
import java.util.Locale

import javax.servlet.http.Part
import org.beangle.commons.codec.digest.Digests
import org.beangle.commons.collection.Order
import org.beangle.commons.io.IOs
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.context.Params
import org.beangle.webmvc.api.view.View
import org.openurp.edu.base.model.{Course, Semester}
import org.openurp.edu.curricula.admin.Constants
import org.openurp.edu.curricula.model.{Attachment, CourseBlog, LecturePlan, Syllabus}


class CourseBlogAction extends AbstractAction[CourseBlog] {

	override def getQueryBuilder: OqlBuilder[CourseBlog] = {
		val builder: OqlBuilder[CourseBlog] = OqlBuilder.from(entityName, simpleEntityName)
		builder.where("courseBlog.semester=:semester", getSemester)
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
		super.editSetting(courseBlog)
	}

	override def saveAndRedirect(courseBlog: CourseBlog): View = {
		val user = getUser
		val course = if (courseBlog.persisted) courseBlog.course else entityDao.getAll(classOf[Course]).find(_.code == get("courseBlog.course").get).get
		val semester = if (courseBlog.persisted) courseBlog.semester else entityDao.get(classOf[Semester], intId("courseBlog.semester"))
		if (!courseBlog.persisted) {
			if (duplicate(classOf[CourseBlog].getName, null, Map("semester" -> courseBlog.semester, "author" -> user, "course" -> courseBlog.course))) {
				return redirect("search", "该课程资料存在,请修改课程资料")
			}
		}
		courseBlog.author = user
		courseBlog.course = course
		courseBlog.department = course.department

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
				val file = new File(Constants.AttachmentBase + "syllabus/" + syllabus.attachment.key)
				if (file.exists()) file.delete()
			}
			val parts = Params.getAll("syllabus.attachment").asInstanceOf[List[Part]]
			for (part <- parts) {
				val attachment = new Attachment()
				attachment.size = part.getSize.toInt
				val ext = Strings.substringAfterLast(part.getSubmittedFileName, ".")
				attachment.key = Digests.md5Hex(part.getSubmittedFileName + Instant.now().toString) + (if (Strings.isEmpty(ext)) "" else "." + ext)
				attachment.mimeType = "application/pdf"
				attachment.name = part.getSubmittedFileName
				IOs.copy(part.getInputStream, new FileOutputStream(Constants.AttachmentBase + "syllabus/" + attachment.key))
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
				val file = new File(Constants.AttachmentBase + "lecturePlan/" + syllabus.attachment.key)
				if (file.exists()) file.delete()
			}
			val parts = Params.getAll("lecturePlan.attachment").asInstanceOf[List[Part]]
			for (part <- parts) {
				val attachment = new Attachment()
				attachment.size = part.getSize.toInt
				val ext = Strings.substringAfterLast(part.getSubmittedFileName, ".")
				attachment.key = Digests.md5Hex(part.getSubmittedFileName + Instant.now().toString) + (if (Strings.isEmpty(ext)) "" else "." + ext)
				attachment.mimeType = "application/pdf"
				attachment.name = part.getSubmittedFileName
				IOs.copy(part.getInputStream, new FileOutputStream(Constants.AttachmentBase + "lecturePlan/" + attachment.key))
				lecturePlan.attachment = attachment
			}
		}
		entityDao.saveOrUpdate(lecturePlan)
		super.saveAndRedirect(courseBlog)
	}


}
