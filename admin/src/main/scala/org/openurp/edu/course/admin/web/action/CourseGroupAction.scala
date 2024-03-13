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

import java.time.Instant

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.model.util.Hierarchicals
import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.RestfulAction
import org.openurp.edu.curricula.model.{CourseBlogMeta, CourseGroup}

class CourseGroupAction extends RestfulAction[CourseGroup] {

  protected override def editSetting(courseGroup: CourseGroup): Unit = {

    var folders = Collections.newBuffer[CourseGroup]
    // 查找可以作为父节点的分组
    val folderBuilder = OqlBuilder.from(classOf[CourseGroup], "courseGroup")
    folderBuilder.orderBy("courseGroup.indexno")
    val rs = entityDao.search(folderBuilder)
    folders ++= rs
    courseGroup.parent foreach { p =>
      if (!folders.contains(p)) folders += p
    }
    folders --= Hierarchicals.getFamily(courseGroup)
    put("parents", folders)
  }

  protected override def removeAndRedirect(courseGroups: Seq[CourseGroup]): View = {
    val parents = Collections.newBuffer[CourseGroup]
    courseGroups.foreach(courseGroup => {
      courseGroup.parent foreach { p =>
        p.children -= courseGroup
        parents += p
      }
    })
    entityDao.saveOrUpdate(parents)
    val courseBlogMetas = entityDao.findBy(classOf[CourseBlogMeta], "courseGroup", courseGroups)
    courseBlogMetas.foreach(courseBlogMeta => {
      courseBlogMeta.courseGroup = None
      courseBlogMeta.updatedAt = Instant.now()
    })
    entityDao.saveOrUpdate(courseBlogMetas)
    super.removeAndRedirect(courseGroups)
  }

  protected override def saveAndRedirect(courseGroup: CourseGroup): View = {
//    val a =get("courseGroup.color")
    val newParentId = getInt("parent.id")
    val indexno = getInt("indexno", 0)
    var parent: CourseGroup = null
    if (newParentId.isDefined) parent = entityDao.get(classOf[CourseGroup], newParentId.get)
    move(courseGroup, parent, indexno)
    entityDao.evict(courseGroup)
    if (null != parent) {
      entityDao.evict(parent)
    }
    redirect("search", "info.save.success")
  }

  def move(courseGroup: CourseGroup, location: CourseGroup, index: Int): Unit = {
    courseGroup.parent foreach { p =>
      if (null == location || p != location) {
        courseGroup.parent = None
        entityDao.saveOrUpdate(courseGroup)
        entityDao.refresh(p)
      }
    }

    val nodes =
      if (null != location) {
        Hierarchicals.move(courseGroup, location, index)
      } else {
        val builder = OqlBuilder.from(classOf[CourseGroup], "courseGroup")
          .where("courseGroup.parent is null")
          .orderBy("courseGroup.indexno")
        Hierarchicals.move(courseGroup, entityDao.search(builder).toBuffer, index)
      }
    entityDao.saveOrUpdate(nodes)

    if (null != location) {
      entityDao.refresh(location)
    }
  }

}
