[#ftl/]
[@b.head/]
<table class="infoTable" width="100%">
  <tr>
    <td class="title" width="15%">学年学期:</td>
    <td class="content" >${courseBlog.semester.schoolYear!}学年${courseBlog.semester.name!}学期</td>
    <td class="title" width="15%">课程代码:</td>
    <td class="content">${courseBlog.course.code!}</td>
    <td class="title" width="15%">课程名称:</td>
    <td class="content">${courseBlog.course.name!}</td>
  </tr>
  <tr>
    <td class="title" width="15%">开课院系:</td>
    <td class="content">${courseBlog.department.name!}</td>
    <td class="title" width="15%">任课教师:</td>
    <td class="content">[#list courseBlog.teachers as teacher]${teacher.name}[#if teacher_has_next],[/#if][/#list]</td>
    <td class="title" width="15%">负责人:</td>
    <td class="content">${courseBlog.author.name!}</td>
  </tr>
  <tr>
    <td class="title" width="15%">中文简介:</td>
    <td class="content" colspan="5">${courseBlog.description!}</td>
  </tr>
  <tr>
    <td class="title" width="15%">英文简介:</td>
    <td class="content" colspan="5">${courseBlog.enDescription!}</td>
  </tr>
  <tr>
    <td class="title" width="15%">教材和辅助资料:</td>
    <td class="content">${courseBlog.materials!}</td>
    <td class="title" width="15%">课程网站:</td>
    <td class="content">${courseBlog.website!}</td>
    <td class="title" width="15%">最近维护时间:</td>
    <td class="content">${courseBlog.updatedAt?string("yyyy-MM-dd HH:mm")!}</td>
  </tr>
  [#if syllabuses ?? && syllabuses?size>0]
    <tr>
      <td class="title" width="15%">教学大纲:</td>
      <td class="content" colspan="5">
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
      <td class="title" width="15%">授课计划:</td>
      <td class="content" colspan="5">
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
</table>
[@b.foot/]
