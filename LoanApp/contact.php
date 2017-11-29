<?php
$email=$_GET["email"];
$amount=$_GET["amount"];
$newURL="https://www.paypal.com/cgi-bin/webscr?business=".$email."&cmd=_xclick&currency_code=USD&amount=".$amount."&item_name=Transfer+From+Family+and+Friends&return=http://webninjaz.info/saleh/done.php&cancel_return=http://webninjaz.info/saleh/cancel.php";
header('Location: '.$newURL);
die();
?>