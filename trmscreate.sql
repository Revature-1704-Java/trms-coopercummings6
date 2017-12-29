Drop Table Attachment;
DROP TABLE EMPLOYEE;
DROP TABLE EMPLOYEETYPE;
DROP TABLE EVENTTYPE;
DROP TABLE GRADINGFORMAT;
DROP TABLE LOCATION;
DROP TABLE REQUEST;
DROP Sequence emptype_seq;
DROP SEQUENCE emp_seq;
DROP SEQUENCE form_seq;
DROP SEQUENCE type_seq;
DROP SEQUENCE loc_seq;
DROP SEQUENCE att_seq;
DROP SEQUENCE req_seq;
commit;
CREATE TABLE EMPLOYEETYPE
(
    EmployeeType_ID NUMBER NOT NULL,
    EmployeeTypeTitle VARCHAR (127),
    CONSTRAINT PK_EMPLOYEETYPE_ID PRIMARY KEY (EmployeeType_ID)
);
CREATE SEQUENCE emptype_seq START WITH 1;

CREATE OR REPLACE TRIGGER emptype_keys 
BEFORE INSERT ON EMPLOYEETYPE
FOR EACH ROW

BEGIN
  SELECT emptype_seq.NEXTVAL
  INTO   :new.EmployeeType_ID
  FROM   dual;
END;
/

-- Code creating employee table and the constraints that can be done with just employee table
CREATE TABLE EMPLOYEE
(
    Employee_ID NUMBER NOT NULL,
    EmployeeName VARCHAR2(255) NOT NULL, --people can have varying numbers of names, so one field is more universally compatible than multiple
    ClaimsAmountRemaining Number DEFAULT 1000.0 NOT NULL,
    eMail VARCHAR(255),
    Password VARCHAR2(31) NOT NULL,
    SupervisorID NUMBER,        --id of supervisor who approves this employee's requests
    DepHeadID NUMBER,           --id of department head who approves this employee's requests
    BCoordinatorID NUMBER,      --id of benefits coordinator who approves this employee's requests
    EmployeeType_ID NUMBER NOT NULL,
    CONSTRAINT PK_EMPLOYEE_ID PRIMARY KEY (Employee_ID)
);

CREATE SEQUENCE emp_seq START WITH 1;

CREATE OR REPLACE TRIGGER emp_keys 
BEFORE INSERT ON EMPLOYEE
FOR EACH ROW

BEGIN
  SELECT emp_seq.NEXTVAL
  INTO   :new.Employee_ID
  FROM   dual;
END;
/

ALTER TABLE Employee ADD CONSTRAINT FK_SupervisorID
FOREIGN KEY (SupervisorID) REFERENCES Employee (Employee_ID)  ;

ALTER TABLE Employee ADD CONSTRAINT FK_DepHeadID
FOREIGN KEY (DepHeadID) REFERENCES Employee (Employee_ID)  ;

ALTER TABLE Employee ADD CONSTRAINT FK_BCoordinatorID
FOREIGN KEY (BCoordinatorID) REFERENCES Employee (Employee_ID)  ;

ALTER TABLE Employee ADD CONSTRAINT FK_EmployeeType_ID
FOREIGN KEY (EmployeeType_ID) REFERENCES EmployeeType (EmployeeType_ID)  ;


--creation of grading format table
CREATE TABLE GRADINGFORMAT
(
    Format_ID NUMBER NOT NULL,
    FormatName VARCHAR2(63),
    PassingGrade VARCHAR(5),
    CONSTRAINT PK_FORMATID PRIMARY KEY (FORMAT_ID)
);
CREATE SEQUENCE form_seq START WITH 1;
CREATE OR REPLACE TRIGGER form_keys 
BEFORE INSERT ON GRADINGFORMAT
FOR EACH ROW
BEGIN
  SELECT form_seq.NEXTVAL
  INTO   :new.Format_ID
  FROM   dual;
END;
/

