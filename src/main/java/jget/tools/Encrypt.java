package jget.tools;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypt {
    // if runtime command exec failed, these method may be used
    public static String getSha256(String originalStr){
        return baseEncrypt(originalStr, "SHA-256");
    }
    public static String baseEncrypt(String str, String method){
        if(str !=null && !str.isEmpty()){
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(method);
                byte[] result = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
                StringBuilder stringBuilder = new StringBuilder();
                for(var i: result){
                    String hex = Integer.toHexString(0xff&i);
                    if(hex.length() == 1)
                        stringBuilder.append('0');
                    stringBuilder.append(hex);
                }
                return stringBuilder.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
}
