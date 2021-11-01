package com.antoniooreany.openadresshashmap;

import java.text.MessageFormat;

public class Util {
    static int nearestPrimeLeftFrom(int n) {
        if (n % 2 != 0) n -= 2;
        else n--;
        int i, j;
        for (i = n; i >= 2; i -= 2) {
            for (j = 3; j <= Math.sqrt(i); j += 2) if (i % j == 0) break;
            if (j > Math.sqrt(i)) return i;
        }
        return 2;
    }

    public static int hash(int key) {
        int capacity = 16;
        double f = (Math.sqrt(5.0) - 1.0) / 2.0;
        int hash = (int) Math.floor(capacity * (f * key - Math.floor(f * key)));
        System.out.println(MessageFormat.format("key={0} hash={1}", key, hash));
        return 0;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            hash(i);
        }
    }
}
