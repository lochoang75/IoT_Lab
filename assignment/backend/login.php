<?php
	// Get post data
	$username = $_POST['Username'];
	$password = $_POST['Password'];
	$servername = "127.0.0.1";
	$dbname = "test";
	$pass = "";
	$admin = "root";

	// Validate data from client



	// Connect to database
	$conn = mysqli_connect ($servername, $admin, $pass, $dbname);
	if (!$conn) {
			die("Connection failed". mysqli_connect_error());
	}

	// Get data from data base
	$sql = "SELECT * FROM User";

	$result = mysqli_qerry($con,$sql);

	while ($row = mysqli_fetch_array($result))
	{
			if ($row["Username"] == $username && $row["Password"] == $password)
			{
				// Move to main page
			}
	}

	// Access deny

?>
