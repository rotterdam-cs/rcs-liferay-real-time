package com.rcs.liferaysense.utils;

import com.liferay.portal.kernel.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility classes for applying different types of hash functions to data.
 * @author juan
 */
public class HashUtils {

    private static final Logger logger; 
    private static MessageDigest mdMD5;
    private static MessageDigest mdSHA;
    
    
    static {
        logger = LoggerFactory.getLogger(HashUtils.class);
        try {
            mdMD5 = MessageDigest.getInstance("MD5");
            mdSHA = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException ex){
            logger.error("Error while trying to encode data", ex);
        }
    }
    
    /**
     * Encode data using the md5 hash algorithm and also applies a Base64 encode.
     * @param data
     * @return 
     */
    public static String md5Base64(byte[] data) {
        return encodeBase64(data, mdMD5);
    }
    
    /**
     * Encode data using the sha hash algorithm and also applies a Base64 encode.
     * @param data
     * @return 
     */
    public static String shaBase64(byte[] data) {
        return encodeBase64(data, mdSHA);
    }
    
    
    private static String encodeBase64(byte[] data, MessageDigest md) {
        if (md == null)
            return null;
        byte[] hash  = md.digest(data);
        return Base64.encode(hash);
    }
    
    /**
     * Encode data using the md5 algorithm and returun it as a string of
     * hexadecimal characters.
     * @param data
     * @return 
     */
    public static String md5(byte[] data) {
        return encode(data, mdMD5);
    }
    
    /**
     * Encode data using the sha algorithm and returun it as a string of
     * hexadecimal characters.
     * @param data
     * @return 
     */
    public static String sha(byte[] data) {
        return encode(data, mdSHA);
    }
    
    private static String encode(byte[] data, MessageDigest md) {
        if (md == null)
            return null;
        byte[] hash  = md.digest(data);
        StringBuilder builder = new StringBuilder();

         for (byte character : hash) {
             builder.append(String.format("%02x", character));
         }

        return builder.toString();
    }
    
    /**
     * Convenience method for md5 - hashing strings.
     * @param data
     * @return 
     */
    public static String md5(String data) {
        return encode(data, mdMD5);
    }
    
    /**
     * Convenience method for sha - hashing strings.
     * @param data
     * @return 
     */
    public static String sha(String data) {
        return encode(data, mdSHA);
    }
    
    private static String encode(String data, MessageDigest md) {
        if (data == null || md == null) {
            return null;
        }
        
        if (md == mdMD5)
            return md5(data.getBytes());
        else if (md == mdSHA)
            return sha(data.getBytes());
        return null;
    }
}
