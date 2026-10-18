package com.blu.imdg.model;

import java.util.Objects;
import org.apache.ignite.cache.affinity.AffinityKeyMapped;

/**
 * Affinity key for Employee collocation with Department.
 */
public record EmployeeKey(
    int empNo,
    @AffinityKeyMapped int deptNo
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeKey key)) return false;
        return empNo == key.empNo && deptNo == key.deptNo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empNo, deptNo);
    }

    @Override
    public String toString() {
        return "EmployeeKey[empNo=" + empNo + ", deptNo=" + deptNo + ']';
    }
}
