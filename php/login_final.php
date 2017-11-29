<?php
include("rsa_2.0.php");

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
$pswd=isset($_POST['pswd']) ? $_POST['pswd'] : '';

//
$result = mysqli_query($con,"SELECT key_d,key_e,key_n FROM keytable ORDER BY id DESC LIMIT 1");
if (!$result) {
    echo 'Could not run query: ' . mysqli_error($con);
    exit;
}

$row = mysqli_fetch_row($result);
$key_d= gmp_init($row[0]);
$key_e= gmp_init($row[1]);
$key_n= gmp_init($row[2]);

$result = mysqli_query($con,"SELECT key_d,key_n FROM pswd ORDER BY id DESC LIMIT 1");
if (!$result) {
    echo 'Could not run query: ' . mysqli_error($con);
    exit;
}

$row = mysqli_fetch_row($result);
$key_pd= gmp_init($row[0]);
$key_pn= gmp_init($row[1]);


$email=rsa_encrypt($email,$key_d,$key_n);
$pswd=rsa_encrypt($pswd,$key_pd,$key_pn);

//				



$query = "SELECT * FROM user
         WHERE email = '{$email}' AND pswd = '{$pswd}'";
$res = mysqli_query($con, $query);

if($email != "" and $pswd != "") {
  if(mysqli_num_rows($res) == 0) {
    echo "Wrong email or password!";
  }else {
    while($row = mysqli_fetch_assoc($res)) echo "SUCCESS";
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
         Password: <input type = "text" name = "pswd" />
         <input type = "submit" />
      </form>

   </body>
</html>
<?php
}
?>