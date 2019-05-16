> broker的server.properties尽量使用主机名，尽量不使用localhost，程序配置保持和server.properties中的一致，这里我使用的主机名为breath
> application.id是kafka集群其分布式kafkaStreams程序的唯一性标识，而且这里group.id等几个参数会自动使用这个参数(和这个参数保持一致).

# 相关配置
application.id:"streams-demo-processor"
bootstrap.servers:"breath:9092"    
zookeeper.connect:"breath:2181/kafka01021"    
input-topic:"streams-demo-input"    
output-topic:"streams-demo-processor-output"    

# 相关命令
## topic操作
### 创建topic

    bin/kafka-topics.sh --create --zookeeper breath:2181/kafka01021 --replication-factor 1 --partitions 2 --topic streams-demo-input
    bin/kafka-topics.sh --create --zookeeper breath:2181/kafka01021 --replication-factor 1 --partitions 1 --topic streams-demo-processor-output

### 删除topic
    bin/kafka-topics.sh --delete --zookeeper breath:2181/kafka01021 --topic streams-demo-input
    bin/kafka-topics.sh --delete --zookeeper breath:2181/kafka01021 --topic streams-demo-processor-output
    
### 显示topic

    bin/kafka-topics.sh --list --zookeeper breath:2181/kafka01021
    
### console producer

    bin/kafka-console-producer.sh --broker-list breath:9092 --topic streams-demo-input

### console consumer

#### 消费input topic
    bin/kafka-console-consumer.sh --zookeeper breath:2181/kafka01021 --from-beginning --topic streams-demo-input

#### 消费output topic    
    bin/kafka-console-consumer.sh --zookeeper breath:2181/kafka01021 \
          --topic streams-demo-processor-output \
          --from-beginning \
          --formatter kafka.tools.DefaultMessageFormatter \
          --property print.key=true \
          --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
          --property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer
        
## offset操作

> 0.8版本之前使用kafka-consumer-offset-checker.sh来查看zookeeper中的offset，0.9版本后舍弃；在0.10后由于增加了kafka storage offset所以推荐使用kafka-consumer-groups.sh来查询两种offset storage
> 高版本kafka中工具kafka-consumer-offset-checker.sh来查看zookeeper时，仍存在bug：https://issues.apache.org/jira/browse/KAFKA-2155

### broker storage offset

#### 显示broker storage offset
    bin/kafka-consumer-groups.sh --list --bootstrap-server breath:9092
#### 显示broker storage offset中group的topic及其offset    
    bin/kafka-consumer-groups.sh --describe --bootstrap-server breath:9092 --group streams-demo-processor
    
### 显示zookeeper storage offset
    bin/kafka-consumer-groups.sh --list --zookeeper breath:2181/kafka01021
    
#### 显示zookeeper storage offset中group的topic及其offset    
    bin/kafka-consumer-groups.sh --describe --zookeeper breath:2181/kafka01021 --group console-consumer-67420

## reset应用或者offset  
#### Deleting all internal/auto-created topics for application application-id
    bin/kafka-streams-application-reset.sh --application-id streams-demo-processor --bootstrap-servers breath:9092 --zookeeper breath:2181/kafka01021 
    
#### reset某个kafkastreams application的输入topic的offset,and Deleting all internal/auto-created topics for application application-id
    bin/kafka-streams-application-reset.sh --application-id streams-demo-processor --bootstrap-servers breath:9092 --zookeeper breath:2181/kafka01021 --input-topics streams-demo-input

## state store
> 参数StreamsConfig.STATE_DIR_CONFIG，即state.dir，默认值为/tmp/kafka-streams会保存state

如果需要rerun程序，则需要如下步骤：
1. 删除/tmp/kafka-streams/{application.id}的state store，这里需要执行：rm -r /tmp/kafka-streams/streams-demo-processor
2. 重置reset应用或者input topic offset，这里需要执行：bin/kafka-streams-application-reset.sh --application-id streams-demo-processor --bootstrap-servers breath:9092 --zookeeper breath:2181/kafka01021 --input-topics streams-demo-input