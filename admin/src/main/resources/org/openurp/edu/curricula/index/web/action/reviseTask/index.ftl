[#ftl]
[@b.head/]
[@b.form name="indexForm" action="" /]
[@b.toolbar title='修订任务' ]
  bar.addItem("从教学任务导入", function() {
  var form = document.reviseTaskSearchForm;
  bg.form.submit(form, "${b.url("!importFromClazz?semester.id="+currentSemester.id)}");
  }, "action-new");
[/@]
<table  class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="reviseTaskSearchForm"  action="!search" target="reviseTasklist" title="ui.searchForm" theme="search"]
      [@edu_base.semester name="reviseTask.semester.id" label="学年学期"  value=currentSemester required="true"/]
      [@b.textfield name="reviseTask.course.code" label="课程代码"/]
      [@b.textfield name="reviseTask.course.name" label="课程名称"/]
      <input type="hidden" name="orderBy" value="reviseTask.course.code"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="reviseTasklist" href="!search?orderBy=reviseTask.course.code & semester.id="+currentSemester.id /]</td>
  </tr>
</table>
[@b.foot/]