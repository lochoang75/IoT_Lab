<?php
	$servername = "127.0.0.1";
	$username = "root";
	$dbname = "test";
	$password = "";
	$conn = mysqli_connect($servername, $username, $password, $dbname);
	if (!$conn) {
			die( "Connection failed" . mysqli_connect_error());
	}

	$ID = $_POST["ID"];
	$Value = $_POST["Value"];
	$Time = $_POST["Time"];	

	$sql = "INSERT INTO HeatHeat (ID,Value,Time) VALUES ($ID,$Value,$Time)";

	$result = mysqli_query ($conn, $sql);

	if ($result) {
		echo $_POST["pass"];
	}
	else {
		die ("Error: ". mysqli_error($conn));	
	}
   	mysqli_close($conn);
?>
