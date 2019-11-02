package services.crypto;

import android.util.Log;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import static utils.Utils.byteArrayToHex;

public class CryptoBuilder {

    private static byte[] signMessage(PrivateKey privateKey, byte[] message) {
        byte[] sign = new byte[0];
        try {
            Signature sg = Signature.getInstance(Constants.SIGN_ALGO);
            sg.initSign(privateKey);
            sg.update(message);
            sign = sg.sign();
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            Log.e(Constants.TAG, e.getMessage());
        }

        return sign;
    }

    public static String signMessageHex(PrivateKey privateKey, byte[] message) {
        return byteArrayToHex(signMessage(privateKey, message));
    }

    public static boolean validateMessage(PublicKey publicKey, byte[] message, byte[] signature) {
        boolean verified = false;
        try {
            Signature sg = Signature.getInstance(Constants.SIGN_ALGO);
            sg.initVerify(publicKey);
            sg.update(message);
            verified = sg.verify(signature);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            Log.e(Constants.TAG, e.getMessage());
        }

        return verified;
    }
}
