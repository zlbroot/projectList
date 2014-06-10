$(function(){
	var lis = $("ul.nav li");
	var pathname = location.pathname;
	var tmp = null;
	$.each(lis,function(i,o){
		tmp = $(o);
		var a = tmp.find('a').attr('href')
		if(pathname.search(a)>-1){
			lis.removeClass('active');
			tmp.addClass('active');
			return false;
		}
	});
});