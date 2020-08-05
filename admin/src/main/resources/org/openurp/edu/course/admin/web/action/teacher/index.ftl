[#ftl]
[@b.head/]
[@edu.semester_bar name="courseBlog.semester.id" value=currentSemester/]
[@b.form name="searchForm" action=""/]
[@b.div id="listFrame"/]
<script>
  document.semesterForm.method="get";
  var form = document.searchForm;
  search();
  function search(pageNo,pageSize,orderBy){
    form.target="listFrame";
    form.action="${b.url('!search?courseBlog.semester.id='+currentSemester.id)}";
    bg.form.submit(form)
  }
</script>

[@b.foot/]