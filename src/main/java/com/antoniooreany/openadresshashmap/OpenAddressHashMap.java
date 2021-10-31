package com.antoniooreany.openadresshashmap;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

public class OpenAddressHashMap {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final float DEFAULT_RESIZE_CAPACITY_MULTIPLIER = 2.0f;

    /*
    Under the otherwise equal conditions of the particular implementation,
    the more memory we consume, the less time-consumption we achieve.

    The time-, memory-consumption can be changed by setting up the values of the following fields:
    - capacity, influences linearly.
    - loadFactor, influences linearly.
    - resizeCapacityMultiplier, influences exponentially.

    It can be done even during runtime via setters for these fields.

    How to adjust time-, memory-consumption?

    For decreasing of the time-consumption, we need to:
    - increase capacity
      and/or
    - decrease loadFactor
      and/or
    - increase resizeCapacityMultiplier

    For decreasing of the memory consumption, we need to:
    - decrease capacity
      and/or
    - increase loadFactor
      and/or
    - decrease resizeCapacityMultiplier

    These 6 relations are stochastic functions,
    moreover, there might exist some influence factors
    we do not consider (such as GC, some details of the HW-components etc.),
    therefore for achieving appropriate results,
    it is better to find the needed parameters
    experimentally, but not analytically.

    By the way, resizeCapacityMultiplier gives maximum influence
    because this parameter influences exponentially, but not linearly.

    I am not sure that my conclusions are 100% correct,
    but that was my current thoughts about a
    time-, memory-consumption of the OpenAddressHashMap.
    */

    private int capacity;
    private float loadFactor;
    private float resizeCapacityMultiplier;

    private HashmapElement[] table;
    private int size;

    public void setCapacity(int capacity) {
        parametersValidityCheck(capacity, this.loadFactor, this.resizeCapacityMultiplier);
        if (this.capacity < capacity) this.capacity = capacity;
    }

    public void setLoadFactor(float loadFactor) {
        parametersValidityCheck(this.capacity, loadFactor, this.resizeCapacityMultiplier);
        this.loadFactor = loadFactor;
    }

    public void setResizeCapacityMultiplier(float resizeCapacityMultiplier) {
        parametersValidityCheck(this.capacity, this.loadFactor, resizeCapacityMultiplier);
        this.resizeCapacityMultiplier = resizeCapacityMultiplier;
    }

    private void parametersValidityCheck(int capacity, float loadFactor, float resizeCapacityMultiplier) {
        if (capacity <= 0) throw new IllegalArgumentException("Illegal capacity: " + capacity);
        if (loadFactor <= 0 || loadFactor >= 1) throw new IllegalArgumentException("Illegal loadFactor: " + loadFactor);
        if (resizeCapacityMultiplier <= 1) throw new IllegalArgumentException("Illegal resizeCapacityMultiplier: " + resizeCapacityMultiplier);
    }

    private void init(int capacity, float loadFactor, float resizeCapacityMultiplier) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.resizeCapacityMultiplier = resizeCapacityMultiplier;
        this.table = new HashmapElement[capacity];
        this.size = 0;
    }

    public OpenAddressHashMap(int capacity, float loadFactor, float resizeCapacityMultiplier) {
        parametersValidityCheck(capacity, loadFactor, resizeCapacityMultiplier);
        init(capacity, loadFactor, resizeCapacityMultiplier);
    }

    public OpenAddressHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR, DEFAULT_RESIZE_CAPACITY_MULTIPLIER);
    }

    public void put(int key, long value) {
        if (size >= capacity * loadFactor) resizeCapacity(resizeCapacityMultiplier);
        putByOALogic(key, value);
    }

    public long get(int key) {
        try {
            int bucketNumber = getBucketNumber(key);
            return table[bucketNumber].getValue();
        } catch (NullPointerException e) {
            throw new NoSuchElementException(String.format("Key %s does not exist in the current map", key));
        }
    }

    public int size() {
        return size;
    }

    int getCapacity() {
        return capacity;
    }

    int getBucketNumber(int key) {
        int bucketNumber = hash(key);
        while (table[bucketNumber].getKey() != key) bucketNumber++;
        return bucketNumber;
    }

    private int hash(int key) {
        return Math.abs(key % capacity);
    }

    private void putByOALogic(int key, long value) {
        int bucketNumber = hash(key);
        boolean newKey = true;
        while (table[bucketNumber] != null && (newKey = table[bucketNumber].getKey() != key)) {
            bucketNumber++;
            if (bucketNumber == capacity) resizeCapacity(resizeCapacityMultiplier);
        }
        table[bucketNumber] = new HashmapElement(key, value);
        if (newKey) size++;
    }

    private void resizeCapacity(float resizeCapacityMultiplier) {
        HashmapElement[] oldTable = table;
        capacity = Math.round(capacity * resizeCapacityMultiplier);
        table = new HashmapElement[capacity];
        size = 0;
        Arrays.stream(oldTable).filter(Objects::nonNull).
                forEach(hashmapElement ->
                        putByOALogic(hashmapElement.getKey(), hashmapElement.getValue())
                );
    }
}
