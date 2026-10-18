# Tasks

- [ ] **Add unit test `FindKeyMapToNodeTest.java`**
  - Create a JUnit test in `src/test/java/com/blu/imdg/` that starts an Ignite client node, ensures the `EMPcache` cache contains a binary `Employee` object, retrieves it, and asserts the `ename` field and node mapping.
  - Verify the partition ID matches the owning node.
  - Clean up the Ignite instance after the test.

---

**Note**: This change only adds a test artifact; no production code is modified.
