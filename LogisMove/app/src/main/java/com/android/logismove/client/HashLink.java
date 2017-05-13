package com.android.logismove.client;

import com.android.logismove.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Created by Admin on 5/13/2017.
 */

public class HashLink {
    private final static String SEPARATE = "|";
    public static String secureLink(String uri, String time) {
        String secretKey = ClientConstants.KEY;
        String secretString = secretKey + SEPARATE + getRoute(uri) + SEPARATE + time;
        return getHash(secretString);
    }

    private static String getRoute(String str) {
        return str.replace("/",".");
    }

    public static String getHash(String str) {
        MessageDigest digest = null;
        byte[] input = null;

        try {
            digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            input = digest.digest(str.getBytes("UTF-8"));

        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return convertToHex(input);
    }
    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }
}
