[#ftl/]
[@b.head/]
<div class="xq_list m_t_20">
  <table style="width: 1095px;margin: 0 auto;">
    <tr>
      <td style="background:#faf4eb; color:#333; font-weight:bold;width:165px;">学年学期：</td>
      <td style="width: 200px" >${(courseBlog.semester.schoolYear)!}学年${(courseBlog.semester.name)!}学期</td>
      <td style="background:#faf4eb; color:#333; font-weight:bold;width:165px;">开课院系：</td>
      <td style="width: 200px">${(courseBlog.department.name)!}</td>
      <td style="background:#faf4eb; color:#333; font-weight:bold;width:165px;">负责人：</td>
      <td style="width: 200px">${(courseBlog.author.name)!}</td>
    </tr>
    <tr>
      <td style="background:#faf4eb; color:#333; font-weight:bold;width:165px;">课程代码：</td>
      <td style="width: 200px">${(courseBlog.course.code)!}</td>
      <td style="background:#faf4eb; color:#333; font-weight:bold;width:165px;">课程名称：</td>
      <td style="width: 200px">${(courseBlog.course.name)!}</td>
      <td style="background:#faf4eb; color:#333; font-weight:bold;width:165px;">课程类别：</td>
      <td style="width: 200px">${(courseBlog.meta.courseGroup.name)!}</td>
    </tr>
    <tr>
      <td style="background:#faf4eb; color:#333; font-weight:bold;width:165px;">授课老师：</td>
      <td colspan="5" style="width: 930px">[#list courseBlog.teachers as teacher]${teacher.name}[#if teacher_has_next],[/#if][/#list]</td>
    </tr>
    <tr>
      <td style="width: 165px">中文简介：</td>
      <td colspan="5" style="width: 930px"><div style="width: 900px">[#if courseBlog.description!="--"]${courseBlog.description!}[/#if]</div></td>
    </tr>
    <tr>
      <td style="width: 165px">英文简介：</td>
      <td colspan="5" style="width: 930px"><div style="width: 900px">[#if courseBlog.enDescription!="--"]${courseBlog.enDescription!}[/#if]</div></td>
    </tr>
    <tr>
      <td style="background:#faf4eb; color:#333; font-weight:bold;width:165px;">教学大纲：</td>
      <td style="width: 200px">
        [#list syllabuses! as syllabus]
          [#if syllabus.attachment??]
            <div>
              <a class="m_l_35 xiazai" target="_blank" href="${b.url('syllabus!attachment?id='+syllabus.id)}"><span class="iconfont icon-xiazai"></span>下载</a>
              <a class="m_l_35 yulan" target="_blank" href="${b.url('syllabus!view?id='+syllabus.id)}"><span class="iconfont icon-yulan"></span>预览</a>
            </div>
          [/#if]
        [/#list]
      </td>
      <td style="background:#faf4eb; color:#333; font-weight:bold;width:165px;">授课计划：</td>
      <td style="width: 200px">
        [#list lecturePlans! as lecturePlan]
          [#if lecturePlan.attachment??]
            <div>
              <a class="m_l_35 xiazai" target="_blank" href="${b.url('lecture-plan!attachment?id='+lecturePlan.id)}"><span class="iconfont icon-xiazai"></span>下载</a>
              <a class="m_l_35 yulan" target="_blank" href="${b.url('lecture-plan!view?id='+lecturePlan.id)}"><span class="iconfont icon-yulan"></span>预览</a>
            </div>
          [/#if]
        [/#list]
      </td>
      <td style="background:#faf4eb; color:#333; font-weight:bold;width:165px;">教学资料：</td>
      <td style="width: 200px">
        [#if courseBlog.materialAttachment??]
          <div>
            <a class="m_l_30 xiazai" target="_blank" href="${b.url('teacher!attachment?id='+courseBlog.id)}"><span class="iconfont icon-xiazai"></span>下载</a>
          </div>
        [/#if]
      </td>
    </tr>
    <tr>
      <td style="width: 165px">预修课程：</td>
      <td colspan="5" style="width: 930px">[#if courseBlog.preCourse!="--"]${courseBlog.preCourse!}[/#if]</td>
    </tr>
    <tr>
      <td style="width: 165px">教材和参考书目：</td>
      <td colspan="5" style="width: 930px">[#if courseBlog.books!="--"]${courseBlog.books!}[/#if]</td>
    </tr>
    <tr>
      <td style="width: 165px">课程网站地址：</td>
      <td colspan="5" style="width: 930px"><a href="${courseBlog.website!}" target="_blank">${courseBlog.website!}</a></td>
    </tr>
    <tr>
      <td style="width: 165px">获奖情况：</td>
      <td colspan="5" style="width: 930px">
        [#if courseBlog.meta??]
          [#list courseBlog.meta.awards! as award]
            ${award.awardLabel.name},${award.year}年[#if award_has_next]<br>[/#if]
          [/#list]
        [/#if]
      </td>
    </tr>
    <tr>
      <td style="width: 165px;">备注：</td>
      <td colspan="5" style="width: 930px">${courseBlog.remark!}</td>
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
