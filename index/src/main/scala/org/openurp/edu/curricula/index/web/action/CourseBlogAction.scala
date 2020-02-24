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
package org.openurp.edu.curricula.index.web.action

import java.io.{File, FileOutputStream}
import java.time.Instant
import java.util.Locale

import javax.servlet.http.Part
import org.beangle.commons.codec.digest.Digests
import org.beangle.commons.collection.Order
import org.beangle.commons.io.IOs
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.context.Params
import org.beangle.webmvc.api.view.View
import org.openurp.edu.base.model.{Course, Semester}
import org.openurp.edu.curricula.index.Constants
import org.openurp.edu.curricula.model.{Attachment, CourseBlog, LecturePlan, Syllabus}


class CourseBlogAction extends AbstractAction[CourseBlog] {

	override def getQueryBuilder: OqlBuilder[CourseBlog] = {
		val builder: OqlBuilder[CourseBlog] = OqlBuilder.from(entityName, simpleEntityName)
		builder.where("courseBlog.semester=:semester", getCurrentSemester)
		populateConditions(builder)
		builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
	}

	override def info(id: String): View = {
		val courseBlog = entityDao.get(classOf[CourseBlog], id.toLong)
		val syllabuses = getDatas(classOf[Syllabus], courseBlog)
		if (!syllabuses.isEmpty) {
			put("syllabuses", syllabuses)
		}
		val lecturePlans = getDatas(classOf[LecturePlan], courseBlog)
		if (!lecturePlans.isEmpty) {
			put("lecturePlans", lecturePlans)
		}
		super.info(id)
	}

}
