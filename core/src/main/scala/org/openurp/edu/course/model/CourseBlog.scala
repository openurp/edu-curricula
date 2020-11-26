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
package org.openurp.edu.course.model

import java.time.Instant
import java.util
import java.util.regex.Pattern

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
	var teachers: mutable.Buffer[User] = Collections.newBuffer[User]

	/** 中文简介 */
	var description: String = _

	/** 英文简介 */
	var enDescription: String = _

	/** 开课院系 */
	var department: Department = _

	/** 作者 */
	var author: Option[User] = _

	/** 教材和参考书目 */
	var books: String = _

	/** 教学资料 */
	var materials: Option[String] = None

	/** 教学资料 附件	 */
	var materialAttachment: Attachment = _

	/** 课程网站地址 */
	var website: Option[String] = None

	/** 状态 */
	var status: BlogStatus.Status = BlogStatus.Draft

	/** 审核人 */
	var auditor: Option[User] = None

	/** 审核时间 */
	var auditAt: Option[Instant] = None

	/** 元信息 */
	var meta: Option[CourseBlogMeta] = None

	/** 预修课程 */
	var preCourse: String = _

	/**
	 * 获奖情况
	 */
	var awards: mutable.Buffer[Award] = Collections.newBuffer[Award]

	/** 备注 */
	var remark: Option[String] = None
}

object Transform {

	def getResultsFromHtml(htmlString: String): String = {
		val htmlTagRegEx = "<[a-zA-Z]+.*?>([\\s\\S]*?)<\\/[a-zA-Z ]*?>|<[a-zA-Z]+\\s\\/>"
		val p = Pattern.compile(htmlTagRegEx)
		var m = p.matcher(htmlString)
		var result = ""
		while (m.find) {
			result = m.replaceAll("$1")
			m = p.matcher(result)
		}
		result.trim
	}
}