--creation of EventType table
CREATE TABLE EVENTTYPE
(
    Type_ID NUMBER NOT NULL,
    TypeName VARCHAR2(63),
    PercentReimbursement NUMBER,
    CONSTRAINT PK_Type_ID PRIMARY KEY (Type_ID)
);
CREATE SEQUENCE type_seq START WITH 1;
CREATE OR REPLACE TRIGGER type_keys 
BEFORE INSERT ON EVENTTYPE
FOR EACH ROW
BEGIN
  SELECT type_seq.NEXTVAL
  INTO   :new.Type_ID
  FROM   dual;
END;
/

--creation of location table
CREATE TABLE LOCATION
(
    Location_ID NUMBER NOT NULL,
    Location_Name VARCHAR2(127),
    CONSTRAINT PK_Location_ID PRIMARY KEY (Location_ID)
);
CREATE SEQUENCE loc_seq START WITH 1;
CREATE OR REPLACE TRIGGER loc_keys 
BEFORE INSERT ON LOCATION
FOR EACH ROW
BEGIN
  SELECT loc_seq.NEXTVAL
  INTO   :new.Location_ID
  FROM   dual;
END;
/

CREATE TABLE ATTACHMENT
(
    Attachment_ID NUMBER NOT NULL,
    AttachmentPath VARCHAR2(127),
    CONSTRAINT PK_Attachment_ID PRIMARY KEY (Attachment_ID)
);
DROP TRIGGER att_keys;
CREATE SEQUENCE att_seq START WITH 1;
CREATE OR REPLACE TRIGGER att_keys 
BEFORE INSERT ON ATTACHMENT
FOR EACH ROW
BEGIN
  SELECT att_seq.NEXTVAL
  INTO   :new.Attachment_ID
  FROM   dual;
END;
/

--Request table that holds information from each request
CREATE TABLE REQUEST 
(
    Request_ID NUMBER NOT NULL,
    Requester_ID NUMBER NOT NULL,
    DateTimeSubmitted TIMESTAMP NOT NULL,
    EventLocation_ID NUMBER NOT NULL,
    GradingFormat_ID NUMBER NOT NULL,
    EventType_ID NUMBER NOT NULL,
    Description VARCHAR2(2047),
    Cost NUMBER NOT NULL,
    WorkTimeMissed NUMBER NOT NULL,
    Attachment_ID NUMBER,
    FinalTimestamp TIMESTAMP,
    FinalGrade VARCHAR2(5), --used a varchar in case of any oddities resulting in grades that are not char, 5 should hold pass, fail, percentages, and point values up to 99999.
    SupervisorApproval CHAR check (SupervisorApproval in (0,1, NULL)),--a null value represents not having been approved or disapproved yet, char is necessary because oracle didn't feel like supporting booleans
    DenialReason VARCHAR(2047),
    DepHeadApproval CHAR check (DepHeadApproval in (0,1, NULL)),
    BCoordinatorApproval CHAR check (BCoordinatorApproval in (0,1, NULL)),
    CONSTRAINT PK_REQUEST_ID PRIMARY KEY (Request_ID)
);

ALTER TABLE Request ADD CONSTRAINT FK_RequesterID
FOREIGN KEY (Requester_ID) REFERENCES Employee (Employee_ID)  ;

ALTER TABLE Request ADD CONSTRAINT FK_EventLocation_ID
FOREIGN KEY (EventLocation_ID) REFERENCES LOCATION (Location_ID)  ;

ALTER TABLE Request ADD CONSTRAINT FK_GradingFormat_ID
FOREIGN KEY (GradingFormat_ID) REFERENCES GRADINGFORMAT (FORMAT_ID)  ;

ALTER TABLE Request ADD CONSTRAINT FK_EventType_ID
FOREIGN KEY (EventType_ID) REFERENCES EVENTTYPE (Type_ID)  ;

ALTER TABLE Request ADD CONSTRAINT FK_Attachment_ID
FOREIGN KEY (Attachment_ID) REFERENCES ATTACHMENT (ATTACHMENT_ID)  ;

CREATE SEQUENCE req_seq START WITH 1 CACHE 20;
CREATE OR REPLACE TRIGGER req_keys 
BEFORE INSERT ON REQUEST
FOR EACH ROW

BEGIN
  SELECT req_seq.NEXTVAL
  INTO   :new.Request_ID
  FROM   dual;
END;
/

