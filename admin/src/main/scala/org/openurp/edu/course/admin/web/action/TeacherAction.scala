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

import java.time.{Instant, LocalDate}
import java.util.Locale

import javax.servlet.http.Part
import org.beangle.commons.collection.Collections
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.context.Params
import org.beangle.webmvc.api.view.View
import org.openurp.app.UrpApp
import org.openurp.edu.base.model.{Course, Semester}
import org.openurp.edu.course.app.model.{ReviseSetting, ReviseTask}
import org.openurp.edu.course.model._

class TeacherAction extends AbstractAction[CourseBlog] {

	override def indexSetting(): Unit = {
		put("currentSemester", getSemester)
		put("project", getProject)
		forward()
	}

	override def getQueryBuilder: OqlBuilder[CourseBlog] = {
		val builder: OqlBuilder[CourseBlog] = OqlBuilder.from(entityName, simpleEntityName)
		populateConditions(builder)
		builder.where("courseBlog.author=:user", getUser)
		builder.where("courseBlog.semester=:semester", getSemester)
	}

	override def search(): View = {
		if (getUser != null) {
			val reviseSettingBuilder = OqlBuilder.from(classOf[ReviseSetting], "rs")
			reviseSettingBuilder.where("rs.semester=:semester", getSemester)
			reviseSettingBuilder.where(":now between rs.beginAt and rs.endAt", Instant.now())
			val reviseSettings = entityDao.search(reviseSettingBuilder)
			if (!reviseSettings.isEmpty) {
				put("reviseSetting", reviseSettings.head)
			}
			put("BlogStatus", BlogStatus)

			val courseBlogBuilder = OqlBuilder.from(classOf[CourseBlog], "courseBlog")
			courseBlogBuilder.where("courseBlog.author=:author", getUser)
			put("blogs", entityDao.search(courseBlogBuilder))

			val syllabusMap = Collections.newMap[CourseBlog, Syllabus]
			val courseBlogs = entityDao.search(getQueryBuilder)
			courseBlogs.foreach(courseBlog => {
				val syllabuses = getDatas(classOf[Syllabus], courseBlog)
				syllabuses.foreach(syllabus => {
					syllabusMap.put(courseBlog, syllabus)
				})
			})
			put("syllabusMap", syllabusMap)

		}
		put("user", getUser)

		super.search()
	}

	def getSemester: Semester = {
		val semesterString = get("courseBlog.semester.id").orNull
		if (semesterString != null) entityDao.get(classOf[Semester], semesterString.toInt) else getCurrentSemester
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
		val builder = OqlBuilder.from(classOf[CourseGroup], "courseGroup")
		builder.orderBy("courseGroup.indexno")
		put("courseGroups", entityDao.search(builder))

		val metas = entityDao.findBy(classOf[CourseBlogMeta], "course", List(courseBlog.course))
		put("meta", metas.head)

		val awardMap = Collections.newMap[AwardLabelType, Seq[AwardLabel]]
		val labelTypes = getCodes(classOf[AwardLabelType])
		put("labelTypes", labelTypes)
		labelTypes.foreach(labelType => {
			awardMap.put(labelType, entityDao.findBy(classOf[AwardLabel], "labelType", List(labelType)))
		})
		put("awardMap", awardMap)
		put("yearMap", courseBlog.awards.map(e => (e.awardLabel.labelType, e.year)).toMap)

		put("choosedType", courseBlog.awards.map(_.awardLabel.labelType))
		put("choosedLabel", courseBlog.awards.map(_.awardLabel))
		val years = Collections.newBuffer[String]
		for (a <- 0 to 9) {
			years.+=:(LocalDate.now().minusYears(a).getYear.toString)
		}
		put("years", years)
		super.editSetting(courseBlog)
	}

