/* stored event and procedure for 

   deleting events that already took place 

 */


delimiter $$

CREATE PROCEDURE delEvents ()

BEGIN

delete from events where dateEvent < CURDATE();


END$$

DELIMITER ;

/* turn scheduluer on if not on */
SET GLOBAL event_scheduler = ON; 

/*  procedure happens twice a day */
CREATE EVENT delEvents
    ON SCHEDULE EVERY 12 HOUR
    STARTS '2016-04-29 12:30:00'
    ON COMPLETION PRESERVE 
    COMMENT 'delete events that have passed twice a day'
    DO CALL delEvents();