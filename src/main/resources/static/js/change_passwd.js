$(document).ready(function() {
    // form表单处理
    $('#btn_change_submit').click(function() {
        //document.cookie('username', "2333");
        var geturl="/manager/see";
        var user;
        var username = $.cookie('username');
        var password = $("#r_password").val();
        var ppasswd = $("#r_p_password").val();
        var identity;
        var name;
        var number;
        var className;
        var email;
        if (password==ppasswd) {
            $.get(geturl, function (data) {
                user = eval("(" + data + ")");
                for (var o in user) {
                    if (user[o].username == username) {
                        identity = user[o].identity;
                        name = user[o].name;
                        number = user[o].number;
                        className = user[o].className;
                        email = user[o].className;
                        break;
                    }
                }
                var ajaxUrl = "/manager/update";
                $.ajax({
                    url: ajaxUrl,
                    type: 'post',
                    contentType: 'application/json',
                    data:JSON.stringify({
                        username: username,
                        password: password,
                        identity: identity,
                        name: name,
                        number: username,
                        className: className,
                        email: email
                    }),
                    success: function (data) {
                        var json = eval("(" + data + ")");
                        if (json.status == "success") {
                            alert("change password successfully!\n" +
                                "the password is :" + password + "!\n");
                        }
                        else {
                            alert("change password  failed!\n");
                        }
                    },
                    error: function () {
                        alert("ajax error!");
                    }
                });
            });
            //alert($("#form_login").serialize());

        }
        else { alert("输入的密码不一致！");
        }
    });
});
