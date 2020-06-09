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

import org.beangle.webmvc.api.action.ServletSupport
import org.beangle.webmvc.api.annotation.param
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.app.UrpApp
import org.openurp.edu.course.model.LecturePlan

class LecturePlanAction extends RestfulAction[LecturePlan] with ServletSupport{

	def attachment(@param("id") id: Long): View = {
		val lecturePlan = entityDao.get(classOf[LecturePlan], id)
		val path = UrpApp.getBlobRepository(true).url(lecturePlan.attachment.key)
		response.sendRedirect(path.get.toString)
		null
	}


	def view(@param("id") id: Long): View = {
		val lecturePlan = entityDao.get(classOf[LecturePlan], id)
		if (null != lecturePlan.attachment && null != lecturePlan.attachment.key) {
			val path = UrpApp.getBlobRepository(true).url(lecturePlan.attachment.key)
			put("lecturePlan", lecturePlan)
			put("url",path.get.toString)
		}
		forward()
	}

}
