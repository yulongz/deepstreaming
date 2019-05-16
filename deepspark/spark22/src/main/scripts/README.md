> 重置${group.id}的topic的offset，例如：group.id=zyltest,topic=zyl-orgtopc-miss
```
kafka-streams-application-reset.sh --application-id ${group.id} --bootstrap-servers hadoop001:9092,hadoop002:9092,hadoop003:9092 --zookeeper hadoop001:2181,hadoop002:2181,hadoop003:2181/kafka011 --input-topics ${topic}

kafka-streams-application-reset.sh --application-id zyltest --bootstrap-servers localhost:9092 --zookeeper localhost:2181/kafka01101 --input-topics zyl-orgtopc-miss
```

