# Design

## Context

The `chapter-5/memoization` module uses Hibernate with Spring cache annotations to persist and cache exchange rate data. It currently fails to compile because:
1. Missing `hibernate-core` dependency in `pom.xml` (parent pom manages version `6.4.4.Final`)
2. `WebService.java` class name conflicts with `jakarta.jws.WebService` annotation
3. `Run.java` references a non-existent `EmpDao` class (this module only has `ExchangeRateDao`)
4. `ExchangeRateDaoImpl.java` uses raw Hibernate types instead of Hibernate 6.x typed APIs

The module depends on PostgreSQL, Spring ORM, JAX-WS (Jakarta), and Apache Ignite. The parent project (`chapters-java11x`) already configures Java 21, Spring Boot 3.2.4, and Hibernate 6.4.4.Final.

## Goals / Non-Goals

**Goals:**
- Add `hibernate-core` dependency to resolve compilation errors
- Rename `WebService` to `WebServiceImpl` to avoid annotation conflict (same pattern as hibernate module)
- Fix `Run.java` to use `ExchangeRateDao` instead of `EmpDao`
- Migrate `ExchangeRateDaoImpl.java` to Hibernate 6.x typed APIs (`NativeQuery`, typed `Query`)
- Ensure the module compiles successfully with Java 21

**Non-Goals:**
- Database schema changes or migration scripts
- Web service client implementation or testing
- Performance optimization of Hibernate queries
- Adding unit tests for the DAO layer (requires database setup)

## Decisions

### Decision 1: Add hibernate-core dependency (not hibernate-cache or ignite-hibernate)

**Rationale:** `hibernate-cache` and `ignite-hibernate` are not available in Maven Central. The `hibernate-core` artifact includes all caching support needed (Second Level Cache API is part of core since Hibernate 5.x). The Ignite integration is handled separately via Spring cache abstraction (`@Cacheable`, `@CacheEvict`) configured in `spring-core.xml`.

**Alternatives considered:**
- Using `ignite-hibernate` from a private repository: Rejected because it's not publicly available and would require custom repository configuration.
- Relying only on Spring ORM without Hibernate: Rejected because the module explicitly uses Hibernate session management and native SQL queries.

### Decision 2: Rename WebService to WebServiceImpl

**Rationale:** The class name `WebService` conflicts with the `jakarta.jws.WebService` annotation imported in the same file. Renaming to `WebServiceImpl` follows the same pattern used in the `hibernate` module fix and avoids the "already defined in this compilation unit" error.

**Alternatives considered:**
- Using fully qualified annotation name (`@jakarta.jws.WebService`) in imports: Rejected because it's verbose and the class name conflict would still cause issues.
- Moving the class to a different package: Rejected because it would require updating all references and doesn't solve the root cause.

### Decision 3: Fix Run.java to use ExchangeRateDao

**Rationale:** The `Run.java` file references `EmpDao.class` which doesn't exist in the memoization module. This module only has `ExchangeRateDao` interface and `ExchangeRateDaoImpl` implementation. The `Run` class should use `ExchangeRateDao` to interact with the exchange rate data.

**Alternatives considered:**
- Adding a stub `EmpDao` interface: Rejected because it would be dead code and confusing.
- Copying `EmpDao` from the hibernate module: Rejected because the memoization module is specifically for exchange rate caching, not employee data.

### Decision 4: Migrate to Hibernate 6.x typed APIs

**Rationale:** Hibernate 6.x requires typed `Query<T>` and `NativeQuery<T>` instead of raw types. The `ExchangeRateDaoImpl.java` uses `session.createSQLQuery()` which returns `NativeQuery<T>` in Hibernate 6.x. The `query.addEntity()` method is still supported but the query variable should be typed.

**Alternatives considered:**
- Using Hibernate Criteria API: Rejected because the existing code uses native SQL queries which are more appropriate for this use case (direct table access for performance).
- Switching to JPA CriteriaQuery: Rejected because native SQL is already used and works correctly; no benefit to migration.

## Risks / Trade-offs

[Risk: Missing Spring cache configuration] → The module uses `@Cacheable` and `@CacheEvict` annotations, which require Ignite-backed cache configuration in `spring-core.xml`. If the Spring configuration is incorrect, caching won't work. **Mitigation:** Verify `spring-core.xml` has proper cache manager configuration pointing to Ignite.

[Risk: PostgreSQL database not available] → The module requires a running PostgreSQL database with the `exchangerate` table. **Mitigation:** This is expected for runtime; compilation doesn't require the database.

[Risk: Hibernate version conflicts] → The parent pom manages Hibernate 6.4.4.Final, but Ignite 2.16.0 may have transitive dependencies on older Hibernate versions. **Mitigation:** Run `mvn dependency:tree` to verify no version conflicts; use Maven BOM import to manage versions.

[Risk: JAX-WS Jakarta namespace changes] → The `jaxws-ri` dependency (version 4.0.2) provides Jakarta namespace JAX-WS APIs. **Mitigation:** This dependency is already correctly configured in `pom.xml`.

## Migration Plan

### Steps to deploy:
1. Add `hibernate-core` dependency to `chapter-5/memoization/pom.xml`
2. Rename `WebService.java` to `WebServiceImpl.java` and update class name
3. Update `RunWebService.java` to reference `WebServiceImpl.class`
4. Fix `Run.java` to use `ExchangeRateDao` instead of `EmpDao`
5. Update `ExchangeRateDaoImpl.java` to use Hibernate 6.x typed APIs
6. Run `mvn clean compile -pl chapter-5/memoization` to verify zero compilation errors

### Rollback strategy:
- This change only adds dependencies and fixes compilation errors
- No runtime behavior changes (except class rename, which is internal)
- Revert the commit to restore original state

## Open Questions

- Does the `spring-core.xml` configuration need updates for Hibernate 6.x Second Level Cache with Ignite? (Can be answered during implementation by testing cache behavior)
- Are there any Ignite 2.16.0 compatibility issues with Hibernate 6.4.4.Final? (Can be answered during integration testing)
