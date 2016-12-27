<?php
	$data = array (
		'0' => array (
			'id' => '101',
			'question' => '你今天几点起床啊？\nA.7 B.8\nC.9 D.10',
			'point' => 2,
			'type' => 0
			),
		'1' => array (
			'id' => '502',
			'question' => '别处bug？\nA.7 B.8\nC.9 D.10',
			'point' => 2,
			'type' => 0
			),
		'2' => array (
			'id' => '311',
			'question' => '说说你对这个的感受？',
			'point' => 5,
			'type' => 1
			),
		);

	echo json_encode($data);
?>