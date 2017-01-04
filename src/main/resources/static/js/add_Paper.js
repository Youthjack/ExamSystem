/**
 * Created by Jack on 2016/12/28.
 */
$(document).ready(function () {
    init_multiple_choice_ul();
//    $('#myModal').modal({
//        backdrop:'static',
//        keyboard:false
//    })

//    $('#publish_papers_smt').click(function () {
//        var paper_chapter = $('#paper-chapter').val();
//        var paper_name = $('#paper-name').val();
//        var paper_duration = $('#paper-duration').val();
//        var result = "";
//        var questions = $('#multiple-choice').find('input[type=checkbox]:checked');
//        for (var i = 0; i < questions.length; i++) {
//            result += ($(questions[i]).parent().next().text() + '\n');
//        }
//        alert('章节：' + paper_chapter +
//            '\n试卷名：' + paper_name +
//            '\n考试时间：' + paper_duration +
//            '\n题目:\n' + result)
//    });

//    测试将所有选中题目id组成字符串，输出试卷名和题号字符串
    /*    $('#publish_papers_smt').click(function(){
     var paper_chapter = $('#paper-chapter').find(':selected').val();
     var paper_name = $('#paper-name').val();
     var questions = "";
     var question_checkboxes = $('input[type=checkbox].question_checkbox:checked');
     for(var i = 0; i < question_checkboxes.length; i++) {
     if(i==0) {
     questions += $(question_checkboxes[i]).attr('id');
     }
     else {
     questions += ('-'+$(question_checkboxes[i]).attr('id'));
     }
     }
     //        alert('章节：'+paper_chapter+'\n试卷名：'+paper_name+'\n题号字符串：'+questions);
     });*/

    $('#publish_papers_smt').click(function () {
        var paper_name = $('#paper-name').val();
        var questions = "";
        var question_checkboxes = $('input[type=checkbox].question_checkbox:checked');
        for (var i = 0; i < question_checkboxes.length; i++) {
            if (i == 0) {
                questions += $(question_checkboxes[i]).attr('id');
            }
            else {
                questions += ('-' + $(question_checkboxes[i]).attr('id'));
            }
        }
        $.ajax({
            contentType: 'application/json',
            type: 'post',
            url: '/teacher/addPaper',
            data: JSON.stringify({'paperName': paper_name, 'questions': questions}),
            success:function(data,status){
                alert(data);
                // console.log(data);
            },
            error:function(xmlhttprequest,textstatus,errorthrown){
                console.log(errorthrown);
            }
        });
    });

    $('#paper-chapter').change(function () {
        $('#tbody').html('');
        init_multiple_choice_ul();
    });
});

function showInfo() {
    var selectedQuestions = $('#multiple-choice').find('input[type=checkbox]:checked');
    alert(selectedQuestions.length);
    for (var i = 0; i < selectedQuestions.length; i++) {
        alert($(selectedQuestions[i]).parent().next().text());
    }
}

function init_multiple_choice_ul() {
    $.ajaxSetup({
        contentType: 'application/json'
    });
    $.ajax({
        type: 'GET',
        url: '/teacher/seeQuestion',
        success: function (data, status) {
            data = eval("("+data+")");
            var chapter = $('#paper-chapter').val();
            var targetQuestionJSONarr = new Array();
            for (var i = 0; i < data.length; i++) {
                var curQuestion = data[i];
                if (curQuestion['chapter'] == chapter) {
                    targetQuestionJSONarr.push(JSON.stringify(curQuestion));
                }
            }
            var multiple_choice_ul_tbody = $('#multiple-choice').find('tbody');
            for (var j = 0; j < targetQuestionJSONarr.length; j++) {
                var curQuestion = eval('(' + targetQuestionJSONarr[j] + ')');
                var question_description = curQuestion['question'];
                var question_id = curQuestion['id'];
                multiple_choice_ul_tbody.append("<tr><td><input type='checkbox' class='question_checkbox' id="+question_id+"></td><td>" + question_description + "</td></tr>");
            }
        },
        error: function (xmlhttprequest, textstatus, errorthrown) {
            console.log(errorthrown);
        }
    })
}



//    $('#paper-duration').change(function () {
//        alert($('#paper-duration').find('option:selected').val());
//    });