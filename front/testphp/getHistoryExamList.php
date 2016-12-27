<?php
	$d = array (
		'0' => array (
			'pk' => array ('1', '3'),
			'timeSec' => 1800,
			'date' => '2017-1-30 14:00',
			'status' => 0,
			'mark' => 0,
			'name' => '第三章测试'
			),
		'1' => array (
			'pk' => array ('1', '2'),
			'timeSec' => 1800,
			'date' => '2016-12-21 14:00',
			'status' => 0,
			'mark' => 0,
			'name' => '第二章测试'
			),
		'2' => array (
			'pk' => array ('1', '1'),
			'timeSec' => 1800,
			'date' => '2016-12-12 14:00',
			'status' => 1,
			'mark' => 60,
			'name' => '第一章测试'
			),
		);

	echo json_encode($d);
?>