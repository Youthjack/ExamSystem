$(document).ready(function() {
    // form表单处理
    $('#btn_s_add_submit').click(function() {
        var name = $("#s_name").val();
        var number = $("#s_username").val();
        var className = $("#s_className").val();
        var email = $("#s_mail").val();
        var username = $("#s_username").val();
        var password = "123456";
        var identity = "STUDENT";
        //alert($("#form_login").serialize());
        var ajaxUrl = "/manager/add";
        var data = JSON.stringify({name: name, number: number, className:className,
            email:email, username: username, password:password, identity:identity});
        $.ajax({
            url: ajaxUrl,
            type: 'post',
            contentType: "application/json",
            data: data,
            success: function(data) {
                var json=eval("(" + data + ")");
                if (json.message == "success")
                {
                    $(".alert").alert("add student: "+name+" successfully!\n"+"the username of "+name +" is :"+username+"!\n"+
                        "the default password is :"+password+"!\n");
                }
                else {
                    $(".alert").alert("add student: "+name+" failed!\n");
                }
            },
            error: function() {
                alert("ajax error!");
            }
        });
        // var formData = new FormData();
        // if(null != $('#s_upfile').files[0]) {
        //     formData.append("file", $("#s_upfile").files[0]);
        // }
/*        $.ajax({
            url: "/manager/batchAddUser",
            type: "POST",
            data: formData,
            /!**
             *必须false才会自动加上正确的Content-Type
             *!/
            contentType: false,
            /!**
             * 必须false才会避开jQuery对 formdata 的默认处理
             * XMLHttpRequest会对 formdata 进行正确的处理
             *!/
            processData: false,
            success: function (data) {
                if (data.message == "success") {
                    alert("批量文件上传成功！");
                }
                if (data.message == "error") {
                    alert("批量文件上传失败！");
                }
            },
            error: function () {
                alert("批量文件上传失败！");
            }
        });*/


    });
});
