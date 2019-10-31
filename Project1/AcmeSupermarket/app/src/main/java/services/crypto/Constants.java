package services.crypto;

public class Constants {
    static final String TAG = "Crypto";
    static final int KEY_SIZE = 512;
    static final String ANDROID_KEYSTORE = "AndroidKeyStore";

    public static final String ENC_ALGO = "RSA/NONE/PKCS1Padding";
    static final String KEY_ALGO = "RSA";
    static final String SIGN_ALGO = "SHA256WithRSA";
    static final String PUBLIC_KEY_BEGIN = "-----BEGIN PUBLIC KEY-----";
    static final String PUBLIC_KEY_END = "-----END PUBLIC KEY-----";

    static String keyname = "acmeClientKey";
}
