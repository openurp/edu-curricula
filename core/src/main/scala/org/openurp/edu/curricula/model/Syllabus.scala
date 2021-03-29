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
package org.openurp.edu.curricula.model

import java.util.Locale

import org.beangle.data.model.pojo.Updated
import org.beangle.data.model.{Component, LongId}
import org.openurp.base.edu.model.{Course, Semester}
import org.openurp.base.model.User

/** 教学大纲
 *  每个课程、每个学期、每个语种、每个作者做唯一限制
 */
class Syllabus extends LongId with Updated {
  var course: Course = _

  var locale: Locale = _

  var semester: Semester = _

  var author: User = _

  var attachment: Attachment = new Attachment

  var passed: Boolean = _
}

class Attachment extends Component {
  var name: Option[String] = None
  var size: Option[Integer] = None
  var mimeType: Option[String] = None
  var key: Option[String] = None
}