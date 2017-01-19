$(document).ready(function() {
	var name = $.cookie('name');
	var idty = $.cookie('idty');
	$("#stu_name").text(name);
	$("#identity").text(idty);

	// 功能选择界面hover
	func_hover('#exam', '考试', '考试2');
	func_hover('#review', '知识', '知识2');
	func_hover('#history', '历史', '历史2');
	func_hover('#info', '个人信息', '个人信息2');

	// 功能界面，用于ajax刷新内容
	$('#exam').on('click', function() {
		window.location.href = 'student_exam.html';
	});
	$('#review').on('click', function() {
		window.location.href = 'student_review.html';
	});
	$('#history').on('click', function() {
		window.location.href = 'student_history.html';
	});
	$('#info').on('click', function() {
		window.location.href = 'student_info.html';
	});
});

function func_hover(whois, img1, img2) {
	$(whois).hover(
	function(){
		$(this).css("background-color", "#1296DB");
		$(this).children('img').attr('src', '../img/' + img2 + '.png');
		$(this).children('h4, span').css("color", "#FFFFFF");
		return false;
	},
	function(){
		$(this).css("background-color", "#FFFFFF");
		$(this).children('img').attr('src', '../img/' + img1 + '.png');
		$(this).children('h4').css("color", "#333333");
		$(this).children('span').css("color", "#777777");
		return false;
	});
}