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

	/**
	 * 替换掉html标签里面的style内容
	 */
	def replaceStyle(content: String): String = {
		val regEx = " style=\"(.*?)\""
		val p = Pattern.compile(regEx)
		val m = p.matcher(content)
		if (m.find) {
			m.replaceAll("")
		} else {
			content
		}
	}

	/**
	 * 移除掉</br>标签
	 */
	def removeBrTag(src: String): String = {
		if (src != null && !src.isEmpty) {
			src.replaceAll("<br/>", "")
		} else {
			src
		}
	}

	/**
	 * 针对多个标签嵌套的情况进行处理
	 * 比如 <p><span style="white-space: normal;">王者荣耀</span></p>
	 * 预处理并且正则匹配完之后结果是 <span>王者荣耀
	 * 需要手工移除掉前面的起始标签
	 */
	def replaceStartTag(content: String): String = {
		val regEx = "<[a-zA-Z]*?>([\\s\\S]*?)"
		val p = Pattern.compile(regEx)
		val m = p.matcher(content)
		if (m.find) {
			m.replaceAll("")
		}
		else {
			content
		}
	}

	val HTML_TAG_PATTERN = Pattern.compile("<[a-zA-Z]+.*?>([\\s\\S]*?)<\\/[a-zA-Z ]*?>")

	//	def getResultsFromHtml(htmlString: String): mutable.Buffer[String] = {
	//		val results = Collections.newBuffer[String]
	//		// 数据预处理
	//		if (htmlString != null && !htmlString.isEmpty) {
	//			val newHtmlString = replaceStyle(removeBrTag(htmlString))
	//			if (newHtmlString != null && newHtmlString.length > 0) {
	//				val imageTagMatcher = HTML_TAG_PATTERN.matcher(newHtmlString)
	//				// 针对多个并列的标签的情况
	//				while (imageTagMatcher.find) {
	//					// group(1)对应正则表达式中的圆括号括起来的数据
	//					val result = imageTagMatcher.group(1).trim
	//					// 针对多个标签嵌套的情况进行处理
	//					if (result != null && result.length > 0) {
	//						val newResult = replaceStartTag(result)
	//						results += newResult
	//					}
	//				}
	//			}
	//		}
	//		results
	//	}
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