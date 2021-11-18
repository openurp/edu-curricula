
<div class="con_area">
  <div class="clearfix">
    <div class="logo fl"><img alt="logo" src="${b.static_url('local','images/course.png')}"></div>
      [@b.form name="blogSearchForm"  action="!courseBlogForName"  title="ui.searchForm" theme="html" ]
        <div class="search_con fr">
          <input class="secar_input" type="text" name="nameOrCode" value="${(Parameters['nameOrCode']?html)!}" placeholder="输入课程代码，名称"><button class="secar_btn" type="button" onclick="courseBlogForName()"><span class="iconfont icon-search01"></span>查询</button>
        </div>
      [/@]
  </div>
</div>

<div class="nav_con">
  <div class="con_area">
    <ul class="nav_list clearfix">
      <li><a href="${b.url('index')}">首页</a></li>
      <li><a href="${b.url('index')}">课程查询</a>
        <ul class="subnav">
          <li>[@b.a  href="!courseBlogForType"]课程类别[/@]</li>
          <li>[@b.a  href="!courseBlogForDepart?id="+firstDepartment.id]院系[/@]
            <ul class="trlnav">
              [#list departments as department]
                <li>[@b.a href="!courseBlogForDepart?id="+department.id]${department.name}[/@]</li>
              [/#list]
              <li>[@b.a href="!courseBlogForDepart?id=0"]其他[/@b.a]</li>
            </ul>
          </li>
        </ul>
      </li>
      <li>[@b.a href="!awardLabelMap"]优质课程[/@b.a]
        <ul class="subnav">
[#--          <li>[@b.a href="!awardLabelMap"]课程总览[/@]</li>--]
          [#list awardLabelTypes?sort_by("code") as type]
            <li>
                [@b.a href="!awardLabel?labelTypeId="+type.id]${type.name}[/@]
            </li>
          [/#list]
        </ul>
      </li>
[#--      <li><a href="#">课程统计</a></li>--]
[#--      <li><a href="#">课程建设</a></li>--]
[#--      <li><a href="#">课程服务</a>--]
[#--        <ul class="subnav">--]
[#--          <li><a href="#">使用指南</a></li>--]
[#--          <li><a href="#">常用下载</a>--]
[#--          </li>--]
[#--        </ul>--]
[#--      </li>--]
    </ul>
  </div>
</div>

<script>
  function courseBlogForName() {
    var form = document.blogSearchForm;
    setSearchParams(form);
    bg.form.submit(form);
  }

  function setSearchParams(form) {
    jQuery('input[name=params]', form).remove();
    var params = jQuery(form).serialize();
    bg.form.addInput(form, 'params', params);
  }
</script>
