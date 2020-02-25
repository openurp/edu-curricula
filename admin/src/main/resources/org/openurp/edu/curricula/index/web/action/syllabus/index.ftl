[#ftl]
[@b.head/]
[@b.toolbar title='教学大纲维护' /]
<table  class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="syllabusSearchForm"  action="!search" target="syllabuslist" title="ui.searchForm" theme="search"]
      [@edu_base.semester name="syllabus.semester.id" label="学年学期"  value=currentSemester required="true"/]
      [@b.textfield name="syllabus.course.code" label="课程代码"/]
      [@b.textfield name="syllabus.course.name" label="课程名称"/]
      [@b.select name="syllabus.locale" label="语言" items=languages  empty="..."/]
      <input type="hidden" name="orderBy" value="syllabus.course.code"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="syllabuslist" href="!search?orderBy=syllabus.course.code & semester.id="+currentSemester.id /]</td>
  </tr>
</table>
[@b.foot/]