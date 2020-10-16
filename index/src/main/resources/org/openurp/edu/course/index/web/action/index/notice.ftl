[#ftl]
[@b.head/]
[#include "head.ftl"/]

<body>
<div class="wrapper">


    [#include "nav.ftl"/]

	<div class="con_area m_t_30">
		<div class="bg_white p_lr_10">
			<div class="title_con"><span class="title_text"><i class="quan"></i>通知公告</span></div>
			<div>
				<div id="title" class="tit"></div>
				<div id="createdAt" class="futit"></div>
				<div id="content" class="rong"></div>
				<div id="docs" class="rong"></div>
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
		"url": "${base}/api/platform/bulletin/notices/${id}.json",
		"dataType": "json",
		"async": false,
		"success": function (data) {
				var date = new Date(data.createdAt);
				var month = date.getMonth() + 1;
				if (month < 10) month = "0" + month;
				var day = date.getDate()
				if (day < 10) day = "0" + day
				var dateString = date.getFullYear() + "-" + month + "-" + day;
				$("#title").text(data.title);
				$("#createdAt").text("发布日期：" + dateString);
				$("#content").html(data.contents);
				data.docs.forEach(function (doc,index) {
					var no = index+1
					$("#docs").append("<a class=\"doc\" href=\"" + doc.url + "\">附件"+no+":"+doc.name+"</a><br>")
				})
		}
	});
</script>
<style>
	.doc{
		color: #0b54b0;
	}
	.tit {
		text-align: center;
		font-family: 微软雅黑;
		font-size: 18px;
		color: rgb(81, 81, 81);
		height: 36px;
		line-height: 36px;
	}
	.futit {
		font-size: 12px;
		font-family: 宋体;
		color: rgb(127, 126, 126);
		text-align: center;
		height: 22px;
		line-height: 22px;
	}
	.rong {
		width: 1180px;
		font-size: 12px;
		line-height: 24px;
		font-family: 微软雅黑;
		color: rgb(51, 51, 51);
		margin: 20px auto;
	}
</style>