[#ftl]
[@b.head/]
<div class="search-container">
    <div class="search-panel">
    [@b.form name="awardLabelTypeSearchForm" action="!search" target="awardLabelTypelist" title="ui.searchForm" theme="search"]
      [@b.textfields names="awardLabelType.code;代码"/]
      [@b.textfields names="awardLabelType.name;名称"/]
      <input type="hidden" name="orderBy" value="awardLabelType.code"/>
    [/@]
    </div>
    <div class="search-list">[@b.div id="awardLabelTypelist" href="!search?orderBy=awardLabelType.code"/]
    </div>
    </div>
[@b.foot/]
