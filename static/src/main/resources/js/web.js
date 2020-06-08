// JavaScript Document
$(function(){
	$(".nav_list > li").click(function(){
		$(this).addClass("nav_on").siblings("li").removeClass("nav_on");
	});
	$(".select_zk option:even").css({"background":"#f3efeb"});
	$(".kc_table tbody tr:odd").css({"background":"#eeebea"});
	$(".yx_nav li").click(function(){
		$(this).addClass("yx_navon").siblings("li").removeClass("yx_navon");
	});
	$(".jkc_item li:last-child").css({"width":"865px","padding-left":"0","text-align":"right"});

})