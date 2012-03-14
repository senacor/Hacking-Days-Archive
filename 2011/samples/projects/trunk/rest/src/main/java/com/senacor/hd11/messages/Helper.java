package com.senacor.hd11.messages;

/**
 * Ralph Winzinger, Senacor
 */
public class Helper {
    public static boolean containsBadWord(String text) {
        if (text.length() > 10) {
            return true;
        } else {
            return false;
        }
    }
}
