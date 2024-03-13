/*
 * Copyright (C) 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openurp.edu.course.admin.web.action

import org.beangle.commons.collection.Order
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.transfer.exporter.ExportContext
import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.ExportSupport
import org.openurp.base.edu.model.Course
import org.openurp.base.model.{Project, Semester, User}
import org.openurp.edu.curricula.app.model.ReviseTask
import org.openurp.edu.curricula.model.CourseBlog

class ReviseTaskAction extends AbstractAction[ReviseTask], ExportSupport[ReviseTask] {

  override def indexSetting(): Unit = {
    given project: Project = getProject

    put("currentSemester", getSemester)
    super.indexSetting()
  }

  override def getQueryBuilder: OqlBuilder[ReviseTask] = {
    given project: Project = getProject

    val builder: OqlBuilder[ReviseTask] = OqlBuilder.from(entityClass, simpleEntityName)
    builder.where("reviseTask.semester=:semester", getSemester)
    addDepart(builder, "reviseTask.department")
    get("teachers").foreach {
      case "2" => builder.where("size(reviseTask.teachers) > 1")
      case "1" => builder.where("size(reviseTask.teachers) = 1")
      case "0" => builder.where("size(reviseTask.teachers) = 0")
      case _ =>
    }
    get("appointed").foreach {
      case "1" => builder.where("reviseTask.author is not null")
      case "0" => builder.where("reviseTask.author is null")
      case _ =>
    }
    val teacherName = get("teacherName").orNull
    if (teacherName != null && teacherName != "") {
      builder.where("exists(from reviseTask.teachers t where t.name like :name)", s"%$teacherName%")
    }
    populateConditions(builder)
    builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
  }


  protected override def getSemester(using project: Project): Semester = {
    val semesterString = get("reviseTask.semester.id").orNull
    if (semesterString != null) entityDao.get(classOf[Semester], semesterString.toInt)
    else
      given project: Project = getProject

      super.getSemester
  }

  override def search(): View = {
    given project: Project = getProject

    put("departments", getDeparts)
    put("semester", getSemester)
    super.search()
  }

  override def editSetting(entity: ReviseTask): Unit = {
    super.editSetting(entity)
  }

  override def saveAndRedirect(reviseTask: ReviseTask): View = {
    val semester = if (reviseTask.persisted) reviseTask.semester else entityDao.get(classOf[Semester], getIntId("reviseTask.semester"))
    val course = if (reviseTask.persisted) reviseTask.course else entityDao.findBy(classOf[Course], "code", List(get("reviseTask.course").get)).head
    reviseTask.semester = semester
    reviseTask.course = course
    val courseBlogs = getCourseBlogs(semester, course)
    get("reviseTask.author") match {
      case Some(authorCode) => {
        if (authorCode != "") {
          val author = entityDao.findBy(classOf[User], "code", List(authorCode)).head
          reviseTask.author = Option(author)
          courseBlogs.foreach(courseBlog => {
            courseBlog.author = Option(author)
            entityDao.saveOrUpdate(courseBlog)
          })
        } else {
          courseBlogs.foreach(courseBlog => {
            courseBlog.author = null
            entityDao.saveOrUpdate(courseBlog)
          })
        }
      }
      case None => {
        courseBlogs.foreach(courseBlog => {
          courseBlog.author = null
          entityDao.saveOrUpdate(courseBlog)
        })
      }
    }

    get("reviseTask.author").foreach(authorCode => {
      if (authorCode != "") {
        val author = entityDao.findBy(classOf[User], "code", List(authorCode)).head
        reviseTask.author = Option(author)
        courseBlogs.foreach(courseBlog => {
          courseBlog.author = Option(author)
          entityDao.saveOrUpdate(courseBlog)
        })
      }
    })
    super.saveAndRedirect(reviseTask)
  }

  def appointedAuthor(): View = {
    val ids = getLongIds("reviseTask")
    val builder = OqlBuilder.from(classOf[ReviseTask], "reviseTask")
    builder.where("reviseTask.id in :ids", ids)
    builder.where("size(reviseTask.teachers) = 1")
    val reviseTasks = entityDao.search(builder)
    reviseTasks.foreach(reviseTask => {
      reviseTask.author = Option(reviseTask.teachers.head)
      val courseBlogs = getCourseBlogs(reviseTask.semester, reviseTask.course)
      courseBlogs.foreach(courseBlog => {
        courseBlog.author = Option(reviseTask.teachers.head)
        entityDao.saveOrUpdate(courseBlog)
      })
    })
    entityDao.saveOrUpdate(reviseTasks)
    redirect("search", "info.save.success")
  }

  def getCourseBlogs(semester: Semester, course: Course): Seq[CourseBlog] = {
    val courseBlogBuilder = OqlBuilder.from(classOf[CourseBlog], "courseBlog")
    courseBlogBuilder.where("courseBlog.semester=:semester", semester)
    courseBlogBuilder.where("courseBlog.course=:course", course)
    entityDao.search(courseBlogBuilder)
  }

  override def configExport(context: ExportContext): Unit = {
    context.extractor = new CoursePropertyExtractor()
    super.configExport(context)
  }

}
