CREATE TABLE EMPLOYEETYPE
(
    EmployeeType_ID NUMBER NOT NULL,
    EmployeeTypeTitle VARCHAR (127),
    CONSTRAINT PK_EMPLOYEETYPE_ID PRIMARY KEY (EmployeeType_ID)
);
CREATE SEQUENCE emptype_seq START WITH 1 CACHE 20;

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

CREATE SEQUENCE emp_seq START WITH 1 CACHE 20;

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
CREATE SEQUENCE form_seq START WITH 1 CACHE 20;
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
CREATE SEQUENCE type_seq START WITH 1 CACHE 20;
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
CREATE SEQUENCE loc_seq START WITH 1 CACHE 20;
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
    Attachement_ID NUMBER NOT NULL,
    AttachmentPath VARCHAR2(127),
    CONSTRAINT PK_Attachment_ID PRIMARY KEY (Attachment_ID)
);
CREATE SEQUENCE att_seq START WITH 1 CACHE 20;
CREATE OR REPLACE TRIGGER att_keys 
BEFORE INSERT ON LOCATION
FOR EACH ROW
BEGIN
  SELECT att_seq.NEXTVAL
  INTO   :new.Attachent_ID
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
    Attachment_ID VARCHAR2(255),
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

commit;