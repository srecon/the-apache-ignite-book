# Proposal

## Why
The chapter‑2 module fails to compile due to missing imports, type mismatches, and missing casts. These errors prevent Maven tests from running and block progress on the Ignite integration examples.

## What Changes
- Add missing imports for `org.apache.ignite.Ignition`, `java.util.List`, and `java.util.Map`.
- Cast `cache.get(900L)` to `BinaryObject` where `BinaryObject.type()` is used.
- Update `Benchmark.java`, `HelloThinClient.java`, `HelloIgnite.java`, `HelloIgniteSpring.java`, and `IsolatedCluster.java` accordingly.

## Capabilities
### New Capabilities
- `ignite-compile-fixes`: Provides the necessary imports and type handling to compile the Ignite examples.

## Impact
This change removes compilation failures, enabling Maven test execution for chapter‑2 and allowing further development of Ignite features.
