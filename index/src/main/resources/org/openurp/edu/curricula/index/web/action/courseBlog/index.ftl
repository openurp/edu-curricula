[#ftl]
[@b.head/]
[@b.toolbar title='课程资料' /]
<table  class="indexpanel" >
  <tr>
    <td class="index_view" style="width: 80%;">
    [@b.form name="courseBlogSearchForm"  action="!search" target="courseBloglist" title="ui.searchForm" theme="search" ]
      <table width="100%">
        <tr>
          <td align="right">课程代码:</td>
          <td><input type="text" name="courseBlog.course.code" style="width:118px;" /></td>
          <td align="right">课程名称:</td>
          <td><input type="text" name="courseBlog.course.name" style="width:118px;" /></td>
          <td align="right">开课院系:</td>
          <td>
            <select style="width:120px" name="courseBlog.department.id" >
              <option value="">...</option>
              [#list departments?sort_by("code") as department]
              <option value="${department.id}">${department.name}</option>
              [/#list]
            </select>
          </td>
          <td align="right">负责人工号:</td>
          <td><input type="text" name="courseBlog.author.code" style="width:118px;" /></td>
          <td align="right">负责人姓名:</td>
          <td><input type="text" name="courseBlog.author.name" style="width:118px;" /></td>
        </tr>
      </table>
      <input type="hidden" name="orderBy" value="courseBlog.course.code"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="courseBloglist" href="!search?orderBy=courseBlog.course.code"/]</td>
  </tr>
  <tr>
    <td valign="top" height="90%">[@b.div id="courseBloglist"/]</td>
  </tr>
</table>
[@b.foot/]