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
package org.openurp.edu.curricula.admin.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.openurp.edu.curricula.model.TeacherBlog

class TeacherBlogAction extends AbstractAction[TeacherBlog] {

	override def index(): View = {
		val builder = OqlBuilder.from(classOf[TeacherBlog], "tb")
		builder.where("tb.user=:user", getUser)
		val teacherBlogs = entityDao.search(builder)
		if (teacherBlogs.isEmpty) {
			redirect("editNew")
		} else {
			redirect("info", "&id=" + teacherBlogs.head.id, null)
		}
	}

	override def editSetting(entity:TeacherBlog): Unit = {
		put("user", getUser)
		super.editSetting(entity)
	}

	override def saveAndRedirect(teacherBlog: TeacherBlog): View = {
		teacherBlog.user = getUser
		saveOrUpdate(teacherBlog)
		redirect("index", "info.save.success")
	}


}
