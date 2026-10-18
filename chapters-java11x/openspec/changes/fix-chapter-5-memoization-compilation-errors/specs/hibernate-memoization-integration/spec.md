# Spec Delta

## Purpose

Defines the requirements for Hibernate integration with Spring cache annotations to persist and cache exchange rate data in a PostgreSQL database, ensuring the memoization module compiles and functions correctly with Java 21 and Hibernate 6.x.

## ADDED Requirements

### Requirement: Hibernate dependencies must be resolved

The system MUST include `hibernate-core` as a Maven dependency in the memoization module's `pom.xml`, providing `jakarta.persistence` and `org.hibernate` packages for compilation with Java 21 and Hibernate 6.4.4.Final.

#### Scenario: Maven dependency tree includes hibernate-core
- **WHEN** `mvn dependency:tree` is run on the memoization module
- **THEN** the output includes `org.hibernate.orm:hibernate-core` with version `6.4.4.Final`

#### Scenario: Jakarta Persistence API is available transitively
- **WHEN** the memoization module compiles
- **THEN** `jakarta.persistence` package classes (e.g., `@Entity`, `@Table`, `@Id`, `@Column`) are resolvable without explicit dependency declaration

### Requirement: ExchangeRate entity must persist correctly

The system MUST define `ExchangeRate` as a JPA entity with `@Entity` and `@Table` annotations, mapping to the `exchangerate` table with fields for `region` (primary key), `rateDate`, `usdollar`, `euro`, and `gbp`.

#### Scenario: ExchangeRate entity maps to database table
- **WHEN** the application initializes the Hibernate session factory
- **THEN** `ExchangeRate` is mapped to the `exchangerate` table with correct column mappings

#### Scenario: ExchangeRate composite key uses region and rateDate
- **WHEN** an `ExchangeRate` object is persisted
- **THEN** the database row uses `region` and `rateDate` as the composite identifier

### Requirement: ExchangeRateDao must provide data access with Hibernate 6.x APIs

The system MUST implement `ExchangeRateDao` interface using Hibernate 6.x APIs (`org.hibernate.query.Query`, `org.hibernate.query.NativeQuery`) to execute database queries for exchange rate data.

#### Scenario: Get USD dollar exchange rate by region
- **WHEN** `getUSDollarExchangeRateByRegion("Moscow")` is called
- **THEN** the system executes a native SQL query and returns the USD dollar rate for the specified region on the hardcoded date `2015-05-02`

#### Scenario: Update exchange rate persists to database
- **WHEN** `updateExchange(ExchangeRate)` is called
- **THEN** the system executes an SQL UPDATE statement and commits the transaction

### Requirement: Spring cache annotations must integrate with Hibernate

The system MUST use Spring cache annotations (`@Cacheable`, `@CacheEvict`) on `ExchangeRateDaoImpl` methods to cache exchange rate data in an Ignite-backed cache, reducing database queries.

#### Scenario: Get method caches exchange rate
- **WHEN** `getUSDollarExchangeRateByRegion` is called with a region
- **THEN** the result is stored in the Ignite cache keyed by "exchangeRate"

#### Scenario: Update method evicts cached data
- **WHEN** `updateExchange` is called
- **THEN** the corresponding entry in the "exchangeRate" cache is evicted

### Requirement: Web service must expose exchange rate operations

The system MUST provide a JAX-WS web service (`WebServiceImpl`) that exposes `getExchangeRateByRegion` and `updateExchangeRate` operations, allowing external clients to query and update exchange rates.

#### Scenario: Web service WSDL is accessible
- **WHEN** the application starts and publishes the web service
- **THEN** the WSDL is available at `http://localhost:7001/invokeRules?wsdl`

#### Scenario: Web service returns exchange rate for region
- **WHEN** a client calls `getExchangeRateByRegion` with a region name
- **THEN** the system returns the USD dollar exchange rate as a string

#### Scenario: Web service updates exchange rate
- **WHEN** a client calls `updateExchangeRate` with region, date, and rate values
- **THEN** the system persists the updated exchange rate to the database

### Requirement: Application entry point must run successfully

The system MUST provide a `Run` class that initializes the Spring context, retrieves an `ExchangeRateDao` bean, and executes a sample exchange rate update operation.

#### Scenario: Application starts and updates exchange rate
- **WHEN** `Run.main()` is executed
- **THEN** the application initializes Spring, updates an exchange rate, and logs the result
