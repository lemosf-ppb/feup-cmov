package services.crypto;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.security.auth.x500.X500Principal;

public class CryptoKeysManagement {

    private static KeyStore.Entry getKeyStoreEntry() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableEntryException {
        KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
        ks.load(null);
        return ks.getEntry(Constants.keyname, null);
    }

    public static void generateAndStoreKeys(Context context) {
        try {
            KeyStore.Entry entry = getKeyStoreEntry();
            if (entry == null) {
                Calendar start = new GregorianCalendar();
                Calendar end = new GregorianCalendar();
                end.add(Calendar.YEAR, 20);
                KeyPairGenerator kgen = KeyPairGenerator.getInstance(Constants.KEY_ALGO, Constants.ANDROID_KEYSTORE);
                AlgorithmParameterSpec spec = new KeyPairGeneratorSpec.Builder(context)
                        .setKeySize(Constants.KEY_SIZE)
                        .setAlias(Constants.keyname)
                        .setSubject(new X500Principal("CN=" + Constants.keyname))   // Usually the full name of the owner (person or organization)
                        .setSerialNumber(BigInteger.valueOf(12121212))
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                kgen.initialize(spec);
                kgen.generateKeyPair();
            }
        } catch (Exception ex) {
            Log.d(Constants.TAG, ex.getMessage());
        }
    }

    public static PublicKey getPublicKey() {
        try {
            KeyStore.Entry entry = getKeyStoreEntry();
            return ((KeyStore.PrivateKeyEntry) entry).getCertificate().getPublicKey();
        } catch (Exception ex) {
            Log.d(Constants.TAG, ex.getMessage());
        }

        return null;
    }

    public static PrivateKey getPrivateKey() {
        try {
            KeyStore.Entry entry = getKeyStoreEntry();
            return ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
        } catch (Exception ex) {
            Log.d(Constants.TAG, ex.getMessage());
        }

        return null;
    }

    public static PublicKey getPublicKeyFromString(String key) {
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

    public static PrivateKey getPrivateKeyFromString(String key) {
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

    public static String convertToPublicKeyPEMFormat(Key key) {
        byte[] encodedKey = key.getEncoded();
        return Constants.PUBLIC_KEY_BEGIN + "\n" +
                Base64.encodeToString(encodedKey, Base64.DEFAULT) +
                Constants.PUBLIC_KEY_END + "\n";
    }
}
