[#ftl/]
[@b.head/]
[@b.toolbar title="教师资料"/]
<table class="infoTable" width="100%">
	<tr>
		<td class="title" width="15%">工号:</td>
		<td class="content">${(teacherBlog.user.code)!}</td>
		<td class="title" width="15%">姓名:</td>
		<td class="content">${(teacherBlog.user.name)!}</td>
		<td class="title" width="15%">部门:</td>
		<td class="content">${(teacherBlog.user.department.name)!}</td>
	</tr>
	<tr>
		<td class="title" width="15%">个人简介:</td>
		<td class="content" colspan="5">${teacherBlog.intro!}</td>
	</tr>
	<tr>
		<td class="title" width="15%">方向:</td>
		<td class="content" colspan="5">${teacherBlog.research!}</td>
	</tr>
	<tr>
		<td class="title" width="15%">科研成果:</td>
		<td class="content" colspan="5">${teacherBlog.harvest!}</td>
	</tr>
	<tr>
		<td class="title" width="15%">联系方式:</td>
		<td class="content" colspan="5">${teacherBlog.contact!}</td>
	</tr>
</table>
<div style="text-align:center">
	<a class="btn btn-default" href="${b.url('!edit?id=' +teacherBlog.id)}" role="button">修改</a>
</div>
[@b.foot/]
