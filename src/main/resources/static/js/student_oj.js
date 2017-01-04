$(document).ready(function() {
	getOjLists();
	set_info();
});

function getOjLists() {
	var ajaxUrl = '/oj/get/0';
	// var ajaxUrl = '../testphp/getAllOj.php';
	$.ajaxSetup({
		contentType: 'application'
	});
	$.ajax({
		type: 'GET',
		url: ajaxUrl,
		success: function(data, status) {
			var d = eval("(" + data + ")");

			var num = 0;
			for (dd in d) {
				num += 1;
			}

			// 渲染页面
			var tmp = "";
			$('#oj_questions').html("");
			for (var i=0; i<num; i++) {
				tmp += '<tr>' +
                				'<th>' + d[i]['id'] + '</th>' + 
                				'<th><a id="' + d[i]['id'] + '" onclick="getOj(this)">' + d[i]['title'] + '</a></th>' +
                				'<th>' + d[i]['acceptance'] + '</th>' +
                				'<th>' + d[i]['difficulty'] + '</th>' +
              				'</tr>';
			}
			$('#oj_questions').html(tmp);

		}
	});
}

function getOj(e) {
	var qid = e.id;
	var username = $.cookie('studyid');

	// var ajaxUrl = '../testphp/getAllOj.php';
	var ajaxUrl = '/oj/get/' + qid;
	$.ajaxSetup({
		contentType: 'application'
	});
	$.ajax({
		type: 'GET',
		url: ajaxUrl,
		success: function(data, status) {

			var d = eval('(' + data + ')');
			$('#right').html("");

			var tmp = '<h2 class="page-header">' + d['id'] + '.&emsp;' + d['title'] + '</h2>' +
            			'<h4 class="page-header">Title:</h4>' +
            			'<div class="panel panel-default">' +
              				'<div class="panel-body">' + d['content'] + '</div>' +
           				'</div>' +
            			'<h4 class="page-header">Answer:</h4>' +
            			'<textarea id="mySQL" class="form-control" rows="4" placeholder="please input your sql"></textarea>' +
            			'<p></p>' +
            			'<button id="submit" class="btn btn-primary btn-lg">submit</button>' +
            			'<button id="reset" class="btn btn-default btn-lg">reset</button>' +
            			'<a style="float: right" href="student_oj.html">返回</a>';
            $('#right').html(tmp)

            $('#submit').click(function() {
            	var mySQL = $('#mySQL').val();
            	if (mySQL != "") { // 如果输入不为空的时候提交
            		$.ajaxSetup({
            			contentType: 'application/json'
            		});
            		$.ajax({
            			type: 'POST',
            			url: '/oj/submit_auth',
            			data: JSON.stringify({'id': parseInt(qid), 'username': username, 'sql': mySQL}),
            			success: function(data, status) {
            				data = eval('('+data+')');
            				if(data.status == 'success') {
            					alert('access');
            				} else {
            					alert('error');
            				}
            			}
            		});
            	} else {
            		alert("输入为空！");
            	}
            });

            $('#reset').click(function() {
            	$('#mySQL').val("");
            });
		},
	});
}

function set_info() {
	// 从cookie取出信息渲染页面
	var name = $.cookie('name');
	var className = $.cookie('className');
	var id = $.cookie('studyid');
	var idty = $.cookie('idty');

	$('#stu_id').text(id);
	$('#stu_name').text(name);
	$('#stu_name2').text(name);
	$('#stu_class').text(className);
	$("#identity").text(idty);
}