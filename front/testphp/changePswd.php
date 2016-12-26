<?php
	$d = array(
		'state' => '0');
	
	$d['newpassword'] = $_POST['newpswd'];
	$d['oldpassword'] = $_POST['oldpswd'];
	$d['id'] = $_POST['id'];

	echo json_encode($d);
?>