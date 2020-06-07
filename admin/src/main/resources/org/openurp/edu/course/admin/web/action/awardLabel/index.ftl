[#ftl]
[@b.head/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="awardLabelSearchForm" action="!search" target="awardLabellist" title="ui.searchForm" theme="search"]
      [@b.textfields names="awardLabel.code;代码"/]
      [@b.textfields names="awardLabel.name;名称"/]
      <input type="hidden" name="orderBy" value="awardLabel.code"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="awardLabellist" href="!search?orderBy=awardLabel.code"/]
    </td>
  </tr>
</table>
[@b.foot/]
