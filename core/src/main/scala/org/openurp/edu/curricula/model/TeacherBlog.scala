package org.openurp.edu.curricula.model

import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Updated
import org.openurp.base.model.User

class TeacherBlog extends LongId with Updated {


	var user: User = _

	/** 个人简介 */
	var intro: String = _

	/** 方向 */
	var research: Option[String] = None

	/** 联系方式 */
	var contact: Option[String] = None

	/** 科研成果 */
	var harvest: Option[String] = None

}
