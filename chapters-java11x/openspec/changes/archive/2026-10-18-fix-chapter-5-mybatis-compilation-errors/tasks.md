# Tasks

## 1. Update JAX-WS Imports in WebServices.java

- [x] 1.1 Replace `import javax.jws.WebService;` with `import jakarta.jws.WebService;` in `WebServices.java` and verify the file compiles without "package javax.jws does not exist" errors
- [x] 1.2 Replace `import javax.jws.WebMethod;` with `import jakarta.jws.WebMethod;` in `WebServices.java` and verify the `@WebMethod` annotations resolve correctly
- [x] 1.3 Replace `import javax.jws.WebParam;` with `import jakarta.jws.WebParam;` in `WebServices.java` and verify the `@WebParam` annotation resolves correctly

## 2. Verify JAX-WS Dependencies in pom.xml

- [x] 2.1 Run `mvn dependency:tree -pl chapter-5/mybatis` and verify that `jaxws-ri:4.0.2` (type=pom) pulls in `jakarta.xml.ws-api` and `jaxws-rt` as transitive dependencies; if missing, add explicit `jakarta.xml.ws-api` and `com.sun.xml.ws:rt` dependencies to `pom.xml` and verify the dependency tree is clean
- [x] 2.2 Verify that `mybatis`, `mybatis-spring`, and `mybatis-ignite` dependencies are present in `pom.xml` and resolve `UserMapper` at compile time

## 3. Verify Spring Configuration

- [x] 3.1 Review `spring-core.xml` to confirm `MapperScannerConfigurer` scans `com.blu.imdg.mapper` and registers `UserMapper` as a bean named `userMapper`, matching the `<property name="userMapper" ref="userMapper"/>` reference in `userServicesBean`
- [x] 3.2 Verify that `UserMapper.xml` mapper namespace (`com.blu.imdg.mapper.UserMapper`) matches the Java interface and the select statement `getEmployee` is correctly configured

## 4. Verify Compilation

- [x] 4.1 Run `mvn clean compile -pl chapter-5/mybatis` and verify zero compilation errors (no "package does not exist", "cannot find symbol", or "already defined" errors)
- [x] 4.2 Run `mvn dependency:tree -pl chapter-5/mybatis` to confirm no version conflicts between Jakarta XML WS API and JAX-WS runtime artifacts
