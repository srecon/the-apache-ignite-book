CREATE TABLE dept
(
  deptno LONG,
  dname  VARCHAR,
  loc    VARCHAR,
  CONSTRAINT pk_dept PRIMARY KEY (deptno)
);

CREATE TABLE emp
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
);

CREATE INDEX ename_idx ON emp (ename);