# Design

## Overview

The change adds a unit test for the `FindKeyMapToNode` example. The design focuses on the test itself, ensuring it is isolated, repeatable, and does not alter production code.

## Test Strategy

1. **Environment** – Start an Ignite client node using the existing `IgniteConfiguration` from the example. The test will run in the same Maven module that contains the example.
2. **Cache Setup** – Use the `EMPcache` cache. If it does not exist, create it with binary support (`withKeepBinary()`).
3. **Data Insertion** – Insert a sample `Employee` binary object if the key is not already present. The key will be a simple string such as `"emp-123"`.
4. **Assertions**
   * Retrieve the binary object and assert the `ename` field equals the expected value.
   * Use `Affinity.mapKeyToNode(cache, key)` to get the owning node and assert it matches the client node’s ID.
   * Verify the partition ID returned by `Affinity.partition(cache, key)` matches the node’s primary partition.
5. **Cleanup** – Stop the Ignite client after the test.

## Dependencies

The test relies on the Ignite test dependencies already declared in the module’s `pom.xml` (e.g., `org.apache.ignite:ignite-core`, `org.apache.ignite:ignite-test`). No new dependencies are added.

## Artifact Placement

The test file will be placed at:
```
src/test/java/com/blu/imdg/FindKeyMapToNodeTest.java
```

## Rationale

Adding this test provides regression protection for key‑to‑node mapping logic and binary object handling in Ignite. It does not modify production code, so it can be merged without impacting the application.

---

**Note**: The change is marked to skip spec generation because it only adds a test artifact.
