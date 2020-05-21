[#ftl]
[@b.head/]
[@b.toolbar title='课程分组' /]
<table  class="indexpanel">
  <tr>
    <td class="index_view" style="width: 200px;">
    [@b.form name="courseBlogMetaSearchForm"  action="!search" target="courseBlogMetalist" title="ui.searchForm" theme="search"]
      [@b.textfield name="courseBlogMeta.course.code" label="课程代码"/]
      [@b.textfield name="courseBlogMeta.course.name" label="课程名称"/]
      [@b.textfield name="courseBlogMeta.course.credits" label="学分"/]
      [@b.select name="courseBlogMeta.course.department.id" label="开课院系" items=departments?sort_by("code") empty="..."/]
      [@b.select name="courseBlogMeta.course.courseType.id" label="课程类别" items=courseTypes?sort_by("code") empty="..."/]
      [@b.select name="courseBlogMeta.courseGroup.id" label="课程分组" ]
        <option value="">...</option>
        [#list courseGroups as courseGroup]
          <option value="${courseGroup.id}">
            [#if (courseGroup.indexno?split('.'))?size == 2]&nbsp;&nbsp;
            [#elseif (courseGroup.indexno?split('.'))?size == 3]&nbsp;&nbsp;&nbsp;&nbsp;
            [/#if]
            ${courseGroup.name}
          </option>
        [/#list]
      [/@]
      [@b.select name="hasGroup" items={} label="是否设置分组"]
        <option value="">...</option>
        <option value="0">已设置</option>
        <option value="1">未设置</option>
      [/@]
      <input type="hidden" name="orderBy" value="courseBlogMeta.course.code"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="courseBlogMetalist" href="!search?orderBy=courseBlogMeta.course.code" /]</td>
  </tr>
</table>
[@b.foot/]