package com.antoniooreany.openadresshashmap;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

public class OpenAddressHashMap {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int DEFAULT_RESIZE_CAPACITY_MULTIPLIER = 2;

    private int capacity;
    private final float loadFactor;
    private final int resizeCapacityMultiplier;
    private HashmapElement[] table;
    private int size;

    public OpenAddressHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.resizeCapacityMultiplier = DEFAULT_RESIZE_CAPACITY_MULTIPLIER;
        this.table = new HashmapElement[DEFAULT_CAPACITY];
    }

    public OpenAddressHashMap(int capacity, float loadFactor, int resizeCapacityMultiplier) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Illegal capacity: " + capacity);
        }
        if (loadFactor <= 0) {
            throw new IllegalArgumentException("Illegal loadFactor: " + loadFactor);
        }
        if (resizeCapacityMultiplier <= 0) {
            throw new IllegalArgumentException("Illegal resizeCapacityMultiplier: " + resizeCapacityMultiplier);
        }
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.resizeCapacityMultiplier = resizeCapacityMultiplier;
        this.table = new HashmapElement[capacity];
    }

    public void put(int key, long value) {
        if (size >= capacity * loadFactor) {
            resizeCapacity(resizeCapacityMultiplier);
        }
        putToAppropriateBucket(key, value);
    }

    public long get(int key) {
        try {
            int bucketNumber = getBucketNumber(key);
            return table[bucketNumber].getValue();
        } catch (NullPointerException e) {
            throw new NoSuchElementException(String.format("Key %s doesn't exist in the map", key));
        }
    }

    public int size() {
        return size;
    }

    private int hashFunction(int key) {
        return Math.abs(key % 31);
    }

    private void putToAppropriateBucket(int key, long value) {
        int bucketNumber = hashFunction(key);
        boolean newKey = true;
        while (table[bucketNumber] != null && (newKey = table[bucketNumber].getKey() != key)) {
            bucketNumber++;
            if (bucketNumber == capacity) {
                resizeCapacity(resizeCapacityMultiplier);
            }
        }
        table[bucketNumber] = new HashmapElement(key, value);
        if (newKey) {
            size++;
        }
    }

    protected int getBucketNumber(int key) {
        int bucketNumber = hashFunction(key);
        while (table[bucketNumber].getKey() != key) {
            bucketNumber++;
        }
        return bucketNumber;
    }

    private void resizeCapacity(int resizeCapacityMultiplier) {
        HashmapElement[] oldTable = table;
        this.capacity *= resizeCapacityMultiplier;
        size = 0;
        table = new HashmapElement[this.capacity];
        Arrays.stream(oldTable).filter(Objects::nonNull).forEach(hashmapElement ->
                putToAppropriateBucket(hashmapElement.getKey(), hashmapElement.getValue()));
    }

    protected int getCapacity() {
        return capacity;
    }
}
