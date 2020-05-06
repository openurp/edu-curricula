<ul class="nav nav-pills">
	<li role="presentation" class="active"><a href="${b.url('index')}">首页</a></li>
	[#list courseGroups as courseGroup]
		<li role="presentation" class="dropdown">
			<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true"
				 aria-expanded="false">
				${courseGroup.name} <span class="caret"></span>
			</a>
			<ul class="dropdown-menu">
				[#list courseGroup.children as child]
					<li>
						[@b.a href="!search?courseGroup_child="+child.id target="courseBloglist"]${child.name}[/@b.a]
					</li>
				[/#list]
			</ul>
		</li>
	[/#list]
	<li role="presentation" class="dropdown">
		<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true"
			 aria-expanded="false">
        院系 <span class="caret"></span>
		</a>
		<ul class="dropdown-menu">
        [#list departments as department]
					<li>
						[@b.a href="!search?courseBlog.department.id="+department.id  target="courseBloglist" ]${department.name}[/@b.a]
					</li>
        [/#list]
		</ul>
	</li>
</ul>
[@b.div id="nav"/]