> Note that, for running examples of this chapter, you have to add the following ```--add-opens``` option to your ```MAVEN_OPTS``` environmental variables. This is the easiest way to run these examples 
> from the maven build tool.
> You can also add the above ```MAVEN_OPTS``` environmental variable in your ```.bash-profile``` file.

```
MAVEN_OPTS=" --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED --add-opens=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.management/com.sun.jmx.mbeanserver=ALL-UNNAMED --add-opens=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED --add-opens=java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED --add-opens=jdk.management/com.sun.management.internal=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.nio=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED --add-opens=java.base/java.time=ALL-UNNAMED --add-opens java.base/java.math=ALL-UNNAMED"

```

```
export MAVEN_OPTS
``` 



## Chapter 7 examples:

- Distributed Closures
- MapReduce and Fork-join
- Compute task split adapter/Per-Node share state
- Distributed task session
- Fault tolerance and checkpointing
- Collocation of computation and data
- Service Grid
- Node singleton
- Service management and configuration
- Microservices on Ignite (not recommended)

### To run the Distributed Closures (Example 1-2) example:

Use this command `mvn exec:java -Dexec.mainClass=com.blu.imdg.StartCacheNode` to start an Ignite node.

1) Execute this command `mvn exec:java -Dexec.mainClass=com.blu.imdg.example1.SimpleComputation`to run the SimpleComputation application.

2) Execute this command `mvn exec:java -Dexec.mainClass=com.blu.imdg.example1.SimpleComputationClosure` to run the SimpleComputationClosure application.

3) Execute this command `mvn exec:java -Dexec.mainClass=com.blu.imdg.example2.AsyncComputation` to run the AsyncComputation application.

### To run the MapReduce and Fork-join (Example 3) example:

Use this command `mvn exec:java -Dexec.mainClass=com.blu.imdg.StartCacheNode` to start a few instance of the Ignite node.

1) Execute this command `mvn exec:java -Dexec.mainClass=ForkJoinComputation`to run the ForkJoinComputation application.

### To run the Compute task split adapter/Per-Node share state (Example 4) example:

1) Run http simulator: `mvn exec:java -Dexec.mainClass=com.blu.imdg.common.HttpAuditEmulator`
2) Run an Ignite node (if not started any):  `mvn clean package exec:java -Dexec.mainClass=com.blu.imdg.StartCacheNode`
3) Run the application: `mvn exec:java -Dexec.mainClass=com.blu.imdg.example4.ForkJoinComputationExt`

### To run the Distributed task session (Example 5) example: 
1) Run http simulator: `mvn exec:java -Dexec.mainClass=com.blu.imdg.common.HttpAuditEmulator`
2) Run an Ignite node (if not started any):  `mvn clean package exec:java -Dexec.mainClass=com.blu.imdg.StartCacheNode`
3) Run the application: `mvn exec:java -Dexec.mainClass=com.blu.imdg.example5.ForkJointWithSessionComputation`

### To run the Fault tolerance and checkpointing (Example 6) example:
1) Run http simulator: `mvn exec:java -Dexec.mainClass=com.blu.imdg.common.HttpAuditEmulator`
2) Run an Ignite node (you have to run more than one Ignite node for getting fault tolerance of an Ignite job):  `mvn clean package exec:java -Dexec.mainClass=com.blu.imdg.StartCacheNode`
3) Run the application: `mvn exec:java -Dexec.mainClass=com.blu.imdg.example6.ForkJoinWithCheckpointComputation` 

Make sure that, you are using the valid XML samples from the folder /resources/data/*.xml

### To run the Collocation of computation and data (Example 8) example:

1) Run an Ignite node (if not started any):  `mvn clean package exec:java -Dexec.mainClass=com.blu.imdg.StartCacheNode`
2) Run the application: `mvn exec:java -Dexec.mainClass=com.blu.imdg.example8.TestAccountSavingsMain`

### To run the Service Grid (Example 7) example:

1) Run http simulator: `mvn exec:java -Dexec.mainClass=com.blu.imdg.common.HttpAuditEmulator`
2) Run an Ignite node (if not started any):  `mvn clean package exec:java -Dexec.mainClass=com.blu.imdg.StartCacheNode`
3) Run the application: `mvn exec:java -Dexec.mainClass=com.blu.imdg.example7.TestXsdValidatingService`

### To run the Node singleton (Example 7) example:

1) Start 2 Ignite nodes:  `mvn clean package exec:java -Dexec.mainClass=com.blu.imdg.StartCacheNode`
2) Deploy and Node Singleton service on the cluster: `mvn exec:java -Dexec.mainClass=com.blu.imdg.example7.DeployNodeSingleton`
3) Run the XSD validation service: `mvn clean package exec:java -Dexec.mainClass=com.blu.imdg.example7.RunXsdValidationService`

### To run the cluster Singleton service (Example 7) example: 
1) comment the following line of code `private static final String VALIDATING_SERVICE = "validatingService";` into the `RunXsdValidationService.Java`
2) Uncomment the following line of code `private static final String VALIDATING_SERVICE = "validatingService-cluster-singleton";` into the `RunXsdValidationService.Java`
3) Compile the project `mvn clean install`

4) Start 2 Ignite nodes:  `mvn clean package exec:java -Dexec.mainClass=com.blu.imdg.StartCacheNode`
5) Deploy and Singleton service on the cluster: `mvn exec:java -Dexec.mainClass=com.blu.imdg.example7.DeployClusterSingleton`
6) Run the XSD validation service: `mvn clean package exec:java -Dexec.mainClass=com.blu.imdg.example7.RunXsdValidationService`

### To run the Service management and configuration (Example 7) example: 

1) Start 2 Ignite nodes:  `mvn clean package exec:java -Dexec.mainClass=com.blu.imdg.StartCacheNode`
2) Run the application: `mvn exec:java -Dexec.mainClass=com.blu.imdg.example7.ServiceManagements`

### To run Microservice (Example 9) examples: 

1) Run http simulator: `mvn exec:java -Dexec.mainClass=com.blu.imdg.common.HttpAuditEmulator`
2) Run an Ignite node (if not started any):  `mvn clean package exec:java -Dexec.mainClass=com.blu.imdg.StartCacheNode`
3) Deploy the service: `mvn exec:java -Dexec.mainClass=com.blu.imdg.example9.DeployService`
4) Run the service: `mvn exec:java -Dexec.mainClass=com.blu.imdg.example9.TestMicroServiceMain`

### To run Microservice (Example 9) REST client example:

1) Run http simulator: `mvn exec:java -Dexec.mainClass=com.blu.imdg.example9.ServiceHttpClient`
2) Run an Ignite node (if not started any):  `mvn clean package exec:java -Dexec.mainClass=com.blu.imdg.StartCacheNode`
3) Deploy the service: `mvn exec:java -Dexec.mainClass=com.blu.imdg.example9.DeployService`
4) Run the service: `mvn exec:java -Dexec.mainClass=com.blu.imdg.example9.TestMicroServiceMain`

