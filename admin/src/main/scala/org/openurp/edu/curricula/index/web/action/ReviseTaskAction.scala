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

import org.beangle.commons.collection.Order
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.openurp.base.model.User
import org.openurp.edu.base.model.{Course, Semester}
import org.openurp.edu.course.model.Clazz
import org.openurp.edu.curricula.app.model.ReviseTask

class ReviseTaskAction extends AbstractAction[ReviseTask] {

	override def getQueryBuilder: OqlBuilder[ReviseTask] = {
		val builder: OqlBuilder[ReviseTask] = OqlBuilder.from(entityName, simpleEntityName)
		builder.where("reviseTask.semester=:semester", getSemester)
		populateConditions(builder)
		builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
	}

	def getSemester(): Semester = {
		val semesterString = get("reviseTask.semester.id").orNull
		if (semesterString != null) entityDao.get(classOf[Semester], semesterString.toInt) else getCurrentSemester
	}


	override def editSetting(entity: ReviseTask): Unit = {
		val users = entityDao.getAll(classOf[User])
		if (!entity.teachers.isEmpty) {
			val newUsers = users.appendedAll(entity.teachers)
			put("users", newUsers)
		} else {
			put("users", users)
		}
		super.editSetting(entity)
	}

	def importFromClazz(): View = {
		val semester = entityDao.get(classOf[Semester], intId("semester"))
		val clazzBuilder = OqlBuilder.from(classOf[Clazz], "clazz")
		clazzBuilder.where("clazz.semester=:semeter", semester)
		val clazzes = entityDao.search(clazzBuilder)
		clazzes.foreach(clazz => {
			val reviseTaskBuilder = OqlBuilder.from(classOf[ReviseTask], "reviseTask")
			reviseTaskBuilder.where("reviseTask.semester=:semester", semester)
			reviseTaskBuilder.where("reviseTask.course=:course", clazz.course)
			val reviseTasks = entityDao.search(reviseTaskBuilder)
			if (reviseTasks.isEmpty) {
				val reviseTask = new ReviseTask
				reviseTask.semester = semester
				reviseTask.course = clazz.course
				reviseTask.teachers = clazz.teachers.map(_.user)
				entityDao.saveOrUpdate(reviseTask)
			} else {
				reviseTasks.foreach(rt => {
					clazz.teachers.map(_.user).foreach(user => {
						if (!rt.teachers.contains(user)) {
							rt.teachers.addOne(user)
							entityDao.saveOrUpdate(rt)
						}
					})
				})
			}
		})
		redirect("index")
	}


	override def saveAndRedirect(reviseTask: ReviseTask): View = {
		val semester = if (reviseTask.persisted) reviseTask.semester else entityDao.get(classOf[Semester], intId("reviseTask.semester"))
		val course = if (reviseTask.persisted) reviseTask.course else entityDao.findBy(classOf[Course],"code",List(get("reviseTask.course").get)).head
		reviseTask.semester = semester
		reviseTask.course = course
		get("reviseTask.author").foreach(authorCode => {
//			val userBuilder = OqlBuilder.from(classOf[User],"user")
//			userBuilder.where("user.code=:code",authorCode)
			val author = entityDao.getAll(classOf[User]).find(_.code == authorCode)
			reviseTask.author = author
		})
		super.saveAndRedirect(reviseTask)
	}

}
