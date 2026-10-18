# Design

## Context

The `chapter-5/hibernate` module currently fails to compile because it references Hibernate 5.x APIs that were removed or refactored in Hibernate 6.x. The project uses Java 21 with Spring Boot 3.2.4, which requires Hibernate 6.x. The codebase uses deprecated classes like `org.hibernate.Query`, `org.hibernate.SQLQuery`, `org.hibernate.Criteria`, and `org.hibernate.criterion.Restrictions`.

## Goals / Non-Goals

**Goals:**
- Add missing Maven dependencies for Hibernate 6.x ORM, caching, and Ignite integration
- Migrate all source files to use Hibernate 6.x APIs
- Ensure Maven compilation succeeds for the `hibernate` module
- Maintain existing functionality and API contracts

**Non-Goals:**
- Refactoring the DAO pattern or business logic
- Changing the database schema or data definitions
- Adding new features or capabilities
- Updating other chapter modules (chapter-2, chapter-6, etc.)

## Decisions

### Decision 1: Use Hibernate 6.x Native APIs

**Choice:** Migrate from Hibernate 5.x APIs to Hibernate 6.x query APIs.

**Rationale:** Spring Boot 3.x requires Hibernate 6.x, which removed deprecated classes:
- `org.hibernate.Query` → `org.hibernate.query.Query` (generic, type-safe)
- `org.hibernate.SQLQuery` → `org.hibernate.query.NativeQuery` (renamed for clarity)
- `org.hibernate.Criteria` → `org.hibernate.query.criteria.CriteriaQuery` (part of JPA criteria API)
- `org.hibernate.criterion.Restrictions` → `jakarta.persistence.criteria.CriteriaBuilder` with `Predicate`

**Alternatives considered:**
1. Downgrade to Hibernate 5.x - Rejected: Incompatible with Spring Boot 3.x and Jakarta EE namespaces
2. Keep Hibernate 5.x APIs with adapters - Rejected: Adds unnecessary complexity and maintenance burden

### Decision 2: Add Explicit Hibernate Dependencies

**Choice:** Add three explicit dependencies to `chapter-5/hibernate/pom.xml`:
- `hibernate-core`: Provides Jakarta Persistence API, ORM engine, and Hibernate 6.x APIs
- `hibernate-cache`: Provides second-level cache integration with Apache Ignite
- `ignite-hibernate`: Provides the Ignite-Hibernate connector for distributed caching

**Rationale:** The current `pom.xml` only declares Ignite dependencies and Spring ORM, but lacks explicit Hibernate dependencies. Maven's dependency resolution may not pull in the correct versions without explicit declarations.

**Alternatives considered:**
1. Rely on transitive dependencies from `spring-orm` - Rejected: May pull inconsistent Hibernate versions; explicit declaration ensures correctness

### Decision 3: Use java.time.LocalDate for Date Fields

**Choice:** Use `java.time.LocalDate` for `hireDate` field in `Employee` entity and `rateDate` in `ExchangeRate` entity.

**Rationale:** Java 8+ `java.time` API is the modern, recommended approach for date handling. The `Run.java` file currently uses `java.util.Date` which causes type incompatibility. Changing the DAO to use `LocalDate` eliminates the need for date conversion and aligns with Java 21 best practices.

**Alternatives considered:**
1. Keep `java.util.Date` - Rejected: Legacy API, type incompatibility with entity definition, discouraged in modern Java

### Decision 4: Fix Duplicate Class Definition

**Choice:** Remove or rename the duplicate `WebService` class definition.

**Rationale:** The compilation error indicates `WebService.java` has a duplicate class definition on line 24. This is likely a copy-paste error or accidental class duplication within the file. The fix is to remove the duplicate definition while preserving the intended functionality.

**Alternatives considered:**
1. Rename one of the duplicates - Rejected: Without understanding the intent, renaming may break functionality; removal is safer

## Risks / Trade-offs

| Risk | Mitigation |
|------|------------|
| Hibernate 6.x API changes may affect query behavior | Test all DAO methods after migration; verify query results match expected output |
| Adding explicit dependencies may version-conflict with Spring Boot BOM | Use Spring Boot BOM managed versions; validate with `mvn dependency:tree` |
| Date type change may affect existing database records | `LocalDate` maps correctly to SQL DATE columns in Hibernate 6.x; no migration needed |
| Duplicate class definition may indicate deeper structural issue | Investigate root cause; if duplication was intentional (e.g., overloaded class), refactor to separate classes |

## Migration Plan

1. **Update `pom.xml`**: Add `hibernate-core`, `hibernate-cache`, and `ignite-hibernate` dependencies
2. **Update imports**: Replace all Hibernate 5.x imports with Hibernate 6.x equivalents in `Employee.java`, `ExchangeRate.java`, and `EmpDaoImpl.java`
3. **Fix API calls**: Update query creation, criteria building, and parameter setting to use Hibernate 6.x methods
4. **Fix type compatibility**: Change `java.util.Date` to `java.time.LocalDate` in `Run.java` and `EmpDaoImpl.java`
5. **Fix duplicate class**: Remove duplicate `WebService` class definition
6. **Verify compilation**: Run `mvn clean compile -pl chapter-5/hibernate` to confirm success
7. **Run tests**: Execute module tests to verify functionality

## Open Questions

- None identified. All technical decisions are resolved based on current code analysis and requirements.
