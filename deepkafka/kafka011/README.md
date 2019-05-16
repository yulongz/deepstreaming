# kafka streams相关
> broker的server.properties尽量使用主机名，测试环境可使用localhost,生产环境尽量不使用localhost，程序配置保持和server.properties中的一致
> application.id是kafka集群其分布式kafkaStreams程序的唯一性标识，而且这里group.id等几个参数会自动使用这个参数(和这个参数保持一致).

# 相关配置
application.id
bootstrap.servers
zookeeper.connect   


# 相关命令
## topic操作
### 创建topic

    bin/kafka-topics.sh --create --zookeeper localhost:2181/kafka011 --replication-factor 1 --partitions 1 --topic streams-file-input
    bin/kafka-topics.sh --create --zookeeper localhost:2181/kafka011 --replication-factor 1 --partitions 1 --topic streams-wordcount-output

### 删除topic
    bin/kafka-topics.sh --delete --zookeeper localhost:2181/kafka011 --topic streams-file-input
    bin/kafka-topics.sh --delete --zookeeper localhost:2181/kafka011 --topic streams-wordcount-output
    
### 显示topic

    bin/kafka-topics.sh --list --zookeeper localhost:2181/kafka011
    
### console producer

    bin/kafka-console-producer.sh --broker-list localhost:9092 --topic streams-file-input

### console consumer

#### 消费input topic
    bin/kafka-console-consumer.sh --zookeeper localhost:2181/kafka011 --from-beginning --topic streams-file-input

#### 消费output topic    
    bin/kafka-console-consumer.sh --zookeeper localhost:2181/kafka011 \
          --topic streams-wordcount-output \
          --from-beginning \
          --formatter kafka.tools.DefaultMessageFormatter \
          --property print.key=true \
          --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
          --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
        
## offset操作

> 0.8版本之前使用kafka-consumer-offset-checker.sh来查看zookeeper中的offset，0.9版本后舍弃；在0.10后由于增加了kafka storage offset所以推荐使用kafka-consumer-groups.sh来查询两种offset storage
> 高版本kafka中工具kafka-consumer-offset-checker.sh来查看zookeeper时，仍存在bug：https://issues.apache.org/jira/browse/KAFKA-2155

### broker storage offset

#### 显示broker storage offset
    bin/kafka-consumer-groups.sh --list --bootstrap-server localhost:9092
#### 显示broker storage offset中group的topic及其offset    
    bin/kafka-consumer-groups.sh --describe --bootstrap-server localhost:9092 --group streams-wordcount
    
### 显示zookeeper storage offset
    bin/kafka-consumer-groups.sh --list --zookeeper localhost:2181/kafka011
#### 显示zookeeper storage offset中group的topic及其offset    
    bin/kafka-consumer-groups.sh --describe --zookeeper localhost:2181/kafka011 --group console-consumer-67420

## reset应用或者offset  
#### Deleting all internal/auto-created topics for application application-id
    bin/kafka-streams-application-reset.sh --application-id streams-wordcount --bootstrap-servers localhost:9092 --zookeeper localhost:2181/kafka011 
    
#### reset某个kafkastreams application的输入topic的offset,and Deleting all internal/auto-created topics for application application-id
    bin/kafka-streams-application-reset.sh --application-id streams-wordcount --bootstrap-servers localhost:9092 --zookeeper localhost:2181/kafka011 --input-topics streams-file-input

## state store
> 参数StreamsConfig.STATE_DIR_CONFIG，即state.dir，默认值为/tmp/kafka-streams会保存state

如果需要rerun程序，则需要如下步骤：
1. 删除/tmp/kafka-streams/{application.id}的state store，这里需要执行：rm -r /tmp/kafka-streams/streams-wordcount
2. 重置reset应用或者input topic offset，这里需要执行：bin/kafka-streams-application-reset.sh --application-id streams-wordcount --bootstrap-servers localhost:9092 --zookeeper localhost:2181/kafka011 --input-topics streams-file-input


# storm相关
com.yulongz.kafka.record.storm.normal.AvroKafkaExample
com.yulongz.kafka.record.storm.trident.AvroKafkaTridentExample
两个类所使用的storm-kafka-clients依赖需要修改源代码