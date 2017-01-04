$(document).ready(function() {
	set_info();
	//test();
	var sid = $.cookie('id');
	//var ajaxUrl = '../testphp/getHistoryExamList.php';
	var ajaxUrl = '/student/getExams/' + sid + '/0';
	$.ajax({
		url: ajaxUrl,
		type: 'get',
		success: function(data) {
			var d = eval("(" + data + ")");
			console.log(d);
			var num = 0; // 获取数据数
			for (dd in d) {
				num += 1;
			}

			var table_content = "";
			$('#history_exam').html(table_content);
			for (var i=0; i<num; i++) {
				s_i = i.toString();
				var cnt = 0;
				// 三个时间的字符串表示
				var now_time = new Date();
				now_time = now_time.toLocaleString();
				var start_time = getEndTime(d[s_i]['date'], 0);
				var end_time = getEndTime(d[s_i]['date'], d[s_i]['timeSec']);

				// 三个时间的时间戳表示
				var now_ts = Date.parse(new Date()) / 1000;
				//var start_ts = Date.parse(new Date(d[s_i]['date'] / 1000;
				var start_ts = d[s_i]['date'] / 1000;
				var end_ts = start_ts + d[s_i]['timeSec'];

				// 如果过了试卷的结束时间
				if (true) {
					cnt += 1;
					if (d[s_i]['status'] == 0) { //如果该名考生没考试
						var btn_id = d[s_i]['pk']['studentId'] + '_' + d[s_i]['pk']['paperId'];
						table_content += '<tr>' +
                							'<th>' + cnt.toString() + '</th>' + 
                							'<th>' + d[s_i]['name'] + '</th>' +
                							'<th>' + start_time + '&emsp;到&emsp;' + end_time + '</th>' +
                							'<th>0</th>' + 
                							'<th><button id="' + btn_id + '" class="btn btn-danger btn-sm" disabled="true">未曾考试</button></th>' + 
             							'</tr>';
						console.log(table_content);
					}
					if (d[s_i]['status'] == 1) { //如果该名考生考了试
						var btn_id = d[s_i]['pk']['studentId'] + '_' + d[s_i]['pk']['paperId'];
						table_content += '<tr>' + 
                							'<th>' + cnt.toString() + '</th>' + 
                							'<th>' + d[s_i]['name'] + '</th>' +
                							'<th>' + start_time + '&emsp;到&emsp;' + end_time + '</th>' +
                							'<th>' + d[s_i]['mark'] + '</th>' + 
                							'<th><button id="' + btn_id + '" class="btn btn-default btn-sm" data-toggle="modal" data-target="#myModal">查看试卷</button></th>' + 
             							'</tr>';
					}
				}

			} // end for
			$('#history_exam').html(table_content);
		}
	}, 'json'); // 历史列表渲染完毕

	$('#history_exam').on('click', 'button', function(e) {
		var btn_id = e.target.id.split('_');
		var id = btn_id[0];
		var exam_id = btn_id[1];

		// var ajaxUrl = '../testphp/getHistoryExam.php';
		var ajaxUrl = '/student/getHistoryExam/'+id+'/'+exam_id;
		$.ajax({
			url: ajaxUrl,
			type: 'get',
			success: function(data) {
				// console.log(data);
				// data = JSON.stringify(data);
				var d = eval("(" + data + ")");
				console.log(d);
				var modal_content = $('#modal_content');
				var modal_situ = $('#modal_situ');

				modal_content.html("");
				modal_situ.html("");

				var modal_content_string = "";
				var modal_situ_string = "";

				var num = 0;
				for (dd in d) {
					num += 1;
				}


				for(var i=1; i < num+1; i++) {
					var t = i;
					console.log(d[t-1]['type']);
					if(d[t-1]['type'] == 1) { //选择题
						var myAnswer = changeABCDtoInt(d[t-1]['myAnswer']);
						var rightAnswer = changeABCDtoInt(d[t-1]['rightAnswer']);
						var question = d[t-1]['question'].replace(/-/gm, '<br/>');

						var isRight = myAnswer == rightAnswer ? true : false; //是否答对
						var panelStyle = isRight ? 'panel panel-success' : 'panel panel-danger';

						modal_content_string += '<div id="t' + t + '" class="panel panel-' + panelStyle + '">' +
              									'<div class="panel-heading">第' + t + '题-选择题（2分）<a style="float: right" href="#modal_content">返回顶部</a></div>' +
              									'<div class="panel-body">' +
                								'<p>' + question + '</p>' +
              									'</div>' +
              									'<div class="btn-group">';
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

					else if (d[t-1]['type'] == 2) { //问答题
						modal_content_string += '<div id="t' + t + '" class="panel panel-default">' +
              										'<div class="panel-heading">第' +  t + '题-问答题<a style="float: right" href="#modal_content">返回顶部</a></div>' +
              										'<div class="panel-body">' +
                										'<p>' + d[t-1]['question'] + '</p>' +
              											'<div>' +
                											'<div class="well">' +
                  											// '<p>你的答案（' + d[t]['myScore'].toString() + '分）：</p>' +
                  											'<p>' + d[t-1]['myAnswer'] + '</p>' +
                											'</div>' +
                											'<div class="well">' +
                  											// '<p>参考答案（' + d[t]['rightScore'].toString() + '分）：</p>' +
                  											'<p>' + d[t-1]['rightAnswer'] +'</p>' +
                											'</div>' +
              											'</div>' +
              										'</div>' +
            									'</div>';
					}

				} //end - for each question
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
	var id = $.cookie('studyid');
	var idty = $.cookie('idty');

	$('#stu_id').text(id);
	$('#stu_name').text(name);
	$('#stu_name2').text(name);
	$('#stu_class').text(className);
	$("#identity").text(idty);
}

function getEndTime(start_time, timeSec) {
	//var now_ts = Date.parse(new Date(start_time));
	var now_ts = start_time;
	var end_ts = now_ts / 1000 + timeSec;
	
	var newDate = new Date();
	newDate.setTime(end_ts * 1000);
	return newDate.toLocaleString();
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

function test() {
	var end_time = getEndTime(1482847704000, 1800);
	console.log(end_time);
}