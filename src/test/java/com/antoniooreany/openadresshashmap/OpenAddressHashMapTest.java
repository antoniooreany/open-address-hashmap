package com.antoniooreany.openadresshashmap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

public class OpenAddressHashMapTest {

    private OpenAddressHashMap openAddressHashMap;

    @Before
    public void initEach() {
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

        Assert.assertEquals(16, (int) openAddressHashMap.getCapacity());
    }

    @Test
    public void resizeCapacity() {
        for (int i = 0; i < 1024; i++) openAddressHashMap.put(i, i);

        Assert.assertEquals(2048, (int) openAddressHashMap.getCapacity());
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
    public void hashSynonymTest0() {
        int key0 = 0 * 16 + 5;
        int key1 = 1 * 16 + 5;
        int key2 = 2 * 16 + 5;

        openAddressHashMap.put(key0, 1L);
        openAddressHashMap.put(key1, 2L);
        openAddressHashMap.put(key2, 3L);

        Assert.assertEquals(1L, openAddressHashMap.get(key0));
        Assert.assertEquals(2L, openAddressHashMap.get(key1));
        Assert.assertEquals(3L, openAddressHashMap.get(key2));

        Assert.assertEquals(5, openAddressHashMap.getBucketNumber(key0));
        Assert.assertEquals(6, openAddressHashMap.getBucketNumber(key1));
        Assert.assertEquals(7, openAddressHashMap.getBucketNumber(key2));
    }

    @Test
    public void hashSynonymTest1() {
        int key0 = 0 * 16 + 5;
        int key1 = 0 * 16 + 6;
        int key2 = 1 * 16 + 5;

        openAddressHashMap.put(key0, 1L);
        openAddressHashMap.put(key1, 2L);
        openAddressHashMap.put(key2, 3L);

        Assert.assertEquals(1L, openAddressHashMap.get(key0));
        Assert.assertEquals(2L, openAddressHashMap.get(key1));
        Assert.assertEquals(3L, openAddressHashMap.get(key2));

        Assert.assertEquals(5, openAddressHashMap.getBucketNumber(key0));
        Assert.assertEquals(6, openAddressHashMap.getBucketNumber(key1));
        Assert.assertEquals(7, openAddressHashMap.getBucketNumber(key2));
    }

    @Test(expected = NoSuchElementException.class)
    public void noSuchElementExceptionTest() {
        openAddressHashMap.get(4);
    }

    @Test
    public void resizeCapacityMultiplierTest() {
        int initialCapacity = 13;
        float initialResizeCapacityMultiplier = 1.3f;
        float initialLoadFactor = 0.85f;
        OpenAddressHashMap openAddressHashMap = new OpenAddressHashMap(initialCapacity,
                initialLoadFactor,
                initialResizeCapacityMultiplier);
        for (int i = 0; i < 13; i++) openAddressHashMap.put(i, i);

        Assert.assertEquals(Math.round(initialCapacity * initialResizeCapacityMultiplier),
                openAddressHashMap.getCapacity());
    }

    @Test
    public void loadFactorTest() {
        int initialCapacity = 100;
        float initialResizeCapacityMultiplier = 2.0f;
        float initialLoadFactor = 0.1f;
        OpenAddressHashMap openAddressHashMap = new OpenAddressHashMap(initialCapacity,
                initialLoadFactor,
                initialResizeCapacityMultiplier);
        for (int i = 0; i < 11; i++) openAddressHashMap.put(i, i);

        Assert.assertEquals(Math.round(initialCapacity * initialResizeCapacityMultiplier),
                openAddressHashMap.getCapacity());
    }

    @Test
    public void performanceTest0(){
        OpenAddressHashMap openAddressHashMap = new OpenAddressHashMap(
                16, 0.75f, 1.1f);
        for (int i = 0; i < 10000000; i++) {
            openAddressHashMap.put(i, i);
        }
    }

    @Test
    public void performanceTest1(){
        OpenAddressHashMap openAddressHashMap = new OpenAddressHashMap(
                16, 0.75f, 2.0f);
        for (int i = 0; i < 10000000; i++) {
            openAddressHashMap.put(i, i);
        }
    }
}
