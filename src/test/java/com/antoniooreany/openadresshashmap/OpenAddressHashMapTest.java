package com.antoniooreany.openadresshashmap;

import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;

import java.util.NoSuchElementException;

public class OpenAddressHashMapTest {

    private OpenAddressHashMap openAddressHashMap;

    @Before
    public void beforeEachMethod() {
        openAddressHashMap = new OpenAddressHashMap();
    }

    @Test
    public void put() {
        openAddressHashMap.put(0, 0L);
        openAddressHashMap.put(1, 1L);
        openAddressHashMap.put(2, 2L);
        openAddressHashMap.put(3, 3L);

        Assert.assertEquals(2, openAddressHashMap.get(2));
    }

    @Test
    public void get() {
        openAddressHashMap.put(0, 0L);
        openAddressHashMap.put(1, 1L);
        openAddressHashMap.put(2, 2L);
        openAddressHashMap.put(3, 3L);

        Assert.assertEquals(3L, openAddressHashMap.get(3));
    }

    @Test
    public void size() {
        openAddressHashMap.put(0, 0L);
        openAddressHashMap.put(1, 1L);
        openAddressHashMap.put(2, 2L);
        openAddressHashMap.put(3, 3L);

        Assert.assertEquals(4, openAddressHashMap.size());
    }

    @Test
    public void getBucketNumber() {
        openAddressHashMap.put(0, 0L);
        openAddressHashMap.put(1, 1L);
        openAddressHashMap.put(2, 2L);
        openAddressHashMap.put(3, 3L);

        Assert.assertEquals(2, openAddressHashMap.getBucketNumber(2));
    }

    @Test
    public void getCapacity() {
        openAddressHashMap.put(0, 0L);
        openAddressHashMap.put(1, 1L);
        openAddressHashMap.put(2, 2L);
        openAddressHashMap.put(3, 3L);

        Assert.assertEquals(16, openAddressHashMap.getCapacity());
    }

    @Test
    public void sameKeysTest() {
        openAddressHashMap.put(1, 0L);
        openAddressHashMap.put(1, 1L);
        openAddressHashMap.put(1, 2L);
        openAddressHashMap.put(1, 3L);

        Assert.assertEquals(3L, openAddressHashMap.get(1));
        Assert.assertEquals(1, openAddressHashMap.size());
    }

    @Test
    public void resizeCapacityTest() {
        for (int i = 0; i < 1024; i++) {
            openAddressHashMap.put(i, i);
        }

        Assert.assertEquals(2048, ((OpenAddressHashMap) openAddressHashMap).getCapacity());
    }

    @Test
    public void hashSynonymTest0() {
        openAddressHashMap.put(0 * 31 + 5, 1L);
        openAddressHashMap.put(1 * 31 + 5, 2L);
        openAddressHashMap.put(2 * 31 + 5, 3L);

        Assert.assertEquals(1L, openAddressHashMap.get(0 * 31 + 5));
        Assert.assertEquals(2L, openAddressHashMap.get(1 * 31 + 5));
        Assert.assertEquals(3L, openAddressHashMap.get(2 * 31 + 5));

        Assert.assertEquals(5, ((OpenAddressHashMap) openAddressHashMap).getBucketNumber(0 * 31 + 5));
        Assert.assertEquals(6, ((OpenAddressHashMap) openAddressHashMap).getBucketNumber(1 * 31 + 5));
        Assert.assertEquals(7, ((OpenAddressHashMap) openAddressHashMap).getBucketNumber(2 * 31 + 5));
    }

    @Test
    public void hashSynonymTest1() {
        openAddressHashMap.put(0 * 31 + 5, 1L);
        openAddressHashMap.put(0 * 31 + 6, 2L);
        openAddressHashMap.put(1 * 31 + 5, 3L);

        Assert.assertEquals(1L, openAddressHashMap.get(0 * 31 + 5));
        Assert.assertEquals(2L, openAddressHashMap.get(0 * 31 + 6));
        Assert.assertEquals(3L, openAddressHashMap.get(1 * 31 + 5));

        Assert.assertEquals(5, ((OpenAddressHashMap) openAddressHashMap).getBucketNumber(0 * 31 + 5));
        Assert.assertEquals(6, ((OpenAddressHashMap) openAddressHashMap).getBucketNumber(0 * 31 + 6));
        Assert.assertEquals(7, ((OpenAddressHashMap) openAddressHashMap).getBucketNumber(1 * 31 + 5));
    }

    @Test(expected = NoSuchElementException.class)
    public void NoSuchElementExceptionTest() {
        openAddressHashMap.get(4);
    }
}
