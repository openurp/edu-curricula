[#ftl]
[@b.head/]
[#include "head.ftl"/]

<body>
	<div class="wrapper">


    [#include "nav.ftl"/]
        
        <div class="con_area m_t_30">
        	<div class="bg_white p_lr_10">
                <div class="title_con"><span class="title_text"><i class="quan"></i>精品课程</span></div>
                    <div class="jkc_con m_t_20">
                      [#list labelTypeMap?keys?sort_by("code") as labelType]
                        <div class="jkc_list">
                            <div class="jkc_title"><img alt="${labelType.name}" src="${b.static_url('openurp-edu-course','images/jpzy_${labelType.code}_t.png')}"></div>
                            <ul class="jkc_item clearfix">
                              [#list labelTypeMap.get(labelType) as blog]
                                [#if blog_index<16]
                                <li>
                                [#if blog.status = BlogStatus.Published]
                                  [@b.a href="index!detail?id=${blog.id!}" target="_blank"]
                                    ${blog.course.name}
                                  [/@]
                                [#else ]${blog.course.name}
                                [/#if]
                                </li>
                                [/#if]
                              [/#list]
                              [#if labelTypeMap.get(labelType)?? && labelTypeMap.get(labelType)?size>0]
                              <li>[@b.a class="more" href="!awardLabel?labelTypeId="+labelType.id]更多<span class="iconfont icon-gengduo1"></span>[/@]</li>
                              [/#if]
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
