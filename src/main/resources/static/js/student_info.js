$(document).ready(function() {
	set_info();

	$('#btn_submit').click(function() {
		var oldpswd = $('#oldpswd').val();
		var newpswd = $('#newpswd').val();
		var newpswd2 = $('#newpswd2').val();

		if(newpswd2 != newpswd) {
			reset();
			alert("两次输入不一致！");
			return false;
		}

		var studyid = $.cookie('studyid');
		//var ajaxUrl = "../testphp/changePswd.php";
		var ajaxUrl = "/student/changePwd";
		$.ajax({
			url: ajaxUrl,
			type: 'post',
			contentType:'application/json',
			data: JSON.stringify({username: studyid, beforePassword: oldpswd, password: newpswd}),
			success: function(data) {
				var d = eval("(" + data + ")");
				if(d.status == "success") {
					alert('修改密码成功！');
					reset();
				}
				else {
					alert('修改失败！');
					reset();
				}
			}
		});
	});

});

function set_info() {
	// 从cookie取出信息渲染页面
	var name = $.cookie('name');
	var className = $.cookie('className');
	var studyid = $.cookie('studyid'); // 学号
	var idty = $.cookie('idty');

	$('#stu_id').text(studyid);
	$('#stu_name').text(name);
	$('#stu_name2').text(name);
	$('#stu_class').text(className);
	$("#identity").text(idty);
}

function reset() {
	$('#newpswd').val("");
	$('#oldpswd').val("");
	$('#newpswd2').val("");
}
