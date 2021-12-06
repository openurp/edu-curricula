[#ftl]
[@b.toolbar title='历史课程资料' /]
[@b.grid  items=hisBlogs var="courseBlog" ]
  [@b.row]
    [@b.col width="15%" property="semester.code" title="学年学期"]${courseBlog.semester.schoolYear}学年${courseBlog.semester.name}学期[/@]
    [@b.col width="15%" property="course.code" title="课程代码"/]
    [@b.col width="20%" property="course.name" title="课程名称"][@b.a href="!info?id=${courseBlog.id!}" target="_blank"]${(courseBlog.course.name)!}[/@][/@]
    [@b.col width="20%" property="department.name" title="开课院系"/]
    [@b.col width="15%" property="updatedAt" title="最新修改时间"]${courseBlog.updatedAt?string("yyyy-MM-dd HH:mm")!}[/@]
    [@b.col width="15%" property="status" title="审核状态"]${(courseBlog.status.name)!}[/@]
  [/@]
[/@]
