<?php 
  
  require_once('response.php');
  

  $con = new mysqli("crisler","user","csci","android");


  //if(isset($_POST['club'])) {

    $club = 1;//$_POST['club'];
    
    $query = "select name from events

    where eventTypeId='$club'";

    $r = myQ($con, $query);

    sendResponse($r,true);
    
  //}
  
  /*$time = microtime(true);
  $iters = 500000;
  for($i =0; $i<$iters; $i++) { 

    sendResponse($r,true);
  }
  $end = microtime(true);
  echo "<br><br>time is " . ($end - $time) . " for " . $iters . " iterations"; */

  // get button pressed say club 
  // get club data from db
  // format via sendResponse i.e. comma separated row stuff (possibly 2 dimensional)
  // java get above string and puts it intent 
  // next activity parses nicely for listing 
?>