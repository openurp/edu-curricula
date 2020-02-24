[#ftl]
[@b.head/]
[@b.toolbar title='课程资料维护' /]
<table  class="indexpanel" >
  <tr>
    <td class="index_view" style="width: 180px;">
    [@b.form name="courseBlogSearchForm"  action="!search" target="courseBloglist" title="ui.searchForm" theme="search" ]
      [@b.select name="courseBlog.semester.id" label="学年学期" items=semesters  value=currentSemester option="id,code"/]
      [@b.textfield name="courseBlog.course.code" label="课程代码"/]
      [@b.textfield name="courseBlog.course.name" label="课程名称"/]
      [@b.select name="courseBlog.department.id" label="开课院系" items=departments?sort_by("code") empty="..."/]
      [@b.textfield name="courseBlog.author.code" label="负责人工号"/]
      [@b.textfield name="courseBlog.author.name" label="负责人姓名"/]
      <input type="hidden" name="orderBy" value="courseBlog.course.code"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="courseBloglist" href="!search?orderBy=courseBlog.course.code & courseBlog.semester.id="+currentSemester.id /]</td>
  </tr>
</table>
[@b.foot/]