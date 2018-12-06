<?php
	$servername = "localhost";
	$username = "root";
	$dbname = "test";
	$password = "";
	$conn = mysqli_connect($servername, $username, $password, $dbname);

	if (!$conn) {
			die( "Connection failed" . mysqli_connect_error());
	}

	list($GatewayID, $ID, $Value, $Time)=
		split("&",file_get_contents('php://input'),4);

	$sql = "INSERT INTO Data (GatewayID,ID,Value,Time) VALUES ($GatewayID, $ID, $Value, $Time)";

	$result = mysqli_query ($conn, $sql);

	if ($result) {
		echo done;
	}
	else {
		die ("Error: ". mysqli_error($conn));	
	}
   	mysqli_close($conn);
?>
