[#ftl]
[@b.head/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="courseGroupSearchForm" action="!search" target="courseGrouplist" title="ui.searchForm" theme="search"]
      [@b.textfields names="courseGroup.code;代码"/]
      [@b.textfields names="courseGroup.name;名称"/]
      <input type="hidden" name="orderBy" value="courseGroup.code"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="courseGrouplist" href="!search?orderBy=courseGroup.code"/]
    </td>
  </tr>
</table>
[@b.foot/]
