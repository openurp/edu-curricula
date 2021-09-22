[#ftl]
[@b.head/]
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
    <th>授课教师</th>
  </tr>
  </thead>
  <tbody>
  [#list courseBlogMetas as courseBlogMeta]
    <tr>
      <td width="10%" >${(courseBlogMeta.course.code)!}</td>
      <td width="15%" >
        [#if blogMap.get(courseBlogMeta) ??]
          <a onclick="openLayer('${blogMap.get(courseBlogMeta).id}')">
            <span style="text-decoration:underline;">${courseBlogMeta.course.name}</span>
          </a>
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
[#if courseBlogMetas?size>0]
  [#assign param = "&courseGroup=${Parameters['courseGroup']!}&courseGroup_child=${Parameters['courseGroup_child']!}&courseGroup_child_child=${Parameters['courseGroup_child_child']!}&courseBlog.department.id=${Parameters['courseBlog.department.id']!}&courseBlog.semester.id=${Parameters['courseBlog.semester.id']!}"]
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
  <div class="text_center m_t_20">
    <div class="page">
      <ul class="clearfix">
        <li>
          [@b.a href="!search?pageIndex=1&pageSize=25"+param target="courseBloglist"]
            <span aria-hidden="true">首页</span>
          [/@]
        </li>
        [#if pageIndex>1]
          <li>
            [@b.a href="!search?pageIndex=${pageIndex-1}&pageSize=25"+param target="courseBloglist"]
              <span aria-hidden="true">上一页</span>
            [/@]
          </li>
        [/#if]
        [#list start..end as i]
          <li>[@b.a href="!search?pageIndex=${i}&pageSize=25"+param  target="courseBloglist"]${i}[/@]</li>
        [/#list]
        [#if pageIndex!=totalPages]
          <li>
            [@b.a href="!search?pageIndex=${pageIndex+1}&pageSize=25"+param  target="courseBloglist"]
              <span aria-hidden="true">下一页</span>
            [/@]
          </li>
        [/#if]
        <li>
          [@b.a href="!search?pageIndex=${totalPages}&pageSize=25"+param target="courseBloglist"]
            <span aria-hidden="true">尾页</span>
          [/@]
        </li>
      </ul>
    </div>
  </div>
[/#if]
<script>
  $(".kc_table tbody tr:odd").css({"background":"#eeebea"});
  function openLayer(id){
    var url = "${b.url('index!info?id=aaa')}";
    var newUrl = url.replace("aaa",id);
    $('.tk_box').load(newUrl,null,function () {
      $(".tk_box").css("display","block");
      var tkH = $(".tk_box").height();
      $(".tk_box").animate({marginTop:-tkH/2},"slow");
      $(".tk_box .tk_title a").click(function(){
        $(".tk_box").css({"top":"50%","margin-top":"0px"});
        $(this).parents(".tk_box").css("display","none");
      });
    })
  };
</script>
[@b.foot/]
