package org.feup.apm.genproducttag;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

class Crypto {

    static PublicKey getPublicKeyFromString(String key) {
        String parsedKey = removePublicKeyPEMFormatHeaders(key);

        byte[] encoded = Base64.decode(parsedKey, Base64.DEFAULT);
        KeyFactory kf;
        try {
            kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(new X509EncodedKeySpec(encoded));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String removePublicKeyPEMFormatHeaders(String key) {
        String parsedKey = key.replace("\n", "");
        parsedKey = parsedKey.replace(Constants.PUBLIC_KEY_BEGIN, "");
        parsedKey = parsedKey.replace(Constants.PUBLIC_KEY_END, "");

        return parsedKey;
    }

    static PrivateKey getPrivateKeyFromString(String key) {
        String parsedKey = removePrivateKeyPEMFormatHeaders(key);

        byte[] encoded = Base64.decode(parsedKey, Base64.DEFAULT);
        KeyFactory kf;
        try {
            kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(new PKCS8EncodedKeySpec(encoded));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }


    private static String removePrivateKeyPEMFormatHeaders(String key) {
        String parsedKey = key.replace("\n", "");
        parsedKey = parsedKey.replace(Constants.PRIVATE_KEY_BEGIN, "");
        parsedKey = parsedKey.replace(Constants.PRIVATE_KEY_END, "");

        return parsedKey;
    }
}
