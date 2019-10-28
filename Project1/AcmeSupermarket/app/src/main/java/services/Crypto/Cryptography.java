package services.Crypto;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.util.Log;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.security.auth.x500.X500Principal;

public class Cryptography {

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
            Log.d("DEBUG", "Msg size = " + message.length + " bytes.");
            Log.d("DEBUG", "Sign size = " + sign.length + " bytes.");
        } catch (Exception ex) {
            Log.d("DEBUG", ex.getMessage());
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
            Log.d("DEBUG", ex.getMessage());
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
            Log.d("DEBUG", ex.getMessage());
        }
    }

    PubKey getPubKey() {
        PubKey pkey = new PubKey();
        try {
            KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(Constants.keyname, null);
            PublicKey pub = ((KeyStore.PrivateKeyEntry) entry).getCertificate().getPublicKey();
            pkey.modulus = ((RSAPublicKey) pub).getModulus().toByteArray();
            pkey.exponent = ((RSAPublicKey) pub).getPublicExponent().toByteArray();
        } catch (Exception ex) {
            Log.d("DEBUG", ex.getMessage());
        }
        return pkey;
    }

    byte[] getPrivExp() {
        byte[] exp = null;

        try {
            KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(Constants.keyname, null);
            PrivateKey priv = ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
            exp = ((RSAPrivateKey) priv).getPrivateExponent().toByteArray();
        } catch (Exception ex) {
            Log.d("DEBUG", ex.getMessage());
        }
        if (exp == null)
            exp = new byte[0];
        return exp;
    }

    class PubKey {
        byte[] modulus;
        byte[] exponent;
    }
}
