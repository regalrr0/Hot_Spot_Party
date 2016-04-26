<?php 

  require_once('response.php');

  $messages = "";
  $con = new mysqli("crisler","user","csci","android");
  
  // make sure that every field has a value
  if(isset($_POST['userName']) &&
     isset($_POST['pass']) &&
     isset($_POST['age']) &&
     isset($_POST['email']) &&
     isset($_POST['fName']) &&
     isset($_POST['lName']) &&
     isset($_POST['sex']) && 
     isset($_POST['cPass'])) {   
    
    // get vars from post 
    $userName    = getPost($con,$_POST['userName']); 
    $age         = getPost($con,$_POST['age']);
    $email       = getPost($con,$_POST['email']);
    $fName       = getPost($con,$_POST['fName']); 
    $lName       = getPost($con,$_POST['lName']);
    $sex         = getPost($con,$_POST['sex']);
    $cPass       = getPost($con,$_POST['cPass']);
    $pass        = getPost($con,$_POST['pass']);

    // check provided data validity
    // check length and matching passwords
    if (empty($pass) || empty($cPass)) {
        $messages .=  'The password field is empty';
    } elseif ($pass != $cPass) {
        $messages .=  "The password fields do not match";
    } elseif (strlen($pass) < 6) {
        $messages .= "The password isn't long enough";
        
      // check length and validity of the email address and username
    } elseif(empty($userName)) {
        $messages .="The username field is empty";
    } elseif (strlen($userName) > 20) {
        $messages .=  "The username is too long";
    } elseif (empty($email)) {
        $messages .= "The email field is empty";
    } elseif (strlen($email) > 50) {
        $messages .="The email entered is too long";
    } elseif (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $messages .= "The email you entered is invalid";
      // check length of first name
    } elseif (strlen($fName)==0 || strlen($fName) > 20) {
        $messages.="The first name field is either empty or too long ". strlen($fname);
      // check length of last name 
    } elseif (strlen($lName)==0 || strlen($lName) > 20) {
        $messages.= "The last name field is either empty or too long ". strlen($lName);
        
      // check length of career
    } elseif (empty($age)) {
       $messages.= "The age field is empty";
    } elseif ($age > 100) {
       $messages.= "You're too old bitch. Sorry";
    }

    else {   
      // check if user with that userName already exists
      $query = "select userName from users where userName = '$userName'";
      $r = $con->query($query);
    
      if(!$r) echo $con->error;
    
    
      if($r->num_rows > 0) {
        $messages[] = "Username already taken";
      }
      else {
        $pass_hash = password_hash($pass, PASSWORD_DEFAULT);
      
        $query = "insert into users(userName, passHash,email,fName,lName,age,gender) 
    
                  values('$userName','$pass','$email','$fName','$lName','$age','$sex')";
      
        $r = $con->query($query);
      
        if(!$r) 
          echo $con->error;
        else
          $messages.= "true // ";
          $messages.= "You successfully registered!";
      }
    }
  } // END ISSET 
  echo $messages;
?>
