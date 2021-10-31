package com.antoniooreany.openadresshashmap;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

public class OpenAddressHashMap {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final float DEFAULT_RESIZE_CAPACITY_MULTIPLIER = 2.0F;

    /*
    A performance directly correlate with memory consumption,
    meaning: the more performance you need under otherwise equal conditions,
    the more memory have to be used.
    performance / memory consumption can be changed by changing values of following fields:
        - capacity, influences linearly
        - loadFactor, influences linearly
        - resizeCapacityMultiplier, influences exponentially

    It can be done even during runtime via setters for these fields.

    How to adjust performance / memory consumption:

    For performance increasing, we need to:
    - increase capacity
      or/and
    - decrease loadFactor
      or/and
    - increase resizeCapacityMultiplier exponentially

    For memory consumption decreasing,
    we need to do verse-versa of what has to be done for performance increasing:
    - decrease capacity
      or/and
    - increase loadFactor
      or/and
    - decrease resizeCapacityMultiplier

    These 6 relations are stochastic functions,
    and therefore for achieving needed results better to find the appropriate parameters
    experimentally, but not analytically.
    Nevertheless, resizeCapacityMultiplier gives maximum influence because this parameter influences exponentially.
    */
    // TODO rewrite the comment above to the topic "time-, memory-consumption".
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

    public OpenAddressHashMap() {
        init(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR, DEFAULT_RESIZE_CAPACITY_MULTIPLIER);
    }

    public OpenAddressHashMap(int capacity, float loadFactor, float resizeCapacityMultiplier) {
        parametersValidityCheck(capacity, loadFactor, resizeCapacityMultiplier);
        init(capacity, loadFactor, resizeCapacityMultiplier);
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
        int bucketNumber = getHash(key);
        while (table[bucketNumber].getKey() != key) bucketNumber++;
        return bucketNumber;
    }

    private int getHash(int key) {
        return Math.abs(key % capacity);
    }

    private void putByOALogic(int key, long value) {
        int bucketNumber = getHash(key);
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
        Arrays.stream(oldTable).filter(Objects::nonNull).forEach(hashmapElement ->
                putByOALogic(hashmapElement.getKey(), hashmapElement.getValue()));
    }
}
