CREATE TABLE IF NOT EXISTS EMP
  (
     empno    LONG,
     ename    VARCHAR,
     job      VARCHAR,
     mgr      INTEGER,
     hiredate DATE,
     sal      LONG,
     comm     LONG,
     deptno   LONG,
     CONSTRAINT pk_emp PRIMARY KEY (empno)
  ) WITH "template=partitioned,CACHE_NAME=EMPcache";

CREATE TABLE IF NOT EXISTS DEPT
  (
     deptno LONG,
     dname  VARCHAR,
     loc    VARCHAR,
     CONSTRAINT pk_dept PRIMARY KEY (deptno)
  ) WITH "template=partitioned,CACHE_NAME=DEPTcache";
