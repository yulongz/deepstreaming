> Storm version 1.*的storm localcluster模式下分区不能重新执行，总是显示只有一个分区，相关测试在集群模式下是正常的的
> Storm version 2.*的storm localcluster模式下分区能正常执行
***

- partition

***

    /**
     * ## Repartitioning Operation
     *
     * @param partitioner
     * @return
     */
    public Stream partition(CustomStreamGrouping partitioner) {
        return partition(Grouping.custom_serialized(Utils.javaSerialize(partitioner)));
    }
    
    /**
     * ## Repartitioning Operation
     *
     * This method takes in a custom partitioning function that implements
     * {@link org.apache.storm.grouping.CustomStreamGrouping}
     *
     * @param grouping
     * @return
     */
    public Stream partition(Grouping grouping) {
        if(_node instanceof PartitionNode) {
            return each(new Fields(), new TrueFilter()).partition(grouping);
        } else {
            return _topology.addSourcedNode(this, new PartitionNode(_node.streamId, _name, getOutputFields(), grouping));
        }
    }
        

- partitionBy

***

    /**
     * ## Repartitioning Operation
     *
     * @param fields
     * @return
     */
    public Stream partitionBy(Fields fields) {
        projectionValidation(fields);
        return partition(Grouping.fields(fields.toList()));
    }


- shuffle

***
    
    /**
     * ## Repartitioning Operation
     *
     * Use random round robin algorithm to evenly redistribute tuples across all target partitions
     *
     * @return
     */
    public Stream shuffle() {
        return partition(Grouping.shuffle(new NullStruct()));
    }
    
- global

***

    /**
     * ## Repartitioning Operation
     *
     * All tuples are sent to the same partition. The same partition is chosen for all batches in the stream.
     * @return
     */
    public Stream global() {
        // use this instead of storm's built in one so that we can specify a singleemitbatchtopartition
        // without knowledge of storm's internals
        return partition(new GlobalGrouping());
    }

- broadcast

***

    /**
     * ## Repartitioning Operation
     *
     * Every tuple is replicated to all target partitions. This can useful during DRPC – for example, if you need to do
     * a stateQuery on every partition of data.
     *
     * @return
     */
    public Stream broadcast() {
        return partition(Grouping.all(new NullStruct()));
    }
    
- batchGlobal

> 参数topology.spout.max.batch.size设置batch的大小，默认是1000，如果测试IRichSpout发送的的数据量太小，可能导致所有的数据都在一个batch中，导致debug显示只有一个task，其他并不会显示

***

    /**
     * ## Repartitioning Operation
     *
     *  All tuples in the batch are sent to the same partition. Different batches in the stream may go to different
     *  partitions.
     *
     * @return
     */
    public Stream batchGlobal() {
        // the first field is the batch id
        return partition(new IndexHashGrouping(0));
    }

- identityPartition

> 多个分区操作时，identityPartition负责将前面的partition操作并发度增大至identityPartition并发度，如果小于前面的则不操作。
> 举例：shuffle1 with parallel(2) -> shuffle2 with parallel(4) -> identityPartition with parallel(3),则shuffle1的parallel增至3,shuffle2不变。
> identityPartition与spout之间不存在partition操作时，可提升spout的并行度。
> 举例：spout with parallel(1) -> identityPartition with parallel(2),则spout的parallel增至2。

***

    /**
     * ## Repartitioning Operation
     *
     * @return
     */
    public Stream identityPartition() {
        return partition(new IdentityGrouping());
    }
    
    
- localOrShuffle

***    

    /**
     * ## Repartitioning Operation
     *
     * Use random round robin algorithm to evenly redistribute tuples across all target partitions, with a preference
     * for local tasks.
     *
     * @return
     */
    public Stream localOrShuffle() {
        return partition(Grouping.local_or_shuffle(new NullStruct()));
    }


