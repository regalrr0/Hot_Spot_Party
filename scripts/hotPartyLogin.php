<?php 


  require_once('response.php');
  require_once('dbLogin.php');
  

  $con = new mysqli($host,$u,$p,$db);
  $m = "";
  
  if(isset($_POST['username']) && isset($_POST['pass'])) {
    
    $userName = getPost($con,$_POST['username']);
    $pass  = getPost($con,$_POST['pass']);

    $query = "select userName,passHash from users where userName = '$userName'";
    $r = myQ($con,$query);
    
    $rObject = $r->fetch_object();
    
    //echo $rObject->userName;

    if(($rObject->userName == $userName) && ($pass == $rObject->passHash)) {
      $m .= "true // login successful";	

    }
    else {
      $m .= "Your username or password is wrong";
    } 
    echo $m;
    //sendResponse($m);
  }



?> 
