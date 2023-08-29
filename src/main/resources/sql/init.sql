create table E_THIRD_TIME(
  id                 NUMBER,
  type               VARCHAR2(80),
  propel_time        DATE default SYSDATE,
  remark             VARCHAR2(200),
  brand              VARCHAR2(20)
);

-- Create table
create table E_SCHEDULEJOB
(
  id             NUMBER(10) not null primary key,
  ad_client_id   NUMBER(10),
  ad_org_id      NUMBER(10),
  ownerid        NUMBER(10),
  modifierid     NUMBER(10),
  creationdate   DATE,
  modifieddate   DATE,
  isactive       CHAR(1) default 'Y',
  jobname        VARCHAR2(200),
  groupname      VARCHAR2(200),
  cronexpression VARCHAR2(200),
  status         CHAR(1) default 'Y',
  runstatus      VARCHAR2(10),
  description    VARCHAR2(2000)
);
-- Add comments to the columns
comment on column E_SCHEDULEJOB.status is '启用状态：N：未启用；Y：启用';
comment on column E_SCHEDULEJOB.runstatus is '运行状态：NONE：无；NORMAL：正常状态；PAUSED：暂停状态；COMPLETE：完成；ERROR：错误；BLOCKED：堵塞';
comment on column E_SCHEDULEJOB.cronexpression is '表达式';
-- Create/Recreate indexes
create unique index IDX_SCHEDULEJOB_NAME on E_SCHEDULEJOB (JOBNAME, GROUPNAME);


-- 发送日志
-- Create table
create table C_INTERFACE_SENDLOG
(
    id             NUMBER(10) not null primary key,
    ad_client_id   NUMBER(10),
    ad_org_id      NUMBER(10),
    ownerid        NUMBER(10),
    modifierid     NUMBER(10),
    creationdate   DATE,
    modifieddate   DATE,
    isactive       CHAR(1) default 'Y',
    application    VARCHAR2(255),
    name           VARCHAR2(255),
    url            VARCHAR2(800),
    method         VARCHAR2(255),
    dealdate       NUMBER(8),
    dealtime       DATE,
    source         VARCHAR2(255),
    source_id      NUMBER(20),
    request_id     VARCHAR2(255),
    data           CLOB,
    result         CHAR(1),
    result_message CLOB,
    error_message  CLOB
);

-- 接受日志
-- Create table
create table C_INTERFACE_RECEIVELOG
(
    id             NUMBER(10) not null primary key,
    ad_client_id   NUMBER(10),
    ad_org_id      NUMBER(10),
    ownerid        NUMBER(10),
    modifierid     NUMBER(10),
    creationdate   DATE,
    modifieddate   DATE,
    isactive       CHAR(1) default 'Y',
    application    VARCHAR2(255),
    name           VARCHAR2(255),
    url            VARCHAR2(800),
    method         VARCHAR2(255),
    dealdate       NUMBER(8),
    dealtime       DATE,
    source         VARCHAR2(255),
    source_id      NUMBER(20),
    request_id     VARCHAR2(255),
    data           CLOB,
    result         CHAR(1),
    result_message CLOB,
    error_message  CLOB
);

-- AccessToken表
-- Create table
create table E_ACCESS_TOKEN
(
    id           NUMBER(10) not null primary key,
    app_id       VARCHAR2(80) not null,
    app_secret   VARCHAR2(200) not null,
    access_token VARCHAR2(800),
    expire_in    NUMBER(10),
    date_begin   DATE,
    date_end     DATE
);
-- Create/Recreate indexes
create unique index UDX_ACCESS_TOKEN_001 on E_ACCESS_TOKEN (APP_ID);