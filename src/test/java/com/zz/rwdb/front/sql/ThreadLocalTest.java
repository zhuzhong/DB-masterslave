/**
 * 
 */
package com.zz.rwdb.front.sql;

import org.junit.Test;

/**
 * @author sunff
 *
 */
public class ThreadLocalTest {

    private volatile Integer i;
    private static ThreadLocal<Integer> local = new ThreadLocal<Integer>();

    @Test
    public void test() {
        int j = 100;
        i = j;
        System.out.println(String.format("i=%s", i));
        local.set(i);

        System.out.println(String.format("locali=%s", local.get()));
        j = 200;
        i = j;
        System.out.println(String.format("i=%s", i));
        System.out.println(String.format("locali=%s", local.get()));

    }
}
