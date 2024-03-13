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

import org.beangle.data.dao.OqlBuilder
import org.beangle.web.action.view.View
import org.openurp.base.edu.model.Course
import org.openurp.base.model.{Project, Semester}
import org.openurp.edu.clazz.model.Clazz
import org.openurp.edu.curricula.app.model.ReviseTask
import org.openurp.edu.curricula.model
import org.openurp.edu.curricula.model.{CourseBlog, CourseBlogMeta}

import java.time.Instant

class ImportAction extends AbstractAction[ReviseTask] {

  override def editSetting(entity: ReviseTask): Unit = {
    given project: Project = getProject

    put("currentSemester", getSemester)
    super.editSetting(entity)
  }

  def importFromClazz(): View = {
    given project: Project = getProject

    val semester = getSemester
    val clazzBuilder = OqlBuilder.from(classOf[Clazz], "clazz")
    clazzBuilder.where("clazz.semester=:semeter and clazz.project=:project", semester, project)

    val clazzes = entityDao.search(clazzBuilder)
    println(s"find ${clazzes.size} tasks")

    var value = 0
    clazzes.foreach(clazz => {
      val metas = entityDao.findBy(classOf[CourseBlogMeta], "course", List(clazz.course))
      var meta: CourseBlogMeta = metas.headOption.orNull
      if (metas.isEmpty) {
        meta = new CourseBlogMeta
        meta.course = clazz.course
        meta.author = getUser
        meta.updatedAt = Instant.now()
        entityDao.saveOrUpdate(meta)
      }

      val reviseTaskBuilder = OqlBuilder.from(classOf[ReviseTask], "reviseTask")
      reviseTaskBuilder.where("reviseTask.semester=:semester", semester)
      reviseTaskBuilder.where("reviseTask.course=:course", clazz.course)
      val reviseTasks = entityDao.search(reviseTaskBuilder)
      if (reviseTasks.isEmpty) {
        val reviseTask = new ReviseTask
        reviseTask.semester = semester
        reviseTask.course = clazz.course
        reviseTask.teachers = clazz.teachers.map(_.user)
        reviseTask.department = clazz.teachDepart
        entityDao.saveOrUpdate(reviseTask)
        value += 1
      } else {
        reviseTasks.foreach(rt => {
          rt.department = clazz.teachDepart
          if (!clazz.teachers.isEmpty) {
            clazz.teachers.map(_.user).foreach(user => {
              if (!rt.teachers.contains(user)) {
                rt.teachers.addOne(user)
              }
            })
          }
          entityDao.saveOrUpdate(rt)
          entityDao.saveOrUpdate(reviseTasks)
        })
      }

      val courseBlogs = getCourseBlogs(semester, clazz.course)
      if (courseBlogs.isEmpty) {
        val courseBlog = new CourseBlog
        courseBlog.semester = semester
        courseBlog.course = clazz.course
        reviseTasks.foreach(reviseTask => {
          reviseTask.teachers.foreach(teacher => {
            if (!courseBlog.teachers.contains(teacher)) {
              courseBlog.teachers += teacher
            }
          })
        })
        courseBlog.description = "--"
        courseBlog.enDescription = "--"
        courseBlog.books = "--"
        courseBlog.preCourse = "--"
        courseBlog.department = clazz.teachDepart
        courseBlog.updatedAt = Instant.now()
        courseBlog.meta = Some(meta)
        entityDao.saveOrUpdate(courseBlog)
      } else {
        courseBlogs.foreach(cb => {
          cb.department = clazz.teachDepart
          reviseTasks.foreach(reviseTask => {
            reviseTask.teachers.foreach(teacher => {
              if (!cb.teachers.contains(teacher)) {
                cb.teachers += teacher
              }
            })
          })
          entityDao.saveOrUpdate(cb)
        })
      }
      put("value", value)
    })

    println("new revise task build complete.")
    //删除不存在任务的courseBlog,reviseTask
    entityDao.findBy(classOf[CourseBlog], "semester", List(getSemester)).foreach(blog => {
      val hasSyllabus = duplicate(classOf[model.Syllabus].getName, null, Map("semester" -> semester, "course" -> blog.course))
      val hasClazz = duplicate(classOf[Clazz].getName, null, Map("semester" -> semester, "course" -> blog.course))
      if (!hasSyllabus && !hasClazz) {
        entityDao.remove(blog)
        val reviseTaskBuilder = OqlBuilder.from(classOf[ReviseTask], "reviseTask")
        reviseTaskBuilder.where("reviseTask.semester=:semester", semester)
        reviseTaskBuilder.where("reviseTask.course=:course", blog.course)
        entityDao.remove(entityDao.search(reviseTaskBuilder))
      }
    })
    forward()
  }

  protected override def getSemester(using project: Project): Semester = {
    val semesterString = get("reviseTask.semester.id").orNull
    if (semesterString != null) entityDao.get(classOf[Semester], semesterString.toInt)
    else
      given project: Project = getProject

      super.getSemester
  }

  def getCourseBlogs(semester: Semester, course: Course): Seq[CourseBlog] = {
    val courseBlogBuilder = OqlBuilder.from(classOf[CourseBlog], "courseBlog")
    courseBlogBuilder.where("courseBlog.semester=:semester", semester)
    courseBlogBuilder.where("courseBlog.course=:course", course)
    entityDao.search(courseBlogBuilder)
  }
}
