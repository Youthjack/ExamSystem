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

		var id = $.cookie('id');
		var ajaxUrl = "../testphp/changePswd.php";
		$.ajax({
			url: ajaxUrl,
			type: 'post',
			data: {id: id, oldpswd: oldpswd, newpswd: newpswd},
			success: function(data) {
				var d = eval("(" + data + ")");
				if(d.state == "0") {
					alert('修改密码成功！');
					reset();
				}
				else {
					alert('修改失败！');
					reset();
				}
			}
		}, 'json');
	});

});

function set_info() {
	// 从cookie取出信息渲染页面
	var name = $.cookie('name');
	var className = $.cookie('className');
	var id = $.cookie('id');
	var idty = $.cookie('idty');

	$('#stu_id').text(id);
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
