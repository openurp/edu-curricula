[#ftl/]
[@b.head/]
[#if courseBlog.status = BlogStatus.Published]
    <div class="tk_title">标题内容标题内容标题内容标题内容 <a href="javascript:;"><span class="iconfont icon-guanbi"></span></a></div>
    <div class="tk_con">
      <table class="tk_table">
        <tr>
          <td style="width:100px;"><span class="tk_table_title">课程代码：</span></td>
          <td>${courseBlog.course.code!}</td>
        </tr>
        <tr>
          <td><span class="tk_table_title">课程名称：</span></td>
          <td>${courseBlog.course.name!}</td>
        </tr>
        <tr>
          <td><span class="tk_table_title">中文简介：</span></td>
          <td>${courseBlog.description!}</td>
        </tr><tr>
          <td><span class="tk_table_title">英文简介：</span></td>
          <td>${courseBlog.enDescription!}</td>
        </tr>
        <tr>
          <td><span class="tk_table_title">总学分：</span></td>
          <td>${(courseBlog.course.credits)!}</td>
        </tr>
        <tr>
          <td><span class="tk_table_title">总学时：</span></td>
          <td>${(courseBlog.course.creditHours)!}</td>
        </tr>
      </table>
      <div class="text_center">
        <a class="jrkc" href="${b.url('!detail?id='+courseBlog.id!)}" style="box-sizing: content-box;" target="_blank">进入课程</a>
      </div>
    </div>
[#else ]该课程资料未发布，暂时不能查看
[/#if]
[@b.foot/]
