1. download apache spark 2.3.1
2. download apache-ignite-fabric-2.6.0-bin

3.run ignite

cd /home/mzheludkov/work/ignite/apache-ignite-fabric-2.6.0-bin
./ignite.sh

4.compile project

mvn clean package

6.run load data (last parameter - path to current dir)
~/spark-2.3.1-bin-hadoop2.7/bin/spark-submit load-data-module/target/ignite.spark.load.module-1.0-spring-boot.jar /home/mzheludkov/work/ignite/IgniteSparkApp/


7.check data is loaded
in dir ~/work/ignite/apache-ignite-fabric-2.6.0-bin/bin$
./sqlline.sh --verbose=true -u jdbc:ignite:thin://127.0.0.1/

select count(*) from FLIGHTS

8. run query on spark

~/spark-2.3.1-bin-hadoop2.7/bin/spark-submit load-data-module/target/ignite.spark.load.module-1.0-spring-boot.jar
