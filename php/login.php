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
  $con = mysqli_connect("csci150.c9g7nukf2ffx.us-west-1.rds.amazonaws.com","root","1234qwer","CSCI150");
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

$query = "SELECT * FROM user_db
         WHERE email = '{$email}' AND pswd = '{$pswd}'";
$res = mysqli_query($con, $query);
$res_num = mysqli_num_rows($res);

if($email != "" and $pswd != "") {
  if($res_num> 0){
    while($row = mysqli_fetch_assoc($res)) {
      echo "SUCCESS";
    }
  }
  else{
    echo "ERROR - SQL : ";
    echo mysqli_error($con);
  }
} else {
  echo "please input data";
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