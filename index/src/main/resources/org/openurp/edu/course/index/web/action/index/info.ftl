[#ftl/]
[@b.head/]
<table class="infoTable" width="60%" align="center">
  <tr>
    <td class="title" width="30%" >课程代码:</td>
    <td class="content">${courseBlog.course.code!}</td>
  </tr>
  <tr>
    <td class="title" width="30%" >课程名称:</td>
    <td class="content">${courseBlog.course.name!}</td>
  </tr>
  <tr>
    <td class="title" width="30%" >中文简介:</td>
    <td class="content">${courseBlog.description!}</td>
  </tr>
  <tr>
    <td class="title" width="30%" >英文简介:</td>
    <td class="content">${courseBlog.enDescription!}</td>
  </tr>
  <tr>
    <td class="title" width="30%" >总学分:</td>
    <td class="content">${(courseBlog.course.credits)!}</td>
  </tr>
  <tr>
    <td class="title" width="30%" >总学时:</td>
    <td class="content">${(courseBlog.course.creditHours)!}</td>
  </tr>
</table>
<div style="text-align:center">
  [@b.a href="index!detail?id=${courseBlog.id!}" target="_blank"]进入课程[/@]
</div>
[@b.foot/]
