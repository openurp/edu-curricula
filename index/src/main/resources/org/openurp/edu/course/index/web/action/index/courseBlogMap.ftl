[#ftl]
<div class="container">
  <div>
    <div class="text-right"><a href="${base}/portal/" target="_blank">课程维护</a></div>
    <div ><img src="${b.static_url("urp","/images/logo_course.png")}"></div>
  </div>
  [#include "nav.ftl"/]
  [@b.head/]
  [@b.toolbar title="课程详细资料"/]
  <table class="table" style="width:70%" align="center">
    <thead>
      <tr>
        <th>课程分类</th>
        <th>课程</th>
      </tr>
    </thead>
    <tbody>
    [#list courseBlogMap?keys as courseGroup]
      <tr>
        <td bgcolor="#e1ecff" width="20%" >${courseGroup.name}</td>
        <td>
          [#list courseBlogMap.get(courseGroup) as blog]
            [@b.a href="index!detail?id=${blog.id!}" target="_blank"]${blog.course.name}[#if blog?has_next]，[/#if][/@]
          [/#list]
        </td>
      </tr>
    [/#list]
    </tbody>
  </table>

</div>
[@b.foot/]
