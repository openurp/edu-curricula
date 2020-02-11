[#ftl]
[@b.head/]
[@b.grid  items=reviseTasks var="reviseTask"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("指定负责人",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="20%" property="course.code" title="课程代码"/]
    [@b.col width="30%" property="course.name" title="课程名称"/]
    [@b.col width="25%" property="course.department.name" title="开课院系"/]
    [@b.col width="20%" property="author.name" title="负责人"/]
  [/@]
[/@]
[@b.foot/]