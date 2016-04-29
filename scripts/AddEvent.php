<html> 
  <head>
  </head>

  <body>
      <form id="myform" action="AddEvent.php" method="post">
      Name:<br>

  	  <input type="text" name="name" size="100" value="hello"><br><br>
  	  Description:
  	  <input type="text" name="description" size="175" value="hello"><br><br>

  	  address:<br>

  	  <input type="text" name="address" size="100" value="hello"><br><br>
  	  specialNotes:<br>

  	  <input type="text" name="specialNotes" size="100" value="hello"><br><br>
  	    	  dateOfEvent:<br>

  	  <input type="text" name="year" value="2016">
  	  <select name="month" id="month" onchange="" size="1">
        <option value="01">January</option>
        <option value="02">February</option>
        <option value="03">March</option>
        <option value="04" selected>April</option>
        <option value="05">May</option>
        <option value="06">June</option>
        <option value="07">July</option>
        <option value="08">August</option>
        <option value="09">September</option>
        <option value="10">October</option>
        <option value="11">November</option>
        <option value="12">December</option>
      </select>
      <select name="day" id="day" onchange="" size="1">
        <option value="01">01</option>
        <option value="02">02</option>
        <option value="03">03</option>
        <option value="04">04</option>
        <option value="05">05</option>
        <option value="06">06</option>
        <option value="07">07</option>
        <option value="08">08</option>
        <option value="09">09</option>
        <option value="10">10</option>
        <option value="11">11</option>
        <option value="12">12</option>
        <option value="13">13</option>
        <option value="14">14</option>
        <option value="15">15</option>
        <option value="16">16</option>
        <option value="17">17</option>
        <option value="18">18</option>
        <option value="19">19</option>
        <option value="20">20</option>
        <option value="21">21</option>
        <option value="22">22</option>
        <option value="23">23</option>
        <option value="24">24</option>
        <option value="25">25</option>
        <option value="26">26</option>
        <option value="27">27</option>
        <option value="28">28</option>
        <option value="29">29</option>
        <option value="30">30</option>
        <option value="31">31</option>
      </select><br><br>


      Event Type:
      <select name="eventType" id="eventType" onchange="" size="1">
        <option value="1">Club</option>
        <option value="2">Festival</option>
        <option value="3">Sports</option>

      </select><br><br>
        
      URL:
      <input type="text" name="url"

      value="https://images6.alphacoders.com/316/316963.jpg">



  	  <button type="submit" name="submit">Submit</button>
    </form>


<?php

  ini_set('display_errors', 1);
  error_reporting(E_ALL);
require_once('response.php');

   if(isset($_POST['name']) &&
       isset($_POST['description']) &&
       isset($_POST['address']) &&
       isset($_POST['year']) &&
       isset($_POST['day']) &&
       isset($_POST['year']) &&
       isset($_POST['eventType']) &&
       isset($_POST['url'])) { 
 
    $con = new mysqli("crisler","user","csci","android");

    $name           = getPost($con,$_POST['name']); 
    $description    = getPost($con,$_POST['description']);
    $year           = getPost($con,$_POST['year']);
    $day            = getPost($con,$_POST['day']);
    $month          = getPost($con,$_POST['month']);
    $address        = getPost($con,$_POST['address']); 
    $sNotes         = getPost($con,$_POST['specialNotes']); 



    // this is a remote url that links to an image 
    $imgPath = getPost($con,$_POST['url']);
    $eventType      = getPost($con,$_POST['eventType']);
    $date = $year . '-' . $month . '-' . $day;
    
    // writes two files: one that will be the big image 
    // and one that will be the small image 
    // the image specified in $imgPath
    $image = writeImgFileFromURL($imgPath,'w+');
    
    // create the small and large image 
    $small = createThumbnail("../images/small_" 
    . $image,50); // new image width of 50

    $large = createThumbnail("../images/large_" 
    . $image,200);

// create the url where the image is found on the server 
$smallImgUrl = "http://hive.sewanee.edu/evansdb0" .
"/android1/images/small_" . $image;
$largeImgUrl = "http://hive.sewanee.edu/evansdb0" .
"/android1/images/large_" . $image;
    
// save that url in the database 
$query = "insert into events(name, description, dateEvent, address, specialNotes, smallImgPath, largeImgPath, eventTypeId) 

             VALUES('$name'
                   ,'$description'
                   ,'$date'
                   ,'$address'
                   ,'$sNotes'
                   ,'$smallImgUrl'
                   ,'$largeImgUrl'
                   ,(select eventTypeId from eventType where eventTypeId = '$eventType'))";
    
    $r = $con->query($query);
    if(!$r) die($con->error);

    /*if(isset($_POST['submit']O))O
    header("Location: http://hive.sewanee.edu/evansdb0/android/AddEvent.php"); */

    echo "<br><br><img src='$small'>";
    echo "<br><br><img src='$large'>";

}
function createThumbnail($filename,$width) {
     
    if(preg_match('/[.](jpg)$/', $filename)) {
        $im = imagecreatefromjpeg($filename);
    } else if (preg_match('/[.](gif)$/', $filename)) {
        $im = imagecreatefromgif($path_to_image_directory . $filename);
    } else if (preg_match('/[.](png)$/', $filename)) {
        $im = imagecreatefrompng($path_to_image_directory . $filename);
    }
     
    $ox = imagesx($im);
    $oy = imagesy($im);
     
    $nx = $final_width_of_image = $width;
    $ny = floor($oy * ($final_width_of_image / $ox));
     
    $nm = imagecreatetruecolor($nx, $ny);
     
    imagecopyresized($nm, $im, 0,0,0,0,$nx,$ny,$ox,$oy);
     
    /*if(!file_exists($path_to_thumbs_directory)) {
      if(!mkdir($path_to_thumbs_directory)) {
           die("There was a problem. Please try again!");
      } 
       } */
    // need to make sure privileges are good on the image saved 
       // or else imagejpeg fails 
    imagejpeg($nm, $filename,100);
    $tn = $filename;
    
    return $tn;
}
function writeImgFileFromURL($imgURL,$access) {
   
   // get the URL components as an array
   $url_array = explode("/", $imgURL);

   $image = end($url_array);

   // write file for small image using the last 
   // element in the $url_array
   $handle = 
   fopen("../images/small_" . $image, $access);
   fwrite($handle, file_get_contents($imgURL));
   fclose($handle);

   // same as above but for a large file 
   $handle = 
   fopen("../images/large_" . $image, $access);
   fwrite($handle, file_get_contents($imgURL));
   fclose($handle);

   // very bad, but not root user so can make this more 
   // secure right now. Allows us to write to the files 
   // just created 
   shell_exec("chmod 0777 ../images/small_". $image);
   shell_exec("chmod 0777 ../images/large_". $image);
   
   // return the name of the file written to 
   return $image;
}
?>
  </body>

<html>