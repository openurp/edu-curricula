[#ftl]
<div class="title_con m_t_30"><span class="title_text"><i class="quan"></i>历史课程资料</span></div>
<div class=" bg_white m_t_20 p_lr_30">
  <table class="kc_table">
    <thead>
    <tr>
      <th>学年学期</th>
      <th>课程代码</th>
      <th>课程名称</th>
      <th>开课院系</th>
      <th>学分</th>
      <th>学时</th>
      <th>课程类别</th>
      <th>授课老师</th>
    </tr>
    </thead>
    <tbody>
    [#list hisBlogs as courseBlog]
      <tr>
        <td width="15%" >${courseBlog.semester.schoolYear}学年${courseBlog.semester.name}学期</td>
        <td width="10%" >${(courseBlog.course.code)!}</td>
        <td width="15%" >
          [#if courseBlog.status = BlogStatus.Published]
            [@b.a href="index!detail?id=${courseBlog.id!}" target="_blank"]
              ${courseBlog.course.name}
            [/@]
          [#else ]${courseBlog.course.name}
          [/#if]
        </td>
        <td width="15%" >${(courseBlog.department.name)!}</td>
        <td width="5%" >${(courseBlog.course.credits)!}</td>
        <td width="5%" >${(courseBlog.course.creditHours)!}</td>
        <td width="10%" >${(courseBlog.meta.courseGroup.name)!}</td>
        <td width="25%" >[#list courseBlog.teachers as teacher]${teacher.name}[#if teacher_has_next],[/#if][/#list]</td>
      </tr>
    [/#list]
    </tbody>
  </table>
</div>