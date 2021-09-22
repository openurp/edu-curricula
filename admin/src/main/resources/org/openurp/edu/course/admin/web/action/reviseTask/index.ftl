[#ftl]
[@b.head/]
[@b.form name="indexForm" action="" /]
[@b.toolbar]
  bar.addItem("从教学任务导入课程", function() {
  var form = document.reviseTaskSearchForm;
  bg.form.submit(form, "${b.url("import!editNew")}");
  }, "action-new");
[/@]
<div class="search-container">
    <div class="search-panel" style="width: 200px">
    [@b.form name="reviseTaskSearchForm"  action="!search" target="reviseTasklist" title="ui.searchForm" theme="search"]
      [@urp_base.semester name="reviseTask.semester.id" label="学年学期"  value=currentSemester required="true"/]
      [@b.textfield name="reviseTask.course.code" label="课程代码"/]
      [@b.textfield name="reviseTask.course.name" label="课程名称"/]
      [@b.select name="reviseTask.department.id" label="开课院系" items=departments?sort_by("code") empty="..."/]
      [@b.select name="teachers" label="是否多人授课"]
        <option value="">...</option>
        <option value="1">是</option>
        <option value="0">否</option>
      [/@]
      [@b.textfield name="teacherName" label="授课教师"/]
      [@b.textfield name="reviseTask.author.name" label="负责人"/]
      [@b.select name="appointed" label="是否已分配"]
        <option value="">...</option>
        <option value="1">是</option>
        <option value="0">否</option>
      [/@]
      <input type="hidden" name="orderBy" value="reviseTask.course.code"/>
    [/@]
    </div>
    <div class="search-list">[@b.div id="reviseTasklist" href="!search?orderBy=reviseTask.course.code & semester.id="+currentSemester.id /]
    </div>
</div>
[@b.foot/]
<style>
  .search-item{
    width:190px;
  }
</style>
