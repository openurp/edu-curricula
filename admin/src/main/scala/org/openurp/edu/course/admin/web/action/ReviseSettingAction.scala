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
package org.openurp.edu.course.admin.web.action

import org.beangle.webmvc.api.view.View
import org.openurp.edu.course.app.model.ReviseSetting

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
