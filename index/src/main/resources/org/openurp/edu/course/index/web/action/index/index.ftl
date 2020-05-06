[#ftl]
[#include "nav.ftl"/]
[@b.head/]
<table class="indexpanel">
	<tr>
		<td class="index_view" style="width: 80%;">
        [@b.form name="courseBlogSearchForm"  action="!search" target="courseBloglist" title="ui.searchForm" theme="search" ]
					<table width="100%">
						<tr>
							<td align="right">课程代码:</td>
							<td><input type="text" name="courseBlog.course.code" style="width:118px;"/></td>
							<td align="right">课程名称:</td>
							<td><input type="text" name="courseBlog.course.name" style="width:118px;"/></td>
							<td align="right">开课院系:</td>
							<td>
								<select style="width:120px" name="courseBlog.department.id">
									<option value="">...</option>
                    [#list departments?sort_by("code") as department]
											<option value="${department.id}">${department.name}</option>
                    [/#list]
								</select>
							</td>
						</tr>
						<tr>
							<td align="right">课程类别:</td>
							<td>
								<select style="width:120px" name="courseGroup" id="courseGroupId">
									<option value="">...</option>
                    [#list courseGroups?if_exists as courseGroup]
											<option value="${courseGroup.id}">${courseGroup.name}</option>
                    [/#list]
								</select>
							</td>
							<td align="right">课程子类别:</td>
							<td>
								<select style="width:120px" name="courseGroup_child" id="courseGroup_childId" items={} empty="..."
												theme="html"/>
							</td>
							<td align="right">课程子子类别:</td>
							<td>
								<select style="width:120px" name="courseGroup_child_child" id="courseGroup_child_childId" items={}
												empty="..." theme="html"/>
							</td>
						</tr>
					</table>
					<input type="hidden" name="orderBy" value="courseBlog.course.code"/>
        [/@]
		</td>
		<td class="index_content">[@b.div id="courseBloglist" href="!search?orderBy=courseBlog.course.code"/]</td>
	</tr>
	<tr>
		<td valign="top" height="90%">[@b.div id="courseBloglist"/]</td>
	</tr>
</table>
[@b.foot/]
<script>
	beangle.load(["jquery-ui", "jquery-chosen", "jquery-colorbox"], function () {
		var formObj = $("form[name=courseBlogSearchForm]");
		var isChildTip = true;
		var isChildChildTip = true;
		formObj.find("select[name='courseGroup_child']").click(function () {
			isChildTip = true;
		}).focus(function () {
			var currVal = $(this).val();
			var courseGroupId = formObj.find("[name='courseGroup']").val();
			var currObj = $(this);
			currObj.empty();

			$.ajax({
				"type": "post",
				"url": "${b.url("index!childrenAjax")}",
				"dataType": "json",
				"data": {
					"courseGroupId": courseGroupId
				},
				"async": false,
				"success": function (data) {

					currObj.append("<option value=\"\">...</option>");
					for (var i = 0; i < data.courseGroups.length; i++) {
						var optionObj = $("<option>");
						optionObj.val(data.courseGroups[i].id);
						optionObj.text(data.courseGroups[i].name);
						if (currVal == "" && data.courseGroups[i].id == currVal) {
							optionObj.attr("selected", true);
						}
						currObj.append(optionObj);
					}
				}
			});
		});

		formObj.find("select[name='courseGroup_child_child']").click(function () {
			isChildChildTip = true;
		}).focus(function () {
			var currVal = $(this).val();
			var courseGroupId = formObj.find("[name='courseGroup_child']").val();
			var currObj = $(this);
			currObj.empty();

			$.ajax({
				"type": "post",
				"url": "${b.url("index!childrenAjax")}",
				"dataType": "json",
				"data": {
					"courseGroupId": courseGroupId
				},
				"async": false,
				"success": function (data) {

          currObj.append("<option value=\"\">...</option>");
					for (var i = 0; i < data.courseGroups.length; i++) {
						var optionObj = $("<option>");
						optionObj.val(data.courseGroups[i].id);
						optionObj.text(data.courseGroups[i].name);
						if (currVal == "" && data.courseGroups[i].id == currVal) {
							optionObj.attr("selected", true);
						}
						currObj.append(optionObj);
					}
				}
			});
		});
	});
</script>