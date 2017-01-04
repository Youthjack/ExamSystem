$(document).ready(function() {
    // form表单处理
    $('#btn_t_reset_submit').click(function() {
        var geturl="/manager/see";
        var user;
        var username = $("#r_username").val();
        var password = "123456";
        var identity;
        var name;
        var number;
        var className;
        var email;
        $.get(geturl,function(data){
            user=eval("(" + data + ")");
            for(var o in user){
                if (user[o].username==username)
                {
                    identity=user[o].identity;
                    name=user[o].name;
                    number=user[o].number;
                    className=user[o].className;
                    email=user[o].email;
                    break;
                }
            }
            var ajaxUrl = "/manager/update";
            $.ajax({
                url: ajaxUrl,
                type: 'post',
                contentType:"application/json",
                data: JSON.stringify({ username: username, password:password, identity:identity,
                    name:name, number:username, className:className, email:email}),
                success: function(data) {
                    var json=eval("(" + data + ")");
                    if (json.status == "success")
                    {
                        alert("reset password for username: "+username+" successfully!\n"+
                            "the default password is :"+password+"!\n");
                    }
                    else {
                        alert("reset password for username: "+username+" failed!\n");
                    }
                },
                error: function() {
                    alert("ajax error!");
                }
            });

        });
        //alert($("#form_login").serialize());

    });
});
