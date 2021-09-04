# flink-kafka-stream
Trying a flink streaming job

## Pre-requisites
1. Install jdk 8
2. Download and unzip Kafka 2.2 and run the following commands after _**cd**_ to the kafka install directory
```
    bin/zookeeper-server-start.sh config/zookeeper.properties
    bin/kafka-server-start.sh config/server.properties. 
```
3. Create 2 topics in Kafka using the following commands 
```
    bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 6 --topic raw
    bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 6 --topic valid
    bin/kafka-topics.sh --list --bootstrap-server localhost:9092 //This is to see that the topics have been created
```
4. Download and unzip flink 1.13.1

## Running the code
1. Clone this repo and _**cd**_ to the cloned folder
2. Run mvn clean package
3. _**cd**_ to the extracted flink folder and execute the following commands. The [flink console](http://localhost:8081/) can be accessed once the flink cluster is started.
```
    ./bin/start-cluster.sh
    ./bin/flink run /pathtotheclonedfolder/target/flink-kafka-stream-1.0-SNAPSHOT.jar
```
4. Add messages to the raw topic by issuing the following commands after _**cd**_ to the kafka install directory
```
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic raw
Add the following text as input ABCD
```
5. Use a console consumer on the valid topic to see the messages that have been processed by the streaming job.
```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic valid --from-beginning
```

## Understanding the source code
**BaseStreaming** has the simple boilerplate code for the Kafka serializer and de-serializer.  
**StreamingJob** has the simple boilerplate code for a flink job.
**CaseHandlerProcessFunction** is a simple process function that splits the incoming data by space and to lowercase
**application.conf** has the input and output topic names that can be configured. It defaults to raw (input topic) and valid (output topic) along with other configuration


## Benchmarking the code on your workstation
1. Insert messages into the raw topic using the below command. The below command would insert 10 mil messages of 100 chars to the raw topic
```
bin/kafka-producer-perf-test.sh --topic raw --num-records 10000000 --record-size 100 --throughput 5000000 --producer-props bootstrap.servers=localhost:9092
```
2. Execute the program so that it starts streaming from raw to valid.
3. You can look at the lag for the consumer group (stream1 by default, can be changed in application.properties) of the valid topic every 10 seconds (using watch command) by executing the following command
```
   bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group stream1
```