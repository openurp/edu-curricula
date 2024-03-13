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

package org.openurp.edu.course.index.web.action

import org.beangle.commons.collection.Collections
import org.beangle.commons.collection.page.PageLimit
import org.beangle.commons.lang.Numbers
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.data.model.Entity
import org.beangle.data.model.util.Hierarchicals
import org.beangle.ems.app.{Ems, EmsApp}
import org.beangle.security.realm.cas.CasConfig
import org.beangle.web.action.annotation.param
import org.beangle.web.action.support.{ActionSupport, ServletSupport}
import org.beangle.web.action.view.{Status, View}
import org.beangle.webmvc.support.helper.QueryHelper
import org.openurp.base.model.{Department, Project}
import org.openurp.code.Code
import org.openurp.edu.curricula.model
import org.openurp.edu.curricula.model.*

import java.time.LocalDate

class IndexAction extends ActionSupport with ServletSupport {

  var entityDao: EntityDao = _
  var casConfig: CasConfig = _

  def nav(): Unit = {
    val departBuilder = OqlBuilder.from(classOf[Department], "department")
    departBuilder.where("department.school=:school", getProject.school)
    departBuilder.where("department.endOn is null")
    departBuilder.where("department.teaching is true")
    departBuilder.orderBy("department.code")
    val departments = entityDao.search(departBuilder.cacheable())
    put("departments", departments)
    put("firstDepartment", departments.head)
    put("awardLabelTypes", getCodes(classOf[AwardLabelType]))
  }

  def index(): View = {
    nav()
    put("portal", Ems.portal)
    put("Ems", Ems)
    put("casConfig", casConfig)
    // 没有父类的分组
    var courseGroups = Collections.newBuffer[CourseGroup]
    val folderBuilder = OqlBuilder.from(classOf[CourseGroup], "courseGroup")
    folderBuilder.orderBy("courseGroup.indexno")
    folderBuilder.cacheable()
    val rs = entityDao.search(folderBuilder)
    rs.foreach(courseGroup => {
      if (courseGroup.parent.isEmpty) {
        courseGroups += courseGroup
      }
    })
    put("courseGroups", courseGroups)
    forward()
  }

  def search(): View = {
    try {
      val courseBlogMetas = entityDao.search(getQueryBuilder)
      put("courseBlogMetas", courseBlogMetas)
      put("blogMap", getBlogMap(courseBlogMetas))
      put("BlogStatus", BlogStatus)
      forward()
    } catch {
      case e: Exception =>
        logger.info("无效字符", e)
        redirect("index", null)
    }
  }

  def getQueryBuilder: OqlBuilder[CourseBlogMeta] = {
    val metaBuilder = OqlBuilder.from(classOf[CourseBlogMeta], "meta")
    get("nameOrCode").foreach(nameOrCode => {
      metaBuilder.where("(meta.course.name like :name or meta.course.code like :code)", s"%$nameOrCode%", s"%$nameOrCode%")
    })
    val first = getInt("courseGroup")
    val second = getInt("courseGroup_child")
    val third = getInt("courseGroup_child_child")
    val groups = Collections.newSet[CourseGroup]
    if (third != None && third != null) {
      groups ++= getCourseGroups(third.get)
    } else if (second != None && second != null) {
      groups ++= getCourseGroups(second.get)
    } else if (first != None && first != null) {
      groups ++= getCourseGroups(first.get)
    }
    if (!groups.isEmpty) {
      metaBuilder.where("meta.courseGroup in :groups", groups)
    }
    get("courseBlog.department.id").foreach { depart =>
      depart match {
        case "0" => metaBuilder.where("meta.course.department.teaching is false ")
        case "" =>
        case _ =>
          val departId = if (Numbers.isDigits(depart)) depart.toInt else 0
          metaBuilder.where("meta.course.department.id=:id", departId)
      }
    }
    metaBuilder.limit(QueryHelper.pageLimit)
    metaBuilder.orderBy("meta.course.code")
  }

  def getCourseGroups(id: Int): Set[CourseGroup] = {
    val courseGroup = entityDao.get(classOf[CourseGroup], id)
    Hierarchicals.getFamily(courseGroup)
  }

  def info(@param("id") id: String): View = {
    if (!Numbers.isDigits(id)) return Status.NotFound
    try {
      val courseBlog = entityDao.get(classOf[CourseBlog], id.toLong)
      put("courseBlog", courseBlog)
      put("BlogStatus", BlogStatus)
      put("Transform", Transform)
      forward()
    } catch {
      case e: Exception =>
        logger.info("无效字符", e)
        redirect("index", null)
    }
  }

