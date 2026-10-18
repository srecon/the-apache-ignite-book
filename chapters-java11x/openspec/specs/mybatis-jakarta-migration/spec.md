# mybatis-jakarta-migration Specification

## Purpose
Migrate the MyBatis web services module from Java EE (`javax.*`) namespaces to Jakarta EE (`jakarta.*`) namespaces so the module compiles and runs on Jakarta EE 9+ application servers and modern dependency versions.

## Requirements

### Requirement: JAX-WS imports use Jakarta namespace

The `WebServices` class SHALL import JAX-WS annotations from `jakarta.jws` package instead of `javax.jws` package, ensuring compatibility with Jakarta EE 9+ where the Java EE packages were renamed.

#### Scenario: WebServices uses jakarta.jws.WebService

- **WHEN** `WebServices.java` is compiled
- **THEN** the import statement `import jakarta.jws.WebService;` is present and resolves successfully

#### Scenario: WebServices uses jakarta.jws.WebMethod

- **WHEN** `WebServices.java` is compiled
- **THEN** the import statement `import jakarta.jws.WebMethod;` is present and resolves successfully

#### Scenario: WebServices uses jakarta.jws.WebParam

- **WHEN** `WebServices.java` is compiled
- **THEN** the import statement `import jakarta.jws.WebParam;` is present and resolves successfully

### Requirement: MyBatis UserMapper is resolvable at compile time

The `UserMapper` interface SHALL be resolvable by the Maven compiler and Spring's `MapperScannerConfigurer` so that `UserServices` can inject it as a dependency without compilation errors.

#### Scenario: UserMapper interface exists and is on classpath

- **WHEN** the project is compiled with `mvn compile`
- **THEN** the file `com/blu/imdg/mapper/UserMapper.java` is found and compiled without "cannot find symbol: class UserMapper" errors

#### Scenario: UserMapper bean is registered by MapperScannerConfigurer

- **WHEN** the Spring context loads `spring-core.xml`
- **THEN** `MapperScannerConfigurer` scans `com.blu.imdg.mapper` and registers `UserMapper` as a bean named `userMapper`

### Requirement: Web service endpoint publishes correctly

The `RunWebService` entry point SHALL publish the web service endpoint using `jakarta.xml.ws.Endpoint` and make the WSDL available at the configured URL.

#### Scenario: Web service publishes on configured port

- **WHEN** `RunWebService.main()` executes
- **THEN** `Endpoint.publish("http://localhost:7001/invokeRules", services)` succeeds and logs "Server started on port 7001"

#### Scenario: WSDL is accessible at endpoint URL

- **WHEN** the web service is running
- **THEN** a GET request to `http://localhost:7001/invokeRules?wsdl` returns valid WSDL XML
