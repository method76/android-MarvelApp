package com.method76.common.util;


/**
 * Created by Sungjoon Kim on 2016-02-05.
 */
public class ByteUtil {

    public static String toHexString(byte abyte0[]){
        return toHexString(abyte0, "");
    }

    public static String toHexString(byte abyte0[], String s){
        return toHexString(abyte0, s, false);
    }

    public static String toHexString(byte abyte0[], String s, boolean flag){
        return toHexString(abyte0, 0, abyte0.length, s, flag);
    }

    public static String toHexString(byte abyte0[], int i, int j, String s){
        return toHexString(abyte0, i, j, s, false);
    }

    public static String toHexString(byte abyte0[], int i, int j, String s,
                                     boolean flag){
        if (null == abyte0)
            return "";
        if (null == s)
            s = "";
        StringBuffer stringbuffer = new StringBuffer();
        int k = 0;
        for (int l = j - 1; l >= 0; l--) {
            String s1 = Integer.toHexString(abyte0[i + l] & 0xff);
            stringbuffer.insert(0, s1);
            for (int i1 = s1.length(); i1 < 2; i1++)
                stringbuffer.insert(0, "0");

            k++;
            if (!flag)
                stringbuffer.insert(0, s);
            else if (flag && 2 == k)
                stringbuffer.insert(0, s);
            if (2 == k)
                k = 0;
        }

        return stringbuffer.toString().toUpperCase().trim();
    }

    public static byte[] fromHexString(String s){
        if (null == s || s.length() < 1)
            return new byte[0];
        s = s.toUpperCase();
        StringBuffer stringbuffer = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ('0' <= c && c <= '9') {
                stringbuffer.append(c);
                continue;
            }
            if ('A' <= c && c <= 'F')
                stringbuffer.append(c);
        }

        if ((stringbuffer.length() / 2) * 2 != stringbuffer.length())
            stringbuffer.insert(0, "0");
        String s1 = stringbuffer.toString();
        byte abyte0[] = new byte[s1.length() / 2];
        Object obj = null;
        for (int j = 0; j < abyte0.length; j++)
            try {
                String s2 = s1.substring(j * 2, j * 2 + 2);
                abyte0[j] = (byte)Integer.parseInt(s2, 16);
            } catch (Exception exception) {
            }

        return abyte0;
    }

    public static String toBitString(byte abyte0[], int i){
        if (null == abyte0)
            return "";
        StringBuffer stringbuffer = new StringBuffer();
        Object obj = null;
        for (int j = abyte0.length - 1; j >= 0; j--) {
            String s = Integer.toBinaryString(abyte0[j] & 0xff);
            stringbuffer.insert(0, s);
            if (j <= 0)
                continue;
            for (int l = s.length(); l < 8; l++)
                stringbuffer.insert(0, "0");

        }

        if (i > 0) {
            int k = stringbuffer.length();
            for (int i1 = k; i1 < i; i1++)
                stringbuffer.insert(0, "0");

        }
        return stringbuffer.toString();
    }

    public static byte[] fromBitString(String s){
        if (null == s || s.length() < 1)
            return new byte[0];
        StringBuffer stringbuffer = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ('0' == c || '1' == c)
                stringbuffer.append(c);
        }

        int j = stringbuffer.length() / 8;
        if (j * 8 != stringbuffer.length()) {
            j++;
            do
                stringbuffer.insert(0, "0");
            while (stringbuffer.length() < j * 8);
        }
        int k = stringbuffer.length() - 1;
        byte abyte0[] = new byte[j];
        for (int l = j - 1; l >= 0; l--) {
            int i1 = 0;
            for (int j1 = 0; j1 < 8; j1++)
                if ('1' == stringbuffer.charAt(k - j1))
                    i1 = (int)((double)i1 + Math.pow(2D, j1));

            abyte0[l] = (byte)i1;
            k -= 8;
        }

        return abyte0;
    }

    public static byte[] toBitArray(byte abyte0[]){
        return toBitArray(abyte0, 0, abyte0.length);
    }


    public static byte[] toBitArray(byte abyte0[], int i, int j){
        if (null == abyte0 || 0 == abyte0.length)
            return new byte[0];
        byte abyte1[] = new byte[j * 8];
        for (int k = 0; k < j; k++) {
            byte byte0 = abyte0[i + k];
            for (int l = 0; l < 8; l++) {
                byte byte1 = 0;
                if (0 != (byte0 & 0x80))
                    byte1 = 1;
                abyte1[k * 8 + l] = byte1;
                byte0 <<= 1;
            }

        }

        return abyte1;
    }

    public static byte[] fromBitArray(byte abyte0[]){
        if (null == abyte0 || 0 == abyte0.length)
            return new byte[0];
        int i = abyte0.length / 8;
        if (i * 8 < abyte0.length)
            i++;
        byte abyte1[] = new byte[i];
        int j = i - 1;
        int k = abyte0.length - 1;
        int l = 1;
        do {
            if (1 == abyte0[k])
                abyte1[j] = (byte)(abyte1[j] | l);
            k--;
            l <<= 1;
            if ((double)l > Math.pow(2D, 7D)) {
                l = 1;
                j--;
            }
        } while (k >= 0);
        return abyte1;
    }

}
