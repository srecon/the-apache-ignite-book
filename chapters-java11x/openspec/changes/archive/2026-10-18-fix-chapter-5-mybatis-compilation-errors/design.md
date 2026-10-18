# Design

## Context

The `chapter-5/mybatis` module uses Java EE 8 namespaces (`javax.jws.*`) for JAX-WS web services. Jakarta EE 9+ renamed all Java EE packages from `javax.*` to `jakarta.*`. The project's `pom.xml` already declares `jaxws-ri` version 4.0.2 (a Jakarta EE 9+ implementation), but the source code still imports `javax.jws.WebService`, `javax.jws.WebMethod`, and `javax.jws.WebParam`, causing "package does not exist" compilation errors.

Additionally, the `UserMapper` interface at `com.blu.imdg.mapper.UserMapper` exists in source but fails to resolve during compilation, suggesting either a missing MyBatis dependency or a Spring configuration issue where `MapperScannerConfigurer` does not register the mapper bean correctly.

## Goals / Non-Goals

**Goals:**
- Migrate all JAX-WS imports from `javax.jws.*` to `jakarta.jws.*` in `WebServices.java`
- Ensure `UserMapper` compiles and is registered as a Spring bean by `MapperScannerConfigurer`
- Verify the module compiles with zero errors

**Non-Goals:**
- No changes to the web service contract (WSDL, operations, parameters)
- No changes to MyBatis mapper XML (`UserMapper.xml`)
- No changes to Spring configuration structure (only verification)
- No database integration testing

## Decisions

### Decision: Direct import replacement (javax → jakarta)

**Choice:** Replace `javax.jws.*` imports with `jakarta.jws.*` imports in `WebServices.java` only.

**Rationale:** The `jaxws-ri` dependency (version 4.0.2) already provides Jakarta EE 9+ namespaces. No dependency changes are needed — only source code updates. This is a mechanical find-and-replace:
- `import javax.jws.WebService;` → `import jakarta.jws.WebService;`
- `import javax.jws.WebMethod;` → `import jakarta.jws.WebMethod;`
- `import javax.jws.WebParam;` → `import jakarta.jws.WebParam;`

**Alternatives considered:**
- Add a separate `javax.xml.ws` dependency — rejected because `jaxws-ri:4.0.2` already provides Jakarta namespaces, and mixing javax/jakarta namespaces causes classpath conflicts.
- Create a separate Jakarta-compatible module — rejected because this is a simple namespace migration, not a compatibility layer problem.

### Decision: Verify UserMapper resolution via MapperScannerConfigurer

**Choice:** Do not modify `UserMapper.java` or `UserMapper.xml`. Instead, verify that `MapperScannerConfigurer` in `spring-core.xml` correctly registers the mapper as a Spring bean.

**Rationale:** The `UserMapper` interface already exists at `com.blu.imdg.mapper.UserMapper`. The `spring-core.xml` configures:
```xml
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="basePackage" value="com.blu.imdg.mapper" />
</bean>
```
This should auto-register `UserMapper` as a bean named `userMapper` (lowercase), which `UserServices` references via `<property name="userMapper" ref="userMapper"/>`. The compilation error "cannot find symbol: class UserMapper" likely stems from a missing MyBatis dependency rather than a missing interface.

**Alternatives considered:**
- Uncomment the manual `userMapper` bean definition — rejected because `MapperScannerConfigurer` is the cleaner, project-standard approach and already configured.
- Create a new mapper bean — rejected because `UserMapper` already exists.

### Decision: Verify jaxws-ri dependency tree

**Choice:** Run `mvn dependency:tree` to confirm `jaxws-ri:4.0.2` (type=pom) pulls in the required Jakarta namespaces (`jakarta.xml.ws-api`, `jaxws-rt`) as transitive dependencies.

**Rationale:** The `pom.xml` declares `jaxws-ri` with `<type>pom</type>`, which means it is a dependency management BOM. The actual runtime artifacts (`jakarta.xml.ws-api`, `jaxws-rt`) may not be pulled in automatically. If the dependency tree shows missing artifacts, add explicit `jakarta.xml.ws-api` and `jaxws-rt` dependencies.

**Alternatives considered:**
- Use a different JAX-WS implementation (e.g., `com.sun.xml.ws:rt`) — rejected because `jaxws-ri` is already declared and is the standard reference implementation.

## Risks / Trade-offs

| Risk | Mitigation |
|------|-----------|
| `jaxws-ri` with `type=pom` may not pull in transitive runtime artifacts | Run `mvn dependency:tree` to verify; add explicit `jakarta.xml.ws-api` and `jaxws-rt` dependencies if missing |
| `MapperScannerConfigurer` may not register `UserMapper` if MyBatis dependency is missing | Verify `mybatis` and `mybatis-spring` dependencies are present in `pom.xml`; run `mvn compile` to confirm |
| Web service WSDL may reference old namespace in cached metadata | Restart the web service after changes; WSDL is generated from annotations, not cached |
| Existing deployments expecting `javax.jws` namespace will break | This is a fix for modern Jakarta EE — old deployments should be migrated separately |

## Migration Plan

1. Update `WebServices.java` imports from `javax.jws.*` to `jakarta.jws.*`
2. Verify `pom.xml` declares `jaxws-ri` correctly; add explicit Jakarta dependencies if `mvn dependency:tree` shows gaps
3. Verify `spring-core.xml` `MapperScannerConfigurer` configuration is correct
4. Run `mvn clean compile -pl chapter-5/mybatis` to confirm zero compilation errors
5. No rollback needed — this is a namespace migration with no behavioral changes

## Open Questions

- None. The scope is limited to compilation fixes; no spec-level behavior changes.
