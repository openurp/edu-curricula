[#ftl/]
[@b.head/]
<table class="infoTable" width="100%">
  <tr>
    <td class="title" width="15%">学年学期:</td>
    <td class="content">${syllabus.semester.schoolYear!}学年${syllabus.semester.name!}学期</td>
    <td class="title" width="15%">课程代码:</td>
    <td class="content">${syllabus.course.code!}</td>
    <td class="title" width="15%">课程名称:</td>
    <td class="content">${syllabus.course.name!}</td>
  </tr>
  <tr>
    <td class="title" width="15%">教学大纲语言:</td>
    <td class="content">${languages[syllabus.locale?string]!}</td>
    <td class="title" width="15%">负责人:</td>
    <td class="content">${syllabus.author.name!}</td>
    <td class="title" width="15%">最近维护时间:</td>
    <td class="content">${syllabus.updatedAt?string("yyyy-MM-dd HH:mm")!}</td>
  </tr>
  <tr>
    <td class="title" width="15%">教学大纲附件:</td>
    <td class="content" colspan="5">
        [#if syllabus.attachment??]
          ${(syllabus.attachment.name)!}
          [@b.a target="_blank" href="!attachment?id=${syllabus.id}"]下载[/@]
          &nbsp;&nbsp;[@b.a target="_blank" href="!view?id=${syllabus.id}"]预览[/@]
        [/#if]
    </td>
  </tr>
</table>
[@b.foot/]
