package org.openurp.edu.curricula.model

import org.beangle.commons.collection.Collections
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Updated
import org.openurp.base.model.{Department, User}
import org.openurp.edu.base.model.{Course, Semester}

import scala.collection.mutable

/** 课程资料
 *
 */
class CourseBlog extends LongId with Updated {

  /** 学年学期 */
  var semester: Semester = _

  /** 课程 */
  var course: Course = _

  /** 授课教师 */
  var teachers: mutable.Set[User] = Collections.newSet[User]

  /** 简介 */
  var description: String = _

  /** 英文简介 */
  var enDescription: Option[String] = None

  /** 开课院系 */
  var department: Department = _

  /** 作者 */
  var author: User = _

  /** 教材和辅助资料 */
  var materials: Option[String] = None

  /** 课程网站 */
  var website: Option[String] = None

  /** 状态 */
  var status: BlogStatus.Status = BlogStatus.Draft
}