CREATE OR REPLACE TRIGGER req_sub_timestamp 
BEFORE INSERT ON REQUEST
FOR EACH ROW
BEGIN
  SELECT SYSTIMESTAMP
  INTO   :new.DateTimeSubmitted
  FROM   dual;
END;
/

--add data for reference tables that is not expected to change under normal circumstances
INSERT INTO GRADINGFORMAT (FORMAT_ID, FormatName, PassingGrade) VALUES (1, 'PASS/FAIL', 'PASS');
INSERT INTO GRADINGFORMAT (FORMAT_ID, FormatName, PassingGrade) VALUES (2, 'A/B/C/D/F D PASS', 'D');
INSERT INTO GRADINGFORMAT (FORMAT_ID, FormatName, PassingGrade) VALUES (3, 'A/B/C/D/F C PASS', 'C');
INSERT INTO GRADINGFORMAT (FORMAT_ID, FormatName, PassingGrade) VALUES (4, 'OTHER', NULL);

INSERT INTO EVENTTYPE (Type_ID, TypeName, PercentReimbursement) VALUES (1, 'University Course', 80);
INSERT INTO EVENTTYPE (Type_ID, TypeName, PercentReimbursement) VALUES (2, 'Seminar', 60);
INSERT INTO EVENTTYPE (Type_ID, TypeName, PercentReimbursement) VALUES (3, 'Certification Preparation Class', 75);
INSERT INTO EVENTTYPE (Type_ID, TypeName, PercentReimbursement) VALUES (4, 'Certification', 100);
INSERT INTO EVENTTYPE (Type_ID, TypeName, PercentReimbursement) VALUES (5, 'Technical Training', 90);
INSERT INTO EVENTTYPE (Type_ID, TypeName, PercentReimbursement) VALUES (6, 'Other', 30);

INSERT INTO EMPLOYEETYPE (EmployeeType_ID, EmployeeTypeTitle) VALUES (1, 'Non-approving employee');
INSERT INTO EMPLOYEETYPE (EmployeeType_ID, EmployeeTypeTitle) VALUES (2, 'Manager');
INSERT INTO EMPLOYEETYPE (EmployeeType_ID, EmployeeTypeTitle) VALUES (3, 'Department Head');
INSERT INTO EMPLOYEETYPE (EmployeeType_ID, EmployeeTypeTitle) VALUES (4, 'Benefits Coordinator');

--sample data for testing purposes
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (1,'Mike',1000,'burneremail32@mail.com','pass',NULL,NULL,NULL,4);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (2,'Bob',1000,'thisisnotmyrealemail@mail.com','word',NULL,NULL,NULL,3);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (3,'John',1000,'notarealemail@mail.com','pass',NULL,NULL,NULL,2);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (4,'Jeff',1000,'fake@burner.ninja','pass',NULL,NULL,NULL,2);
commit;

UPDATE EMPLOYEE SET SUPERVISORID = 1, DEPHEADID = 1, BCOORDINATORID = 1 WHERE employee.Employee_ID = (SELECT MIN(EMPLOYEE_ID) FROM EMPLOYEE);
UPDATE EMPLOYEE SET SUPERVISORID = 1, DEPHEADID = 2, BCOORDINATORID = 1 WHERE employee.Employee_ID = (SELECT MIN(EMPLOYEE_ID) FROM EMPLOYEE)+1;
UPDATE EMPLOYEE SET SUPERVISORID = 2, DEPHEADID = 2, BCOORDINATORID = 1 WHERE employee.Employee_ID = (SELECT MIN(EMPLOYEE_ID) FROM EMPLOYEE)+2;
UPDATE EMPLOYEE SET SUPERVISORID = 2, DEPHEADID = 2, BCOORDINATORID = 1 WHERE employee.Employee_ID = (SELECT MIN(EMPLOYEE_ID) FROM EMPLOYEE)+3;
commit;

INSERT INTO LOCATION (Location_ID, Location_Name) VALUES (1, 'A State University Campus');
INSERT INTO REQUEST (Request_ID, Requester_ID, DateTimeSubmitted, EventLocation_ID, GradingFormat_ID, EventType_ID, Description, Cost, WorkTimeMissed, Attachment_ID) VALUES (1, 2, SYSTIMESTAMP, 1, 1, 1, '', 100, 1, NULL);
INSERT INTO REQUEST (Request_ID, Requester_ID, DateTimeSubmitted, EventLocation_ID, GradingFormat_ID, EventType_ID, Description, Cost, WorkTimeMissed, Attachment_ID, FINALGRADE) VALUES (2, 2, SYSTIMESTAMP, 1, 1, 1, '', 100, 1, NULL, 'PASS');

INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (5,'Vance Andrews',1000,'quam.a@MaurisnullaInteger.org','TDI80REC0IX',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (6,'Velma Stewart',1000,'adipiscing@egestasSed.ca','PZV83WJF1OM',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (7,'Sybill Snider',1000,'Praesent.interdum@cursusdiam.ca','GUN01VPP9IJ',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (8,'Lydia U. Leach',1000,'Proin.vel.nisl@Etiambibendumfermentum.edu','DXQ88WEN0XU',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (9,'Fredericka W. Tucker',1000,'enim@cursuset.edu','BFB39DBY2VH',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (10,'Amy Leblanc',1000,'Phasellus@blandit.org','TDM87MGR7WO',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (11,'Sharon Garcia',1000,'faucibus@id.net','ZYM96VPP7WL',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (12,'Drew A. Payne',1000,'id.enim.Curabitur@vitaedolor.net','MGB60KTL6IU',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (13,'Peter N. Cobb',1000,'tellus@dolor.edu','LUV60GFK6QP',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (14,'Justina Strong',1000,'ipsum.sodales.purus@Suspendissetristiqueneque.org','YYE17ARY9DV',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (15,'Xenos Albert',1000,'natoque.penatibus@sem.ca','IAE18BZY8NQ',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (16,'Alexis Watts',1000,'dolor@metusfacilisislorem.com','KHR77HCO6PY',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (17,'Jescie Nieves',1000,'semper.egestas.urna@aduiCras.co.uk','HHT19LAY0HJ',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (18,'Barrett N. Parks',1000,'dolor.Fusce@purussapiengravida.edu','DQX22TAY3KM',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (19,'Zena U. Decker',1000,'pede.Praesent.eu@convallisin.co.uk','XRC82FQI4HS',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (20,'Carly U. Bonner',1000,'erat.vitae.risus@Crasdictum.co.uk','XZG57DKD2WY',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (21,'Illiana U. Atkins',1000,'mus@incursuset.org','XFX56DDE2UM',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (22,'Tamekah Alford',1000,'eu.elit.Nulla@nonloremvitae.org','UDU49RAT9MJ',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (23,'Elmo Fuller',1000,'fames.ac.turpis@molestie.org','GIE31MAV9PX',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (24,'Ainsley Harding',1000,'facilisis.lorem.tristique@diam.ca','SOW72ZMS4JF',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (25,'Callie F. Bradford',1000,'at@molestie.ca','EMI59JMD4BL',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (26,'Reed Aguirre',1000,'vel.vulputate@rutrumjusto.ca','IFR81TSA3NL',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (27,'Uta K. Ortiz',1000,'arcu@magnis.co.uk','IOS18KII0XL',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (28,'Dale Hobbs',1000,'lacus.Quisque@Cumsociisnatoque.edu','YJY04DRQ5AH',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (29,'Serena Burnett',1000,'quam@pedenonummyut.ca','XTD75EKB7LV',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (30,'Tiger H. Henderson',1000,'Lorem.ipsum.dolor@lorem.co.uk','OPV21YZS7VZ',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (31,'Stone T. Ford',1000,'volutpat.ornare.facilisis@sitametlorem.com','FYQ16LBJ3EU',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (32,'Fleur Conner',1000,'fringilla@ornarefacilisiseget.com','OIS03LXJ7EH',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (33,'Jenette G. Cantu',1000,'molestie.tortor.nibh@Crassedleo.ca','EJK99INI5CM',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (34,'Leigh N. Flores',1000,'magna@egestasFusce.co.uk','SQT62UOK9WC',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (35,'Justine Mayo',1000,'tincidunt.neque.vitae@nullaIntegervulputate.co.uk','HDV24SQR1OA',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (36,'September J. Brennan',1000,'Fusce@lobortisClass.net','TMY13WUB3BB',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (37,'Felix Huber',1000,'metus@sociisnatoque.edu','POF30LRX1FK',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (38,'Lavinia B. Flynn',1000,'quis.pede.Suspendisse@purusgravida.com','KIW03HGK3GC',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (39,'Rooney Z. Humphrey',1000,'lorem.fringilla.ornare@duiquis.com','IFN48GYO3SB',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (40,'Jamal U. Farrell',1000,'cursus.Nunc@conubia.net','UVV19QQL9BG',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (41,'Nina Olson',1000,'id@tincidunt.com','TNI10VVC5LW',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (42,'Diana Cannon',1000,'cursus@velitAliquam.com','MJK38XDQ4QT',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (43,'Lars Lyons',1000,'nec.orci.Donec@lacus.com','KLV70XID2IX',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (44,'Jaime Mccarthy',1000,'imperdiet.erat.nonummy@luctus.com','VBP11KBT4QS',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (45,'Burton D. Vance',1000,'diam.nunc.ullamcorper@facilisisvitae.ca','IPP08UEN4FG',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (46,'Ina Norman',1000,'dui.nec@auctorodioa.org','VNM56IXT3DM',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (47,'Zoe O. Pitts',1000,'at.libero@imperdietnon.net','LIR10BLY5WW',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (48,'Ethan U. Bernard',1000,'elementum.lorem@maurisSuspendisse.co.uk','NQI24TLJ6PL',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (49,'Nicholas Holland',1000,'neque.vitae@vehiculaPellentesquetincidunt.net','AIK30KKT0VA',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (50,'Lev Oneal',1000,'eu.augue.porttitor@sapienCrasdolor.org','WIN23GSI2JF',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (51,'Chaney N. Levy',1000,'arcu.Sed@justo.co.uk','IKQ66VKI5JZ',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (52,'Candace Shepard',1000,'dolor.dolor@Class.edu','ISF40TSW1BV',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (53,'Ahmed P. Elliott',1000,'mi.Aliquam@odio.org','UMI69MHJ1HC',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (54,'Emily Jordan',1000,'vestibulum.neque.sed@Duis.org','CUW34YIS3QJ',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (55,'Moses Young',1000,'urna@eueratsemper.ca','SHX60QYW6MN',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (56,'Gary Horton',1000,'eget.dictum.placerat@Nullamsuscipitest.org','UNX60NZJ8JN',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (57,'Nelle Delacruz',1000,'Suspendisse@vestibulumMaurismagna.edu','QPG30KZJ4XH',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (58,'Fatima Q. Schroeder',1000,'porttitor@volutpatNullafacilisis.co.uk','XUQ50NZP0TC',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (59,'Ursa Wooten',1000,'neque@erat.co.uk','YJJ70FPT5IB',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (60,'Salvador Finch',1000,'est.vitae@atiaculisquis.org','JDM30DUM1XF',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (61,'Chanda Y. Briggs',1000,'ante@natoquepenatibuset.net','ZHM45OJQ9NM',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (62,'Zorita D. Norris',1000,'ornare.Fusce@eueuismodac.net','BWY39XXT9IO',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (63,'Hunter B. Blair',1000,'est.mauris.rhoncus@malesuadamalesuada.ca','AFJ33XCR6CW',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (64,'Boris T. Hensley',1000,'Aliquam.nec@duinec.ca','CSJ34OPN3DC',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (65,'Sacha Q. Dorsey',1000,'malesuada.vel@Cumsociisnatoque.co.uk','SVD73SZX7ZD',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (66,'Marsden J. Ellis',1000,'urna@ligulaNullam.org','UVZ55OUY8EI',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (67,'Chava X. Fischer',1000,'risus.odio@necmollisvitae.edu','LLP58YRD3RT',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (68,'Candace I. Knox',1000,'elementum.purus@DonecegestasDuis.ca','HBF67YOC8ZM',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (69,'MacKenzie Reynolds',1000,'quis.diam@mollisDuissit.edu','EAM96ARO3TH',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (70,'Katelyn Collins',1000,'est.Nunc@tellus.ca','JTS18HIR3MM',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (71,'Talon Baker',1000,'eu.elit.Nulla@anteMaecenasmi.co.uk','WMV93RRC1WV',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (72,'Samson Rosa',1000,'Nam.ligula@MorbimetusVivamus.edu','DUE35NRN8RB',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (73,'Octavius D. Tran',1000,'non.sapien@afacilisis.net','BXM45ZAC4RJ',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (74,'Tyrone Cherry',1000,'nunc@lobortisrisusIn.ca','YAI82ETS4YD',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (75,'Deanna W. Larsen',1000,'ridiculus.mus@atpretium.co.uk','JEE07VXL8LE',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (76,'Noelani C. Randall',1000,'consectetuer@tempor.co.uk','QED15KQK0DQ',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (77,'Craig Pierce',1000,'Mauris.blandit@velit.co.uk','SNF77NYW8UV',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (78,'Keegan M. Rojas',1000,'inceptos.hymenaeos.Mauris@Nuncpulvinararcu.org','SXJ30RDP7SY',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (79,'Amery Ratliff',1000,'amet.risus.Donec@Proinnonmassa.net','WDW00VZA6CY',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (80,'Miranda Y. Guerrero',1000,'risus.a@temporest.org','QJD70IRT1MN',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (81,'Urielle Odonnell',1000,'Curabitur.vel@Nullam.ca','KZZ56NLQ0BS',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (82,'Aaron U. Sanders',1000,'sapien.Aenean.massa@AeneanmassaInteger.org','FLL77XXL9FH',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (83,'Randall W. Doyle',1000,'volutpat.nunc@velarcu.com','AGE32DSQ0TI',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (84,'Tallulah E. Gamble',1000,'vel.mauris.Integer@tinciduntduiaugue.edu','DMQ01TPZ5BR',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (85,'Orlando Oneal',1000,'Curabitur.dictum@utipsumac.edu','JRW52LIB4UK',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (86,'Serena Shepard',1000,'ut@Duis.com','IDD62IHL2XZ',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (87,'Cleo M. Cox',1000,'mi.enim@auctorMauris.org','VRX63IZK1PS',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (88,'Sonya G. Gates',1000,'augue.eu.tempor@sedorcilobortis.ca','GHV87URX8NH',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (89,'Jack G. Reeves',1000,'viverra.Donec@elit.net','LAB28ZZY7ZV',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (90,'Gary Q. Craft',1000,'Sed.eu@nequeSed.net','OWO52MXM1WZ',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (91,'Eagan I. Johnson',1000,'libero.at@molestie.co.uk','GMH50CFB4CK',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (92,'Sopoline M. Foley',1000,'Donec@ridiculusmus.ca','ZNK25RNX6NN',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (93,'Pamela V. Gomez',1000,'et.nunc@Proinsed.com','MRS82BHZ6GT',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (94,'Colby S. Rush',1000,'vulputate.nisi@eget.edu','PIF42ZJB8UD',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (95,'Knox U. Terry',1000,'lectus@Curabiturconsequatlectus.net','GCQ02XKS1MK',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (96,'Christopher S. Mitchell',1000,'et.arcu@laciniavitaesodales.net','OVQ37AFI2RH',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (97,'Quin Weiss',1000,'eros.turpis@sapienAenean.com','HCU37OBT2BO',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (98,'Arden Humphrey',1000,'ultrices.posuere@eratsemper.net','JVY11NII7AA',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (99,'Davis Dillard',1000,'est@ligulaeuenim.edu','KZD29RWE4JJ',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (100,'Celeste Cook',1000,'dolor@fringilla.com','BAY20SLR6UG',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (101,'Kylynn Q. Schmidt',1000,'Etiam.bibendum.fermentum@metusInnec.net','XYC21JYG7BB',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (102,'Mariko Z. Collins',1000,'volutpat.ornare@orcilobortisaugue.ca','AFU52WQB4IH',4,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (103,'Cyrus V. Mcmahon',1000,'eros@ipsum.com','VLW92LSG9IF',3,2,1,1);
INSERT INTO employee (Employee_ID,EmployeeName,ClaimsAmountRemaining,email,Password,SupervisorID,DepHeadID,BCoordinatorID,EmployeeType_ID) VALUES (104,'Jenette Bright',1000,'mauris@tristiquesenectus.org','ZOX33CUK4QU',4,2,1,1);



commit;