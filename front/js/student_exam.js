$(document).ready(function() {
	set_info();

	var sid = $.cookie('id');
	var ajaxUrl = '../testphp/getHistoryExamList.php';
	//var ajaxUrl = '/student/getExams/' + sid + '/0';
	$.ajax({
		url: ajaxUrl,
		type: 'get',
		success: function(data) {
			var d = eval("(" + data + ")");
			var num = 0; // 获取数据数
			for (dd in d) {
				num += 1;
			}

			var table_content = "";
			$('#table_content').html(table_content);
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
				//var start_ts = Date.parse(new Date(d[s_i]['date'])) / 1000;
				var start_ts = d[s_i]['date'];
				var end_ts = start_ts + d[s_i]['timeSec'];

				// 如果没过试卷的结束时间,并且没有考试
				if (now_ts < end_ts && d[s_i]['status'] == 0) {
					cnt += 1;
					var btn_id = d[s_i]['pk'][0] + '_' + d[s_i]['pk'][1] + "_" + d[s_i]['name'] + "_" + end_ts.toString();
					// ?? 如果不到考试时间不能进入考试
					table_content += '<tr>' + 
                						'<th>' + cnt.toString() + '</th>' + 
                						'<th>' + d[s_i]['name'] + '</th>' +
                						'<th>' + start_time + '&emsp;到&emsp;' + end_time + '</th>' +
                						'<th><button id="' + btn_id + '" class="btn btn-primary btn-sm">进入考试</button></th>' + 
             						'</tr>';
				}

			} // end for
			$('#table_content').html(table_content);
		}
	}, 'json'); // 历史列表渲染完毕


	// 按钮事件
	$('#table_content').on('click', 'button', function(e) {
		var btn_id = e.target.id.split('_');
		var sid = btn_id[0];
		var pid = btn_id[1];
		var pname = btn_id[2];
		var end_ts = btn_id[3];

		var env = [];
		env[pid + '_pname'] = pname;
		env[pid + '_end_ts'] = end_ts;

		if ($.cookie(pid + '_pname') == null) {
			for (i in env) {
				$.cookie(i, env[i], {expries: 1, raw: true});
			}
		}
		window.location.href = "exam.html?pid=" + pid; 
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
	var now_ts = start_time;
	var end_ts = now_ts / 1000 + timeSec;
	
	var newDate = new Date();
	newDate.setTime(end_ts * 1000);
	return newDate.toLocaleString();
}
