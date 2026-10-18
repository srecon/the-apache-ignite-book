# Design

## Context
The chapter‑2 module contains several Ignite example classes that fail to compile due to missing imports, type mismatches, and missing casts. The Maven build for this module is blocked, preventing further development and testing of Ignite integration examples.

## Goals / Non-Goals
**Goals:**
- Ensure all Ignite example classes in chapter‑2 compile successfully.
- Preserve existing functionality and behavior of the examples.
- Keep changes minimal and localized to the affected files.

**Non-Goals:**
- Refactor or redesign the Ignite examples beyond necessary import and type corrections.
- Add new features or alter business logic.

## Decisions
- **Import Strategy:** Add the required imports (`org.apache.ignite.Ignition`, `java.util.List`, `java.util.Map`, `org.apache.ignite.binary.BinaryObject`) directly in each affected file. No package‑wide import changes.
- **Type Handling:** Cast the result of `cache.get(900L)` to `BinaryObject` only where `type()` is invoked. This avoids unnecessary casts elsewhere.
- **Compilation Target:** Use Java 21 syntax (e.g., `List.of`, `Map.of`) as the project is configured for Java 21.
- **Build Configuration:** No changes to Maven `pom.xml` are required; the existing compiler settings already target Java 21.

## Risks / Trade-offs
- **Risk:** Introducing casts may mask runtime type errors if the cache contains unexpected values. **Mitigation:** The affected code paths are exercised by existing unit tests; after changes, run `mvn -pl chapter-2 test` to confirm.
- **Risk:** Adding imports may inadvertently bring in unused classes. **Mitigation:** Imports are added only for classes actually referenced.

## Migration Plan
1. Apply the import and cast changes to the affected source files.
2. Run `mvn -pl chapter-2 test` to verify compilation and test success.
3. Commit the changes and merge into the main branch.

## Open Questions
- None.
