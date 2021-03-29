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

import jakarta.servlet.http.Part
import org.beangle.commons.bean.Properties
import org.beangle.commons.collection.{Collections, Order}
import org.beangle.data.dao.OqlBuilder
import org.beangle.ems.app.EmsApp
import org.beangle.webmvc.api.annotation.{param, response}
import org.beangle.webmvc.api.view.View
import org.openurp.base.edu.model.{Course, Semester}
import org.openurp.edu.curricula.model
import org.openurp.edu.curricula.model._


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
		val syllabuses = getDatas(classOf[model.Syllabus], courseBlog)
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
		if (courseBlog.description == "--") {
			courseBlog.description = ""
		}
		if (courseBlog.enDescription == "--") {
			courseBlog.enDescription = ""
		}
		if (courseBlog.books == "--") {
			courseBlog.books = ""
		}
		if (courseBlog.preCourse == "--") {
			courseBlog.preCourse = ""
		}
		if (courseBlog.persisted) {
			val syllabuses = getDatas(classOf[model.Syllabus], courseBlog)
			if (!syllabuses.isEmpty) {
				put("syllabus", syllabuses.head)
			}
			val lecturePlans = getDatas(classOf[LecturePlan], courseBlog)
			if (!lecturePlans.isEmpty) {
				put("lecturePlan", lecturePlans.head)
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
		val user = getUser
		val course = if (courseBlog.persisted) courseBlog.course else entityDao.findBy(classOf[Course], "code", List(get("courseBlog.course").get)).head
		val semester = if (courseBlog.persisted) courseBlog.semester else entityDao.get(classOf[Semester], intId("courseBlog.semester"))
		if (!courseBlog.persisted) {
			if (duplicate(classOf[CourseBlog].getName, null, Map("semester" -> courseBlog.semester, "author" -> user, "course" -> courseBlog.course))) {
				return redirect("search", "该课程资料存在,请修改课程资料")
			}
		}
		//		courseBlog.author = Option(user)
		//		courseBlog.course = course
		//		courseBlog.department = course.department
		courseBlog.status = BlogStatus.Submited

		get("courseBlog.website").foreach(a => {
			val website = a.trim
			if (website != "" && !website.startsWith("http://") && !website.startsWith("https://")) {
				val newWebsite = "http://" + website
				courseBlog.website = Option(newWebsite)
			}
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

		//在处理附件之前先保存一下其他对象，这样不至于附件保存不成功导致其他内容丢失
		saveOrUpdate(courseBlog)
		//		val path = Constants.AttachmentBase + "/" + courseBlog.semester.id.toString
		//		Dirs.on(path).mkdirs()
		//保存syllabus

		val parts = getAll("syllabus.attachment", classOf[Part])
		val syllabuses = getDatas(classOf[model.Syllabus], courseBlog)
		if (parts.nonEmpty && parts.head.getSize > 0) {
			val syllabus = if (syllabuses.isEmpty) new model.Syllabus else syllabuses.head
			syllabus.semester = semester
			syllabus.course = course
			syllabus.locale = Locale.CHINESE
			syllabus.author = user
			syllabus.updatedAt = Instant.now()
			val blob = EmsApp.getBlobRepository(true)
			val part = parts.head
			if (null != syllabus.attachment && syllabus.attachment.key.nonEmpty) {
				blob.remove(syllabus.attachment.key.get)
			}
			val meta = blob.upload("/" + semester.id.toString,
				part.getInputStream, part.getSubmittedFileName, getUser.code + " " + getUser.name)
			val attachment = new Attachment()
			attachment.size = Option(meta.fileSize)
			attachment.key = Option(meta.filePath)
			attachment.mimeType = Option(meta.mediaType)
			attachment.name = Option(meta.name)
			syllabus.attachment = attachment
			entityDao.saveOrUpdate(syllabus)
		}

		//lecturePlan

		val LParts = getAll("lecturePlan.attachment", classOf[Part])
		val lecturePlans = getDatas(classOf[LecturePlan], courseBlog)
		if (LParts.nonEmpty && LParts.head.getSize > 0) {
			val lecturePlan = if (lecturePlans.isEmpty) new LecturePlan else lecturePlans.head
			lecturePlan.semester = semester
			lecturePlan.course = course
			lecturePlan.locale = Locale.CHINESE
			lecturePlan.author = user
			lecturePlan.updatedAt = Instant.now()
			val blob = EmsApp.getBlobRepository(true)
			val part = LParts.head
			if (null != lecturePlan.attachment && lecturePlan.attachment.key.nonEmpty) {
				blob.remove(lecturePlan.attachment.key.get)
			}
			val meta = blob.upload("/" + semester.id.toString, part.getInputStream, part.getSubmittedFileName, getUser.code + " " + getUser.name)
			val attachment = new Attachment()
			attachment.size = Option(meta.fileSize)
			attachment.key = Option(meta.filePath)
			attachment.mimeType = Option(meta.mediaType)
			attachment.name = Option(meta.name)
			lecturePlan.attachment = attachment
			entityDao.saveOrUpdate(lecturePlan)
		}

		//教学资料附件
		val materialParts = getAll("materialAttachment", classOf[Part])
		if (materialParts.nonEmpty && materialParts.head.getSize > 0) {
			val blob = EmsApp.getBlobRepository(true)
			val part = materialParts.head
			if (courseBlog.materialAttachment != null && courseBlog.materialAttachment.key.nonEmpty) {
				blob.remove(courseBlog.materialAttachment.key.get)
			}
			val meta = blob.upload("/" + semester.id.toString,
				part.getInputStream, part.getSubmittedFileName, getUser.code + " " + getUser.name)
			val attachment = new Attachment()
			attachment.size = Option(meta.fileSize)
			attachment.key = Option(meta.filePath)
			attachment.mimeType = Option(meta.mediaType)
			attachment.name = Option(meta.name)
			courseBlog.materialAttachment = attachment
		}
		saveOrUpdate(courseBlog)

		redirect("search", "info.save.success")
	}

	def audit(): View = {
		val courseBlogs = entityDao.find(classOf[CourseBlog], longIds("courseBlog"))
		get("passed").orNull match {
			case "1" => {
				var i = 0
				courseBlogs.foreach(courseBlog => {
					if (courseBlog.status != BlogStatus.Draft && courseBlog.status != BlogStatus.Published) {
						i = i + 1
						courseBlog.status = BlogStatus.Passed
						courseBlog.auditor = Option(getUser)
						courseBlog.auditAt = Option(Instant.now())
					}
				})
				entityDao.saveOrUpdate(courseBlogs)
				redirect("search", s"成功审核${i}条课程资料")
			}
			case "0" => {
				var j = 0
				courseBlogs.foreach(courseBlog => {
					if (courseBlog.status != BlogStatus.Draft && courseBlog.status != BlogStatus.Published) {
						j = j + 1
						courseBlog.status = BlogStatus.Unpassed
						courseBlog.auditor = Option(getUser)
						courseBlog.auditAt = Option(Instant.now())
					}
				})
				entityDao.saveOrUpdate(courseBlogs)
				redirect("search", s"成功审核${j}条课程资料")
			}
			case _ => redirect("search", "审核失败")
		}
	}

	def publish(): View = {
		val courseBlogs = entityDao.find(classOf[CourseBlog], longIds("courseBlog"))
		var i = 0
		courseBlogs.foreach(courseBlog => {
			if (courseBlog.status == BlogStatus.Passed) {
				i = i + 1
				courseBlog.status = BlogStatus.Published
				courseBlog.auditor = Option(getUser)
				courseBlog.auditAt = Option(Instant.now())
				courseBlog.awards.foreach(award => {
					award.meta = courseBlog.meta
					entityDao.saveOrUpdate(award)
				})
			}
		})
		entityDao.saveOrUpdate(courseBlogs)
		redirect("search", s"成功发布${i}条课程资料")
	}

	override def removeAndRedirect(courseBlogs: Seq[CourseBlog]): View = {
		val blob = EmsApp.getBlobRepository(true)
		courseBlogs.foreach(courseBlog => {
			courseBlog.description = "--"
			courseBlog.enDescription = "--"
			courseBlog.preCourse = "--"
			courseBlog.books = "--"
			courseBlog.updatedAt = Instant.now()
			courseBlog.materials = None
			courseBlog.website = None
			courseBlog.remark = None
			val syllabuses = getDatas(classOf[model.Syllabus], courseBlog)
			syllabuses.foreach(
				syllabus => {

					if (null != syllabus.attachment && null != syllabus.attachment.key) {
						blob.remove(syllabus.attachment.key.get)
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
						blob.remove(lecturePlan.attachment.key.get)
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
		})
		redirect("search", "info.remove.success")
	}

	//删除附件功能
	@response
	def removeAtta(@param("id") id: Long): Boolean = {
		val blob = EmsApp.getBlobRepository(true)
		val courseBlog = entityDao.get(classOf[CourseBlog], id)
		try {
			blob.remove(courseBlog.materialAttachment.key.get)
			courseBlog.materialAttachment.key = null
			courseBlog.materialAttachment.mimeType = null
			courseBlog.materialAttachment.name = null
			courseBlog.materialAttachment.size = null
			entityDao.saveOrUpdate(courseBlog)
			true
		} catch {
			case e: Exception =>
				logger.info("removeAndRedirect failure", e)
				false
		}
	}

	//保存单个对象
	@response
	def saveAttribute(): Boolean = {
		val id = getLong("id").get
		val courseBlog = entityDao.get(classOf[CourseBlog], id)
		val name = get("name").get
		val value = get("value").get
		if (courseBlog.persisted) {
			try {
				if (name.equals("website")) {
					if (value != "" && !value.startsWith("http://") && !value.startsWith("https://")) {
						val newWebsite = "http://" + value
						courseBlog.website = Option(newWebsite)
					}
				} else {
					println(getUser.code + "(" + getUser.name + ")" + " " + "try to save" + " " + name + "," + " courseBlog_id=" + courseBlog.id + "in " + Instant.now())
					Properties.set(courseBlog, name, value)
				}
				entityDao.saveOrUpdate(courseBlog)
				true
			} catch {
				case e: Exception =>
					logger.info("saveAttribute failure", e)
					false
			}
		} else {
			false
		}
	}
}
