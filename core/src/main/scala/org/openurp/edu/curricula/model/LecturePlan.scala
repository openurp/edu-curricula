package org.openurp.edu.curricula.model

import java.util.Locale

import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Updated
import org.openurp.base.model.User
import org.openurp.edu.base.model.{Course, Semester}

/** 授课计划
 * 每个课程、每个学期、每个语种、每个作者做唯一限制
 */
class LecturePlan extends LongId with Updated {

  var course: Course = _

  var locale: Locale = _

  var semester: Semester = _

  var author: User = _

  var attachment: Attachment = new Attachment

  var passed: Boolean = _
}
