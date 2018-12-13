<?php

header('Content-Type: aplication/json');

/* Record for humidnity and time*/
class Record {
	/* Attribute */
	var $time;
	var $humidnity;

	/* Constructor */
	function __construct($time, $value)
	{
		$this->time = $time;
		$this->humidnity = $value;
	}

	/* Method */
	function setData($time, $value)
	{
		$this->time = $time;
		$this->humidnity = $value;
	}

	function getTime()
	{
		return $this->time;
	}

	function getHumid()
	{
		return $this->humidnity;
	}

}

/* Sensor class for each sensor in gateway */
class Sensor {
	/* Sensor attribute */
	/* NodeID: ID of Sensor node */
	/* NodeData: All record about sensor */
	var	$NodeID;
	var $NodeData;

	/* Constructor */
	function __construct($ID, $record)
	{
		$this->NodeID = $ID;
		$this->NodeData = array();
		$this->pushRecord($record);
	}

	/* Sensor method */
	/* Push record to data */
	function pushRecord ($record)
	{
		array_push($this->NodeData,$record);
	}

	/* Set ID for Node */
	function setIdNode($id)
	{
		$this->NodeID = $id;
	}

	/* get ID from Node */
	function getIdNode()
	{
		return $this->NodeID;
	}
}

/* Gateway class */
class Gateway {
	/* Atribute */
	/* GatewayID: ID of gateway */
	/* GatewayData: Data of all sensor node in gateway */
	var $GatewayID;
	var $GatewayData;

	/* Constructor */
	function __construct($ID, $sensor)
	{
		$this->GatewayID = $ID;
		$this->GatewayData = array();
		$this->pushSensor($sensor);
	}

	/* Method */
	/* Push sensor data to Gateway */
	function pushSensor($sensor)
	{
		array_push($this->GatewayData, $sensor);
	}

	/* Get Gateway ID */
   function getGatewayID()	
   {
		return $this->GatewayID;
   }

	/* Set Gateway ID */
	function setGatewayID($gatewayID)
	{
		$this->GatewayID = $gatewayID;
	}

}

class json {
	var $Gateways;

	function __construct($gatewaylist)
	{
		$this->Gateways = $gatewaylist;
	}
}

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
	$gatewaylist = [];

	$sql = "SELECT * FROM Data";

	$result = mysqli_query($con,$sql);

	while ($row = mysqli_fetch_array($result))
	{
		$GatewayID = $row["GatewayID"];
		$SensorID = $row["ID"];
		$Time = $row["Time"];
		$Humid = $row["Value"];

		$Record = new Record($Time, $Humid);

		/* Create Gatewaylist*/
		if (empty($gatewaylist))
		{
			$Sensor = new Sensor($SensorID, $Record);
			$Gateway = new Gateway($GatewayID, $Sensor);
			array_push($gatewaylist, $Gateway);
			continue;
		}

		$addSuccess = false;

		foreach ($gatewaylist as $gateway)
		{
			if ($gateway->getGatewayID() == $GatewayID)
			{
				foreach ($gateway->GatewayData as $sensor)
				{
					if ($sensor->getIdNode() == $SensorID)
					{
						$sensor->pushRecord($Record);
						$addSuccess = true;
					}
				}
				
				if ($addSuccess == false)
				{
					$sensor = new Sensor($SensorID, $Record);
					$gateway->pushSensor($sensor);
					$addSuccess = true;
				}
			}

		}
		if ($addSuccess == false)
		{
			$Sensor = new Sensor($SensorID, $Record);
			$Gateway = new Gateway($GatewayID, $Sensor);
			array_push($gatewaylist, $Gateway);
		}

	}
	 $json = new json($gatewaylist);

	echo json_encode($json, JSON_NUMERIC_CHECK);

}

mysqli_close($con);
?>

