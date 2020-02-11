package org.openurp.edu.curricula.admin.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.User
import org.openurp.edu.base.model.Course
import org.openurp.edu.base.web.ProjectSupport

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
//		get("term").orNull match {
//			case codeOrName =>{
//				query.where("(user.name like :name or user.code like :code)", '%' + codeOrName + '%', '%' + codeOrName + '%')
//			}
//			case null =>
//		}
		query.limit(getPageLimit)
		put("users", entityDao.search(query))
		forward("usersJSON")
	}

}
