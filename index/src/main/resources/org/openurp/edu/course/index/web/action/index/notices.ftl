[#ftl]
[#include "head.ftl"/]
<div class="wrapper">

    [#include "nav.ftl"/]

  <div class="con_area m_t_30">
    <div class="bg_white p_lr_10" style="min-height: 500px">
      <div class="title_con"><span class="title_text"><i class="quan"></i>通知公告</span></div>
      <div class="tzgg_con">
        <ul id="tzgg">
        </ul>
      </div>
    </div>
  </div>

</div>

</div>

[#include "foot.ftl"/]

</div>
</body>
[@b.foot/]

<script>

  $.ajax({
    "type": "get",
    "url": "${base}/api/platform/bulletin/notices/edu-course-indexapp/3.json",
    "dataType": "json",
    "async": false,
    "success": function (data) {
      data.forEach(function (item) {
        var date = new Date(item.createdAt);
        var month = date.getMonth() + 1;
        if (month < 10) month = "0" + month;
        var day = date.getDate()
        if (day < 10) day = "0" + day
        var dateString = date.getFullYear() + "-" + month + "-" + day;
        var url = "${base}/index/notice?id=" + item.id;
        $("#tzgg").append("<li><span class=\"yuan\"></span><a href=\"" + url + "\">" + item.title + "</a><span class=\"gg_time\">" + dateString + "</span></li>");
      })
    }
  });
</script>
