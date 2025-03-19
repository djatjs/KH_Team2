SELECT * FROM cafe.member;

SET GLOBAL event_scheduler = On;
#-----------------------------------------------
# 이벤트 확인
SELECT * FROM information_schema.events;

# 이벤트 삭제
drop event if exists update_delete_event_123;

# 이벤트 실행날짜 변경
ALTER EVENT update_delete_event_123
ON SCHEDULE AT '2025-03-19 19:38:00';
