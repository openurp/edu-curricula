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

import org.beangle.ems.app.EmsApp
import org.beangle.webmvc.api.action.ServletSupport
import org.beangle.webmvc.api.annotation.{param, response}
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.curricula.model.LecturePlan

class LecturePlanAction extends RestfulAction[LecturePlan] with ServletSupport{

  def attachment(@param("id") id: Long): View = {
    val lecturePlan = entityDao.get(classOf[LecturePlan], id)
    val path = EmsApp.getBlobRepository(true).path(lecturePlan.attachment.key.get)
    response.sendRedirect(path.get)
    null
  }

  def view(@param("id") id: Long): View = {
    val lecturePlan = entityDao.get(classOf[LecturePlan], id)
    if (null != lecturePlan.attachment && null != lecturePlan.attachment.key) {
      val path = EmsApp.getBlobRepository(true).path(lecturePlan.attachment.key.get)
      put("lecturePlan", lecturePlan)
      put("url",path.get)
    }
    forward()
  }

  @response
  def removeAtta(@param("id") id: Long): Boolean = {
    val blob = EmsApp.getBlobRepository(true)
    val lecturePlan = entityDao.get(classOf[LecturePlan], id)
    try {
      blob.remove(lecturePlan.attachment.key.get)
      entityDao.remove(lecturePlan)
      true
    } catch {
      case e: Exception =>
        logger.info("removeAndRedirect failure", e)
        false
    }
  }

}
