[#ftl/]
<div class="container">
[@b.head/]
[@b.toolbar title="课程详细资料"/]
<table class="table" style="width:70%" align="center">
  <tr>
    <td class="title" width="100px" bgcolor="#e1ecff">课程代码:</td>
    <td class="content">${(courseBlog.course.code)!}</td>
  </tr>
  <tr>
    <td class="title" width="20%" bgcolor="#e1ecff">课程名称:</td>
    <td class="content">[@b.a href="!courseBlogList?id=${courseBlog.id!}" title="点击历史课程资料" target="_blank"]${(courseBlog.course.name)!}[/@]</td>
  </tr>
  <tr>
    <td class="title" width="20%" bgcolor="#e1ecff">课程类别:</td>
    <td class="content">${(courseBlog.meta.courseGroup.name)!}</td>
  </tr>
  <tr>
    <td class="title" width="20%" bgcolor="#e1ecff">开课院系:</td>
    <td class="content">${(courseBlog.department.name)!}</td>
  </tr>
  <tr>
    <td class="title" width="20%" bgcolor="#e1ecff">学年学期:</td>
    <td class="content" >${(courseBlog.semester.schoolYear)!}学年${(courseBlog.semester.name)!}学期</td>
  </tr>
  <tr>
    <td class="title" width="20%" bgcolor="#e1ecff">任课教师:</td>
    <td class="content">[#list courseBlog.teachers as teacher]${teacher.name}[#if teacher_has_next],[/#if][/#list]</td>
  </tr>
  <tr>
    <td class="title" width="20%" bgcolor="#e1ecff">中文简介:</td>
    <td class="content">${courseBlog.description!}</td>
  </tr>
  <tr>
    <td class="title" width="20%" bgcolor="#e1ecff">英文简介:</td>
    <td class="content">${courseBlog.enDescription!}</td>
  </tr>
  [#if syllabuses ?? && syllabuses?size>0]
    <tr>
      <td class="title" width="20%" bgcolor="#e1ecff">教学大纲:</td>
      <td class="content" >
        [#list syllabuses as syllabus]
          [#if syllabus.attachment??]
            ${(syllabus.attachment.name)!}
            [@b.a target="_blank" href="syllabus!attachment?id=${syllabus.id}"]下载[/@]
            &nbsp;&nbsp;[@b.a target="_blank" href="syllabus!view?id=${syllabus.id}"]预览[/@]
            [#if syllabus_has_next]<br>[/#if]
          [/#if]
        [/#list]
      </td>
    </tr>
  [/#if]
  [#if lecturePlans ?? && lecturePlans?size>0]
    <tr>
      <td class="title" width="20%" bgcolor="#e1ecff">授课计划:</td>
      <td class="content">
        [#list lecturePlans as lecturePlan]
          [#if lecturePlan.attachment??]
            ${(lecturePlan.attachment.name)!}
            [@b.a target="_blank" href="lecture-plan!attachment?id=${lecturePlan.id}"]下载[/@]
            &nbsp;&nbsp;[@b.a target="_blank" href="lecture-plan!view?id=${lecturePlan.id}"]预览[/@]
            [#if lecturePlan_has_next]<br>[/#if]
          [/#if]
        [/#list]
      </td>
    </tr>
  [/#if]
  <tr>
    <td class="title" width="20%" bgcolor="#e1ecff">教材和辅助资料:</td>
    <td class="content">${courseBlog.materials!}</td>
  </tr>
  <tr>
    <td class="title" width="20%" bgcolor="#e1ecff">课程网站:</td>
    <td class="content">${courseBlog.website!}</td>
  </tr>
</table>
</div>
[@b.foot/]
