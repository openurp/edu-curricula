[#ftl]
[@b.head/]
<div class="search-container">
    <div class="search-panel">
    [@b.form name="awardLabelSearchForm" action="!search" target="awardLabellist" title="ui.searchForm" theme="search"]
      [@b.textfields names="awardLabel.code;代码"/]
      [@b.textfields names="awardLabel.name;名称"/]
      <input type="hidden" name="orderBy" value="awardLabel.code"/>
    [/@]
    </div>
    <div class="search-list">[@b.div id="awardLabellist" href="!search?orderBy=awardLabel.code"/]
    </div>
    </div>
[@b.foot/]
