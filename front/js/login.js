$(document).ready(function() {
	// form表单处理
	$('#btn_login_submit').click(function() {
		var username = $("#username").val();
		var password = $("#password").val();
		var identity = $("#identity input:radio:checked").val();

		alert(identity);
		//alert($("#form_login").serialize());
		var ajaxUrl = "";
		$.ajax({
			url: ajaxUrl,
			type: 'post',
			data: {user: username, pswd: password, id: identity},
			beforeSend: test,
			success: function(data) {
				var d = eval("(" + data + ")");
				alert(d);
			},
			error: function() {
				alert("ajax error!");
			}
		});

	});
});
