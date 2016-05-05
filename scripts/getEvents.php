<?php 
  
  require_once('response.php');
  
  require_once('dbLogin.php');

  $con = new mysqli($host,$u,$p,$db);


  //if(isset($_POST['club'])) {

    $club = 1;//$_POST['club'];
    
    $query = "select name,smallImgPath,eventId from events

    where eventTypeId='$club'";

    $r = myQ($con, $query);    // myQ in response.php

    sendResponse($r);
    
  //}
  if(isset($_POST['festival'])) {

    $festival = 2;//$_POST['club'];
    
    $query = "select name,smallImgPath,eventId from events

    where eventTypeId='$festival'";

    $r = myQ($con, $query);         // myQ in response.php

    sendResponse($r);
    
  }
  elseif(isset($_POST['sport'])) {

    $sport = 1;//$_POST['club'];
    
    $query = "select name,smallImgPath,eventId from events

    where eventTypeId='$sport'";

    $r = myQ($con, $query);          // myQ in response.php

    sendResponse($r);
    
  }
?>