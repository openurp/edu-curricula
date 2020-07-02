[#ftl]
[@b.head/]
[#if user ??]
  [#if courseBlogs ?? && courseBlogs?size>0]
    [@b.grid  items=courseBlogs var="courseBlog"]
  [@b.row]
    [@b.col width="20%" property="course.code" title="课程代码"/]
    [@b.col width="20%" property="course.name" title="课程名称"/]
    [@b.col width="20%" property="course.department.name" title="开课院系"/]
    [@b.col width="25%"  title="课程资料"]

      [#if reviseSetting??]
        [#if courseBlog.status = BlogStatus.Draft || courseBlog.status = BlogStatus.Unpassed]
          [@b.a href="!edit?id=${courseBlog.id!}"]编辑[/@]
          [#if syllabusMap.get(courseBlog) ??]
            [@b.a href="course-blog!info?id=${courseBlog.id!}" target="_blank"]查看[/@]
            [@b.a href="!submit?id=${courseBlog.id!}"]提交审核[/@]
            [@b.a href="!remove?id=${courseBlog.id!}"]重置[/@]
          [#else ]
            [@b.a href="!copy?id=${courseBlog.id!}"]复制历史资料[/@]
          [/#if]
        [#else ]
          [@b.a href="course-blog!info?id=${courseBlog.id!}" target="_blank"]查看[/@]
        [/#if]
      [#else ]
        [#if syllabusMap.get(courseBlog) ??]
          [@b.a href="course-blog!info?id=${courseBlog.id!}" target="_blank"]查看[/@]
        [#else ]
          不在可编辑课程资料时间范围内
        [/#if]
      [/#if]

    [/@]
    [@b.col width="15%" property="status.name" title="状态"/]
  [/@]
[/@]
  [#else ]该学期没有需要维护的课程
  [/#if]
<br><br><br><br>
[#include "courseBlogList.ftl" /]
[#else ]请管理员添加此登录用户的人员信息
[/#if]
[@b.foot/]