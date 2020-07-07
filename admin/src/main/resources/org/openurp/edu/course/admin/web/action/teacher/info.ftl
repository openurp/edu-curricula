[#ftl/]
[@b.head/]
<div class="xq_list m_t_20">
  <table style="width: 70%;margin: 0 auto;">
    <tr>
      <td style="width:160px;">学年学期：</td>
      <td>${(courseBlog.semester.schoolYear)!}学年${(courseBlog.semester.name)!}学期</td>
    </tr>
    <tr>
      <td>课程代码：</td>
      <td>${(courseBlog.course.code)!}</td>
    </tr>
    <tr>
      <td>课程名称：</td>
      <td>${(courseBlog.course.name)!}</td>
    </tr>
    <tr>
      <td>课程类别：</td>
      <td>${(courseBlog.meta.courseGroup.name)!}</td>
    </tr>
    <tr>
      <td>开课院系：</td>
      <td>${(courseBlog.department.name)!}</td>
    </tr>
    <tr>
      <td>授课老师：</td>
      <td>[#list courseBlog.teachers as teacher]${teacher.name}[#if teacher_has_next],[/#if][/#list]</td>
    </tr>
    <tr>
      <td>中文简介：</td>
      <td>[#if courseBlog.description!="--"]${courseBlog.description!}[/#if]</td>
    </tr>
    <tr>
      <td>英文简介：</td>
      <td>[#if courseBlog.enDescription!="--"]${courseBlog.enDescription!}[/#if]</td>
    </tr>
    [#if syllabuses ?? && syllabuses?size>0]
      <tr>
        <td>教学大纲：</td>
        <td>
          [#list syllabuses as syllabus]
            [#if syllabus.attachment??]
              <div>${(syllabus.attachment.name)!}
                <a class="m_l_30 xiazai" target="_blank" href="${b.url('syllabus!attachment?id='+syllabus.id)}"><span class="iconfont icon-xiazai"></span>下载</a>
                <a class="m_l_30 yulan" target="_blank" href="${b.url('syllabus!view?id='+syllabus.id)}"><span class="iconfont icon-yulan"></span>预览</a>
                [#if syllabus_has_next]<br>[/#if]
              </div>
            [/#if]
          [/#list]
        </td>
      </tr>
    [/#if]
    [#if lecturePlans ?? && lecturePlans?size>0]
      <tr>
        <td>授课计划：</td>
        <td>
          [#list lecturePlans as lecturePlan]
            [#if lecturePlan.attachment??]
              <div>${(lecturePlan.attachment.name)!}
                <a class="m_l_30 xiazai" target="_blank" href="${b.url('lecture-plan!attachment?id='+lecturePlan.id)}"><span class="iconfont icon-xiazai"></span>下载</a>
                <a class="m_l_30 yulan" target="_blank" href="${b.url('lecture-plan!view?id='+lecturePlan.id)}"><span class="iconfont icon-yulan"></span>预览</a>
                [#if lecturePlan_has_next]<br>[/#if]
              </div>
            [/#if]
          [/#list]
        </td>
      </tr>
    [/#if]
    <tr>
      <td>预修课程：</td>
      <td>[#if courseBlog.preCourse!="--"]${courseBlog.preCourse!}[/#if]</td>
    </tr>
    <tr>
      <td>教材和参考书目：</td>
      <td>[#if courseBlog.books!="--"]${courseBlog.books!}[/#if]</td>
    </tr>
    <tr>
      <td>辅助资料：</td>
      <td>${courseBlog.materials!}</td>
    </tr><tr>
      <td>课程网站地址：</td>
      <td><a href="${courseBlog.website!}" target="_blank">${courseBlog.website!}</a></td>
    </tr>
    <tr>
      <td>获奖情况：</td>
      <td >
        [#list courseBlog.awards! as award]
          ${award.awardLabel.name},${award.year}年[#if award_has_next]<br>[/#if]
        [/#list]
      </td>
    </tr>
    <tr>
      <td>备注：</td>
      <td>${courseBlog.remark!}</td>
    </tr>
  </table>
</div>
<style>
  .xq_list{ padding:30px 30px;}
  .xq_list table{ border:1px solid #e1e1e1; background:#fff; width:100%;}
  .xq_list table tr td{ padding:15px; color:#826d4c; line-height:24px; word-break: break-all}
  .xq_list table tr td:first-child{ background:#faf4eb; color:#333; font-weight:bold;}
  .xq_list table tr{ border-bottom:1px solid #e1e1e1;}
  .xq_list table tr td p {color: #826d4c;}
  .m_t_20{ margin-top:20px;}
</style>
  </style>
[@b.foot/]
