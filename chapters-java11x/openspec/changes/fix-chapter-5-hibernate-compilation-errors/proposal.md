# Proposal

## Why

The `chapter-5/hibernate` module fails to compile due to missing Hibernate dependencies and incompatible API usage between Hibernate 5.x and 6.x. The code uses deprecated Hibernate 5.x APIs (such as `org.hibernate.Query`, `Criteria`, and `SQLQuery`) which were removed or significantly refactored in Hibernate 6.x, and lacks required dependencies for Jakarta Persistence API and Hibernate cache integration. This prevents the chapter-5 examples from being buildable and testable.

## What Changes

- **Add missing Maven dependencies** to `chapter-5/hibernate/pom.xml`:
  - `hibernate-core` for Jakarta Persistence API and ORM functionality
  - `hibernate-cache` for second-level cache integration with Apache Ignite
  - `ignite-hibernate` connector for Ignite-Hibernate integration

- **Update import statements** in source files to use Hibernate 6.x APIs:
  - Replace `org.hibernate.Query` with `org.hibernate.query.Query`
  - Replace `org.hibernate.SQLQuery` with `org.hibernate.query.NativeQuery`
  - Replace `org.hibernate.Criteria` with `org.hibernate.query.criteria.CriteriaQuery`
  - Replace `org.hibernate.criterion.Restrictions` with `jakarta.persistence.criteria.Predicate`
  - Add missing `jakarta.persistence` imports for `@Entity`, `@Table`, `@Id`, `@Column`

- **Fix method signature incompatibilities**:
  - Update `EmpDaoImpl.create()` to match the interface method signature with `int empno` parameter
  - Change `java.util.Date` to `java.time.LocalDate` for `hireDate` field in `Run.java`

- **Fix duplicate class definition** in `WebService.java` (line 24: "is already defined in this compilation unit")

## Capabilities

### New Capabilities
- `hibernate-integration`: Hibernate ORM integration with Apache Ignite including cache configuration, entity mapping, and DAO pattern implementation

## Impact

- **Files modified**:
  - `chapter-5/hibernate/pom.xml` - Add Hibernate dependencies
  - `chapter-5/hibernate/src/main/java/com/blu/imdg/dto/Employee.java` - Update imports
  - `chapter-5/hibernate/src/main/java/com/blu/imdg/dto/ExchangeRate.java` - Update imports
  - `chapter-5/hibernate/src/main/java/com/blu/imdg/EmpDaoImpl.java` - Migrate to Hibernate 6.x APIs
  - `chapter-5/hibernate/src/main/java/com/blu/imdg/Run.java` - Fix Date to LocalDate conversion
  - `chapter-5/hibernate/src/main/java/com/blu/imdg/ws/WebService.java` - Fix duplicate class definition

- **Dependencies added**: hibernate-core, hibernate-cache, ignite-hibernate
- **Build system**: Maven compilation must succeed for `hibernate` module
- **Runtime**: No API changes for consumers; internal Hibernate API migration only