  def detail(@param("id") id: String): View = {
    if (!Numbers.isDigits(id)) return Status.NotFound
    nav()
    try {
      val blobQuery = OqlBuilder.from(classOf[CourseBlog], "cb").where("cb.id=:id", id.toLong).cacheable()
      val courseBlog = entityDao.search(blobQuery).head
      put("courseBlog", courseBlog)

      val courseBlogBuilder = OqlBuilder.from(classOf[CourseBlog], "courseBlog")
      courseBlogBuilder.where("courseBlog.course=:course", courseBlog.course)
      courseBlogBuilder.where("courseBlog.status= :status", BlogStatus.Published)
      courseBlogBuilder.where("courseBlog.semester<>:semester", courseBlog.semester)
      courseBlogBuilder.cacheable()
      val hisBlogs = entityDao.search(courseBlogBuilder)
      put("hisBlogs", hisBlogs)

      val syllabuses = getDatas(classOf[model.Syllabus], courseBlog)
      if (!syllabuses.isEmpty) {
        put("syllabus", syllabuses.head)
      }
      val lecturePlans = getDatas(classOf[LecturePlan], courseBlog)
      if (!lecturePlans.isEmpty) {
        put("lecturePlan", lecturePlans.head)
      }
      put("BlogStatus", BlogStatus)
    } catch {
      case e: Exception => return redirect("index", null)
    }
    forward()
  }

  /*
  院系页
   */
  def courseBlogForDepart(@param("id") id: String): View = {
    if (!Numbers.isDigits(id)) return Status.NotFound
    val departId = id.toInt
    nav()
    if (departId != 0) {
      try {
        put("choosedDepartment", entityDao.get(classOf[Department], departId))
      } catch {
        case e: Exception =>
      }
    }
    val metaBuilder = OqlBuilder.from(classOf[CourseBlogMeta], "meta")
    metaBuilder.where("meta.courseGroup is not null")
    metaBuilder.orderBy("meta.course.code")
    metaBuilder.cacheable()
    id match {
      case "0" => metaBuilder.where("meta.course.department.teaching is false")
      case _ => {
        try {
          metaBuilder.where("meta.course.department.id=:id", departId)
        } catch {
          case e: Exception => metaBuilder.where("meta.course.department.teaching is false")
        }
      }
    }
    val courseBlogMetas = entityDao.search(metaBuilder)
    courseBlogMetas.foreach(meta => {
      if (meta.courseGroup.isEmpty) {
        println("-----------------------" + meta.course.name + meta.id + "-----------------------")
      }
    })
    val metaMap = courseBlogMetas.groupBy(_.courseGroup.get)
    put("metaMap", metaMap)
    put("blogMap", getBlogMap(courseBlogMetas))
    val courseGroups = getCodes(classOf[CourseGroup])
    val rootMap = courseGroups.map(x => (x, getRoot(x))).toMap
    val roots = courseGroups.filter(a => a.parent.isEmpty)
    put("roots", roots)
    put("rootMap", rootMap)
    forward()
  }

  def getRoot(courseGroup: CourseGroup): CourseGroup = {
    if (courseGroup.parent.nonEmpty) {
      getRoot(courseGroup.parent.get)
    } else courseGroup
  }

  /*
  课策类别页面
   */
  def courseBlogForType(): View = {
    nav()
    // 没有父类的分组
    var courseGroups = Collections.newBuffer[CourseGroup]
    val folderBuilder = OqlBuilder.from(classOf[CourseGroup], "courseGroup")
    folderBuilder.orderBy("courseGroup.indexno")
    val rs = entityDao.search(folderBuilder)
    rs.foreach(courseGroup => {
      if (courseGroup.parent.isEmpty) {
        courseGroups += courseGroup
      }
    })
    put("courseGroups", courseGroups)
    //    put("semesters", getSemesters)
    //    put("currentSemester", getCurrentSemester)
    forward()
  }

  /*
  代码名称查询界面
   */
  def courseBlogForName(): View = {
    nav()
    val courseBlogMetas = entityDao.search(getQueryBuilder)
    put("courseBlogMetas", courseBlogMetas)
    put("blogMap", getBlogMap(courseBlogMetas))
    forward()
  }

  /*
  获奖分类页
   */
  def awardLabel(): View = {
    nav()
    val metaBuilder = OqlBuilder.from(classOf[CourseBlogMeta], "meta")
    //    val metaBuilder = getQueryBuilder
    getInt("labelTypeId").foreach(labelTypeId => {
      try {
        metaBuilder.where("exists(from meta.awards a where a.awardLabel.labelType.id=:labelTypeId)", labelTypeId)
        put("labelTypeId", labelTypeId)
        put("labelType", entityDao.get(classOf[AwardLabelType], labelTypeId))
        val awardLabels = entityDao.findBy(classOf[AwardLabel], "labelType.id", List(labelTypeId))
        put("awardLabels", awardLabels)
      } catch {
        case e: Exception => return redirect("index", null)
      }
    })
    getInt("labelId").foreach(labelId => {
      try {
        val choosedAwardLabel = entityDao.get(classOf[AwardLabel], labelId)
        put("choosedAwardLabel", choosedAwardLabel)
        metaBuilder.where("exists(from meta.awards a where a.awardLabel.id=:labelId)", labelId)
        val labelTypeId = choosedAwardLabel.labelType.id
        put("labelTypeId", labelTypeId)
        put("labelType", entityDao.get(classOf[AwardLabelType], labelTypeId))
        val awardLabels = entityDao.findBy(classOf[AwardLabel], "labelType.id", List(labelTypeId))
        put("awardLabels", awardLabels)
      } catch {
        case e: Exception => return redirect("index", null)
      }
    })
    put("size", entityDao.search(metaBuilder).size)
    metaBuilder.limit(PageLimit(1, 25))
    val courseBlogMetas = entityDao.search(metaBuilder)
    put("courseBlogMetas", courseBlogMetas)
    put("blogMap", getBlogMap(courseBlogMetas))
    forward()
  }

