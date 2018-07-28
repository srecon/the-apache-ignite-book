#!/bin/sh

java -cp "./ignite-dev-utils.jar:./ignite-indexing-2.6.0.jar:./ignite-core-2.6.0.jar:./h2-1.4.195.jar:./cache-api-1.0.0.jar" org.apache.ignite.development.utils.IgniteWalConverter 4096 $IGNITE_HOME/work/db/wal/node00-fc36db73-ba1c-45a6-a545-0bbd363a8746
