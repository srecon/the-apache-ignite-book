# Proposal

## Why

The `chapter-5/mybatis` module fails to compile because it uses outdated Java EE namespaces (`javax.jws.*`) that are no longer available in Jakarta EE 9+, and the MyBatis mapper configuration references a `UserMapper` class that cannot be resolved during compilation. These issues prevent the module from building with modern Java tooling and Jakarta EE dependencies.

## What Changes

- **Migrate JAX-WS imports** in `WebServices.java` from `javax.jws.*` to `jakarta.jws.*` namespace (`@WebService`, `@WebMethod`, `@WebParam`)
- **Update `RunWebService.java`** to use `jakarta.xml.ws.Endpoint` (already done — verify consistency)
- **Ensure MyBatis dependencies** are correctly declared in `pom.xml` so that `UserMapper` interface can be resolved at compile time
- **Verify Spring configuration** in `spring-core.xml` correctly wires the `UserMapper` bean to `UserServices`

## Capabilities

### New Capabilities
- `mybatis-jakarta-migration`: Migrate MyBatis module from Java EE (`javax.*`) to Jakarta EE (`jakarta.*`) namespaces for JAX-WS web services

### Modified Capabilities
<!-- None — this is a fix, not a behavior change -->

## Impact

- **Code**: `chapter-5/mybatis/src/main/java/com/blu/imdg/WebServices.java` (JAX-WS imports), `chapter-5/mybatis/src/main/java/com/blu/imdg/RunWebService.java` (verify Jakarta consistency)
- **Dependencies**: `chapter-5/mybatis/pom.xml` (verify `jaxws-ri` dependency provides Jakarta namespaces)
- **Configuration**: `chapter-5/mybatis/src/main/resources/spring-core.xml` (verify `UserMapper` bean wiring)
- **No API changes**: The web service contract (WSDL) remains unchanged; only import statements are updated
