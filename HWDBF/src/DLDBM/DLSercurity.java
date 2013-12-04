/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DLDBM;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

class DLSercurity {
  Cipher ecipher;
  Cipher dcipher;
  SecretKeySpec key;
  DLSercurity(String string8char) throws Exception {
    key= new SecretKeySpec(string8char.getBytes(), "DES");
    ecipher = Cipher.getInstance("DES");
    dcipher = Cipher.getInstance("DES");
    ecipher.init(Cipher.ENCRYPT_MODE, key);
    dcipher.init(Cipher.DECRYPT_MODE, key);
  }

  public String Encrypt(String str) throws Exception {
    // Encode the string into bytes using utf-8
    byte[] utf8 = str.getBytes("UTF8");

    // Encrypt
    byte[] enc = ecipher.doFinal(utf8);

    // Encode bytes to base64 to get a string
    return new sun.misc.BASE64Encoder().encode(enc);
  }

  public String Decrypt(String str) throws Exception {
    // Decode base64 to get bytes
    byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

    byte[] utf8 = dcipher.doFinal(dec);

    // Decode using utf-8
    return new String(utf8, "UTF8");
  }

}