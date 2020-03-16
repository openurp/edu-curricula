[#ftl]
[@b.head/]
[@b.toolbar title='修订任务'/]
[@edu_base.semester_bar name="reviseTask.semester.id" value=currentSemester/]
[@b.form name="searchForm" action=""/]
[@b.div id="listFrame"/]

[#include "courseBlogList.ftl" /]
<script>
  document.semesterForm.method="get";
  var form = document.searchForm;
  search();
  function search(pageNo,pageSize,orderBy){
    form.target="listFrame";
    form.action="${b.url('!search?reviseTask.semester.id='+currentSemester.id)}";
    bg.form.submit(form)
  }
</script>

[@b.foot/]