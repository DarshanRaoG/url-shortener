package com.darshanraog.urlshortener.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Base62Encoder {

    private static final char[] base62Digits =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
                    .toCharArray();

    public static String encode(long id) {
        StringBuilder base62Url = new StringBuilder();
        while (id > 0) {
            int remainder = (int) (id % 62);
            base62Url.append(base62Digits[remainder]);
            id = id / 62;
        }
        return base62Url.toString();
    }
}
