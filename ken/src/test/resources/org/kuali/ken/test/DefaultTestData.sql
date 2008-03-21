alter session set NLS_TIMESTAMP_FORMAT='YYYY-MM-DD HH24:MI:SS.FF1';

-- bootstrap defines the notification system user, with id 1...we need to clear this out
DELETE FROM NOTIFICATION_PRODUCERS ;

-- NOTIFICATION_PRODUCERS --
INSERT INTO NOTIFICATION_PRODUCERS 
(ID, NAME, DESCRIPTION, CONTACT_INFO) 
VALUES 
(101, 'Test Producer #1', 'First Producer for Unit Tests', 'producer_1_and_2@127.0.0.1');

INSERT INTO NOTIFICATION_PRODUCERS 
(ID, NAME, DESCRIPTION, CONTACT_INFO) 
VALUES 
(102, 'Test Producer #2', 'Second Producer for Unit Tests', 'producer_1_and_2@127.0.0.1');

INSERT INTO NOTIFICATION_PRODUCERS 
(ID, NAME, DESCRIPTION, CONTACT_INFO) 
VALUES 
(103, 'Test Producer #3', 'Third Producer for Unit Tests', 'producer_3@127.0.0.1');

INSERT INTO NOTIFICATION_PRODUCERS 
(ID, NAME, DESCRIPTION, CONTACT_INFO) 
VALUES 
(104, 'Test Producer #4', 'Fourth Producer for Unit Tests', 'producer_4@127.0.0.1');

INSERT INTO NOTIFICATION_PRODUCERS 
(ID, NAME, DESCRIPTION, CONTACT_INFO) 
VALUES 
(105, 'Notification System', 'This producer represents messages sent from the general message sending form.', 'admins-notsys@127.0.0.1');

-- NOTIFICATION_CHANNELS --

INSERT INTO NOTIFICATION_CHANNELS
(ID, NAME, DESCRIPTION, SUBSCRIBABLE)
VALUES
(101, 'Test Channel #1', 'First Channel for Unit Tests', 'N');

INSERT INTO NOTIFICATION_CHANNELS
(ID, NAME, DESCRIPTION, SUBSCRIBABLE)
VALUES
(102, 'Test Channel #2', 'Second Channel for Unit Tests', 'Y');

INSERT INTO NOTIFICATION_CHANNELS
(ID, NAME, DESCRIPTION, SUBSCRIBABLE)
VALUES
(500, 'KEW', 'Builtin channel for KEW', 'N');

-- NOTIFICATION_CHANNEL_PRODUCERS --
INSERT INTO NOTIFICATION_CHANNEL_PRODUCERS 
(CHANNEL_ID, PRODUCER_ID) 
VALUES 
(101, 103);

INSERT INTO NOTIFICATION_CHANNEL_PRODUCERS 
(CHANNEL_ID, PRODUCER_ID) 
VALUES 
(102, 103);

INSERT INTO NOTIFICATION_CHANNEL_PRODUCERS 
(CHANNEL_ID, PRODUCER_ID) 
VALUES 
(102, 104);

INSERT INTO NOTIFICATION_CHANNEL_PRODUCERS 
(CHANNEL_ID, PRODUCER_ID) 
VALUES 
(101, 105);

INSERT INTO NOTIFICATION_CHANNEL_PRODUCERS 
(CHANNEL_ID, PRODUCER_ID) 
VALUES 
(102, 105);

-- NOTIFICATIONS --
INSERT INTO NOTIFICATIONS 
(ID, DELIVERY_TYPE, CREATED_DATETIME, SEND_DATETIME, AUTO_REMOVE_DATETIME, PRIORITY_ID , CONTENT,
CONTENT_TYPE_ID , NOTIFICATION_CHANNEL_ID , PRODUCER_ID, PROCESSING_FLAG, LOCKED_DATE )
VALUES
(1, 'FYI', systimestamp, '2005-12-31 19:00:00.0','3000-12-31 19:00:00.0', 1,
'<content xmlns="ns:notification/ContentSimple" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="ns:notification/ContentSimple resource:notification/ContentSimple"><message>Check this out!</message></content>',
1, 101, 103, 'RESOLVED', NULL);

