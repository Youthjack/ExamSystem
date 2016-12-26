<?php
	$d = array(
		'state' => '0',
		'id' => '201430561080',
		'name' => '朱孝天',
		'idty' => '学生',
		'className' => '14网络工程');
	
	$d['username'] = $_POST['username'];
	$d['password'] = $_POST['password'];
	$d['identity'] = $_POST['identity'];

	echo json_encode($d);
?>