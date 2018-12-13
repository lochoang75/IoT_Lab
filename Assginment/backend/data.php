<?php
$servername = "localhost";
$usernamedb = "root";
$password = "";
$dbname = "test";
$conn = mysqli_connect($servername, $usernamedb, $password, $dbname);
$content = file_get_contents("php://input");

$js = json_decode($content,true);
var_dump($js);
$str = "INSERT INTO `Data` (`GatewayID`, `ID`, `Value`, `Time`) VALUES ";
foreach($js as $key=>$value){
	foreach ($value as $key1 => $value1) {
		$gate = $value['gateway'];
		$ti = $value['time'];
		if (is_array($value1)) {
			foreach ($value1 as $key1 => $value2){
				$idd = $value2['id'];
				$val = $value2['value'];
				$str .= "($gate, $idd, $val, $ti),";
			}
		}
	}	
}
$str = substr($str, 0, -1);
$str .= ";";
echo $str . "<br>";

mysqli_query($conn,$str);
mysqli_close($conn);
?>
