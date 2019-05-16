/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yulongz.ignite.persistentstore;

import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.DataRegionConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.checkpoint.sharedfs.SharedFsCheckpointSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;

import java.util.Arrays;

/**
 * @see PersistentStoreExampleNodeStartup
 */
public class PersistentStoreExampleNodeStartup {
    /**
     * @param args Program arguments, ignored.
     * @throws Exception If failed.
     */
    public static void main(String[] args) throws Exception {
        Ignition.start("example-persistent-store.xml");
    }

    public static IgniteConfiguration configuration() throws IgniteException {
        IgniteConfiguration cfg = new IgniteConfiguration();

        DataStorageConfiguration dsCfg = new DataStorageConfiguration();

        DataRegionConfiguration drCfg = new DataRegionConfiguration();

        drCfg.setPersistenceEnabled(true);
        drCfg.setName("test");

        dsCfg.setDataRegionConfigurations(drCfg);

        cfg.setDataStorageConfiguration(dsCfg);


        // Configure discovery SPI.
        TcpDiscoverySpi discoSpi = new TcpDiscoverySpi();

        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();

        ipFinder.setAddresses(Arrays.asList("127.0.0.1:47500..47502"));

        discoSpi.setIpFinder(ipFinder);

        cfg.setDiscoverySpi(discoSpi);

        return cfg;
    }
}
