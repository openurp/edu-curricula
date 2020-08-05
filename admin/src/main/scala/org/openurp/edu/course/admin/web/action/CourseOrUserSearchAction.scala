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

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.User
import org.openurp.edu.base.model.Course
import org.openurp.edu.web.ProjectSupport

import scala.xml.Null

class CourseOrUserSearchAction extends RestfulAction[Course] with ProjectSupport {

	def courseAjax(): View = {
		val query = OqlBuilder.from(classOf[Course], "course")
		query.orderBy("course.code")
		query.where("course.project = :project", getProject)
		populateConditions(query)
		get("term").foreach(codeOrName => {
			query.where("(course.name like :name or course.code like :code)", '%' + codeOrName + '%', '%' + codeOrName + '%')
		})
		query.limit(getPageLimit)
		put("courses", entityDao.search(query))
		forward("coursesJSON")
	}

	def userAjax(): View = {
		val query = OqlBuilder.from(classOf[User], "user")
		query.orderBy("user.code")
		populateConditions(query)
		get("term").foreach(codeOrName => {
			query.where("(user.name like :name or user.code like :code)", '%' + codeOrName + '%', '%' + codeOrName + '%')
		})
		query.limit(getPageLimit)
		put("users", entityDao.search(query))
		forward("usersJSON")
	}

}
