package services.crypto;

import android.util.Log;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;

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

    public static byte[] buildMessageWithSignature(PrivateKey privateKey, byte[] message) {
        ByteBuffer messageSigned = ByteBuffer.allocate(message.length + Constants.KEY_SIZE / 8);

        byte[] sign = signMessage(privateKey, message);
        messageSigned.put(message);
        messageSigned.put(sign);
        return messageSigned.array();
    }

    private static boolean validateMessage(PublicKey publicKey, byte[] message, byte[] messageSigned) {
        boolean verified = false;
        try {
            Signature sg = Signature.getInstance(Constants.SIGN_ALGO);
            sg.initVerify(publicKey);
            sg.update(message);
            verified = sg.verify(messageSigned);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            Log.e(Constants.TAG, e.getMessage());
        }

        return verified;
    }

    public static boolean validateMessageWithSignature(PublicKey publicKey, byte[] messageSigned) {
        ArrayList<byte[]> splitMessageSigned = splitMessageSigned(messageSigned);

        return validateMessage(publicKey, splitMessageSigned.get(0), splitMessageSigned.get(1));
    }

    private static ArrayList<byte[]> splitMessageSigned(byte[] messageSigned) {
        ArrayList<byte[]> result = new ArrayList<>();
        int sign_size = Constants.KEY_SIZE / 8;
        int mess_size = messageSigned.length - sign_size;

        ByteBuffer bb = ByteBuffer.wrap(messageSigned);
        byte[] message = new byte[mess_size];
        byte[] signature = new byte[sign_size];
        bb.get(message, 0, mess_size);
        bb.get(signature, 0, sign_size);
        result.add(message);
        result.add(signature);

        return result;
    }

    public static byte[] getMessageFromMessageSigned(byte[] messageSigned) {
        return splitMessageSigned(messageSigned).get(0);
    }
}