INSERT INTO NOTIFICATIONS 
(ID, DELIVERY_TYPE, CREATED_DATETIME, SEND_DATETIME, AUTO_REMOVE_DATETIME, PRIORITY_ID, CONTENT,
CONTENT_TYPE_ID, NOTIFICATION_CHANNEL_ID, PRODUCER_ID, PROCESSING_FLAG, LOCKED_DATE )
VALUES
(2, 'ACK', systimestamp, '2005-12-31 19:00:00.0','3000-12-31 19:00:00.0', 2, 
'<content xmlns="ns:notification/ContentEvent" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="ns:notification/ContentEvent resource:notification/ContentEvent"><message>CCC presents The Strokes at Cornell</message><!-- an event that it happening on campus --><event><summary>CCC presents The Strokes at Cornell</summary><description>blah blah blah</description><location>Barton Hall</location><startDateTime>2006-01-01T00:00:00</startDateTime><stopDateTime>2007-01-01T00:00:00</stopDateTime></event></content>',
2, 102, 104, 'RESOLVED', NULL);

-- the following notifications and recipients list (along with deliverer preferences) are relied upon by the NotificationMessageDeliveryResolverServiceImplTest

INSERT INTO NOTIFICATIONS 
(ID, DELIVERY_TYPE, CREATED_DATETIME, SEND_DATETIME, AUTO_REMOVE_DATETIME, PRIORITY_ID, CONTENT,
CONTENT_TYPE_ID, NOTIFICATION_CHANNEL_ID, PRODUCER_ID, PROCESSING_FLAG, LOCKED_DATE )
VALUES
(3, 'FYI', systimestamp, '2005-12-31 19:00:00.0','2006-12-30 19:00:00.0', 1, 
'<content xmlns="ns:notification/ContentEvent" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="ns:notification/ContentEvent resource:notification/ContentEvent"><message>CCC presents The Strokes at Cornell</message><!-- an event that it happening on campus --><event><summary>CCC presents The Strokes at Cornell</summary><description>blah blah blah</description><location>Barton Hall</location><startDateTime>2006-01-01T00:00:00</startDateTime><stopDateTime>2007-01-01T00:00:00</stopDateTime></event></content>',
2, 101, 103, 'UNRESOLVED', NULL);

INSERT INTO NOTIFICATIONS 
(ID, DELIVERY_TYPE, CREATED_DATETIME, SEND_DATETIME, AUTO_REMOVE_DATETIME, PRIORITY_ID, CONTENT,
CONTENT_TYPE_ID, NOTIFICATION_CHANNEL_ID, PRODUCER_ID, PROCESSING_FLAG, LOCKED_DATE )
VALUES
(4, 'FYI', systimestamp, '2005-12-31 19:00:00.0','3000-12-31 19:00:00.0', 1, 
'<content xmlns="ns:notification/ContentEvent" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="ns:notification/ContentEvent resource:notification/ContentEvent"><message>CCC presents The Strokes at Cornell</message><!-- an event that it happening on campus --><event><summary>CCC presents The Strokes at Cornell</summary><description>blah blah blah</description><location>Barton Hall</location><startDateTime>2006-01-01T00:00:00</startDateTime><stopDateTime>2007-01-01T00:00:00</stopDateTime></event></content>',
2, 101, 103, 'UNRESOLVED', NULL);

-- NOTIFICATION_RECIPIENTS --
INSERT INTO NOTIFICATION_RECIPIENTS
(ID, NOTIFICATION_ID, RECIPIENT_ID, RECIPIENT_TYPE)
VALUES
(1, 1, 'TestUser5', 'USER');

INSERT INTO NOTIFICATION_RECIPIENTS
(ID, NOTIFICATION_ID, RECIPIENT_ID, RECIPIENT_TYPE)
VALUES
(2, 1, 'TestUser6', 'USER');

