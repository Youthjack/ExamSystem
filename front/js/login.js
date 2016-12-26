$(document).ready(function() {
	// form表单处理
	$('#btn_login_submit').click(function() {
		var username = $("#user").val();
		var password = $("#pswd").val();
		var identity = $("#idty input:radio:checked").val();

		var ajaxUrl = '../testphp/login.php';

		// ？？ 加密密码

		// ajax登陆
		$.ajax({
			url: ajaxUrl,
			type: 'post',
			data: {username: username, password: password, identity: identity},
			success: function(data) {
				var d = eval("(" + data + ")");
				if(d.state == "0") { //登陆成功
					$.cookie('id', d.id);
					$.cookie('name', d.name);
					$.cookie('className', d.className);
					$.cookie('idty', d.idty);

					window.location.href = 'student_func.html';
				}
				else {
					alert("密码错误或用户不存在！");
				}
			}
		}, 'json');

	});
});