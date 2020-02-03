package org.openurp.edu.curricula.model

import java.util.Locale

import org.beangle.data.model.pojo.{Named, Updated}
import org.beangle.data.model.{Component, LongId}
import org.openurp.base.model.User
import org.openurp.edu.base.model.{Course, Semester}

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

class Attachment extends Component with Named {
  var size: Int = _
  var mimeType: String = _
  var key: String = _
}
