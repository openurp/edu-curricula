[#ftl]
<div class="container">
	<div>
		<div class="text-right"><a href="${base}/portal/" target="_blank">课程维护</a></div>
		<div ><img src="${b.static_url("urp","/images/logo_course.png")}"></div>
	</div>
[#include "nav.ftl"/]
[@b.head/]
<table class="indexpanel">
	<tr>
		<td class="index_view" style="width: 80%;">
			[@b.form name="courseBlogSearchForm"  action="!search" target="courseBloglist" title="ui.searchForm" theme="html" ]
				<table width="100%">
					<td align="right">课程名称或代码:</td>
					<td><input type="text" name="nameOrCode" style="width:118px;"/></td>
					<td align="right">开课院系:</td>
					<td>
						<select name="courseBlog.department.id" style="width:120px;" >
							<option value="">...</option>
							[#list departments as department]
								<option value="${(department.id)!}" [#if (courseBlog.department.id)?? && (courseBlog.department.id==department.id)]selected[/#if]>${(department.name)!}</option>
							[/#list]
							<option value="else">其他</option>
						</select>
					</td>
					<td align="right">课程类别:</td>
					<td>
						[@b.select style="width:120px" name="courseGroup" id="courseGroupId" empty="..." items=courseGroups option="id,name"/]
						[#if courseGroup_children ??]
							[@b.select style="width:120px" name="courseGroup_child" items=courseGroup_children empty="..." option="id,name"/]
						[#else ]
							[@b.select style="width:120px" name="courseGroup_child" items={} empty="..."/]
						[/#if]
						[#if courseGroup_child_children ??]
							[@b.select style="width:120px" name="courseGroup_child_child" items=courseGroup_child_children empty="..." option="id,name"/]
						[#else ]
							[@b.select style="width:120px" name="courseGroup_child_child" items={} empty="..."/]
						[/#if]
					</td>
					<td>
						[@b.submit value="查询"/]
					</td>
				</table>
				<input type="hidden" name="orderBy" value="courseBlog.course.code"/>
			[/@]
		</td>
	</tr>
	<tr>
		<td class="index_content">[@b.div id="courseBloglist" href="!search?orderBy=courseBlog.course.code &courseGroup_child=" +Parameters["courseGroup_child"]?if_exists +"&courseBlog.department.id="+ Parameters["courseBlog.department.id"]?if_exists/]</td>
	</tr>
</table>
</div>
[@b.foot/]
<script>
	beangle.load(["jquery-ui", "jquery-chosen", "jquery-colorbox"], function () {
		var formObj = $("form[name=courseBlogSearchForm]");

		formObj.find("select[name='courseBlog.department.id']").val("${(choosedDepartment.id)!}");
		formObj.find("select[name='courseGroup']").val("${(choosedCourseGroup.id)!}");

		formObj.find("select[name='courseGroup']").change(function () {
			var secondVal = formObj.find("[name='courseGroup_child']").val();
			var courseGroupId = $(this).val();
			var secondObj = formObj.find("[name='courseGroup_child']");
			secondObj.empty();

			$.ajax({
				"type": "post",
				"url": "${b.url("index!childrenAjax")}",
				"dataType": "json",
				"data": {
					"courseGroupId": courseGroupId
				},
				"async": false,
				"success": function (data) {

					secondObj.append("<option value=\"\">...</option>");
					for (var i = 0; i < data.courseGroups.length; i++) {
						var optionObj = $("<option>");
						optionObj.val(data.courseGroups[i].id);
						optionObj.text(data.courseGroups[i].name);
						[#--if (currVal != "" && currVal == '${(choosedCourseGroup_child.id)!}') {--]
						[#--	optionObj.attr("selected", true);--]
						[#--}--]
						secondObj.append(optionObj);
					}
				}
			});
		});

		formObj.find("select[name='courseGroup_child']").change(function () {
			var thirdVal = formObj.find("[name='courseGroup_child_child']").val();
			var courseGroupId = $(this).val();
			var thirdObj = formObj.find("[name='courseGroup_child_child']");
			thirdObj.empty();

			$.ajax({
				"type": "post",
				"url": "${b.url("index!childrenAjax")}",
				"dataType": "json",
				"data": {
					"courseGroupId": courseGroupId
				},
				"async": false,
				"success": function (data) {

					thirdObj.append("<option value=\"\">...</option>");
					for (var i = 0; i < data.courseGroups.length; i++) {
						var optionObj = $("<option>");
						optionObj.val(data.courseGroups[i].id);
						optionObj.text(data.courseGroups[i].name);
						// if (currVal == "" && data.courseGroups[i].id == currVal) {
						// 	optionObj.attr("selected", true);
						// }
						thirdObj.append(optionObj);
					}
				}
			});
		});
	});
</script>