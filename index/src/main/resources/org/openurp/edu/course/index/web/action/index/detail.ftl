[#ftl]
[@b.head/]
[#include "head.ftl"/]

<body>
	<div class="wrapper">


      [#include "nav.ftl"/]

      [#if courseBlog.status = BlogStatus.Published]
        <div class="con_area m_t_30">
        	<div class="bg_img p_lr_30 p_t_5 p_b_25">
                <div class="title_con"><span class="title_text"><i class="quan"></i>${(courseBlog.course.name)!}</span></div>
                <div class="xq_list m_t_20">
                    <table style="background: none;width: 1065px">
                    	<tr>
												<td style="background:#faf4eb; color:#333; font-weight:bold;width:150px;">学年学期：</td>
												<td style="width: 205px" >${(courseBlog.semester.schoolYear)!}学年${(courseBlog.semester.name)!}学期</td>
												<td style="background:#faf4eb; color:#333; font-weight:bold;width:150px;">开课院系：</td>
												<td style="width: 205px">${(courseBlog.department.name)!}</td>
												<td style="background:#faf4eb; color:#333; font-weight:bold;width:150px;">负责人：</td>
												<td style="width: 205px">${(courseBlog.author.name)!}</td>
											</tr>
											<tr>
												<td style="background:#faf4eb; color:#333; font-weight:bold;width:150px;">课程代码：</td>
												<td style="width: 205px">${(courseBlog.course.code)!}</td>
												<td style="background:#faf4eb; color:#333; font-weight:bold;width:150px;">课程名称：</td>
												<td style="width: 205px">${(courseBlog.course.name)!}</td>
												<td style="background:#faf4eb; color:#333; font-weight:bold;width:150px;">课程类别：</td>
												<td style="width: 205px">${(courseBlog.meta.courseGroup.name)!}</td>
											</tr>
											<tr>
												<td style="background:#faf4eb; color:#333; font-weight:bold;width:150px;">授课老师：</td>
												<td colspan="5" style="width: 915px">[#list courseBlog.teachers as teacher]${teacher.name}[#if teacher_has_next],[/#if][/#list]</td>
											</tr>
											<tr>
												<td style="width: 150px">中文简介：</td>
												<td colspan="5" style="width: 915px"><div style="width: 900px">[#if courseBlog.description!="--"]${courseBlog.description!}[/#if]</div></td>
											</tr>
											<tr>
												<td style="width: 150px">英文简介：</td>
												<td colspan="5" style="width: 915px"><div style="width: 900px">[#if courseBlog.enDescription!="--"]${courseBlog.enDescription!}[/#if]</div></td>
											</tr>
											<tr>
												<td style="background:#faf4eb; color:#333; font-weight:bold;width:150px;">教学大纲：</td>
												<td style="width: 205px">
													[#if syllabus.attachment??]
														<div>
[#--																	<a class="m_l_35 xiazai" target="_blank" href="${b.url('syllabus!attachment?id='+syllabus.id)}"><span class="iconfont icon-xiazai"></span>下载</a>--]
															<a class="m_l_35 yulan" target="_blank" href="${b.url('syllabus!view?id='+syllabus.id)}"><span class="iconfont icon-yulan"></span>预览</a>
														</div>
													[/#if]
												</td>
												<td style="background:#faf4eb; color:#333; font-weight:bold;width:150px;">授课计划：</td>
												<td style="width: 205px">
													[#if lecturePlan.attachment??]
														<div>
[#--																			<a class="m_l_35 xiazai" target="_blank" href="${b.url('lecture-plan!attachment?id='+lecturePlan.id)}"><span class="iconfont icon-xiazai"></span>下载</a>--]
															<a class="m_l_35 yulan" target="_blank" href="${b.url('lecture-plan!view?id='+lecturePlan.id)}"><span class="iconfont icon-yulan"></span>预览</a>
														</div>
													[/#if]
												</td>
												<td style="background:#faf4eb; color:#333; font-weight:bold;width:150px;">教学资料：</td>
												<td style="width: 205px">
                            [#if courseBlog.materialAttachment??]
															<div>
																<a class="m_l_30 xiazai" target="_blank" href="${b.url('index!attachment?id='+courseBlog.id)}"><span class="iconfont icon-xiazai"></span>下载</a>
															</div>
                            [/#if]
												</td>
											</tr>
											<tr>
												<td style="width: 150px">预修课程：</td>
												<td colspan="5" style="width: 915px">[#if courseBlog.preCourse!="--"]${courseBlog.preCourse!}[/#if]</td>
											</tr>
											<tr>
												<td style="width: 150px">教材和参考书目：</td>
												<td colspan="5" style="width: 915px">[#if courseBlog.books!="--"]${courseBlog.books!}[/#if]</td>
											</tr>
											<tr>
												<td style="width: 150px">课程网站地址：</td>
												<td colspan="5" style="width: 915px"><a href="${courseBlog.website!}" target="_blank">${courseBlog.website!}</a></td>
											</tr>
											<tr>
												<td style="width: 150px">获奖情况：</td>
												<td colspan="5" style="width: 915px">
													[#list courseBlog.awards! as award]
															${award.awardLabel.name},${award.year}年[#if award_has_next]<br>[/#if]
													[/#list]
												</td>
											</tr>
											<tr>
												<td style="width: 150px;">备注：</td>
												<td colspan="5" style="width: 915px">${courseBlog.remark!}</td>
											</tr>
                    </table>
                </div>



              [#include "courseBlogList.ftl" /]
            </div>
        </div>


      [#else ]该课程资料未发布，暂时不能查看
      [/#if]
      [#include "foot.ftl"/]
	</div>

</body>
<style>
	.xq_list{ padding:0 30px;}
	.xq_list table{ border:1px solid #e1e1e1; background:#fff;}
	.xq_list table tr td{ padding:8px; color:#826d4c; line-height:24px; word-break: break-all}
	.xq_list table tr td:first-child{ background:#faf4eb; color:#333; font-weight:bold;}
	.xq_list table tr{ border-bottom:1px solid #e1e1e1;}
	.xq_list table tr td p {color: #826d4c;}
</style>
[@b.foot/]
