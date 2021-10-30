package com.antoniooreany.openadresshashmap;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

public class OpenAddressHashMap {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final float DEFAULT_RESIZE_CAPACITY_MULTIPLIER = 2.0F;

    private int capacity;
    private final float loadFactor;
    private final float resizeCapacityMultiplier;
    private HashmapElement[] table;
    private int size;

    public OpenAddressHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.resizeCapacityMultiplier = DEFAULT_RESIZE_CAPACITY_MULTIPLIER;
        this.table = new HashmapElement[DEFAULT_CAPACITY];
    }

    public OpenAddressHashMap(int capacity, float loadFactor, float resizeCapacityMultiplier) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Illegal capacity: " + capacity);
        }
        if (loadFactor <= 0) {
            throw new IllegalArgumentException("Illegal loadFactor: " + loadFactor);
        }
        if (resizeCapacityMultiplier <= 1) {
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
        putByOALogic(key, value);
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

    int getCapacity() {
        return capacity;
    }

    int getBucketNumber(int key) {
        int bucketNumber = hashFunction(key);
        while (table[bucketNumber].getKey() != key) {
            bucketNumber++;
        }
        return bucketNumber;
    }

    private int hashFunction(int key) {
        return Math.abs(key % capacity);
    }

    private void putByOALogic(int key, long value) {
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

    private void resizeCapacity(float resizeCapacityMultiplier) {
        HashmapElement[] oldTable = table;
        capacity = Math.round(capacity * resizeCapacityMultiplier);
        size = 0;
        table = new HashmapElement[capacity];
        Arrays.stream(oldTable).filter(Objects::nonNull).forEach(hashmapElement ->
                putByOALogic(hashmapElement.getKey(), hashmapElement.getValue()));
    }
}
