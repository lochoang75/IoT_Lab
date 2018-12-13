<?php

// Validate data from client


// Get Username Password
$Username = $_POST["Username"];
$Password = $_POST["Password"];
$dbname = "test";
$admin = "root";
$pass = "";
$host = "127.0.0.1";


// Connect to database
$conn = $mysqli_connect($host,$admin,$pass,$dbname);

// Check connect
if (mysqli_connect_errno($conn))
{
	echo "Failed to connect to Database: ". $mysqli_connect_error();
} else {

	$sql = "SELECT * FROM User";

	$result = mysqli_query($conn,$sql);

	while ($row = mysqli_fetch_array($result))
	{
		if($row["Username"] == $Username && $row["Password"] == $Password)
		{
			// Exist in database
		}
	}
	// add to database
}
