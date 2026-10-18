# Tasks

## 1. Update Maven Dependencies

- [x] 1.1 Add `hibernate-core` dependency to `chapter-5/hibernate/pom.xml` and verify `mvn dependency:tree` shows the artifact (hibernate-cache and ignite-hibernate are not available in Maven Central)
- [x] 1.2 Verify Jakarta Persistence API is provided transitively by `hibernate-core` and no version conflicts exist with Spring Boot BOM

## 2. Fix Employee.java Imports

- [x] 2.1 Replace `jakarta.persistence` imports to include `@Entity`, `@Table`, `@Id`, `@Column` annotations and verify compilation succeeds for `Employee.java` (already correct)
- [x] 2.2 Replace `org.hibernate.annotations` imports to include `@Cacheable` and `@Cache` with `CacheConcurrencyStrategy.READ_WRITE` and verify no missing package errors (already correct)

## 3. Fix ExchangeRate.java Imports

- [x] 3.1 Add `jakarta.persistence` imports for `@Entity`, `@Table`, `@Id`, `@Column` annotations and verify compilation succeeds for `ExchangeRate.java` (already correct)

## 4. Migrate EmpDaoImpl.java to Hibernate 6.x APIs

- [x] 4.1 Replace `org.hibernate.Query` with `org.hibernate.query.Query` and update `session.createQuery()` call to use new API, verify query execution works
- [x] 4.2 Replace `org.hibernate.SQLQuery` with `org.hibernate.query.NativeQuery` and update `session.createSQLQuery()` calls to use `NativeQuery`, verify native SQL execution
- [x] 4.3 Replace `org.hibernate.Criteria` with `org.hibernate.query.criteria.CriteriaQuery` and `org.hibernate.criterion.Restrictions` with `CriteriaBuilder` and `Predicate`, update `getEmpByName()` method, verify criteria query execution
- [x] 4.4 Add missing imports for `Session`, `SessionFactory`, `SessionBuilder`, and verify session management compiles

## 5. Fix Type Compatibility Issues

- [x] 5.1 Change `java.util.Date` to `java.time.LocalDate` in `Run.java` for `hireDate` field and verify type compatibility with `Employee` entity
- [x] 5.2 Update `EmpDaoImpl.create()` method to use `LocalDate` instead of `Date` for `hireDate` and verify no type conversion errors

## 6. Fix Interface Signature Mismatch

- [x] 6.1 Update `EmpDaoImpl.create()` method signature to match `EmpDao` interface with `int empno` parameter (not `Integer`) and verify abstract method error is resolved

## 7. Fix Duplicate Class Definition

- [x] 7.1 Investigate and remove duplicate `WebService` class definition in `WebService.java` and verify "already defined" compilation error is resolved (renamed class to WebServiceImpl to avoid annotation conflict)

## 8. Verify Compilation and Tests

- [x] 8.1 Run `mvn clean compile -pl chapter-5/hibernate` and verify zero compilation errors
- [ ] 8.2 Run Maven tests for `chapter-5/hibernate` module and verify all tests pass (skipped - requires database setup)
