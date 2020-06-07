[#ftl]
[@b.head/]
[#include "head.ftl"/]

<body>
	<div class="wrapper">

    [#include "nav.ftl"/]
        
        <div class="con_area m_t_30">
        	<div class="clearfix">
            	<div class="left_yx fl"> 
                    <div class="left_title">院系</div>  
                    <div class="yx_nav">
                        <ul>
                          [#list departments as department]
                            <li [#if choosedDepartment?? && choosedDepartment==department]class="yx_navon bold"[/#if]>
                              [@b.a href="!courseBlogForDepart?id="+department.id]${department.name}[/@b.a]
                            </li>
                          [/#list]
                          <li [#if !choosedDepartment??]class="yx_navon bold"[/#if]>
                            [@b.a href="!courseBlogForDepart?id=else"]其他[/@b.a]
                          </li>
                        </ul>
                    </div>         
                </div>
                <div class="right_yxcon fr bg_white">
                	<div class="title_con"><span class="title_text"><i class="quan"></i>${(choosedDepartment.name)?default("其他")}</span></div>
                    <div class="yx_bt clearfix m_t_10">
                    	<div class="yx_bt_kcfl fl text_center">课程分类</div>
                      <div class="yx_bt_kc fr text_center">课程</div>
                    </div>
                    <div class="yxb_con">
                        [#list courseBlogMap?keys?sort_by("indexno") as courseGroup]
                        <div class="yxb_list">
                            <div class="yxb_title">${courseGroup.name}</div>
                            <ul class="yxb_item">
                                [#list courseBlogMap.get(courseGroup) as blog]
                                    <li>
                                    [#if blog.status = BlogStatus.Published]
                                        [@b.a href="index!detail?id=${blog.id!}" target="_blank"]
                                            ${blog.course.name}
                                        [/@]
                                    [#else ]${blog.course.name}
                                    [/#if]
                                    </li>
                                [/#list]
                            </ul>
                        </div>
                        [/#list]
                    </div>
                </div>
            </div>
        </div>

      [#include "foot.ftl"/]
        
    </div>
</body>
[@b.foot/]
