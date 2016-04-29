<?php 
  
  require_once('response.php');
  

  $con = new mysqli("crisler","user","csci","android");


  if(isset($_POST['club'])) {

    $club = 1;//$_POST['club'];
    
    $query = "select name,imgPath from events

    where eventTypeId='$club'";

    $r = myQ($con, $query);    // myQ in response.php

    //echo $r->num_rows . " /_/ ";

    sendResponse($r);
    
  }
  elseif(isset($_POST['festival'])) {

    $festival = 2;//$_POST['club'];
    
    $query = "select name,imgPath from events

    where eventTypeId='$festival'";

    $r = myQ($con, $query);         // myQ in response.php

    //echo $r->num_rows . " /_/ ";

    sendResponse($r);
    
  }
  elseif(isset($_POST['sport'])) {

    $sport = 1;//$_POST['club'];
    
    $query = "select name,imgPath from events

    where eventTypeId='$sports'";

    $r = myQ($con, $query);          // myQ in response.php

    //echo $r->num_rows . " /_/ ";

    sendResponse($r);
    
  }
?>