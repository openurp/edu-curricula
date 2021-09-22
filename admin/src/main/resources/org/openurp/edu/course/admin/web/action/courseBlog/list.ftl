[#ftl]
[@b.head/]
[@b.grid  items=courseBlogs var="courseBlog" ]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("审核通过",action.multi("audit","确定通过审核?","passed=1"));
    bar.addItem("审核不通过",action.multi("audit","确定不通过审核?","passed=0"));
    bar.addItem("发布",action.multi('publish'));
[#--    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));--]
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="10%" property="course.code" title="课程代码"/]
    [@b.col width="15%" property="course.name" title="课程名称"][@b.a href="!info?id=${courseBlog.id!}"]${(courseBlog.course.name)!}[/@][/@]
    [@b.col width="21%" property="department.name" title="开课院系"/]
    [@b.col width="10%" property="meta.courseGroup.name" title="课程类别"/]
    [@b.col width="7%" property="author.name" title="负责人"/]
    [@b.col width="10%" property="updatedAt" title="最新修改时间"]${courseBlog.updatedAt?string("yyyy-MM-dd HH:mm")!}[/@]
    [@b.col width="10%" property="status" title="审核状态"]${(courseBlog.status.name)!}[/@]
    [@b.col width="7%" property="auditor.name" title="审核人"/]
    [@b.col width="10%" property="auditAt" title="审核时间"]${(courseBlog.auditAt?string("yyyy-MM-dd HH:mm"))!}[/@]
  [/@]
[/@]
[@b.foot/]
