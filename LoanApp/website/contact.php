<?php
$email=$_GET["email"];
$amount=$_GET["amount"];
$newURL="https://www.paypal.me/cgi-bin/".$email."/".$amount;
header('Location: '.$newURL);
die();
?>
