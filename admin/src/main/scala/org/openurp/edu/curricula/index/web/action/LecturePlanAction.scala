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
package org.openurp.edu.curricula.index.web.action

import java.io.{File, FileOutputStream}
import java.time.Instant

import javax.servlet.http.Part
import org.beangle.commons.codec.digest.Digests
import org.beangle.commons.collection.Order
import org.beangle.commons.io.IOs
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.security.Securities
import org.beangle.webmvc.api.annotation.param
import org.beangle.webmvc.api.context.Params
import org.beangle.webmvc.api.view.{Status, Stream, View}
import org.openurp.base.model.User
import org.openurp.edu.base.model.{Course, Semester}
import org.openurp.edu.curricula.index.Constants
import org.openurp.edu.curricula.model.{Attachment, CourseBlog, LecturePlan}

class LecturePlanAction extends AbstractAction[LecturePlan] {


	override def getQueryBuilder: OqlBuilder[LecturePlan] = {
		val builder: OqlBuilder[LecturePlan] = OqlBuilder.from(entityName, simpleEntityName)
		builder.where("lecturePlan.semester=:semester", getSemester)
		populateConditions(builder)
		builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
	}

	def getSemester(): Semester = {
		val semesterString = get("lecturePlan.semester.id").orNull
		if (semesterString != null) entityDao.get(classOf[Semester], semesterString.toInt) else getCurrentSemester
	}


	def attachment(@param("id") id: Long): View = {
		val lecturePlan = entityDao.get(classOf[LecturePlan], id)
		if (null != lecturePlan.attachment && null != lecturePlan.attachment.key) {
			val file = new File(Constants.AttachmentBase + "lecturePlan/" + lecturePlan.attachment.key)
			if (file.exists) {
				Stream(file, lecturePlan.attachment.name)
			} else {
				Status(404)
			}
		} else {
			Status(404)
		}
	}

	override def saveAndRedirect(lecturePlan: LecturePlan): View = {
		val user = getUser
		val course = entityDao.getAll(classOf[Course]).find(_.code == get("lecturePlan.course").get).get
		if (!lecturePlan.persisted) {
			if (duplicate(classOf[LecturePlan].getName, null, Map("semester" -> lecturePlan.semester, "author" -> user, "course" -> course, "locale" -> lecturePlan.locale))) {
				return redirect("search", "该授课计划存在,请修改计划")
			}
		}
		lecturePlan.author = user
		lecturePlan.course = course
		if (!getAll("attachment").exists(_ == "")) {
			if (null != lecturePlan.attachment && null != lecturePlan.attachment.key) {
				val file = new File(Constants.AttachmentBase + "lecturePlan/" + lecturePlan.attachment.key)
				if (file.exists()) file.delete()
			}
			val parts = Params.getAll("attachment").asInstanceOf[List[Part]]
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

		super.saveAndRedirect(lecturePlan)
	}


	override def remove(): View = {
		val lecturePlanes = entityDao.find(classOf[LecturePlan], longIds("lecturePlan"))
		lecturePlanes.foreach {
			lecturePlan =>
				val file = new File(Constants.AttachmentBase + "lecturePlan/" + lecturePlan.attachment.key)
				if (file.exists()) file.delete()
		}
		super.remove()
	}


	def view(@param("id") id: Long): View = {
		val lecturePlan = entityDao.get(classOf[LecturePlan], id)
		if (null != lecturePlan.attachment && null != lecturePlan.attachment.key) {
			val file = new File(Constants.AttachmentBase + "lecturePlan/" + lecturePlan.attachment.key)
			if (file.exists) put("lecturePlan", lecturePlan)
		}
		forward()
	}


}
