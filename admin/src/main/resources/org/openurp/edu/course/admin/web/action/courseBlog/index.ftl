[#ftl]
[@b.head/]
  <div class="search-container">
  <div class="search-panel" style="width: 200px">
    [@b.form name="courseBlogSearchForm"  action="!search" target="courseBloglist" title="ui.searchForm" theme="search" ]
      [@edu.semester name="courseBlog.semester.id" label="学年学期"  value=currentSemester /]
      [@b.textfield name="courseBlog.course.code" label="课程代码"/]
      [@b.textfield name="courseBlog.course.name" label="课程名称"/]
      [@b.select name="courseBlog.department.id" label="开课院系" items=departments?sort_by("code") empty="..."/]
      [@b.textfield name="courseBlog.author.code" label="负责人工号" /]
      [@b.textfield name="courseBlog.author.name" label="负责人姓名"/]
      [@b.field label="审核状态"]
        <select name="courseBlog.status">
          <option value="">全部</option>
          <option value="0">草稿</option>
          <option value="1">已提交</option>
          <option value="3">通过</option>
          <option value="2">未通过</option>
          <option value="4">已发布</option>
        </select>
      [/@]
      <input type="hidden" name="orderBy" value="courseBlog.course.code"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="courseBloglist" href="!search?orderBy=courseBlog.course.code & courseBlog.semester.id="+currentSemester.id /]
  </div>
  </div>
[@b.foot/]
<style>
  .search-item{
    width:180px;
  }
</style>