[#ftl]
[@b.head/]
[@b.grid  items=courseBlogs var="courseBlog" ]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="10%" property="course.code" title="课程代码"/]
    [@b.col width="20%" property="course.name" title="课程名称"][@b.a href="!info?id=${courseBlog.id!}" target="_blank"]${(courseBlog.course.name)!}[/@][/@]
    [@b.col width="20%" property="department.name" title="开课院系"/]
    [@b.col width="10%" property="author.name" title="负责人"/]
    [@b.col width="15%" property="updatedAt" title="最新修改时间"]${courseBlog.updatedAt?string("yyyy-MM-dd HH:mm")!}[/@]
    [@b.col width="10%" property="status" title="审核状态"]${(courseBlog.status.name)!}[/@]
  [/@]
[/@]
[@b.foot/]