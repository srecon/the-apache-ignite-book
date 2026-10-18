# Tasks

## 1. Code Changes

- [x] 1.1 Add missing imports to `Benchmark.java` (`org.apache.ignite.Ignition`, `org.apache.ignite.binary.BinaryObject`, `java.util.List`).
- [x] 1.2 Add missing imports to `HelloThinClient.java` (`org.apache.ignite.Ignition`).
- [ ] 1.3 Add missing imports to `HelloIgnite.java` (`java.util.List`).
- [ ] 1.4 Add missing imports to `HelloIgniteSpring.java` (`java.util.Map`).
- [ ] 1.5 Add missing imports to `IsolatedCluster.java` (`java.util.List`).
- [ ] 1.6 Cast `cache.get(900L)` to `BinaryObject` in `Benchmark.java` where `type()` is used.
- [ ] 1.7 Verify that no other files reference `Ignition` or `BinaryObject` without imports.

## 2. Verification

- [ ] 2.1 Run `mvn -pl chapter-2 test` to confirm compilation succeeds and all tests pass.
- [ ] 2.2 Review the test output to ensure no compilation errors remain.
- [ ] 2.3 Commit the changes and push to the feature branch.

## 3. Documentation

- [ ] 3.1 Update any relevant README or documentation that references the affected classes to reflect new imports.
