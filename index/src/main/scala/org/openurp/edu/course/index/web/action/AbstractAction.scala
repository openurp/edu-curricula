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

import org.beangle.data.dao.OqlBuilder
import org.beangle.data.model.Entity
import org.beangle.security.Securities
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.User
import org.openurp.edu.base.model.Semester
import org.openurp.edu.base.web.ProjectSupport
import org.openurp.edu.course.model.CourseBlog

class AbstractAction[T <: Entity[_]] extends RestfulAction[T] with ProjectSupport {

	override def indexSetting(): Unit = {
		put("currentSemester", getCurrentSemester)
		put("departments", getProject.departments)
		super.indexSetting()
	}


	def getDatas[T <: Entity[_]](clazz: Class[T], courseBlog: CourseBlog): Seq[T] = {
		val builder = OqlBuilder.from(clazz, "aa")
		builder.where("aa.course=:course", courseBlog.course)
		builder.where("aa.semester=:semester", courseBlog.semester)
		builder.where("aa.author=:author", courseBlog.author)
		entityDao.search(builder)
	}



}