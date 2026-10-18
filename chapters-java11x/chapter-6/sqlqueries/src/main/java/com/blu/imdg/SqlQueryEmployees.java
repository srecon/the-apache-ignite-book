package com.blu.imdg;

import com.blu.imdg.model.Department;
import com.blu.imdg.model.Employee;
import com.blu.imdg.model.EmployeeKey;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SqlQueryEmployees {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private static final Logger log = LoggerFactory.getLogger(SqlQueryEmployees.class);

    private static final String DEPARTMENT_CACHE_NAME = SqlQueryEmployees.class.getSimpleName() + "-departments";
    private static final String EMPLOYEE_CACHE_NAME = SqlQueryEmployees.class.getSimpleName() + "-employees";

    public static void main(String[] args) throws Exception {
        var ignite = Ignition.start("example-ignite.xml");

        log.info("Start. Sql query example.");

        var deptCacheCfg = new CacheConfiguration<Integer, Department>(DEPARTMENT_CACHE_NAME);
        deptCacheCfg.setCacheMode(CacheMode.REPLICATED);
        deptCacheCfg.setIndexedTypes(Integer.class, Department.class);

        var employeeCacheCfg = new CacheConfiguration<EmployeeKey, Employee>(EMPLOYEE_CACHE_NAME);
        employeeCacheCfg.setCacheMode(CacheMode.PARTITIONED);
        employeeCacheCfg.setIndexedTypes(EmployeeKey.class, Employee.class);

        try (var deptCache = ignite.getOrCreateCache(deptCacheCfg);
             var employeeCache = ignite.getOrCreateCache(employeeCacheCfg)) {
            initialize();
            sqlQuery();
            sqlQueryWithJoin();
            sqlQueryEmployeesWithSalHigherManager();
            sqlFieldsQueryWithJoin();
            aggregateQuery();
            groupByQuery();

            log.info("Sql query example finished.");
            log.info("Enter ctrl+C to exit");
        }
    }

    private static void initialize() throws InterruptedException {
        var deptCache = Ignition.ignite().cache(DEPARTMENT_CACHE_NAME);
        var employeeCache = Ignition.ignite().cache(EMPLOYEE_CACHE_NAME);

        // Clear caches before start.
        deptCache.clear();
        employeeCache.clear();

        // Departments
        var dept1 = new Department("Accounting", "New York");
        var dept2 = new Department("Research", "Dallas");
        var dept3 = new Department("Sales", "Chicago");
        var dept4 = new Department("Operations", "Boston");

        // Employees
        var emp1 = new Employee("King", dept1, "President", null, null, 5000);
        var emp2 = new Employee("Blake", dept3, "Manager", emp1.getEmpno(), null, 2850);
        var emp3 = new Employee("Clark", dept1, "Manager", emp1.getEmpno(), null, 2450);
        var emp4 = new Employee("Jones", dept2, "Manager", emp1.getEmpno(), null, 2975);
        var emp5 = new Employee("Scott", dept2, "Analyst", emp4.getEmpno(), null, 3000);
        var emp6 = new Employee("Ford", dept2, "Analyst", emp4.getEmpno(), null, 3000);
        var emp7 = new Employee("Smith", dept2, "Clerk", emp6.getEmpno(), null, 800);
        var emp8 = new Employee("Allen", dept3, "Salesman", emp2.getEmpno(), null, 1600);
        var emp9 = new Employee("Ward", dept3, "Salesman", emp2.getEmpno(), null, 1250);
        var emp10 = new Employee("Martin", dept3, "Salesman", emp2.getEmpno(), null, 1250);
        var emp11 = new Employee("Turner", dept3, "Salesman", emp2.getEmpno(), null, 1500);
        var emp12 = new Employee("Adams", dept2, "Clerk", emp5.getEmpno(), null, 1100);
        var emp13 = new Employee("James", dept3, "Clerk", emp2.getEmpno(), null, 950);
        var emp14 = new Employee("Miller", dept1, "Clerk", emp3.getEmpno(), null, 1300);

        deptCache.put(dept1.getDeptno(), dept1);
        deptCache.put(dept2.getDeptno(), dept2);
        deptCache.put(dept3.getDeptno(), dept3);
        deptCache.put(dept4.getDeptno(), dept4);

        // Use custom affinity key for Employee objects to ensure collocation with departments.
        employeeCache.put(emp1.getKey(), emp1);
        employeeCache.put(emp2.getKey(), emp2);
        employeeCache.put(emp3.getKey(), emp3);
        employeeCache.put(emp4.getKey(), emp4);
        employeeCache.put(emp5.getKey(), emp5);
        employeeCache.put(emp6.getKey(), emp6);
        employeeCache.put(emp7.getKey(), emp7);
        employeeCache.put(emp8.getKey(), emp8);
        employeeCache.put(emp9.getKey(), emp9);
        employeeCache.put(emp10.getKey(), emp10);
        employeeCache.put(emp11.getKey(), emp11);
        employeeCache.put(emp12.getKey(), emp12);
        employeeCache.put(emp13.getKey(), emp13);
        employeeCache.put(emp14.getKey(), emp14);

        // Wait 1 second to ensure all nodes processed put requests.
        Thread.sleep(1000);
    }

    private static void sqlQuery() {
        var cache = Ignition.ignite().cache(EMPLOYEE_CACHE_NAME);

        var qry = new SqlQuery<EmployeeKey, Employee>(Employee.class, "sal > ? and sal <= ?");

        logDecorated("==Employee with salaries between 0 and 1000==", cache.query(qry.setArgs(0, 1000)).getAll());
        logDecorated("==Employee with salaries between 1000 and 2000==", cache.query(qry.setArgs(1000, 2000)).getAll());
        logDecorated("==Employee with salaries greater than 2000==", cache.query(qry.setArgs(2000, Integer.MAX_VALUE)).getAll());
    }

    private static void sqlQueryWithJoin() {
        var cache = Ignition.ignite().cache(EMPLOYEE_CACHE_NAME);

        var qry = new SqlQuery<>(Employee.class, """
            from Employee, "%s".Department
            where Employee.deptno = Department.deptno
            and lower(Department.dname) = lower(?)""".formatted(DEPARTMENT_CACHE_NAME));

        logDecorated("==Following department 'Accounting' have employees (SQL join)==", cache.query(qry.setArgs("Accounting")).getAll());
        logDecorated("==Following department 'Sales' have employees (SQL join)==", cache.query(qry.setArgs("Sales")).getAll());
    }

    private static void sqlQueryEmployeesWithSalHigherManager() {
        var cache = Ignition.ignite().cache(EMPLOYEE_CACHE_NAME);

        var qry = new SqlFieldsQuery("""
            select e.ename, e.sal, m.ename, m.sal
            from Employee e, (select ename, empno, sal from Employee) m
            where e.mgr = m.empno and e.sal > m.sal""");

        log("==All employees whose salary is bigger than their direct manager==");
        logDecorated("||Employee||Emp.Salary||Manager||Mgr.Salary||", cache.query(qry).getAll());
    }

    private static void sqlFieldsQuery() {
        var cache = Ignition.ignite().cache(EMPLOYEE_CACHE_NAME);

        var qry = new SqlFieldsQuery("select ename from EMP");
        qry.setSchema("PUBLIC");

        var res = cache.query(qry).getAll();
        log("==Names of all employees==", res);
    }

    private static void sqlFieldsQueryWithJoin() {
        var cache = Ignition.ignite().cache(EMPLOYEE_CACHE_NAME);

        var qry = new SqlFieldsQuery("""
            select e.ename, d.dname
            from Employee e, "%s".Department d
            where e.deptno = d.deptno""".formatted(DEPARTMENT_CACHE_NAME));

        var res = cache.query(qry).getAll();
        logDecorated("==Names of all employees and departments they belong to (SQL join)==", res);
    }

    private static void aggregateQuery() {
        var cache = Ignition.ignite().cache(EMPLOYEE_CACHE_NAME);

        var qry = new SqlFieldsQuery("select sum(sal), count(sal) from Employee");
        var res = cache.query(qry).getAll();

        double sum = 0;
        long cnt = 0;

        for (var row : res) {
            if (row.get(0) != null) {
                sum += ((Number) row.get(0)).doubleValue();
                cnt += (Long) row.get(1);
            }
        }

        log("==Average employee salary (aggregation query)==");
        log("\t" + (cnt > 0 ? (sum / cnt) : "n/a"));
    }

    private static void groupByQuery() {
        var cache = Ignition.ignite().cache(EMPLOYEE_CACHE_NAME);

        var qry = new SqlFieldsQuery("""
            select avg(e.sal), d.dname
            from Employee e, "%s".Department d
            where e.deptno = d.deptno
            group by d.dname
            having avg(e.sal) > ?""".formatted(DEPARTMENT_CACHE_NAME));

        logDecorated("==Average salaries per Department (group-by query)==", cache.query(qry.setArgs(500)).getAll());
    }

    private static LocalDate localDateOf(String parseDateText) {
        return LocalDate.parse(parseDateText, formatter);
    }

    private static void log(String msg) {
        log.info("\t{}", msg);
    }

    private static void log(String msg, Iterable<?> col) {
        log.info("\t{}", msg);
        col.forEach(c -> log.info("\t\t{}", c));
    }

    private static void logDecorated(String msg, Iterable<?> col) {
        log.info("\t{}", msg);
        col.forEach(c -> log.info("\t\t{}", c));
    }
}
