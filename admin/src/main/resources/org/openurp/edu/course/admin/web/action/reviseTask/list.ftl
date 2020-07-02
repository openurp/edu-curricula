[#ftl]
[@b.head/]
[@b.grid  items=reviseTasks var="reviseTask"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("分配负责人",action.edit());
    bar.addItem("指定唯一的授课教师为负责人",action.multi('appointedAuthor'));
[#--    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));--]
    bar.addItem("${b.text("action.export")}",action.exportData("course.code:课程代码,course.name:课程名称,course.department.name:开课院系,course.credits:学分,course.creditHours:学时,author.name:负责人,teachers:授课教师",null,
    'fileName=[#if departments?size==1]${semester.schoolYear}学年第${semester.name}学期${(departments[0].name)!}学院开课课程一览表[#else ]${semester.schoolYear}学年${semester.name}学期开课课程一览表[/#if]'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="15%" property="course.code" title="课程代码"/]
    [@b.col width="30%" property="course.name" title="课程名称"/]
    [@b.col width="25%" property="course.department.name" title="开课院系"/]
    [@b.col width="10%" property="course.credits" title="学分"/]
    [@b.col width="15%" property="author.name" title="负责人"/]
  [/@]
[/@]
[@b.foot/]