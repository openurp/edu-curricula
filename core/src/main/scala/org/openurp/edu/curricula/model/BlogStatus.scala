package org.openurp.edu.curricula.model

object BlogStatus extends Enumeration(0) {

  class Status(val name: String) extends super.Val {
  }

  val Draft = new Status("草稿")
  val Submited = new Status("已提交")
  val Unpassed = new Status("未通过")
  val Passed = new Status("审核通过")
  val Published = new Status("已发布")

}