  def awardLabelMap(): View = {
    nav()
    val labelTypeMap = Collections.newMap[AwardLabelType, Seq[CourseBlogMeta]]
    val blogMap = Collections.newMap[CourseBlogMeta, CourseBlog]
    val awardLabelTypes = getCodes(classOf[AwardLabelType])
    val metaBuilder = OqlBuilder.from(classOf[CourseBlogMeta], "meta")
    awardLabelTypes.foreach(labelType => {
      metaBuilder.where("exists(from meta.awards a where a.awardLabel.labelType=:labelType)", labelType)
      val courseBlogMetas = entityDao.search(metaBuilder)
      labelTypeMap.put(labelType, courseBlogMetas)
      blogMap.++=(getBlogMap(courseBlogMetas))
    })
    put("labelTypeMap", labelTypeMap)
    put("blogMap", blogMap)
    forward()
  }

  def getBlogMap(metas: Seq[CourseBlogMeta]): collection.mutable.Map[CourseBlogMeta, CourseBlog] = {
    val blogMap = Collections.newMap[CourseBlogMeta, CourseBlog]
    val blogBuilder = OqlBuilder.from(classOf[CourseBlog], "courseBlog")
    blogBuilder.where("courseBlog.status =:status", BlogStatus.Published)
    blogBuilder.orderBy("courseBlog.semester desc")
    blogBuilder.where("courseBlog.course =:course")
    blogBuilder.cacheable(true)
    metas.foreach(meta => {
      blogBuilder.param("course", meta.course)
      val courseBlogs = entityDao.search(blogBuilder)
      if (courseBlogs.nonEmpty) {
        blogMap.put(meta, courseBlogs.head)
      }
    })
    blogMap
  }

  def getDatas[T <: Entity[_]](clazz: Class[T], courseBlog: CourseBlog): Seq[T] = {
    val builder = OqlBuilder.from(clazz, "aa")
    builder.where("aa.course=:course", courseBlog.course)
    builder.where("aa.semester=:semester", courseBlog.semester)
    builder.cacheable()
    entityDao.search(builder)
  }

  def childrenAjax(): View = {
    getInt("courseGroupId").foreach(courseGroupId => {
      val courseGroup = entityDao.get(classOf[CourseGroup], courseGroupId)
      val courseGroupChildren = courseGroup.children
      put("courseGroups", courseGroupChildren)
    })
    forward("childrenJSON")
  }

  //  def getCurrentSemester: Semester = {
  //    val builder = OqlBuilder.from(classOf[Semester], "semester")
  //      .where("semester.calendar in(:calendars)", getProject.calendars)
  //    builder.where(":date between semester.beginOn and  semester.endOn", LocalDate.now)
  //    builder.cacheable()
  //    val rs = entityDao.search(builder)
  //    if (rs.isEmpty) { //如果没有正在其中的学期，则查找一个距离最近的
  //      val builder2 = OqlBuilder.from(classOf[Semester], "semester")
  //        .where("semester.calendar in(:calendars)", getProject.calendars)
  //      builder2.orderBy("abs(semester.beginOn - current_date() + semester.endOn - current_date())")
  //      builder2.cacheable()
  //      builder2.limit(1, 1)
  //      entityDao.search(builder2).headOption.orNull
  //    } else {
  //      rs.head
  //    }
  //  }

  def getProject: Project = {
    val builder = OqlBuilder.from(classOf[Project], "project")
    builder.where("project.endOn is null")
    builder.cacheable()
    val projects = entityDao.search(builder)
    if (projects.isEmpty) {
      null
    } else {
      projects.head
    }
  }

  def getCodes[T](clazz: Class[T]): Seq[T] = {
    val query = OqlBuilder.from(clazz, "c")
    if (classOf[Code].isAssignableFrom(clazz)) {
      query.where("c.endOn is null or :now between c.beginOn and c.endOn", LocalDate.now)
    }
    query.cacheable()
    entityDao.search(query)
  }

  def notice(@param("id") id: String): View = {
    nav()
    put("Ems", Ems)
    put("id", id)
    forward()
  }

  def notices(): View = {
    nav()
    put("Ems", Ems)
    forward()
  }

  def attachment(@param("id") id: Long): View = {
    val courseBlog = entityDao.get(classOf[CourseBlog], id)
    val path = EmsApp.getBlobRepository(true).path(courseBlog.materialAttachment.key.get)
    response.sendRedirect(path.get)
    null
  }
}
