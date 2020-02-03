package org.openurp.edu.curricula.app.model

import org.beangle.data.model.LongId
import org.openurp.base.model.User
import org.openurp.edu.base.model.{Course, Semester}

/** 修订任务
 *
 */
class ReviseTask extends LongId {

  var semester: Semester = _

  var course: Course = _

  var user: User = _
}
