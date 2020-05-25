[#ftl]
<div class="container" style="width: 1200px">
  <div>
    <div class="text-right"><a href="${base}/portal/" target="_blank">课程维护</a></div>
    <div ><img src="${b.static_url("urp","/images/logo_course.png")}"></div>
  </div>
  [#include "nav.ftl"/]
  <div>[#if department ??]${(department.name)!}[#else ]其他[/#if]</div>
  <div style="background-color: #e0eaf3; height: 1px;margin-top: 10px;margin-bottom: 10px;"></div>
  [@b.head/]
  <table class="table" style="width:70%" align="center" >
    <thead>
      <tr>
        <th>课程分类</th>
        <th>课程</th>
      </tr>
    </thead>
    <tbody>
    [#list courseBlogMap?keys?sort_by("indexno") as courseGroup]
      [#assign color]

      [/#assign]
      <tr>
        <td bgcolor="#e1ecff" width="20%" >${courseGroup.name}</td>
        <td>
          [#list courseBlogMap.get(courseGroup) as blog]
            [#if blog.status = BlogStatus.Published]
              [@b.a href="index!detail?id=${blog.id!}" target="_blank"]
                ${blog.course.name}[#if blog?has_next]，[/#if]
              [/@]
            [#else ]${blog.course.name}[#if blog?has_next]，[/#if]
            [/#if]
          [/#list]
        </td>
      </tr>
    [/#list]
    </tbody>
  </table>

</div>
[@b.foot/]
