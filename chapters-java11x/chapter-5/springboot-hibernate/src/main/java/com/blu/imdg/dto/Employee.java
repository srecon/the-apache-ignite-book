package com.blu.imdg.dto;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table( name = "emp" )
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Employee implements Serializable{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "empno")
    private Integer empno;
    @Column(name = "ename")
    private String ename;
    @Column(name = "job")
    private String job;
    @Column(name = "mgr")
    private Integer mgr;
    @Column(name = "hiredate")
    private Date    date;
    @Column(name = "sal")
    private Integer sal;
    @Column(name = "deptno")
    private Integer deptno;
    @Column(name = "comm")
    private Integer comm;

    public Employee() {
    }

    public Integer getEmpno() {
        return empno;
    }

    public void setEmpno(Integer empno) {
        this.empno = empno;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Integer getMgr() {
        return mgr;
    }

    public void setMgr(Integer mgr) {
        this.mgr = mgr;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getSal() {
        return sal;
    }

    public void setSal(Integer sal) {
        this.sal = sal;
    }

    public Integer getDeptno() {
        return deptno;
    }

    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

    public Integer getComm() {
        return comm;
    }

    public void setComm(Integer comm) {
        this.comm = comm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(empno, employee.empno) &&
                Objects.equals(ename, employee.ename);
    }

    @Override
    public int hashCode() {

        return Objects.hash(empno, ename);
    }
}
