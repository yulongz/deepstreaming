Pass hbase config parameter to your topology.

    public static final String HBASE_CONFIG_KEY = "hbase.conf";

1) In storm config put new setting HBASE\_CONFIG_KEY.

    Config config = new Config();
    String rootDir = topologyConfig.getProperty("hbase.rootdir");
    Map<String, Object> hbConf = new HashMap<>();
    hbConf.put("hbase.rootdir", rootDir);
    config.put(HBASE_CONFIG_KEY, hbConf);
    StormSubmitter.submitTopology(topologyName, config, buildTopology());
    
2) In Hbase Bolt config set config Key HBASE\_CONFIG_KEY

    HBaseBolt hBaseBolt = new HBaseBolt(topologyConfig.getProperty(CFG_HBASE_BOLT_TABLE_NAME), new  CbossCdrRecordMapper())
            .withConfigKey(HBASE_CONFIG_KEY)
            .withBatchSize(1000)
            .withFlushIntervalSecs(flushInterval)
            .withBatchSize(Integer.valueOf(topologyConfig.getProperty(CFG_HBASE_BOLT_BATCH_SIZE)));
            
That worked for me.