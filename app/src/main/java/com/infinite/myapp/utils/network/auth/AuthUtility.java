package com.infinite.myapp.utils.network.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 and Base64 encoding and decoding
 *
 * @author weston
 */
public class AuthUtility {

    public static String md5Digest32(String input) {
        String str = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            str = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }

        return str;
    }

    //String in US-ASCII encoding
    public static String base64Encode(byte[] input) {
        return Base64.encodeToString(input, Base64.DEFAULT);
    }

    public static byte[] base64Decode(String input) {
        return Base64.decode(input, Base64.DEFAULT);
    }

    public static boolean isEqual(byte[] local, byte[] remote) {
        if (local == remote) {
            return true;
        }

        if (local.length != remote.length) {
            return false;
        }

        int lenght = local.length;
        for (int i = 0; i < lenght; i++) {
            if (local[i] != remote[i]) {
                return false;
            }
        }
        return true;
    }
}
