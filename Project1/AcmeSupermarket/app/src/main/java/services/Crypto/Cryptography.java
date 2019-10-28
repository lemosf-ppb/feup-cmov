package services.Crypto;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.util.Log;

import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.security.auth.x500.X500Principal;

public class Cryptography {

    private byte[] buildMessage(byte[] message) {
        try {
            KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(Constants.keyname, null);
            PrivateKey pri = ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
            Signature sg = Signature.getInstance(Constants.SIGN_ALGO);
            sg.initSign(pri);
            sg.update(message, 0, message.length + 1);
            int sz = sg.sign(message, message.length + 1, Constants.KEY_SIZE/4);
            Log.d("DEBUG", "Sign size = " + sz + " bytes.");
        } catch (Exception ex) {
            Log.d("DEBUG", ex.getMessage());
        }
        return message;
    }

    public static boolean validate(byte[] message, byte[] signature) {
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

    private void generateAndStoreKeys(Context context) {
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
