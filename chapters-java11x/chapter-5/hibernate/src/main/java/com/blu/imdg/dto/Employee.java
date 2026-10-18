/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blu.imdg.dto;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.LocalDate;

/**
 * Employee entity with Hibernate cache integration.
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "emp")
public class Employee {
    @Id
    @Column(name = "empno")
    private Integer empno;

    @Column(name = "ename")
    private String ename;

    @Column(name = "job")
    private String job;

    @Column(name = "mgr")
    private Integer mgr;

    @Column(name = "hiredate")
    private LocalDate hireDate;

    @Column(name = "sal")
    private Integer sal;

    @Column(name = "deptno")
    private Integer deptno;

    @Column(name = "comm")
    private Integer comm;

    public Employee() {
    }

    public Employee(Integer empno, String ename, String job, Integer mgr, LocalDate hireDate, Integer sal, Integer deptno, Integer comm) {
        this.empno = empno;
        this.ename = ename;
        this.job = job;
        this.mgr = mgr;
        this.hireDate = hireDate;
        this.sal = sal;
        this.deptno = deptno;
        this.comm = comm;
    }

    public Integer getEmpno() { return empno; }
    public void setEmpno(Integer empno) { this.empno = empno; }
    public String getEname() { return ename; }
    public void setEname(String ename) { this.ename = ename; }
    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }
    public Integer getMgr() { return mgr; }
    public void setMgr(Integer mgr) { this.mgr = mgr; }
    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    public Integer getSal() { return sal; }
    public void setSal(Integer sal) { this.sal = sal; }
    public Integer getDeptno() { return deptno; }
    public void setDeptno(Integer deptno) { this.deptno = deptno; }
    public Integer getComm() { return comm; }
    public void setComm(Integer comm) { this.comm = comm; }

    @Override
    public String toString() {
        return "Employee{empno=%d, ename='%s', job='%s', sal=%d}".formatted(empno, ename, job, sal);
    }
}
