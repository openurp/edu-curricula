package org.openurp.edu.curricula.model

import org.beangle.data.model.IntId
import org.beangle.data.model.pojo.Updated
import org.openurp.base.model.User
import org.openurp.edu.base.model.Course

class CourseBlogMeta extends IntId with Updated {

	var course: Course = _

	var courseGroup: Option[CourseGroup] = _
	/*
	CourseBlog的数量
	 */
	var count: Int = _
	/*
	最后更新作者
	 */
	var author: User = _

}
