package services.crypto;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;
import android.util.Log;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.security.auth.x500.X500Principal;

public class Cryptography {
    static final String TAG = "Cryptography";

    public static byte[] buildMessage(byte[] message) {
        ByteBuffer messageSigned = ByteBuffer.allocate(message.length + Constants.KEY_SIZE / 8);
        try {
            KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(Constants.keyname, null);
            PrivateKey pri = ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
            Signature sg = Signature.getInstance(Constants.SIGN_ALGO);
            sg.initSign(pri);
            sg.update(message);
            byte[] sign = sg.sign();
            messageSigned.put(message);
            messageSigned.put(sign);
            Log.d(TAG, "Msg size = " + message.length + " bytes.");
            Log.d(TAG, "Sign size = " + sign.length + " bytes.");
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
        return messageSigned.array();
    }

    public static boolean validate(byte[] messageSigned) {
        int sign_size = Constants.KEY_SIZE / 8;
        int mess_size = messageSigned.length - sign_size;

        ByteBuffer bb = ByteBuffer.wrap(messageSigned);
        byte[] message = new byte[mess_size];
        byte[] signature = new byte[sign_size];
        bb.get(message, 0, mess_size);
        bb.get(signature, 0, sign_size);

        boolean verified = false;
        try {
            KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(Constants.keyname, null);
            PublicKey pub = ((KeyStore.PrivateKeyEntry) entry).getCertificate().getPublicKey();
            Signature sg = Signature.getInstance(Constants.SIGN_ALGO);
            sg.initVerify(pub);
            sg.update(message);
            verified = sg.verify(signature);
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }

        return verified;
    }

    public static byte[] getMessageFromMessageSigned(byte[] messageSigned) {
        int sign_size = Constants.KEY_SIZE / 8;
        int mess_size = messageSigned.length - sign_size;

        ByteBuffer bb = ByteBuffer.wrap(messageSigned);
        byte[] message = new byte[mess_size];
        byte[] signature = new byte[sign_size];
        bb.get(message, 0, mess_size);
        bb.get(signature, 0, sign_size);

        return message;
    }


    public static void generateAndStoreKeys(Context context) {
        try {
            KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(Constants.keyname, null);
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
            Log.d(TAG, ex.getMessage());
        }
    }

    public static PublicKey getPublicKey() {
        PublicKey pub = null;
        try {
            KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(Constants.keyname, null);
            pub = ((KeyStore.PrivateKeyEntry) entry).getCertificate().getPublicKey();
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }

        return pub;
    }

    public static PrivateKey getPrivateKey() {
        PrivateKey priv = null;
        try {
            KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(Constants.keyname, null);
            priv = ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }

        return priv;
    }

    public static PublicKey getPublicKeyFromString(String key) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String parsedKey = removePublicKeyPEMFormatHeaders(key);

        byte[] encoded = Base64.decode(parsedKey, Base64.DEFAULT);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return kf.generatePublic(new X509EncodedKeySpec(encoded));
    }

    private static String removePublicKeyPEMFormatHeaders(String key) {
        String parsedKey = key.replace("\n", "");
        parsedKey = parsedKey.replace(Constants.PUBLIC_KEY_BEGIN, "");
        parsedKey = parsedKey.replace(Constants.PUBLIC_KEY_END, "");

        return parsedKey;
    }

    public static String convertToPublicKeyPEMFormat(Key key) {
        byte[] encodedKey = key.getEncoded();
        return Constants.PUBLIC_KEY_BEGIN + "\n" +
                Base64.encodeToString(encodedKey, Base64.DEFAULT) +
                Constants.PUBLIC_KEY_END + "\n";
    }
}
