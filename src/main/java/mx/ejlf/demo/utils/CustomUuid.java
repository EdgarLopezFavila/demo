package mx.ejlf.demo.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class CustomUuid {

    /**
     * Metodo para regresar un UUID compuesto (Timestamp + UUID)
     *
     * @return
     */
    public static String uuid () {
        String uuid = CustomTimestamp.Timestamp() + UUID.randomUUID();
        return uuid;
    }

    /**
     * Metodo para regresar un UUID quitando los guiones y sin timestamp
     *
     * @return
     */
    public static String pureUuid () {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-","");
    }

    /**
     * Metodo para hace un cifrado
     * @param text
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String toSHA256(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        sha256.update(text.getBytes("UTF-8"));
        byte[] _digest = sha256.digest();
        StringBuffer sb=new StringBuffer();

        for(int i=0; i<_digest.length; i++){
            sb.append(String.format("%02x", _digest[i]));
        }
        return sb.toString();
    }
}
