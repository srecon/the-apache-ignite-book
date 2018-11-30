SELECT Count(empno),
       b.deptno,
       dname
FROM   emp a,
       dept b
WHERE  a.deptno = b.deptno
GROUP  BY b.deptno,
          dname
ORDER BY b.deptno;


SELECT empno,
       ename,
       loc
FROM   emp a,
       dept b
WHERE  a.deptno = b.deptno
       AND loc = 'San Carlos';
