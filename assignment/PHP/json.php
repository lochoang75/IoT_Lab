<?php

header('Content-Type: aplication/json');

$host = "127.0.0.1";
$dbnam = "test";
$password = "";
$username = "root";
$con = mysqli_connect($host,$username,$password,$dbnam);

// check connection
if (mysqli_connect_errno($con))
{
	echo "Failed to connect to Database: ".$mysqli_connect_error();
} else
{
	$data_point = array();

	$sql = "SELECT * FROM HeatHeat";

	$result = mysqli_query($con,$sql);

	while ($row = mysqli_fetch_array($result))
	{
		$point = array("label" => $row["ID"], "y" => $row["Value"], "Time" => $row["Time"]);

		array_push($data_point, $point);
	}

	echo json_encode($data_point, JSON_NUMERIC_CHECK);

}

mysqli_close($con);
?>

