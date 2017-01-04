$(document).ready(function() {
	set_info(); // 设置考生信息和试卷名
	var sid = $.cookie('id');
	var pid = getUrlParam('pid');
	// 设置计时器
	var end_ts = $.cookie(pid + "_end_ts");
	setColck(end_ts);

	// var ajaxUrl = '../testphp/exam.php';
	var ajaxUrl = '/student/getPaper/'+sid+'/'+pid;
	// 上传pid，获取题目并且渲染页面
	$.ajax({
		url: ajaxUrl,
		type: 'get',
		success: function(data) {
			console.log(data);
			var d = eval("(" + data + ")");
			var num = 0; // 题目数量
			var total_socre = 0; // 本次考试的分值

			for(dd in d) {
				num += 1;
			}

			var question_content = "";
			var message_button_list = "";
			$('#question_content').html("");
			$('#message_button_list').html("");
			
			// 选择题和填空题分类，因为选择题要打乱
			var choice_questions = [];
			var fill_questions = [];
			for(var i=0; i<num; i++) {
				if(d[i]['type'] == '1') {
					choice_questions.push(i);
					total_socre += d[i]['point'];
				}
				if(d[i]['type'] == '2') {
					fill_questions.push(i);
					total_socre += parseInt(d[i]['point']);
				}
			}
			console.log(choice_questions);
			console.log(fill_questions);
			// 渲染题目总数和题目总分
			$('#all_question').html(num);
			$('#all_score').html(total_socre);
			// 打乱顺序
			choice_questions.sort(randomsort);
			fill_questions.sort(randomsort);

			var index = 1;
			for(; index<choice_questions.length+1; index++) {
				var t = choice_questions[index-1].toString();
				var question = d[t]['question'].replace(/-/gm, "<br/>");
				question_content += '<div id="' + d[t]['id'] + '" class="panel panel-default">' +
              							'<div class="panel-heading">第' + (index).toString() + '题-选择题（' + d[t]['point'] + '分）</div>' +
              							'<div class="panel-body">' + 
                							'<p>' + question + '</p>' + 
              								'<div class="btn-group">' + 
                								'<button id="' + d[t]['id'] + '_A' + '" class="btn btn-default">A</button>' + 
                								'<button id="' + d[t]['id'] + '_B' + '" class="btn btn-default">B</button>' + 
                								'<button id="' + d[t]['id'] + '_C' + '" class="btn btn-default">C</button>' + 
                								'<button id="' + d[t]['id'] + '_D' + '" class="btn btn-default">D</button>' + 
                							'</div>' + 
              							'</div>' + 
            						'</div>';

            	message_button_list += '<button id="' + d[t]['id'] + "_state" + '"class="btn btn-default">' + index + '</button>';
			}
			for(;index<num+1; index++) {
				var t = fill_questions[index-1-choice_questions.length].toString();
				var question = d[t]['question'].replace(/\\n/gm, "<br>");
				question_content += '<div id="' + d[t]['id'] + '" class="panel panel-default">' + 
              							'<div class="panel-heading">第' + (index).toString() + '题-填空题（' + d[t]['point'] + '分）</div>' + 
              							'<div class="panel-body">' + 
                							'<p>' + question + '</p>' + 
                							'<textarea onblur="inputText(this)" id="' + pid + '_' +  d[t]['id'] + '" style="width: 500px;" placeholder="输入你的答案"></textarea>' + 
              							'</div>' + 
            						'</div>';

            	message_button_list += '<button id="' + d[t]['id'] + "_state" + '"class="btn btn-default">' + index + '</button>';
			}

			$('#question_content').html(question_content);
			$('#message_button_list').html(message_button_list);

			recovery();
		} // end for
	}, 'json'); // 页面渲染完毕

	// 选择题的按钮监听
	$('#question_content').on('click', 'button', function(e) {
		var id = e.target.id.split("_");
		var qid = id[0];
		var choice = id[1];

		// 点击变色事件
		var btn_list = []
		var chs = ['A', 'B', 'C', 'D'];
		for (c in chs) {
			btn_list.push(qid + "_" + chs[c]);
		}
		for(bl in btn_list) {
			var tmp = '#' + btn_list[bl];
			if($(tmp).attr("class") != 'btn btn-default')
				$(tmp).attr("class", "btn btn-default");
		}
		$('#' + e.target.id).attr("class", "btn btn-primary");

		// 右侧变色事件
		var state_id = '#' + qid + '_state';
		$(state_id).attr("class", "btn btn-success");
		
		var cookie_name = 'question_' + pid + '_' + qid;
		$.cookie(cookie_name, choice, {expires: 1, raw: true});
	});

	// 状态栏点击跳转事件
	$('#message_button_list').on('click', 'button', function(e) {
		var qid = e.target.id.split('_')[0];
		window.location.href = "#" + qid;
	});

	// 提交按钮
	$('#exam_submit').click(function() {
		var pid = getUrlParam('pid');
		var sid = $.cookie('id');
		// var sid = $.cookie('sid');
		var idList= [];
		var answerList = [];

		var div = $('#question_content').children('div');

		$.each(div, function(i, sub_div) {
			var cookie_name = "question_" + pid + "_" + sub_div.id;
			idList.push(sub_div.id);
			
			var cookie_val = $.cookie(cookie_name);
			if (cookie_val != null && cookie_val.length != "") {
				answerList.push(cookie_val);
			} else {
				answerList.push(null);
			}
		});

		//console.log(idList);
		//console.log(answerList);

		// var ajaxUrl = '../testphp/submit.php';
		var ajaxUrl = '/student/finishExam';
		$.ajax({
			url: ajaxUrl,
			contentType:"application/json",
			type: 'post',
			data: JSON.stringify({paperId: pid,
					studentId: sid, 
					idList: idList,
					answerList: answerList}),
			success: function(data) {
				var d = eval("(" + data + ")");
				if(d['status'] == "success") {
					alert('提交成功');
					window.location.href = 'student_func.html';
				}
				else {
					alert(d['status']);
				}
			}
		});
	});// end submit
});


