# Spec Delta

## Purpose

Define the requirements for Hibernate ORM integration with Apache Ignite, including entity mapping, cache configuration, and DAO pattern implementation with proper Jakarta Persistence API compliance.

## ADDED Requirements

### Requirement: Hibernate Dependencies Configuration

The system SHALL include all required Maven dependencies in `chapter-5/hibernate/pom.xml` to support Hibernate 6.x ORM, second-level caching, and Apache Ignite integration.

#### Scenario: Required dependencies are present
- **WHEN** Maven resolves dependencies for the hibernate module
- **THEN** the build includes `hibernate-core`, `hibernate-cache`, and `ignite-hibernate` artifacts

#### Scenario: Jakarta Persistence API is available
- **WHEN** the application compiles Jakarta Persistence annotations
- **THEN** `jakarta.persistence-api` is provided transitively by `hibernate-core`

### Requirement: Entity Mapping Compliance

The system SHALL use Jakarta Persistence annotations (`@Entity`, `@Table`, `@Id`, `@Column`) correctly for all entity classes (`Employee`, `ExchangeRate`).

#### Scenario: Employee entity is mapped correctly
- **WHEN** the application loads `Employee` entity
- **THEN** it maps to table `emp` with columns `empno`, `ename`, `job`, `mgr`, `hiredate`, `sal`, `deptno`, `comm`

#### Scenario: ExchangeRate entity is mapped correctly
- **WHEN** the application loads `ExchangeRate` entity
- **THEN** it maps to table `exchangerate` with columns `region`, `ratedate`, `usdollar`, `euro`, `gbp`

### Requirement: Second-Level Cache Integration

The system SHALL configure Hibernate second-level cache integration with Apache Ignite using `@Cacheable` and `@Cache` annotations with `READ_WRITE` concurrency strategy.

#### Scenario: Employee cache is configured
- **WHEN** the application initializes Hibernate session factory
- **THEN** `Employee` entity is marked as cacheable with `CacheConcurrencyStrategy.READ_WRITE`

### Requirement: DAO Pattern Implementation

The system SHALL implement data access operations using Hibernate 6.x APIs (`org.hibernate.query.Query`, `org.hibernate.query.NativeQuery`, `org.hibernate.query.criteria.CriteriaQuery`) instead of deprecated Hibernate 5.x APIs.

#### Scenario: Query employees list
- **WHEN** `EmpDaoImpl.getAllEmployees()` is called
- **THEN** it returns a list of `Employee` entities using `session.createQuery()` with HQL syntax

#### Scenario: Query employee by ID
- **WHEN** `EmpDaoImpl.getEmployeeById(int empno)` is called
- **THEN** it returns the `Employee` entity with the specified `empno` or null if not found

#### Scenario: Native SQL query for exchange rate
- **WHEN** `EmpDaoImpl.getExchangeRateByRegion(String region)` is called
- **THEN** it executes native SQL query using `session.createSQLQuery()` and returns the exchange rate as string

### Requirement: Type Compatibility

The system SHALL use `java.time.LocalDate` for date fields instead of `java.util.Date` to maintain type compatibility between entity definitions and DAO operations.

#### Scenario: Employee hire date uses LocalDate
- **WHEN** the `Employee` entity is instantiated or loaded
- **THEN** the `hireDate` field is of type `java.time.LocalDate`

#### Scenario: DAO creates employee with correct date type
- **WHEN** `EmpDaoImpl.create()` is called with date parameters
- **THEN** it sets the `hireDate` field using `LocalDate` without type conversion errors

### Requirement: Interface Contract Compliance

The system SHALL ensure `EmpDaoImpl` implements all methods defined in `EmpDao` interface with matching signatures.

#### Scenario: Create method signature matches interface
- **WHEN** `EmpDaoImpl.create(int empno, String ename, String job, Integer mgr)` is called
- **THEN** it successfully creates an employee record without abstract method compilation errors

### Requirement: No Duplicate Class Definitions

The system SHALL ensure each Java class is defined exactly once in the compilation unit.

#### Scenario: WebService class is defined once
- **WHEN** the Maven compiler processes source files
- **THEN** no duplicate class definition errors occur for `WebService` or any other class

## Impact

- Specifies behavior contracts for Hibernate integration
- Defines API compatibility requirements for Hibernate 6.x migration
- Establishes entity mapping and caching standards
