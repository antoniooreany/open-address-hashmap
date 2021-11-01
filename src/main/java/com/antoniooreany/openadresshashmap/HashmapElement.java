package com.antoniooreany.openadresshashmap;

import java.util.Objects;

record HashmapElement(int key, long value) {

    public int getKey() {
        return key;
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashmapElement hashmapElement = (HashmapElement) o;
        return key == hashmapElement.key && value == hashmapElement.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return "HashmapElement[" +
                "key=" + key + ", " +
                "value=" + value + ']';
    }

}