	override def saveAndRedirect(courseBlog: CourseBlog): View = {
		val course = if (courseBlog.persisted) courseBlog.course else entityDao.findBy(classOf[Course], "code", List(get("courseBlog.course").get)).head
		val semester = if (courseBlog.persisted) courseBlog.semester else entityDao.get(classOf[Semester], intId("courseBlog.semester"))
		courseBlog.semester = semester
		courseBlog.course = course
		courseBlog.department = course.department
		courseBlog.author = Option(getUser)
		courseBlog.updatedAt = Instant.now()

		val reviseTaskBuilder = OqlBuilder.from(classOf[ReviseTask], "reviseTask")
		reviseTaskBuilder.where("reviseTask.semester=:semester", semester)
		reviseTaskBuilder.where("reviseTask.course=:course", course)
		reviseTaskBuilder.where("reviseTask.author=:author", getUser)
		val reviseTasks = entityDao.search(reviseTaskBuilder)
		reviseTasks.foreach(reviseTask => {
			reviseTask.teachers.foreach(teacher => {
				if (!courseBlog.teachers.contains(teacher)) {
					courseBlog.teachers += teacher
				}
			})
		})

		//		courseBlog.awards.clear()
		var labelIds = Collections.newBuffer[Int]
		val labelTypes = getCodes(classOf[AwardLabelType])
		labelTypes.foreach(labelType => {
			get(labelType.id.toString + "_year").foreach(year => {
				getAll(labelType.id.toString + "_awardLabelId", classOf[Int]).foreach(labelId => {
					labelIds += labelId
					val awardLabel = entityDao.get(classOf[AwardLabel], labelId)
					courseBlog.awards.find { award => award.awardLabel == awardLabel } match {
						case Some(award) => award.year = year
						case None => {
							val newAward = new Award
							newAward.year = year
							newAward.awardLabel = awardLabel
							newAward.courseBlog = courseBlog
							entityDao.saveOrUpdate(newAward)
							courseBlog.awards += newAward
						}
					}
				})
			})
		})
		if (!labelIds.isEmpty) {
			var deleteAwards = Collections.newBuffer[Award]
			val awardBuilder = OqlBuilder.from(classOf[Award], "award")
			awardBuilder.where("award.courseBlog=:courseBlog", courseBlog)
			awardBuilder.where("award.awardLabel.id in(:awardLabelIds)", labelIds)
			val chooseAwards = entityDao.search(awardBuilder)
			courseBlog.awards.foreach(award => {
				if (!chooseAwards.contains(award)) {
					deleteAwards += award
				}
			})
			courseBlog.awards --= deleteAwards
		}

		//		val path = Constants.AttachmentBase + "/" + courseBlog.semester.id.toString
		//		Dirs.on(path).mkdirs()

		//保存syllabus
		val syllabuses = getDatas(classOf[Syllabus], courseBlog)
		val syllabus = if (syllabuses.isEmpty) new Syllabus else syllabuses.head
		syllabus.semester = semester
		syllabus.course = course
		syllabus.locale = Locale.CHINESE
		syllabus.author = getUser
		syllabus.updatedAt = Instant.now()

		val parts = getAll("syllabus.attachment", classOf[Part])
		if (parts.nonEmpty && parts.head.getSize > 0) {
			val blob = UrpApp.getBlobRepository(true)
			val part = parts.head
			if (Strings.isNotEmpty(syllabus.attachment.key)) {
				blob.remove(syllabus.attachment.key)
			}
			val meta = blob.upload("/" + semester.id.toString,
				part.getInputStream, part.getSubmittedFileName, getUser.code + " " + getUser.name)
			val attachment = new Attachment()
			attachment.size = meta.size
			attachment.key = meta.path
			attachment.mimeType = meta.mediaType
			attachment.name = meta.name
			syllabus.attachment = attachment
		}
		entityDao.saveOrUpdate(syllabus)

		//lecturePlan
		val lecturePlans = getDatas(classOf[LecturePlan], courseBlog)
		val lecturePlan = if (lecturePlans.isEmpty) new LecturePlan else lecturePlans.head
		lecturePlan.semester = semester
		lecturePlan.course = course
		lecturePlan.locale = Locale.CHINESE
		lecturePlan.author = getUser
		lecturePlan.updatedAt = Instant.now()

		val LParts = getAll("lecturePlan.attachment", classOf[Part])
		if (parts.nonEmpty && parts.head.getSize > 0) {
			val blob = UrpApp.getBlobRepository(true)
			val part = LParts.head
			if (Strings.isNotEmpty(lecturePlan.attachment.key)) {
				blob.remove(lecturePlan.attachment.key)
			}
			val meta = blob.upload("/" + semester.id.toString, part.getInputStream, part.getSubmittedFileName, getUser.code + " " + getUser.name)
			val attachment = new Attachment()
			attachment.size = meta.size
			attachment.key = meta.path
			attachment.mimeType = meta.mediaType
			attachment.name = meta.name
			lecturePlan.attachment = attachment
		}
		entityDao.saveOrUpdate(lecturePlan)
		entityDao.saveOrUpdate(courseBlog)

		redirect("search", "&courseBlog.semester.id=" + semester.id, "info.save.success")
	}

