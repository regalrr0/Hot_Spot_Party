<?php

define('CLUB',1);
define('FESTIVAL',2);
define('SPORTS',3);

require_once('dbLogin.php');
require_once('response.php');

  error_reporting(E_ALL);
  ini_set('display_errors', 1);


$con = new mysqli($host,$u,$p,$db);


if(isset($_POST['eventId']))  {

    $eventId = getPost($con,$_POST['eventId']);

    getEventDetails($con,$eventId);
}

getEventDetails($con,7);




function getEventDetails($con, $eventId) {


	$q = "select largeImgPath, name, description,specialNotes, dateEvent, address from events

	where eventId = '$eventId'";

    $r = myQ($con,$q);

    sendResponse($r);
}  

?>

