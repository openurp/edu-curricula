package org.openurp.edu.curricula.admin.web.action

import org.apache.poi.ss.formula.functions.T
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
			val reviseTasks = entityDao.getAll(classOf[ReviseTask])
			if (!reviseTasks.exists(x => x.semester == semester && x.course == clazz.course)) {
				val reviseTask = new ReviseTask
				reviseTask.semester = semester
				reviseTask.course = clazz.course
				reviseTask.teachers = clazz.teachers.map(_.user)
				entityDao.saveOrUpdate(reviseTask)
			} else {
				val reviseTask = reviseTasks.find(x => x.semester == semester && x.course == clazz.course)
				reviseTask.foreach(rt => {
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
		val course = if (reviseTask.persisted) reviseTask.course else entityDao.getAll(classOf[Course]).find(_.code == get("reviseTask.course").get).get
		reviseTask.semester = semester
		reviseTask.course = course
		get("reviseTask.author").foreach(authorCode => {
			val author = entityDao.getAll(classOf[User]).find(_.code == authorCode)
			reviseTask.author = author
		})
		super.saveAndRedirect(reviseTask)
	}

}
