$(document).ready(function() {

	// form表单处理
	$('#btn_login_submit').click(function() {
		var username = $("#user").val();
		var password = $("#pswd").val();
		//var ajaxUrl = '../testphp/login.php';
		var ajaxUrl = '/login_auth';
		// ajax登陆
		$.ajax({
			url: ajaxUrl,
			type: 'post',
			contentType: "application/json",
			data: JSON.stringify({username: username, password: password}),
			success: function(data) {
				data = JSON.stringify(data);
				//console.log(data);
				var d = eval("(" + data + ")");
				console.log(d);
				if(d.status == "success") { //登陆成功
					$.cookie('id', d.id);
					$.cookie('idty', d.idty);
					if(d.idty == 'STUDENT') {
						$.cookie('name', d.name);
						$.cookie('className', d.className);
						$.cookie('studyid', username);
						window.location.href = 'student_func.html';
					}
					if(d.idty == 'ADMIN') {
						window.location.href = 'manager.html';
						$.cookie('username',username);
					}
					if(d.idty == 'TEACHER') {
						$.cookie('name', d.name);
						$.cookie('email', d.email);
						window.location.href = 'teacher_func.html';
					}
				}
				else {
					alert("密码错误或用户不存在！");
				}
			}
		});

	});
});
