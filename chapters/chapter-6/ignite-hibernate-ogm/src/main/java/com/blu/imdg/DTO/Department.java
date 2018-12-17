package com.blu.imdg.DTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "DEPT")
public class Department implements Serializable{

    @Id
    @GeneratedValue
    private Integer deptno;

    private String dname;

    private String loc;

    public Department() {}

    public Department(String dname, String loc) {
        this.dname = dname;
        this.loc = loc;
    }

    public Integer getDeptno() {
        return deptno;
    }

    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(deptno, that.deptno) &&
                Objects.equals(dname, that.dname);
    }

    @Override
    public int hashCode() {

        return Objects.hash(deptno, dname);
    }

    @Override
    public String toString() {
        return "Department{" +
                "deptno=" + deptno +
                ", dname='" + dname + '\'' +
                ", loc='" + loc + '\'' +
                '}';
    }
}
