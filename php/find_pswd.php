<?php

$fs = fopen(dirname(__FILE__)."/manager.txt", "r");
$mode = trim(fread($fs, "1024"));
if($mode == "") $mode = "dev";
fclose($fs);

//echo $mode;
error_reporting(E_ALL);
ini_set('display_errors',1);

if($mode == "dev") {
  $con = mysqli_connect("localhost:3306","root","1234qwer","csci150");
}else {
  $con = mysqli_connect("csci150se.c9g7nukf2ffx.us-west-1.rds.amazonaws.com","intrSE_3","tkfkdgo11","SPLITTR");
}

if (!$con)
{
   echo "MySQL Connection Error : ";
   echo mysqli_connect_error();
   exit();
}

mysqli_set_charset($con,"utf8");

$email=isset($_POST['email']) ? $_POST['email'] : '';

$query = "SELECT * FROM user WHERE email = '{$email}'";
$res = mysqli_query($con, $query);

if($email != "") {
  if(mysqli_num_rows($res) == 0) {
    echo "Cannot find user!";
  }else {
	$length = 8;
	$chars =
		'0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
	$chars_length = strlen($chars);
	$random_string = "";
	for($i = 0; $i < $length; $i++){
		$random_string .= $chars[rand(0, $chars_length - 1)];
	}

	$sql = "UPDATE user SET pswd = '{$random_string}' WHERE email = '{$email}'";
	$query=mysqli_query($con, $sql);

	$sql = "SELECT `name` FROM user WHERE email = '{$email}'";
	$query=mysqli_query($con, $sql);
	$res = mysqli_fetch_assoc($query);

	// Always set content-type when sending HTML email
	$headers = "MIME-Version: 1.0" . "\r\n";
	$headers .= "Content-type:text/html;charset=UTF-8" . "\r\n";
	$headers .= 'From: <webmaster@splittr.com>' . "\r\n";

	$message = "
	<html>
		<body>
			<h2>Dear ".$res['name']."</h2>
			<p>You recently requested a temporary password
				for your Splittr ID.
				Please use this temporary password for your login,
				and <b>do not forget</b> to change the password.
			</p>
			<p> Your temporary password is below.</p>
			<p><i> ".$random_string."<i></p>
			</table>
		</body>
	</html>
	";

	mail($email, "Your temporary password", $message, $headers);
  }
}else {
  echo "Please input data";
}

mysqli_close($con);
?>

<?php

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if (!$android){
?>

<html>
   <body>

      <form action="<?php $_PHP_SELF ?>" method="POST">
         Email: <input type = "text" name = "email" />
         <input type = "submit" />
      </form>

   </body>
</html>
<?php
}
?>