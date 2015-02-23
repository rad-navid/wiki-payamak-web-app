package com.peaceworld.wikisms.controller;
 
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
 
/**
 *  
 * @author Giulio
 */
public class CryptoMessage {
 
	//
	private String algorithm = "PBEWithMD5AndDES";
	private String passPhrase = "DBmovedSDcart999";
    private Key key;
    private AlgorithmParameterSpec paramSpec;
 
    /**
     * Generates the encryption key. using "des" algorithm
     * 
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException 
     * @throws InvalidKeySpecException 
     */
    private void generateKey() throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
    	
    	  // 8-bytes Salt
        byte[] salt = {
            (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
            (byte)0x56, (byte)0x34, (byte)0xE3, (byte)0x03
        };

        // Iteration count
        int iterationCount = 19;
        
        KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
        SecretKey key = SecretKeyFactory.getInstance(algorithm).generateSecret(keySpec);
        this.key=key;
        
        // Prepare the parameters to the cipthers
        paramSpec = new PBEParameterSpec(salt, iterationCount);

    }
 
    public String encrypt(String message) throws IllegalBlockSizeException,
	    BadPaddingException, NoSuchAlgorithmException,
	    NoSuchPaddingException, InvalidKeyException,
	    UnsupportedEncodingException, InvalidAlgorithmParameterException {

    	Cipher cipher = Cipher.getInstance(algorithm);
	cipher.init(Cipher.ENCRYPT_MODE, key,paramSpec);
 
	// Gets the raw bytes to encrypt, UTF8 is needed for
	// having a standard character set
	byte[] stringBytes = message.getBytes("UTF8");
 
	// encrypt using the cypher
	byte[] raw = cipher.doFinal(stringBytes);
 
	// converts to base64 for easier display.
	BASE64Encoder encoder = new BASE64Encoder();
	String base64 = encoder.encode(raw);
 
	return base64;
    }
 
      public String decrypt(String encrypted) throws InvalidKeyException,
	    NoSuchAlgorithmException, NoSuchPaddingException,
	    IllegalBlockSizeException, BadPaddingException, IOException, InvalidAlgorithmParameterException {
 
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, key,paramSpec);
 
		//decode the BASE64 coded message
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] raw = decoder.decodeBuffer(encrypted);
 
		//decode the message
		byte[] stringBytes = cipher.doFinal(raw);
 
		//converts the decoded message to a String
		String clear = new String(stringBytes, "UTF8");
		return clear;
    }
 
    public CryptoMessage() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
    	
    	generateKey();
	
    }
 
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
    	String message="would it work for sake of god ?! ";
    	
    	
    	try {
    		CryptoMessage cripto=new CryptoMessage();
    	    System.out.println("clear message: " + message);
     
    	    String encrypted = cripto.encrypt(message);
    	    System.out.println("encrypted message: " + encrypted);
     
    	    String decrypted = cripto.decrypt(encrypted);
    	    System.out.println("decrypted message: " + decrypted);
     
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
 
    }
 
}