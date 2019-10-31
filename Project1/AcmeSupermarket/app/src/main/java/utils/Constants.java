package utils;

public class Constants {

    public static final String BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----";
    public static final String END_PUBLIC_KEY = "-----END PUBLIC KEY-----";
    public static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    public static final int KEY_SIZE = 512;
    public static final String KEY_ALGO = "RSA";
    public static final int CERT_SERIAL = 12121212;
    public static final String ENC_ALGO = "RSA/NONE/PKCS1Padding";
    public static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    public static final String QR_READER_PLAYSTORE = "market://search?q=pname:com.google.zxing.client.android";
    public static final String key = "-----BEGIN PUBLIC KEY-----\n" +
            "    MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALlRwQFsg3LevnTUNHArTQK2BMpPuAYT+vxU8o+6ar9U\n" +
            "    5HKO7C4HzrKkLOBch2H3w/cswry/y7Ksfh6D2rZAJQ0CAwEAAQ==\n" +
            "    -----END PUBLIC KEY-----";
    public static String keyname = "AcmeKey";
    public static int tagId = 0x41636D65;        // equal to "Acme"
}
