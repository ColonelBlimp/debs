DROP TABLE IF EXISTS DEBS.ACCOUNT, DEBS.ENTRY, DEBS.TRANSACTION;

CREATE SCHEMA IF NOT EXISTS DEBS;

CREATE TABLE IF NOT EXISTS DEBS.ACCOUNT (
  ID IDENTITY GENERATED ALWAYS AS IDENTITY,
  CREATED SMALLDATETIME NOT NULL DEFAULT(CURRENT_TIMESTAMP()),
  BALANCE DECIMAL NOT NULL DEFAULT(0.00),
  DELETED BOOLEAN NOT NULL DEFAULT(FALSE),
  NAME VARCHAR(50) UNIQUE NOT NULL,
  DESCRIPTION VARCHAR(255) NOT NULL,
  PARENT_ID BIGINT NOT NULL,
  ACCOUNT_TYPE INT CHECK(ACCOUNT_TYPE >= 1 AND ACCOUNT_TYPE <= 8)
);

CREATE TABLE IF NOT EXISTS DEBS.ENTRY (
  ID IDENTITY GENERATED ALWAYS AS IDENTITY,
  CREATED SMALLDATETIME NOT NULL DEFAULT(CURRENT_TIMESTAMP()),
  DELETED BOOLEAN NOT NULL DEFAULT(FALSE),
  AMOUNT DECIMAL NOT NULL DEFAULT(0.00),
  ETYPE INT CHECK(ETYPE >= 1 AND ETYPE <= 2),
  ACCOUNT_ID BIGINT NOT NULL CHECK(ACCOUNT_ID > 0),
  CLEARED BOOLEAN NOT NULL DEFAULT(FALSE),
  CLEARED_TS SMALLDATETIME NOT NULL,
  FOREIGN KEY(ACCOUNT_ID) REFERENCES DEBS.ACCOUNT(ID)
);

CREATE TABLE IF NOT EXISTS DEBS.TRANSACTION (
  ID IDENTITY GENERATED ALWAYS AS IDENTITY,
  CREATED SMALLDATETIME NOT NULL DEFAULT(CURRENT_TIMESTAMP()),
  DELETED BOOLEAN NOT NULL DEFAULT(FALSE),
  TDATE DATE NOT NULL,
  REFERENCE VARCHAR(255) NOT NULL,
  NARRATIVE VARCHAR(255) NOT NULL,
  EID_FROM BIGINT NOT NULL,
  EID_TO BIGINT NOT NULL,
  CHECK(EID_FROM > 0),
  CHECK(EID_TO > 0),
  CHECK(EID_FROM != EID_TO),
  FOREIGN KEY(EID_FROM) REFERENCES DEBS.ENTRY(ID),
  FOREIGN KEY(EID_TO) REFERENCES DEBS.ENTRY(ID)
);

-- Balance: 1
INSERT INTO DEBS.ACCOUNT(NAME,DESCRIPTION,PARENT_ID,ACCOUNT_TYPE) VALUES('Balance', 'Build-In',0,8);
-- Net Worth: 2
INSERT INTO DEBS.ACCOUNT(NAME,DESCRIPTION,PARENT_ID,ACCOUNT_TYPE) VALUES('Net Worth', 'Build-In',1,8);
-- Assets: 3
INSERT INTO DEBS.ACCOUNT(NAME,DESCRIPTION,PARENT_ID,ACCOUNT_TYPE) VALUES('Assets', 'Build-In',2,8);
-- Liabilities: 4
INSERT INTO DEBS.ACCOUNT(NAME,DESCRIPTION,PARENT_ID,ACCOUNT_TYPE) VALUES('Liabilities', 'Build-In',2,8);
-- Income & Liabilities: 5
INSERT INTO DEBS.ACCOUNT(NAME,DESCRIPTION,PARENT_ID,ACCOUNT_TYPE) VALUES('Income & Liabilities', 'Build-In',1,8);
-- Income: 6
INSERT INTO DEBS.ACCOUNT(NAME,DESCRIPTION,PARENT_ID,ACCOUNT_TYPE) VALUES('Income', 'Build-In',5,8);
-- Expenses: 7
INSERT INTO DEBS.ACCOUNT(NAME,DESCRIPTION,PARENT_ID,ACCOUNT_TYPE) VALUES('Expenses', 'Build-In',5,8);
-- Opening Balance: 8
INSERT INTO DEBS.ACCOUNT(NAME,DESCRIPTION,PARENT_ID,ACCOUNT_TYPE) VALUES('Opening Balance', 'Build-In',1,6);
