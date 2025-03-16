SELECT * FROM cafe.member;



#-----------------------------------------------
SELECT * FROM information_schema.events;

drop event if exists update_delete_event;

SET GLOBAL event_scheduler = On;



DELIMITER $$
CREATE PROCEDURE update_delete_event(IN p_mId VARCHAR(13))
BEGIN
    IF NOT EXISTS (SELECT 1 FROM `member` WHERE M_ID = p_mId AND M_DEL = 'Y') THEN
        UPDATE `member`
        SET M_DEL = 'Y', M_DEL_TIME = NOW()
        WHERE M_ID = p_mId;
    END IF;

    DELETE FROM `member`
    WHERE M_ID = p_mId
    AND M_DEL = 'Y'
    AND M_DEL_TIME <= NOW() - INTERVAL 7 DAY;

    IF ROW_COUNT() > 0 THEN
        DROP EVENT IF EXISTS update_and_delete_members;
    END IF;
END $$

DELIMITER ;

