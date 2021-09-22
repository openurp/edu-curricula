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
package org.openurp.edu.curricula.app.model

import org.beangle.commons.collection.Collections
import org.beangle.data.model.LongId
import org.openurp.base.edu.model.{Course, Semester}
import org.openurp.base.model.{Department, User}

import scala.collection.mutable

/** 修订任务
 *
 */
class ReviseTask extends LongId {

  var semester: Semester = _

  var course: Course = _

  var author: Option[User] = _

  var teachers: mutable.Buffer[User] = Collections.newBuffer[User]

  /** 开课院系 */
  var department: Department = _
}
