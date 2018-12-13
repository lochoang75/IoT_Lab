<?php
$cookie_name = "user";
/* Response class */
class Response
{
	var $success;
	var $optionalData;

	function __construct ($success, $data)
	{
		$this->success = $success;
		$this->optionalData = $data;
	}
}

/* Database info*/
$host = "localhost";
$dbname = "test";
$dbPassword = "";
$dbUsername = "root";

/* Connect to databbase */
$conn = mysqli_connect($host, $dbUsername, $dbPassword, $dbname);

/* Check db connection */
if (!$conn)
{
	die("Connection failed: ".mysqli_connect_error());
}

/* check if user in database */
function isNormalUser($username, $password)
{
	global $conn;
	$sql = "SELECT username, password FROM User";
	$result = mysqli_query($conn, $sql);

	if ($result->num_rows > 0)
	{
		while($row = $result->fetch_assoc())
		{
			/* Compare with user database */
			if ($row["username"] === $username)
			{
				if ($row["password"] === $password)
				{
					return 1;
				}else {
					return 2;
				}
			}
		}
	}
	return 0;
}

/* Server validation 
 * username not conain special character 
 * email in email form */
function accountValidation($username)
{
	if(preg_match('/[\'^£$%&*()}{@#~?><>,|=_+¬-]/', $username))
	{
		return false;
	}

	return true;
}

function emailValidation($email)
{
	return filter_var($email, FILTER_VALIDATE_EMAIL);
}

/* Response for normal user */
function clientRes( $result, $optionalParams )
{
	/* response for client */
	$res = new Response($result, $optionalParams); 
	echo json_encode($res);
}

function loginHandler()
{
	/* User infomation */
	$username = trim($_POST['username']);
	$password = trim($_POST['password']);

	switch (isNormalUser($username, $password))
	{
	case 1:
		clientRes("success", "null");
		break;
	case 2:
		clientRes("incorrect", "null");
		break;
	default:
		clientRes("invalid", "null");
		break;
	}
}

/* Autologin to check available of user account */
function autoLoginHandler()
{
	$username = $_COOKIE[$cookie_name];
	switch (isNormalUser($username, "null"))
	{
	case 1:
		/* correct username and password . Some how :D */
		clientRes("success", "null");
		break;
	case 2:
		/* correct username, that is what we need for autologin */
		clientRes("success", "null");
		break;
	default:
		/* incorrect username, acount maybe removed */
		invalidRes("invalid", "null");
		break;
	}
}

/* Register handle 
 * Error code:
 0 : incorrect input;
 1 : username already exist
 2 : email already exist
 3 : success
 */

function isExist() 
{
	$username;
	$password;
	$email;
	/* Server username validation*/
	if (accountValidation($_POST['username']))
	{
		$username = $_POST['username'];
	} else {
		return 0;
	}

	/* Server password validation */
	if (accountValidation($_POST['password']))
	{
		$password = $_POST['password'];
	} else {
		return 0;
	}

	/* Server email validation */
	if (emailValidation($_POST['email']))
	{
		$email = $_POST['email'];
	} else {
		return 0;
	}

	/* Check if account already exist */
	global $conn;

	$sql = "SELECT Username, Email FROM User";

	$result = mysqli_query($conn, $sql);
	
	if ($result->num_rows >0)
	{
		while ($row = $result->fetch_assoc())
		{
			/* username already in database */
			if ($username === $row['Username'])
			{
				return 1; 
			}
			
			/* email already exist in database */
			if ($email === $row['Email']) 
			{
				return 2;
			}
		}
	}
	/* not found any exist -> insert to database*/
	$sql = "INSERT INTO User (Username, Password, Email) VALUES('".$username. "','". $password."','".$email."')";
	$result = mysqli_query($conn, $sql);
	if (!$result)
	{
		echo "ERROR:" .$sql. "<br>".mysqli_error($conn);
	}
	return 3;
}

function registerHandler()
{
	switch(isExist())
	{
	case 0:
		clientRes("inputErr", "null");
		break;
	case 1:
		clientRes("exist", "username");
		break;
	case 2:
		clientRes("exist", "email");
		break;
	case 3:
		clientRes("success", "null");
		break;
	default:
		break;
	}

}

$action;
if (isset ($_POST['action']) && !empty($_POST['action']))
{
	$action = $_POST['action'];
}

switch ($action)
{
case "login":
	loginHandler();
	break;
case "register":
	registerHandler();
	break;
case "auto":
	autoLoginHandler();
	break;
default:
	break;
}
mysqli_close($conn);
?>
