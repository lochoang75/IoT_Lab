<?php

//header('Content-Type: aplication/json');

$host = "localhost";
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

	$sql = "SELECT * FROM Data";

	$result = mysqli_query($con,$sql);

	while ($row = mysqli_fetch_array($result))
	{
		$GatewayID = (string) $row["GatewayID"];
		$SensorID = (string)$row["ID"];
		$point = array("value" => $row["Value"], "time" => $row["Time"]);

		/* Create Gatewaylist*/
		if (!isset($data_point["Gateway"]))
		{
			$data_point["Gateway"] = array();
		}

		/* Create sensor ID list*/
		if (!isset($data_point["Gateway"][$GatewayID]))
		{
			$data_point["Gateway"][$GatewayID] = array();
		}

		if (!isset($data_point["Gateway"][$GatewayID][$SensorID])) 
		{
			$data_point["Gateway"][$GatewayID][$SensorID] = array();
		}
			
		array_push($data_point["Gateway"][$GatewayID][$SensorID], $point);
	}

	echo json_encode($data_point, JSON_NUMERIC_CHECK);

}

mysqli_close($con);
?>

