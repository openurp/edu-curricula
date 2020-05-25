[#ftl/]
[@b.head/]
[#if courseBlog.status = BlogStatus.Published]
<table class="table" style="width:600px" align="center">
  <tr>
    <td class="title" width="20%" bgcolor="#e1ecff">课程代码:</td>
    <td class="content" width="80%" >${courseBlog.course.code!}</td>
  </tr>
  <tr>
    <td class="title" width="20%" bgcolor="#e1ecff">课程名称:</td>
    <td class="content">${courseBlog.course.name!}</td>
  </tr>
  <tr>
    <td class="title" width="20%" bgcolor="#e1ecff">中文简介:</td>
    <td class="content">${courseBlog.description!}</td>
  </tr>
  <tr>
    <td class="title" width="20%" bgcolor="#e1ecff">英文简介:</td>
    <td class="content">${courseBlog.enDescription!}</td>
  </tr>
  <tr>
    <td class="title" width="20%" bgcolor="#e1ecff">总学分:</td>
    <td class="content">${(courseBlog.course.credits)!}</td>
  </tr>
  <tr>
    <td class="title" width="20%" bgcolor="#e1ecff">总学时:</td>
    <td class="content">${(courseBlog.course.creditHours)!}</td>
  </tr>
</table>
<div style="text-align:center">
  [@b.a href="index!detail?id=${courseBlog.id!}" target="_blank"]进入课程[/@]
</div>
[#else ]该课程资料未发布，暂时不能查看
[/#if]
[@b.foot/]
