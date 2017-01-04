$(document).ready(function() {
    // form表单处理
    $('#btn_model').click(function() {
        var geturl="/manager/see";
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
            var row = $('#row');
            row.html("");
            var rowstring="";
            rowstring+='<table class="table table-bordered table-hover">\n'+
                '<thead>\n'+
                '<tr class="success">\n'+
                '<th>用户名</th>\n'+
                '<th>姓名</th>\n'+
                '<th>邮箱/班级</th>\n'+
                '<th>身份</th>\n'+
                '</tr>\n'+
                '</thead>\n'+
                '<tbody>\n';
            for(var o in user){
                identity=user[o].identity;
                name=user[o].name;
                username=user[o].username;
                if (identity == "STUDENT") email=user[o].className;
                else if (identity == "TEACHER") email=user[o].email;
               else if (identity == "ADMIN") continue;
                rowstring+='<tr>\n<td>'+username+'</td>\n'+
                                '<td>'+name+'</td>\n'+
                                '<td>'+email+'</td>\n'+
                                '<td>'+identity+'</td>\n'
            }
            rowstring+= '</tbody>\n'+
            '</table>\n';
            row.html(rowstring);
            initTableCheckbox();
        });
    });
    $('#btn_delete_submit').click(function() {
        //alert($("#form_login").serialize());
        var ajaxUrl = "/manager/delete";
        var username=$("#r_username").val();
        var geturl="/manager/see";
        var identity;
        var flag=0;
        $.get(geturl,function(data) {
            var user1 = eval("(" + data + ")");
            for (var o in user1) {
                if (user1[o].username == username) {
                    identity = user1[o].identity;
                    flag = 1;
                    break;
                }
            }
            if (flag==0)
            {
                alert("不存在该用户名！");
                return;
            }
            if (identity == "ADMIN")
            {
                alert("没有权限删除管理员用户！\n");
            }
            else {
                $.ajax({
                    url: ajaxUrl,
                    contentType:"application/json",
                    type: 'post',
                    data: JSON.stringify({username: username,
                        identity: identity}),
                    success: function (data) {
                        var json = eval("(" + data + ")");
                        if (json.status == "success") {
                            alert("delete username: " + username + " successfully!\n");
                        }
                        else {
                            alert("delete username: " + username + " failed!\n");
                        }
                    },
                    error: function () {
                        alert("ajax error!");
                    }
                });
            }
        });

    });

    $('#btn_delete_m_submit').click(function() {
        //alert($("#form_login").serialize());
        var $tbr = $('table tbody tr');
        $tbr.find('input:checked').each(function(){
            var username=$(this).parent().parent().find("td").eq(1).text();
            var identity=$(this).parent().parent().find("td").eq(4).text();
            var ajaxUrl='/manager/delete';
        $.ajax({
            url: ajaxUrl,
            type: 'post',
            contentType: 'application/json',
            data: JSON.stringify({ username: username,
                    identity:identity}),
            success: function(data) {
                var json=eval("(" + data + ")");
                if (json.status == "success")
                {
                  alert("delete username: "+username+" successfully!\n");
                }
                else {
                  alert("delete username: "+username+" failed!\n");
                }
            },
            error: function() {
                alert("ajax error!");
            }
        });
        });

    });
    function initTableCheckbox() {
        var $thr = $('table thead tr');
        var $checkAllTh = $('<th><input type="checkbox" id="checkAll" name="checkAll" /></th>');
        /*将全选/反选复选框添加到表头最前，即增加一列*/
        $thr.prepend($checkAllTh);
        /*“全选/反选”复选框*/
        var $checkAll = $thr.find('input');
        $checkAll.click(function(event){
            /*将所有行的选中状态设成全选框的选中状态*/
            $tbr.find('input').prop('checked',$(this).prop('checked'));
            /*并调整所有选中行的CSS样式*/
            if ($(this).prop('checked')) {
                $tbr.find('input').parent().parent().addClass('warning');
            } else{
                $tbr.find('input').parent().parent().removeClass('warning');
            }
            /*阻止向上冒泡，以防再次触发点击操作*/
            event.stopPropagation();
        });
        /*点击全选框所在单元格时也触发全选框的点击操作*/
        $checkAllTh.click(function(){
            $(this).find('input').click();
        });
        var $tbr = $('table tbody tr');
        var $checkItemTd = $('<td><input type="checkbox" name="checkItem" /></td>');
        /*每一行都在最前面插入一个选中复选框的单元格*/
        $tbr.prepend($checkItemTd);
        /*点击每一行的选中复选框时*/
        $tbr.find('input').click(function(event){
            /*调整选中行的CSS样式*/
            $(this).parent().parent().toggleClass('warning');
            /*如果已经被选中行的行数等于表格的数据行数，将全选框设为选中状态，否则设为未选中状态*/
            $checkAll.prop('checked',$tbr.find('input:checked').length == $tbr.length ? true : false);
            /*阻止向上冒泡，以防再次触发点击操作*/
            event.stopPropagation();
        });
        /*点击每一行时也触发该行的选中操作*/
        $tbr.click(function(){
            $(this).find('input').click();
        });
    }
});


$(function(){
    function initTableCheckbox() {
        var $thr = $('table thead tr');
        var $checkAllTh = $('<th><input type="checkbox" id="checkAll" name="checkAll" /></th>');
        /*将全选/反选复选框添加到表头最前，即增加一列*/
        $thr.prepend($checkAllTh);
        /*“全选/反选”复选框*/
        var $checkAll = $thr.find('input');
        $checkAll.click(function(event){
            /*将所有行的选中状态设成全选框的选中状态*/
            $tbr.find('input').prop('checked',$(this).prop('checked'));
            /*并调整所有选中行的CSS样式*/
            if ($(this).prop('checked')) {
                $tbr.find('input').parent().parent().addClass('warning');
            } else{
                $tbr.find('input').parent().parent().removeClass('warning');
            }
            /*阻止向上冒泡，以防再次触发点击操作*/
            event.stopPropagation();
        });
        /*点击全选框所在单元格时也触发全选框的点击操作*/
        $checkAllTh.click(function(){
            $(this).find('input').click();
        });
        var $tbr = $('table tbody tr');
        var $checkItemTd = $('<td><input type="checkbox" name="checkItem" /></td>');
        /*每一行都在最前面插入一个选中复选框的单元格*/
        $tbr.prepend($checkItemTd);
        /*点击每一行的选中复选框时*/
        $tbr.find('input').click(function(event){
            /*调整选中行的CSS样式*/
            $(this).parent().parent().toggleClass('warning');
            /*如果已经被选中行的行数等于表格的数据行数，将全选框设为选中状态，否则设为未选中状态*/
            $checkAll.prop('checked',$tbr.find('input:checked').length == $tbr.length ? true : false);
            /*阻止向上冒泡，以防再次触发点击操作*/
            event.stopPropagation();
        });
        /*点击每一行时也触发该行的选中操作*/
        $tbr.click(function(){
            $(this).find('input').click();
        });
    }
    initTableCheckbox()
});
