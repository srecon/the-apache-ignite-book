# Proposal

## Why

The existing `FindKeyMapToNode` example demonstrates how to retrieve a binary object from Ignite and map a key to its owning node. However, there is no automated test verifying this behavior. Adding a unit test will ensure that future changes to Ignite configuration, cache mapping, or binary handling do not regress this functionality.

## What Changes

- Add a JUnit test `FindKeyMapToNodeTest` in `src/test/java/com/blu/imdg/`.
- The test will:
  1. Start an Ignite node in client mode.
  2. Create or obtain the `EMPcache` cache with binary objects.
  3. Put a sample key‑value pair if not present.
  4. Retrieve the binary object and assert its `ename` field.
  5. Use Ignite affinity to map the key to a node and assert the node ID matches the expected primary node.
  6. Verify the partition ID.
- Clean up the Ignite instance after the test.

No application code changes are required; only the test artifact is added.

## Capabilities

### New Capabilities

- *None*

### Modified Capabilities

- *None*

## Impact

- Adds a new test file under `src/test/java/com/blu/imdg/FindKeyMapToNodeTest.java`.
- Requires the Ignite test dependencies to be available in the test scope (already present in the Maven `pom.xml`).
- No runtime or API changes; only test coverage is increased.

---

**Note**: This change does not modify any production behavior, so it is marked to skip spec generation.
