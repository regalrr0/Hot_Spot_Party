<html> 
  <head>
  </head>

  <body>
      <form action="AddEvent.php" method="post">
      Name:<br>

  	  <input type="text" name="name" size="100"><br><br>
  	  Description:
  	  <input type="text" name="description" size="175"><br><br>

  	  address:<br>

  	  <input type="text" name="address" size="100"><br><br>
  	  specialNotes:<br>

  	  <input type="text" name="specialNotes" size="100"><br><br>
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



  	  <button type="submit" name="submit">Submit</button>
    </form>


<?php

    if(isset($_POST['name']) &&
       isset($_POST['description']) &&
       isset($_POST['address']) &&
       isset($_POST['year']) &&
       isset($_POST['day']) &&
       isset($_POST['year']) &&
       isset($_POST['eventType'])) {

    $con = new mysqli("crisler","user","csci","android");

    $name           = $_POST['name']; 
    $description    = $_POST['description'];
    $year           = $_POST['year'];
    $day            = $_POST['day'];
    $month          = $_POST['month'];
    $address        = $_POST['address']; 
    $sNotes         = $_POST['specialNotes'];
    echo $eventType      = $_POST['eventType'];


    $date = $year . '-' . $month . '-' . $day;

    
    $query = "insert into events(name, description, dateEvent, address, specialNotes, eventTypeId) 

             VALUES('$name'
                   ,'$description'
                   ,'$date'
                   ,'$address'
                   ,'$sNotes'
                   ,(select eventTypeId from eventType where eventTypeId = '$eventType'))";
    
    $r = $con->query($query);
    if(!$r) die($con->error);

    /*if(isset($_POST['submit']))
    header("Location: http://hive.sewanee.edu/evansdb0/android/AddEvent.php"); */

}

?>
  </body>

<html>