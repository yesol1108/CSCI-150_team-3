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

$name=isset($_POST['name']) ? $_POST['name'] : '';
$email=isset($_POST['email']) ? $_POST['email'] : '';
$pswd=isset($_POST['pswd']) ? $_POST['pswd'] : '';

$query = "SELECT * FROM user WHERE email = '{$email}'";
$res = mysqli_query($con, $query); 

if ($name !="" and $email !="" and $pswd !="" ){
  if(mysqli_num_rows($res) > 0) {
    echo "Email Already Exist!";
  }else {
    $sql = "SELECT PASSWORD('{$pswd}') as userpw";
    $res = mysqli_query($con, $sql);
    $row = mysqli_fetch_array($res); //{$row["userpw"]}

	//Encryption begins....
	include("rsa_2.0.php");
	list($key_n,$key_d,$key_e)=get_rsa_keys();//generate the keys
	$name=rsa_encrypt($name,$key_d,$key_n);
	$email=rsa_encrypt($email,$key_d,$key_n);
	$pswd=rsa_encrypt($pswd,$key_d,$key_n);

    $sql = "
INSERT INTO user(
    name
    ,email
    ,pswd
    ,insertedAt
) VALUES(
    '{$name}'
    , '{$email}'
    , '{$pswd}'
    , NOW()
  )
  "
  ;

    $res = mysqli_query($con, $sql);

    if($res){
      echo "SUCCESS";
    }else {
      echo "ERROR - SQL : ";
      echo mysqli_error($con);
    }
  }
}else {
    echo "Please input the data ";
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
         Name: <input type = "text" name = "name" />
         Email: <input type = "text" name = "email" />
         Password: <input type = "text" name = "pswd" />
         <input type = "submit" />
      </form>

   </body>
</html>
<?php
}
?>

