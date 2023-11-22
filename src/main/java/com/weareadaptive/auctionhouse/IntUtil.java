package com.weareadaptive.auctionhouse;

public class IntUtil {
    private IntUtil() {
    }

    public static boolean isValidQty(double qty) {
        return qty > 0;
    }
}

