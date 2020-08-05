[#ftl]
[@b.head/]
<div class="search-container">
    <div class="search-panel">
    [@b.form name="courseGroupSearchForm" action="!search" target="courseGrouplist" title="ui.searchForm" theme="search"]
      [@b.textfields names="courseGroup.code;代码"/]
      [@b.textfields names="courseGroup.name;名称"/]
      <input type="hidden" name="orderBy" value="courseGroup.code"/>
    [/@]
    </div>
    <div class="search-list">[@b.div id="courseGrouplist" href="!search?orderBy=courseGroup.code"/]
    </div>
    </div>
[@b.foot/]
