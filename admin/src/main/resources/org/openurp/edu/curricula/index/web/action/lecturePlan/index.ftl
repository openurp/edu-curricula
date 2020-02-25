[#ftl]
[@b.head/]
[@b.toolbar title='授课计划维护' /]
<table  class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="lecturePlanSearchForm"  action="!search" target="lecturePlanlist" title="ui.searchForm" theme="search"]
      [@edu_base.semester name="lecturePlan.semester.id" label="学年学期"  value=currentSemester required="true"/]
      [@b.textfield name="lecturePlan.course.code" label="课程代码"/]
      [@b.textfield name="lecturePlan.course.name" label="课程名称"/]
      [@b.select name="lecturePlan.locale" label="语言" items=languages  empty="..."/]
      <input type="hidden" name="orderBy" value="lecturePlan.course.code"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="lecturePlanlist" href="!search?orderBy=lecturePlan.course.code & lecturePlan.semester.id="+currentSemester.id /]</td>
  </tr>
</table>
[@b.foot/]