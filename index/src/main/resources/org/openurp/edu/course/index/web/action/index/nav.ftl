<ul class="nav nav-pills">
	[#if choosedCourseGroup?? || choosedDepartment ??]
		<li role="presentation">
	[#else ]
		<li role="presentation" class="active">
	[/#if]
		<a href="${b.url('index')}">首页</a>
	</li>
	[#list courseGroups as courseGroup]
		[#if choosedCourseGroup?? && courseGroup == choosedCourseGroup]
			<li role="presentation" class="dropdown active">
		[#else ]
			<li role="presentation" class="dropdown">
		[/#if]
			<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true"
				 aria-expanded="false">
				${courseGroup.name} <span class="caret"></span>
			</a>
			<ul class="dropdown-menu">
				[#list courseGroup.children as child]
					<li>
						[@b.a href="!index?courseGroup_child="+child.id]${child.name}[/@b.a]
					</li>
				[/#list]
			</ul>
		</li>
	[/#list]
	[#if choosedDepartment??]
		<li role="presentation" class="dropdown active">
	[#else]
		<li role="presentation" class="dropdown">
	[/#if]
		<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
        院系 <span class="caret active"></span>
		</a>
		<ul class="dropdown-menu">
        [#list departments as department]
					<li>
						[@b.a href="!index?courseBlog.department.id="+department.id]${department.name}[/@b.a]
					</li>
        [/#list]
		</ul>
	</li>
</ul>
[@b.div id="nav"/]