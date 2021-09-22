[#ftl]
[@b.head/]
[@b.toolbar title="维护课程分组"]bar.addBack();[/@]
[@b.tabs]
  [@b.form action="!saveGroup" theme="list"]
    <input type="hidden" name="metaIds" value="${metaIds}"/>
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
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[/@]
[@b.foot/]
