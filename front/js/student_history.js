$(document).ready(function() {
	set_info();

	var ajaxUrl = '../testphp/getHistoryExamList.php';

	var id = $.cookie('id');
	$.ajax({
		url: ajaxUrl,
		type: 'post',
		data: {id: id},
		success: function(data) {
			var d = eval("(" + data + ")");
			if(d.state == "0") {
				$('#history_exam').html("");
				var str = "";

				for(var i=0; i<d.num % 3; i++) {
					var btn_id = id + '_' + d[i*3];
					var tmp = '<tr>' +
                				'<th>' + i + '</th>' +
                				'<th>' + d[i*3+0] + '</th>' +
                				'<th>' + d[i*3+1] + '</th>' +
                				'<th>' + d[i*3+2] + '</th>' +
                				'<th><button id="' + btn_id + '" type="button" class="btn btn-primary" data-toggle="modal"' + 
                				' data-target="#myModal">click</button></th>' +
              				'</tr>';
              		str += tmp;
				}
				$('#history_exam').html(str);
			}
			else {
				//alert('修改失败！');
			}
		}
	}, 'json');

	$('#history_exam').on('click', 'button', function(e) {
		var btn_id = e.target.id.split('_');
		var id = btn_id[0];
		var exam_id = btn_id[1];

		var ajaxUrl = '../testphp/getHistoryExam.php';
		$.ajax({
			url: ajaxUrl,
			type: 'post',
			data: {id: id, exam_id: exam_id},
			success: function(data) {
				var modal_content = $('#modal_content');
				var modal_situ = $('#modal_situ');

				modal_content.html("");
				modal_situ.html("");

				var modal_content_string = "";
				var modal_situ_string = "";

				var d = eval("(" + data + ")");
				for(var i=1; i < d['num']+1; i++) {
					var t = i.toString();
					//console.log(d[t]['type']);
					if(d[t]['type'] == '0') { //选择题
						var myAnswer = changeABCDtoInt(d[t]['myAnswer']);
						var rightAnswer = changeABCDtoInt(d[t]['rightAnswer']);

						var isRight = myAnswer == rightAnswer ? true : false; //是否答对
						var panelStyle = isRight ? 'panel panel-success' : 'panel panel-danger';

						modal_content_string += '<div id="t' + t + '" class="panel panel-' + panelStyle + '">' +
              									'<div class="panel-heading">第' + t + '题-选择题（2分）<a style="float: right" href="#modal_content">返回顶部</a></div>' +
              									'<div class="panel-body">' +
                								'<p>' + d[t]['title'] + '</p>' +
              									'</div>' +
              									'<div class="btn btn-group">';
              			for(var j=0; j<4; j++) { //选择项的渲染
              				if (isRight) { // 如果做对了
              					if (j == rightAnswer) {
              						modal_content_string += '<button class="btn btn-success">' + changeInttoABCD(j) + '</button>';
              					} else {
              						modal_content_string += '<button class="btn btn-default">' + changeInttoABCD(j) + '</button>';
              					}
              				}
              				else { // 如果做错了
              					if(j == myAnswer) {
              						modal_content_string += '<button class="btn btn-danger">' + changeInttoABCD(j) + '</button>';
              					}else if (j == rightAnswer) {
              						modal_content_string += '<button class="btn btn-success">' + changeInttoABCD(j) + '</button>';
              					} else {
              						modal_content_string += '<button class="btn btn-default">' + changeInttoABCD(j) + '</button>';
              					}
              				}
              			}
              			modal_content_string += '</div></div>'; // 选择题的左边渲染完毕

              			if (isRight) { // 渲染右边做题情况
              				modal_situ_string += '<a href="#t' + t + '" class="list-group-item list-group-item-success">' + t + '</a>';
              			} else {
              				modal_situ_string += '<a href="#t' + t + '" class="list-group-item list-group-item-danger">' + t + '</a>';
              			}
					}

					else if (d[t]['type'] == '1') { //问答题
						modal_content_string += '<div id="t' + t + '" class="panel panel-default">' +
              										'<div class="panel-heading">第' +  t + '题-问答题<a style="float: right" href="#modal_content">返回顶部</a></div>' +
              										'<div class="panel-body">' +
                										'<p>' + d[t]['title'] + '</p>' +
              											'<div>' +
                											'<div class="well">' +
                  											'<p>你的答案（' + d[t]['myScore'].toString() + '分）：</p>' +
                  											'<p>' + d[t]['myAnswer'] + '</p>' +
                											'</div>' +
                											'<div class="well">' +
                  											'<p>参考答案（' + d[t]['rightScore'].toString() + '分）：</p>' +
                  											'<p>' + d[t]['rightAnswer'] +'</p>' +
                											'</div>' +
              											'</div>' +
              										'</div>' +
            									'</div>';
					}

				} //end - for each title
				modal_content.html(modal_content_string);
				modal_situ.html(modal_situ_string);
			}
		});


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

function changeABCDtoInt(ABCD) {
	var i = 0;
	switch (ABCD) {
	case 'A':
		i = 0;
		break;
	case 'B':
		i = 1;
		break;
	case 'C':
		i = 2;
		break;
	case 'D':
		i = 3;
		break;
	}
	return i;
}

function changeInttoABCD(i) {
	var ABCD = 'A';
	switch (i) {
	case 0:
		ABCD = 'A';
		break;
	case 1:
		ABCD = 'B';
		break;
	case 2:
		ABCD = 'C';
		break;
	case 3:
		ABCD = 'D';
		break;
	}
	return ABCD;
}