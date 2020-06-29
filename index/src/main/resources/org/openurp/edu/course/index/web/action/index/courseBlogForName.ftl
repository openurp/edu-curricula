[#ftl]
[@b.head/]
[#include "head.ftl"/]

<body>
	<div class="wrapper">

    [#include "nav.ftl"/]

        <div class="con_area m_t_30">
        	<div class="bg_img border p_lr_30 p_t_5 p_b_25">
                <div class="title_con"><span class="title_text"><i class="quan"></i>查询结果</span></div>
            <table class="kc_table">
              <thead>
              <tr>
                <th>课程代码</th>
                <th>课程名称</th>
                <th>开课院系</th>
                <th>学分</th>
                <th>学时</th>
                <th>课程类别</th>
                <th>负责人</th>
                <th>授课教师</th>
              </tr>
              </thead>
              <tbody>
              [#list courseBlogs as courseBlog]
                <tr>
                  <td width="10%" >${(courseBlog.course.code)!}</td>
                  <td width="15%" >
                    [#if courseBlog.status = BlogStatus.Published]
                      [@b.a href="index!detail?id=${courseBlog.id!}" target="_blank"]
                        <span style="text-decoration:underline;">${courseBlog.course.name}</span>
                      [/@]
                    [#else ]${courseBlog.course.name}
                    [/#if]
                  </td>
                  <td width="15%" >${(courseBlog.department.name)!}</td>
                  <td width="5%" >${(courseBlog.course.credits)!}</td>
                  <td width="5%" >${(courseBlog.course.creditHours)!}</td>
                  <td width="15%" >${(courseBlog.meta.courseGroup.name)!}</td>
                  <td width="10%" >${(courseBlog.author.name)!}</td>
                  <td width="25%" >[#list courseBlog.teachers as teacher]${teacher.name}[#if teacher_has_next],[/#if][/#list]</td>
                </tr>
              [/#list]
              </tbody>
            </table>
            [#if courseBlogs?size>0]
              [#assign param = "&nameOrCode=${Parameters['nameOrCode']!}"]
              [#assign pageIndex = courseBlogs.pageIndex]
              [#assign totalPages = courseBlogs.totalPages]
              [#if pageIndex-2>0]
                [#assign start = pageIndex-2]
              [#else]
                [#assign start = pageIndex]
              [/#if]

              [#if pageIndex+2 < totalPages]
                [#assign end = pageIndex+2]
              [#else]
                [#assign end = totalPages]
              [/#if]
              <div class="text_center m_t_20">
                <div class="page">
                  <ul class="clearfix">
                    <li>
                      [@b.a href="!courseBlogForName?pageIndex=1&pageSize=20"+param target="courseBloglist"]
                        <span aria-hidden="true">首页</span>
                      [/@]
                    </li>
                    [#if pageIndex>1]
                      <li>
                        [@b.a href="!courseBlogForName?pageIndex=${pageIndex-1}&pageSize=20"+param target="courseBloglist"]
                          <span aria-hidden="true">上一页</span>
                        [/@]
                      </li>
                    [/#if]
                    [#list start..end as i]
                      <li>[@b.a href="!courseBlogForName?pageIndex=${i}&pageSize=20"+param  target="courseBloglist"]${i}[/@]</li>
                    [/#list]
                    [#if pageIndex!=totalPages]
                      <li>
                        [@b.a href="!courseBlogForName?pageIndex=${pageIndex+1}&pageSize=20"+param  target="courseBloglist"]
                          <span aria-hidden="true">下一页</span>
                        [/@]
                      </li>
                    [/#if]
                    <li>
                      [@b.a href="!courseBlogForName?pageIndex=${totalPages}&pageSize=20"+param target="courseBloglist"]
                        <span aria-hidden="true">尾页</span>
                      [/@]
                    </li>
                  </ul>
                </div>
              </div>
            [/#if]
            </div>
        </div>
    <div class="tk_box"/>

    [#include "foot.ftl"/]

    </div>
</body>
[@b.foot/]
<script>
  $(".kc_table tbody tr:odd").css({"background":"#eeebea"});
</script>
