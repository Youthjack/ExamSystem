$(document).ready(function() {
    // form表单处理
    $('#btn_t_add_submit').click(function() {
        var name = $("#t_name").val();
        var number = $("#t_username").val();
        var className = "";
        var email = $("#t_mail").val();
        var username = $("#t_username").val();
        var password = "123456";
        var identity = "TEACHER";
        //alert($("#form_login").serialize());
        var ajaxUrl = '/manager/add';
        $.ajax({
            url: ajaxUrl,
            type: 'post',
            contentType:"application/json",
            data: JSON.stringify(
                {"name": name, "number": number, "className":className, "email":email,
                    "username": username, "password":password, "identity":identity}),
            success: function(data) {
                console.log(data);
                var json=eval("(" + data + ")");
                if (json.status == "success")
                {
                    var text=$("#t_class").find("option:selected").val();
                    console.log(text);
                    var geturl1="/manager/see";
                    var id;
                    var user;
                    var identity1;
                    var studentId="";
                    var teacherId;
                    $.get(geturl1,function(data){
                        user=eval("(" + data + ")");
                        for(var o in user){
                            identity1=user[o].identity;
                            if (identity1 == "STUDENT")
                            {
                                if (text==user[o].className)
                                {
                                    studentId+=user[o].id+"_";
                                }
                            }
                            else if (identity=="TEACHER")
                            {
                                if (username==user[o].username){
                                    teacherId=user[o].id;
                                }
                            }
                        }
                        studentId = studentId.substr(0,studentId.length-1);
                        ajaxUrl="/manager/addStudents";
                        $.ajax({
                            url: ajaxUrl,
                            type: 'post',
                            contentType:'application/json',
                            data: JSON.stringify({"studentsId": studentId, "teacherId": teacherId, "number":number,
                                "name":name, "email": email}),
                            success: function(data) {
                                var json=eval("(" + data + ")");
                                if (json.status == "success")
                                {
                                    alert("add teacher: "+name+" successfully!\n"+"the username of "+name +" is :"+username+"!\n"+
                                        "the default password is :"+password+"!\n");
                                }
                                else {
                                    alert("add teacher: "+name+" failed!\n");
                                    return;
                                }
                            },
                            error: function() {
                                alert("ajax error!");
                                return;
                            }
                        });
                    });
                }
                else {
                    alert("add teacher: "+name+" failed!\n");
                    return;
                }
            },
            error: function() {
                alert("ajax error!");
                return;
            }
        });




    });
    function getclass() {
        var geturl="/manager/see";
        var id;
        var user;
        var username;
        var password;
        var identity;
        var name;
        var number;
        var className;
        var email;
        $.get(geturl,function(data){
            user=eval("(" + data + ")");
            var row = $('#t_class');
            row.html("");
            var rowstring="";
            var myclass=new Array();
            var i=0;
            for(var o in user){
                identity=user[o].identity;
                if (identity == "STUDENT")
                {
                    var flag=0;
                    className=user[o].className;
                    for (var c in myclass)
                    {
                        if (className==myclass[c])
                        {
                            flag=1;
                            break;
                        }
                    }
                    if(flag==0)
                    {
                        myclass[i]=className;
                        i=i+1;
                        rowstring+= '<option>'+className+'</option>\n';
                    }
                }
            }
            row.html(rowstring);
        });
    }
    getclass();
});