//
// 子函数
//
function recovery() {
	var pid = getUrlParam('pid');
	var div = $('#question_content').children('div');
	//console.log(div);
	$.each(div, function(i, sub_div) {
		var qid = sub_div.id;

		var cookie_name = 'question_' + pid + '_' + qid;
		var cookie_val = $.cookie(cookie_name);
		//console.log(cookie_val);
		if(cookie_val != null) {
			var node = $('#' + pid + '_' + qid);
			if(node.get(0) == undefined) { // 如果是空的节点，证明是选择题
				if (cookie_val != null) { // 答案不为空
					$('#' + qid + '_' + cookie_val).attr('class', 'btn btn-primary');
					$('#' + qid + '_state').attr('class', 'btn btn-success');
				}
			} else {
				if (cookie_val != null) {
					node.val(cookie_val);
					$('#' + qid + '_state').attr('class', 'btn btn-success');
				}
			} // end node 空
		}
	});
}

function inputText(e) {
	var pid = e.id.split('_')[0];
	var qid = e.id.split('_')[1];
	var text = $('#' + e.id).val();

	if (text != "")
		$('#' + qid + '_state').attr('class', 'btn btn-success');
	else
		$('#' + qid + '_state').attr('class', 'btn btn-default');

	var cookie_name = 'question_' + pid + '_' + qid;
	$.cookie(cookie_name, text, {expires: 1, raw: true});
}

function set_info() {
	// 从cookie取出信息渲染页面
	var name = $.cookie('name');
	var className = $.cookie('className');
	var id = $.cookie('id');
	var idty = $.cookie('idty');

	var pid = getUrlParam('pid');
	var pname = $.cookie(pid + '_pname');

	$('#message_name').text(name);
	$('#message_studyid').text(id);
	$('#message_class').text(className);
	$('#exam_name').text(pname);
}

function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}

function randomsort(a, b) {
	return Math.random() > .5 ? -1 : 1;
}

function setColck(end_ts) {
	var now_ts = Date.parse(new Date()) / 1000;
	var left_sec = end_ts - now_ts;
	//console.log(left_sec);
	timer(left_sec);
}

function timer(intDiff) {
	window.setInterval(function(){
		var day=0, hour=0, minute=0, second=0;
		if(intDiff > 0) {
			day = Math.floor(intDiff / (60 * 60 * 24));
       		hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
       		minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
       		second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
		} else {
			$('#exam_submit').click();
		}
		if (minute <= 9) minute = '0' + minute;
    	if (second <= 9) second = '0' + second;
    	
    	var left = hour + ":" + minute + ":" + second;
    	//console.log(left);
   		$('#leftTime').html(left);
   		intDiff--;
   		}, 1000);
}
