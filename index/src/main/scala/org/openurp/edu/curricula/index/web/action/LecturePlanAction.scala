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

	def view(@param("id") id: Long): View = {
		val lecturePlan = entityDao.get(classOf[LecturePlan], id)
		if (null != lecturePlan.attachment && null != lecturePlan.attachment.key) {
			val file = new File(Constants.AttachmentBase + "lecturePlan/" + lecturePlan.attachment.key)
			if (file.exists) put("lecturePlan", lecturePlan)
		}
		forward()
	}


}
