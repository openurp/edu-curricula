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
import org.openurp.edu.clazz.model.Clazz
import org.openurp.edu.curricula.app.model.ReviseTask

class ReviseTaskAction extends AbstractAction[ReviseTask] {

	override def indexSetting(): Unit = {
		put("currentSemester", getSemester)
		super.indexSetting()
	}

	override def getQueryBuilder: OqlBuilder[ReviseTask] = {
		val builder: OqlBuilder[ReviseTask] = OqlBuilder.from(entityName, simpleEntityName)
		builder.where("reviseTask.semester=:semester", getSemester)
		addDepart(builder, "reviseTask.course.department")
		get("teachers").foreach(e => {
			e match {
				case "1" => builder.where("size(reviseTask.teachers) > 1")
				case "0" => builder.where("size(reviseTask.teachers) = 1")
				case _ =>
			}
		})
		val teacherName = get("teacherName").orNull
		if (teacherName != null && teacherName != "") {
			builder.where("exists(from reviseTask.teachers t where t.name like :name)", '%' + teacherName + '%')
		}
		populateConditions(builder)
		builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
	}

	def getSemester(): Semester = {
		val semesterString = get("reviseTask.semester.id").orNull
		if (semesterString != null) entityDao.get(classOf[Semester], semesterString.toInt) else getCurrentSemester
	}


	override def editSetting(entity: ReviseTask): Unit = {
		super.editSetting(entity)
	}

	def importFromClazz(): View = {
		val semester = getSemester
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
					if (!clazz.teachers.isEmpty) {
						clazz.teachers.map(_.user).foreach(user => {
							if (!rt.teachers.contains(user)) {
								rt.teachers.addOne(user)
								entityDao.saveOrUpdate(rt)
							}
						})
					}
				})
			}
		})
		redirect("search", "&reviseTask.semester.id=" + semester.id, null)
	}


	override def saveAndRedirect(reviseTask: ReviseTask): View = {
		val semester = if (reviseTask.persisted) reviseTask.semester else entityDao.get(classOf[Semester], intId("reviseTask.semester"))
		val course = if (reviseTask.persisted) reviseTask.course else entityDao.findBy(classOf[Course], "code", List(get("reviseTask.course").get)).head
		reviseTask.semester = semester
		reviseTask.course = course
		get("reviseTask.author").foreach(authorCode => {
			if (authorCode != "") {
				val author = entityDao.findBy(classOf[User], "code", List(authorCode)).head
				reviseTask.author = Option(author)
			}
		})
		super.saveAndRedirect(reviseTask)
	}


	def appointedAuthor(): View = {
		val ids = longIds("reviseTask")
		val builder = OqlBuilder.from(classOf[ReviseTask], "reviseTask")
		builder.where("reviseTask.id in :ids", ids)
		builder.where("size(reviseTask.teachers) = 1")
		val reviseTasks = entityDao.search(builder)
		reviseTasks.foreach(reviseTask => {
			reviseTask.author = Option(reviseTask.teachers.head)
		})
		entityDao.saveOrUpdate(reviseTasks)
		redirect("search", "info.save.success")
	}
}
