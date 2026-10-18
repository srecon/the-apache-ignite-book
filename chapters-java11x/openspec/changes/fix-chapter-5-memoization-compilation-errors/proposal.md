# Proposal

## Why

The `chapter-5/memoization` module fails to compile because it's missing Hibernate dependencies in its `pom.xml`, causing `jakarta.persistence` and `org.hibernate` packages to be unresolved. Additionally, the `WebService.java` class name conflicts with the `jakarta.jws.WebService` annotation (same error as the hibernate module fix), and `Run.java` references a missing `EmpDao` class that doesn't exist in this module. These issues prevent the memoization module from building.

## What Changes

- Add `hibernate-core` dependency to `chapter-5/memoization/pom.xml` to resolve `jakarta.persistence` and `org.hibernate` package errors
- Rename `WebService.java` to `WebServiceImpl.java` to avoid naming conflict with `jakarta.jws.WebService` annotation (same pattern as the hibernate module fix)
- Update `RunWebService.java` to reference `WebServiceImpl.class` instead of `WebService.class`
- Fix `Run.java` to use `ExchangeRateDao` instead of the non-existent `EmpDao` (this module only has `ExchangeRateDao`, not `EmpDao`)
- Update `ExchangeRateDaoImpl.java` to use Hibernate 6.x APIs (`NativeQuery` instead of raw `SQLQuery`, typed `Query` instead of raw `Query`)

## Capabilities

### New Capabilities
- `hibernate-memoization-integration`: Hibernate integration with Spring cache annotation support for exchange rate data persistence and caching

### Modified Capabilities
<!-- None - this is a new module fix, not modifying existing capabilities -->

## Impact

- **Affected code**: `chapter-5/memoization/pom.xml`, `ExchangeRate.java`, `ExchangeRateDaoImpl.java`, `Run.java`, `WebService.java` → `WebServiceImpl.java`, `RunWebService.java`
- **Dependencies**: Adds `hibernate-core` dependency (version managed by parent pom as `6.4.4.Final`)
- ** APIs**: No external API changes; internal class rename from `WebService` to `WebServiceImpl` (only used internally by `RunWebService`)
- **Build**: Module should compile successfully with Java 21 and Hibernate 6.4.4.Final