INSERT INTO NOTIFICATION_RECIPIENTS
(ID, NOTIFICATION_ID, RECIPIENT_ID, RECIPIENT_TYPE)
VALUES
(3, 2, 'TestUser4', 'USER');

INSERT INTO NOTIFICATION_RECIPIENTS
(ID, NOTIFICATION_ID, RECIPIENT_ID, RECIPIENT_TYPE)
VALUES
(4, 2, 'TestUser6', 'USER');

INSERT INTO NOTIFICATION_RECIPIENTS
(ID, NOTIFICATION_ID, RECIPIENT_ID, RECIPIENT_TYPE)
VALUES
(5, 2, 'TestUser5', 'USER');

INSERT INTO NOTIFICATION_RECIPIENTS
(ID, NOTIFICATION_ID, RECIPIENT_ID, RECIPIENT_TYPE)
VALUES
(6, 3, 'RiceTeam', 'GROUP');

INSERT INTO NOTIFICATION_RECIPIENTS
(ID, NOTIFICATION_ID, RECIPIENT_ID, RECIPIENT_TYPE)
VALUES
(7, 3, 'TestUser1', 'USER');


INSERT INTO NOTIFICATION_RECIPIENTS
(ID, NOTIFICATION_ID, RECIPIENT_ID, RECIPIENT_TYPE)
VALUES
(8, 4, 'RiceTeam', 'GROUP');

INSERT INTO NOTIFICATION_RECIPIENTS
(ID, NOTIFICATION_ID, RECIPIENT_ID, RECIPIENT_TYPE)
VALUES
(9, 4, 'TestUser1', 'USER');

-- NOTIFICATION_SENDERS --
INSERT INTO NOTIFICATION_SENDERS
(ID, NOTIFICATION_ID, NAME)
VALUES
(1, 1, 'John Fereira');

INSERT INTO NOTIFICATION_SENDERS
(ID, NOTIFICATION_ID, NAME)
VALUES
(2, 1, 'Aaron Godert');

INSERT INTO NOTIFICATION_SENDERS
(ID, NOTIFICATION_ID, NAME)
VALUES
(3, 2, 'Aaron Hamid');

-- the following NOTIFICATION_MSG_DELIVS are used by NotificationMessageDeliveryDispatchServiceImplTest
-- if this list is changed, verify that the test is updated to reflect expected results

-- NOTIFICATION_MSG_DELIVS --
INSERT INTO NOTIFICATION_MSG_DELIVS
(ID, NOTIFICATION_ID, USER_RECIPIENT_ID, MESSAGE_DELIVERY_STATUS, LOCKED_DATE) 
VALUES 
(1, 1, 'TestUser5', 'UNDELIVERED', NULL);

INSERT INTO NOTIFICATION_MSG_DELIVS 
(ID, NOTIFICATION_ID, USER_RECIPIENT_ID, MESSAGE_DELIVERY_STATUS, LOCKED_DATE) 
VALUES 
(2, 1, 'TestUser6', 'UNDELIVERED', NULL);

INSERT INTO NOTIFICATION_MSG_DELIVS 
(ID, NOTIFICATION_ID, USER_RECIPIENT_ID, MESSAGE_DELIVERY_STATUS, LOCKED_DATE) 
VALUES 
(3, 2, 'TestUser4', 'UNDELIVERED', NULL);

INSERT INTO NOTIFICATION_MSG_DELIVS 
(ID, NOTIFICATION_ID, USER_RECIPIENT_ID, MESSAGE_DELIVERY_STATUS, LOCKED_DATE) 
VALUES 
(4, 2, 'TestUser6', 'UNDELIVERED', NULL);

INSERT INTO NOTIFICATION_MSG_DELIVS 
(ID, NOTIFICATION_ID, USER_RECIPIENT_ID, MESSAGE_DELIVERY_STATUS, LOCKED_DATE) 
VALUES 
(6, 2, 'TestUser5', 'UNDELIVERED', NULL);
