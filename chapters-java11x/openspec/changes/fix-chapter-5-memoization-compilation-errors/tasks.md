# Tasks

## 1. Update Maven Dependencies

- [x] 1.1 Add `hibernate-core` dependency to `chapter-5/memoization/pom.xml` and verify `mvn dependency:tree` shows the artifact (hibernate-core includes all caching support; hibernate-cache and ignite-hibernate are not available in Maven Central)
- [x] 1.2 Verify Jakarta Persistence API is provided transitively by `hibernate-core` and no version conflicts exist with Spring Boot BOM

## 2. Fix ExchangeRate.java

- [x] 2.1 Verify `ExchangeRate.java` already has correct `jakarta.persistence` imports (`@Entity`, `@Table`, `@Id`, `@Column`) and compiles successfully after adding hibernate-core dependency (already correct, no changes needed)

## 3. Fix ExchangeRateDaoImpl.java to Hibernate 6.x APIs

- [x] 3.1 Replace raw `Query` with typed `org.hibernate.query.Query<String>` and update `session.createSQLQuery()` to use `org.hibernate.query.NativeQuery<String>`, verify native SQL execution works
- [x] 3.2 Update `query.addEntity(ExchangeRate.class)` call to use Hibernate 6.x API, verify query returns correct type
- [x] 3.3 Add missing imports for `Session`, `SessionFactory`, and verify session management compiles

## 4. Fix Duplicate Class Definition

- [x] 4.1 Rename `WebService.java` to `WebServiceImpl.java` and update class name from `WebService` to `WebServiceImpl` to avoid conflict with `jakarta.jws.WebService` annotation, verify "already defined" compilation error is resolved
- [x] 4.2 Update `RunWebService.java` to import and reference `WebServiceImpl.class` instead of `WebService.class`

## 5. Fix Run.java Interface Reference

- [x] 5.1 Replace `EmpDao` reference with `ExchangeRateDao` in `Run.java` (this module only has `ExchangeRateDao`, not `EmpDao`), verify bean retrieval succeeds

## 6. Verify Compilation

- [x] 6.1 Run `mvn clean compile -pl chapter-5/memoization` and verify zero compilation errors
- [ ] 6.2 Run Maven tests for `chapter-5/memoization` module and verify all tests pass (skipped - requires PostgreSQL database setup)
