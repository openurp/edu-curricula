package org.openurp.edu.curricula.admin.web.action

import org.beangle.webmvc.api.view.View
import org.openurp.edu.curricula.app.model.ReviseSetting

class ReviseSettingAction extends AbstractAction[ReviseSetting] {

	override def saveAndRedirect(reviseSetting: ReviseSetting): View = {
		if (!reviseSetting.persisted) {
			if (duplicate(classOf[ReviseSetting].getName, null, Map("semester" -> reviseSetting.semester))) {
				return redirect("search", "该修订设置存在,请修改修订设置")
			}
		}
		super.saveAndRedirect(reviseSetting)
	}

}
