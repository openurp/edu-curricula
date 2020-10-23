[#ftl]
[@b.head/]
<div class="search-container">
    <div class="search-panel"  style="width: 200px">
    [@b.form name="courseBlogMetaSearchForm"  action="!search" target="courseBlogMetalist" title="ui.searchForm" theme="search"]
      [@b.textfield name="courseBlogMeta.course.code" label="课程代码"/]
      [@b.textfield name="courseBlogMeta.course.name" label="课程名称"/]
      [@b.textfield name="courseBlogMeta.course.credits" label="学分"/]
      [@b.select name="courseBlogMeta.course.courseType.id" label="课程类别" items=courseTypes?sort_by("code") empty="..."/]
      [@b.select name="courseGroup.id" label="课程分组" ]
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
    </div>
    <div class="search-list">[@b.div id="courseBlogMetalist" href="!search?orderBy=courseBlogMeta.course.code" /]
    </div>
</div>
[@b.foot/]
<style>
  .search-item{
    width:190px;
  }