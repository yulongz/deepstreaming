package com.yulongz.ignite.datagrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.transactions.Transaction;

import static org.apache.ignite.transactions.TransactionConcurrency.PESSIMISTIC;
import static org.apache.ignite.transactions.TransactionIsolation.REPEATABLE_READ;

import java.io.Serializable;

/**
 * Created by hadoop on 17-10-19.
 */
public class CacheTransactionalDemo {
    private static final String CACHE_NAME = CacheTransactionalDemo.class.getSimpleName();

    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")) {
            CacheConfiguration<Integer, Account> cfg = new CacheConfiguration(CACHE_NAME);
            cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
            try(IgniteCache<Integer, Account> cache = ignite.getOrCreateCache(cfg)){
                //Initialize
                cache.put(1, new Account(1, 111));
                cache.put(2, new Account(1, 222));

                System.out.println();
                System.out.println(">>> Accounts before deposit: ");
                System.out.println(">>> " + cache.get(1));
                System.out.println(">>> " + cache.get(2));

                // Make transactional deposits.
                deposit(cache, 1, 100);
                deposit(cache, 2, 200);

                System.out.println();
                System.out.println(">>> Accounts after transfer: ");
                System.out.println(">>> " + cache.get(1));
                System.out.println(">>> " + cache.get(2));

                System.out.println(">>> Cache transaction example finished.");

            } finally {
                ignite.destroyCache(CACHE_NAME);
            }
        }
    }

    private static void deposit(IgniteCache<Integer, Account> cache, int id, double amount){
        try(Transaction tx = Ignition.ignite().transactions().txStart(PESSIMISTIC, REPEATABLE_READ)){
            Account acct = cache.get(id);

            assert acct != null;

            acct.update(amount);

            cache.put(id, acct);

            tx.commit();


        }
    }

    private static class Account implements Serializable {
        /** Account ID. */
        private int id;

        /** Account balance. */
        private double balance;

        /**
         * @param id Account ID.
         * @param balance Balance.
         */
        Account(int id, double balance) {
            this.id = id;
            this.balance = balance;
        }

        /**
         * Change balance by specified amount.
         *
         * @param amount Amount to add to balance (may be negative).
         */
        void update(double amount) {
            balance += amount;
        }

        /** {@inheritDoc} */
        @Override public String toString() {
            return "Account [id=" + id + ", balance=$" + balance + ']';
        }
    }
}
