[#ftl]
[@b.head/]
[@b.toolbar title='修订设置' /]
[@b.form name="searchForm" action="" method="get"/]
[@b.div id="listFrame"/]

<script>
	var form = document.searchForm;
	search();
	function search(pageNo,pageSize,orderBy){
		form.target="listFrame";
		form.action="${b.url('!search')}";
		bg.form.submit(form)
	}
</script>

[@b.foot/]
