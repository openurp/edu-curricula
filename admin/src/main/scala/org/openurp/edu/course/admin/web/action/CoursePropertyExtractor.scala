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

import org.beangle.commons.collection.Collections
import org.beangle.data.transfer.exporter.DefaultPropertyExtractor
import org.openurp.base.model.User
import org.openurp.edu.course.app.model.ReviseTask

class CoursePropertyExtractor  extends DefaultPropertyExtractor {

  override def getPropertyValue(target: Object, property: String): Any = {
    if ("teachers" == property) {
      var teacherName = ""
      var teachers = Collections.newBuffer[User]
      if (target.isInstanceOf[ReviseTask]) {
        val reviseTask = target.asInstanceOf[ReviseTask]
        teachers = reviseTask.teachers
      }
      if (teachers.size == 0) {
        return " "
      }
      for (i <- 0 until teachers.size) {
        if (i > 0) {
          teacherName += ","
        }
        teacherName += teachers(i).name
      }
      teacherName
    } else {
      super.getPropertyValue(target, property)
    }
  }

}
