[#ftl/]
[@b.head/]
<table class="infoTable" width="100%">
  <tr>
    <td class="title" width="15%">学年学期:</td>
    <td class="content">${lecturePlan.semester.schoolYear!}学年${lecturePlan.semester.name!}学期</td>
    <td class="title" width="15%">课程代码:</td>
    <td class="content">${lecturePlan.course.code!}</td>
    <td class="title" width="15%">课程名称:</td>
    <td class="content">${lecturePlan.course.name!}</td>
  </tr>
  <tr>
    <td class="title" width="15%">授课计划语言:</td>
    <td class="content">${languages[lecturePlan.locale?string]!}</td>
    <td class="title" width="15%">负责人:</td>
    <td class="content">${lecturePlan.author.name!}</td>
    <td class="title" width="15%">最近维护时间:</td>
    <td class="content">${lecturePlan.updatedAt?string("yyyy-MM-dd HH:mm")!}</td>
  </tr>
  <tr>
    <td class="title" width="15%">授课计划附件:</td>
    <td class="content" colspan="5">
        [#if lecturePlan.attachment??]
          ${(lecturePlan.attachment.name)!}
          [@b.a target="_blank" href="!attachment?id=${lecturePlan.id}"]下载[/@]
          &nbsp;&nbsp;[@b.a target="_blank" href="!view?id=${lecturePlan.id}"]预览[/@]
        [/#if]
    </td>
  </tr>
</table>
[@b.foot/]
