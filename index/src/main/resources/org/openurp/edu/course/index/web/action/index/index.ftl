[#ftl]
<div class="container"  style="width: 1200px">
[#include "head.ftl"/]
[#include "nav.ftl"/]
[@b.head/]
	<div  style="background-color: white;" >
		[@b.form name="courseBlogSearchForm"  action="!search" target="courseBloglist" title="ui.searchForm" theme="html" ]
			<div class="input-group" style="height: 50px; position: relative;margin: 5px">
				<div style="float: left; margin: 10px;">
					[@b.select  name="courseBlog.department.id" style="width:200px;height:32px;" label="ccc"]
						<option value="">...</option>
						[#list departments as department]
							<option value="${(department.id)!}" [#if (courseBlog.department.id)?? && (courseBlog.department.id==department.id)]selected[/#if]>${(department.name)!}</option>
						[/#list]
						<option value="else">其他</option>
					[/@]
				</div>
				<div style="float: left; margin: 10px;">
					<div class="input-group">
						<input type="text" class="form-control" placeholder="输入课程代码、名称查找">
						<span class="input-group-btn">
							<button class="btn btn-default" type="button" style="width: 34px; height: 34px; background: url(${base}/static/urp/default/images/z_magn.png) center no-repeat white" onclick="search()"></button>
						</span>
					</div>
				</div>
			</div>
			<div style="height: 240px; position: relative; margin: 5px">
				<div style="float: left; margin: 10px;">
					<div>
					[@b.select style="width:200px;height:200px;" name="courseGroup" id="courseGroupId" empty="..." items=courseGroups option="id,name" multiple="multiple"/]
					</div>
				</div>
				<div style="float: left; margin: 10px;">
					<div>
					[#if courseGroup_children ??]
						[@b.select style="width:200px;height:200px;" name="courseGroup_child" items=courseGroup_children empty="..." option="id,name" multiple="multiple" /]
					[#else ]
						[@b.select style="width:200px;height:200px;" name="courseGroup_child" items={} empty="..." multiple="multiple" /]
					[/#if]
					</div>
				</div>
				<div style="float: left; margin: 10px;">
							<div>
							[#if courseGroup_child_children ??]
								[@b.select style="width:200px;height:200px;" name="courseGroup_child_child" items=courseGroup_child_children empty="..." option="id,name" multiple="multiple"/]
							[#else ]
								[@b.select style="width:200px;height:200px;" name="courseGroup_child_child" items={} empty="..." multiple="multiple" /]
							[/#if]
					</div>
						</div>
			</div>

			<input type="hidden" name="orderBy" value="courseBlog.course.code"/>
		[/@]
	</div>
	<div>
		[@b.div id="courseBloglist" href="!search?orderBy=courseBlog.status desc &courseGroup_child=" +Parameters["courseGroup_child"]?if_exists +"&courseBlog.department.id="+ Parameters["courseBlog.department.id"]?if_exists/]
	</div>
</div>
[@b.foot/]
<script>

	function search() {
		var form = document.courseBlogSearchForm;
		setSearchParams(document.courseBlogSearchForm);
		bg.form.submit(form);
	}

	function setSearchParams(form) {
		jQuery('input[name=params]', form).remove();
		var params = jQuery(form).serialize();
		bg.form.addInput(form, 'params', params);
	}

	beangle.load(["jquery-ui", "jquery-chosen", "jquery-colorbox"], function () {
		var formObj = $("form[name=courseBlogSearchForm]");
		formObj.find("select[name='courseBlog.department.id']").val("${(choosedDepartment.id)!}");
		formObj.find("select[name='courseGroup']").val("${(choosedCourseGroup.id)!}");

		formObj.find("select[name='courseBlog.department.id']").change(function () {
			var form = document.courseBlogSearchForm;
			setSearchParams(document.courseBlogSearchForm);
			bg.form.submit(form);
		})

		formObj.find("select[name='courseGroup']").change(function () {
			var form = document.courseBlogSearchForm;
			setSearchParams(document.courseBlogSearchForm);
			bg.form.submit(form);
			})

		formObj.find("select[name='courseGroup_child']").change(function () {
			var form = document.courseBlogSearchForm;
			setSearchParams(document.courseBlogSearchForm);
			bg.form.submit(form);
		})

		formObj.find("select[name='courseGroup_child_child']").change(function () {
			var form = document.courseBlogSearchForm;
			setSearchParams(document.courseBlogSearchForm);
			bg.form.submit(form);
		})

		formObj.find("select[name='courseGroup']").children("option").click(function () {
			var secondObj = formObj.find("[name='courseGroup_child']");
			secondObj.empty();
			var thirdObj = formObj.find("[name='courseGroup_child_child']");
			thirdObj.empty();

			$.ajax({
				"type": "post",
				"url": "${b.url("index!childrenAjax")}",
				"dataType": "json",
				"data": {
					"courseGroupId": $(this).val()
				},
				"async": false,
				"success": function (data) {

					for (var i = 0; i < data.courseGroups.length; i++) {
						var optionObj = $("<option>");
						optionObj.val(data.courseGroups[i].id);
						optionObj.text(data.courseGroups[i].name);
						optionObj.click(function () {
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

									for (var i = 0; i < data.courseGroups.length; i++) {
										var optionObj = $("<option>");
										optionObj.val(data.courseGroups[i].id);
										optionObj.text(data.courseGroups[i].name);
										thirdObj.append(optionObj);
									}
								}
							});
						});
						secondObj.append(optionObj);
					}
				}
			});
		});
	});
</script>