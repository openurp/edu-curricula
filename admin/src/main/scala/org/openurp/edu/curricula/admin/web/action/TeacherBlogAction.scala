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
			redirect("info", "&id=" + teacherBlogs.head.id,null)
		}
	}

	override def saveAndRedirect(teacherBlog: TeacherBlog): View = {
		teacherBlog.user = getUser
		saveOrUpdate(teacherBlog)
		redirect("index", "info.save.success")
	}


}
