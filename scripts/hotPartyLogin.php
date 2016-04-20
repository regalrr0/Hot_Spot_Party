<?php 


  require_once('response.php');
  
  $con = new mysqli("crisler","user","csci","android");

  $m = "";
  
  if(isset($_POST['username']) && isset($_POST['pass'])) {
    
    $userName = getPost($con,$_POST['username']);
    $pass  = getPost($con,$_POST['pass']);

    $query = "select userName,passHash from users where userName = '$userName'";
    $r = myQ($con,$query);
    
    $rObject = $r->fetch_object();

    if(($rObject->userName == $userName) && ($pass == $rObject->passHash)) {
      $m .= "true";
      $m .= "Login successful";	
    }
    else {
      $m .= "Your username or password is wrong";
    } 
    sendResponse($m);
  }



?> 
