[#ftl]
[@b.head/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="awardLabelTypeSearchForm" action="!search" target="awardLabelTypelist" title="ui.searchForm" theme="search"]
      [@b.textfields names="awardLabelType.code;代码"/]
      [@b.textfields names="awardLabelType.name;名称"/]
      <input type="hidden" name="orderBy" value="awardLabelType.code"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="awardLabelTypelist" href="!search?orderBy=awardLabelType.code"/]
    </td>
  </tr>
</table>
[@b.foot/]
