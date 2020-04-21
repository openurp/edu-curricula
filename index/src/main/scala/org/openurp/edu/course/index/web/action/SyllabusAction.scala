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
import org.openurp.edu.course.index.Constants
import org.openurp.edu.course.model.{Attachment, LecturePlan, Syllabus}

class SyllabusAction extends AbstractAction[Syllabus] {

	def attachment(@param("id") id: Long): View = {
		val syllabus = entityDao.get(classOf[Syllabus], id)
		if (null != syllabus.attachment && null != syllabus.attachment.key) {
			val path = Constants.AttachmentBase + syllabus.semester.id.toString + "/" + syllabus.course.id.toString
			val file = new File(path + "/" + syllabus.attachment.key)
			if (file.exists) {
				Stream(file, syllabus.attachment.name)
			} else {
				Status(404)
			}
		} else {
			Status(404)
		}
	}


	def view(@param("id") id: Long): View = {
		val syllabus = entityDao.get(classOf[Syllabus], id)
		if (null != syllabus.attachment && null != syllabus.attachment.key) {
			val path = Constants.AttachmentBase + syllabus.semester.id.toString + "/" + syllabus.course.id.toString
			val file = new File(path + "/" + syllabus.attachment.key)
			if (file.exists) put("syllabus", syllabus)
		}
		forward()
	}


}
