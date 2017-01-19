<?php
	$d = array(
		'1' => array(
			'type' => '0',
			'title' => '数据库的老师叫什么名字？',
			'myAnswer' => 'A',
			'rightAnswer' => 'B',
			),
		'2' => array(
			'type' => '0',
			'title' => '你随便选的吧？',
			'myAnswer' => 'A',
			'rightAnswer' => 'A',
			),
		'3' => array(
			'type' => '1',
			'title' => '写写你的感受！',
			'myAnswer' => '很好的',
			'myScore' => 4,
			'rightAnswer' => '对，就是很好',
			'rightScore' => 5
			)
		);

	echo json_encode($d);
?>