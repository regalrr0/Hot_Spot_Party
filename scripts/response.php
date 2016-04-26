<?php 
  
  error_reporting(E_ALL);
  ini_set('display_errors', 1);

  function sendResponse($r,$lineBreaks=false) {
      // turn response into // separated strings
    if(is_object($r) || $r === null) {
      $m = "";
      // loops throug the rows returned from query 
      for($j=0; $j<$r->num_rows; $j++) { 
        
        // points to next row starting at 0th row
        $r->data_seek($j);
        // gets the columns of that row as a num array
        $rArray = $r->fetch_array(MYSQLI_NUM);
        
        // loops through num array, separating each field with a " // "
        // the loop provides a " /*/ " separator just before the next 
        // row is looped through 
        for($i =0; $i<count($rArray); $i++) {
          if($i < count($rArray) -1)
            $m .= $rArray[$i] . " /_/ ";
          else 
            $m .= $rArray[$i] . " /_/ ";
        }
        // this provides line breaks to see output in browser if true
        $lineBreaks ? $m .= "<br><br>":$m .= "";
      }
      // send the //,/// separated string
      echo $m;
    }
    // $r must be null or non-object
    else {
      echo "You passed me this: " . dump($r) . " which is probably null";
    }
  }
  function getPost($con,$var) {
    return $con->real_escape_string($var);
  } 
  function myP($p) {

    echo "<div>$p</div>";
  }
  function bb($url) {
    echo "<div><button style='height: 500; width: 500;''><a href='$url'>Reload
    </a><button></div>";
  }
  function myQ($con, $q) {
  	$r = $con->query($q);
    if(!$r) echo $con->error;
    return $r;
  }
  function dump($var) {
  	echo "<pre>";
  	var_dump($var);
  	echo "</pre>";
  }
  function myPr($array) {
  	echo "<pre>";
  	print_r($array);
  	echo "</pre>";
  }
  function responseTime($function,$iters=500) {
    
    $time = microtime(true); 
    
    for ($h = 0; $h < $iters; $h++) {
      call_user_func($function);
    }
    
    $end = microtime(true);
    echo "time is " . ($end-$time) . " for " . $iters . " iterations<br>";
  }

?>