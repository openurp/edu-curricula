[#ftl]
[@b.head/]
[#include "head.ftl"/]

<body>
<div class="wrapper">

    [#include "nav.ftl"/]

	<div class="con_area m_t_30">
		<div class="clearfix">
			<div class="left_yx fl">
				<div class="left_title">${labelType.name}</div>
				<div class="yx_nav">
					<ul>
						<li [#if !choosedAwardLabel??]class="yx_navon bold"[/#if]>[@b.a href="!awardLabel?labelTypeId=${labelTypeId!}"]全部[/@b.a]</li>
              [#list awardLabels as awardLabel]
								<li [#if choosedAwardLabel?? && choosedAwardLabel==awardLabel]class="yx_navon bold"[/#if]>
                    [@b.a href="!awardLabel?labelId="+awardLabel.id]${awardLabel.name}[/@b.a]
								</li>
              [/#list]
					</ul>
				</div>
			</div>
			<div class="right_yxcon fr bg_white">
				<div class="title_con">
					<span class="title_text"><i class="quan"></i>${(choosedAwardLabel.name)?default("全部")}</span>
					<a class="fanhui fr" href=""></a></div>
				<div class=" bg_white m_t_20">
					<table class="kc_table">
						<thead>
						<tr>
							<th>课程代码</th>
							<th>课程名称</th>
							<th>所属院系</th>
							<th>学分</th>
							<th>学时</th>
							<th>课程类别</th>
							<th>负责人</th>
							<th>授课老师</th>
						</tr>
						</thead>
						<tbody>
            [#list courseBlogMetas as courseBlogMeta]
							<tr>
								<td width="10%" >${(courseBlogMeta.course.code)!}</td>
								<td width="15%" >
                    [#if blogMap.get(courseBlogMeta) ??]
                        [@b.a href="index!detail?id=${blogMap.get(courseBlogMeta).id!}" target="_blank"]
													<span style="text-decoration:underline;">${courseBlogMeta.course.name}</span>
                        [/@]
                    [#else ]${courseBlogMeta.course.name}
                    [/#if]
								</td>
								<td width="15%" >${(courseBlogMeta.course.department.name)!}</td>
								<td width="5%" >${(courseBlogMeta.course.credits)!}</td>
								<td width="5%" >${(courseBlogMeta.course.creditHours)!}</td>
								<td width="15%" >${(courseBlogMeta.courseGroup.name)!}</td>
								<td width="10%" >
                    [#if blogMap.get(courseBlogMeta) ??]
                        ${(blogMap.get(courseBlogMeta).author.name)!}
                    [/#if]
								</td>
								<td width="25%" >
                    [#if blogMap.get(courseBlogMeta) ??]
                        [#list blogMap.get(courseBlogMeta).teachers as teacher]${teacher.name}[#if teacher_has_next],[/#if][/#list]
                    [/#if]
								</td>
							</tr>
            [/#list]
						</tbody>
					</table>
					<div class="text_right m_t_20 ptn_relative">
						<div class="kcs_num">课程总数：<span>${size}</span></div>

              [#if courseBlogMetas?size>0]
                  [#assign param = "&labelTypeId=${labelTypeId!}&labelId=${Parameters['labelId']!}"]
                  [#assign pageIndex = courseBlogMetas.pageIndex]
                  [#assign totalPages = courseBlogMetas.totalPages]
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
								<div class="page">
										<ul class="clearfix">
											<li>
                          [@b.a href="!awardLabel?pageIndex=1&pageSize=25"+param ]
														<span aria-hidden="true">首页</span>
                          [/@]
											</li>
                        [#if pageIndex>1]
													<li>
                              [@b.a href="!awardLabel?pageIndex=${pageIndex-1}&pageSize=25"+param ]
																<span aria-hidden="true">«</span>
                              [/@]
													</li>
                        [/#if]
                        [#list start..end as i]
													<li>[@b.a href="!awardLabel?pageIndex=${i}&pageSize=25"+param ]${i}[/@]</li>
                        [/#list]
                        [#if pageIndex!=totalPages]
													<li>
                              [@b.a href="!awardLabel?pageIndex=${pageIndex+1}&pageSize=25"+param]
																<span aria-hidden="true">»</span>
                              [/@]
													</li>
                        [/#if]
											<li>
                          [@b.a href="!awardLabel?pageIndex=${totalPages}&pageSize=25"+param ]
														<span aria-hidden="true">尾页</span>
                          [/@]
											</li>
										</ul>
								</div>
              [/#if]
					</div>
				</div>
			</div>
		</div>
	</div>
    [#include "foot.ftl"/]

</div>


</body>
[@b.foot/]