	override def remove(): View = {
		val courseBlog = entityDao.get(classOf[CourseBlog], longId("courseBlog"))
		courseBlog.description = "--"
		courseBlog.updatedAt = Instant.now()
		courseBlog.enDescription = None
		courseBlog.materials = None
		courseBlog.website = None
		val blob = UrpApp.getBlobRepository(true)
		val syllabuses = getDatas(classOf[Syllabus], courseBlog)
		syllabuses.foreach(
			syllabus => {

				if (null != syllabus.attachment && null != syllabus.attachment.key) {
					blob.remove(syllabus.attachment.key)
				}
			}
			//				val file = new File(Constants.AttachmentBase + syllabus.attachment.key)
			//				if (file.exists()) file.delete()
		)
		entityDao.remove(syllabuses)

		val lecturePlans = getDatas(classOf[LecturePlan], courseBlog)
		lecturePlans.foreach(
			lecturePlan => {
				if (null != lecturePlan.attachment && null != lecturePlan.attachment.key) {
					blob.remove(lecturePlan.attachment.key)
				}
			})
		entityDao.remove(lecturePlans)
		entityDao.saveOrUpdate(courseBlog)

		val courseBlogMeta = entityDao.findBy(classOf[CourseBlogMeta], "course", List(courseBlog.course))
		courseBlogMeta.foreach(meta => {
			meta.updatedAt = Instant.now()
			meta.author = getUser
		})
		entityDao.saveOrUpdate(courseBlogMeta)
		redirect("search", "&courseBlog.semester.id=" + courseBlog.semester.id, "重置成功")
	}

	def submit(): View = {
		val courseBlog = entityDao.get(classOf[CourseBlog], longId("courseBlog"))
		courseBlog.status = BlogStatus.Submited
		entityDao.saveOrUpdate(courseBlog)
		redirect("search", "&courseBlog.semester.id=" + courseBlog.semester.id, "info.save.success")
	}

	def copy(): View = {
		val courseBlog = entityDao.get(classOf[CourseBlog], longId("courseBlog"))
		val builder = OqlBuilder.from(classOf[CourseBlog], "courseBlog")
		builder.where("courseBlog.course=:course", courseBlog.course)
		//		builder.where("courseBlog.status =:status", BlogStatus.Published)
		builder.orderBy("courseBlog.semester desc")
		val hisBlogs = entityDao.search(builder)
		if (!hisBlogs.isEmpty) {
			val hisBlog = hisBlogs.head
			courseBlog.description = hisBlog.description
			courseBlog.enDescription = hisBlog.enDescription
			courseBlog.author = Option(getUser)
			courseBlog.materials = hisBlog.materials
			courseBlog.website = hisBlog.website
			courseBlog.preCourse = hisBlog.preCourse
			courseBlog.awards = hisBlog.awards
			courseBlog.updatedAt = Instant.now()
			courseBlog.meta = hisBlog.meta
			courseBlog.remark = hisBlog.remark
			entityDao.saveOrUpdate(courseBlog)
			redirect("edit", "&id=" + courseBlog.id, "")
		} else {
			redirect("search", "&courseBlog.semester.id=" + courseBlog.semester.id, "不存在历史课程资料")
		}
	}
}
