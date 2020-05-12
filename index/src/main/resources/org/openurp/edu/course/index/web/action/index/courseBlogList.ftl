[#ftl]
  <div class="container">
[@b.head/]
[@b.toolbar title='历史课程资料' /]
[@b.grid  items=courseBlogs var="courseBlog" ]
  [@b.row]
    [@b.col width="15%" property="semester.code" title="学年学期"]${courseBlog.semester.schoolYear}学年${courseBlog.semester.name}学期[/@]
    [@b.col width="15%" property="course.code" title="课程代码"/]
    [@b.col width="20%" property="course.name" title="课程名称"][@b.a href="!detail?id=${courseBlog.id!}" target="_blank"]${(courseBlog.course.name)!}[/@][/@]
    [@b.col width="10%" property="course.credits" title="学分"/]
  [/@]
[/@]
  </div>
[@b.foot/]