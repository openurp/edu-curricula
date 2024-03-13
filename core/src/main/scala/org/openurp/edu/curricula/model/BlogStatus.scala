/*
 * Copyright (C) 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openurp.edu.curricula.model

enum BlogStatus(val id: Int, val name: String) {

  case Draft extends BlogStatus(1, "草稿")
  case Submited extends BlogStatus(2, "已提交")
  case Unpassed extends BlogStatus(3, "未通过")
  case Passed extends BlogStatus(4, "审核通过")
  case Published extends BlogStatus(5, "已发布")

}
