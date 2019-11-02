package org.feup.apm.genproducttag;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;

import javax.crypto.Cipher;

public class MainActivity extends AppCompatActivity {
    final String TAG = "GenProductTag";
    UUID productUUID;
    boolean hasKey = true;
    PrivateKey pri;
    PublicKey pub;
    byte[] encTag;

    EditText edUUID, edName, edEuros, edCents;
    TextView tvNoKey, tvKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edUUID = findViewById(R.id.ed_uuid);
        edName = findViewById((R.id.ed_name));
        edEuros = findViewById(R.id.ed_euros);
        edCents = findViewById((R.id.ed_cents));
        tvNoKey = findViewById(R.id.tv_nokey);
        tvKey = findViewById(R.id.tv_pubkey);
        findViewById(R.id.bt_ugen).setOnClickListener((v) -> {
            productUUID = UUID.randomUUID();
            edUUID.setText(productUUID.toString());
        });
        findViewById(R.id.bt_gentag).setOnClickListener((v) -> genTag());
        findViewById(R.id.bt_dectag).setOnClickListener((v) -> decTag());
        findViewById(R.id.bt_showkey).setOnClickListener((v) -> showKey());

        if (hasKey) {
            pri = Crypto.getPrivateKeyFromString(Constants.privateKeySupermarket);
            pub = Crypto.getPublicKeyFromString(Constants.publicKeySupermarket);
        }

        findViewById(R.id.bt_showkey).setEnabled(hasKey);
    }

    void genTag() {
        ByteBuffer tag;
        String name = edName.getText().toString();

        if (!hasKey || edUUID.getText().toString().length() == 0 || name.length() == 0 ||
                edEuros.getText().toString().length() == 0 || edCents.getText().toString().length() == 0) {
            tvNoKey.setText(R.string.msg_empty);
            return;
        }
        int len = 4 + 16 + 4 + 4 + 1 + edName.getText().toString().length();
        int l = edName.getText().toString().length();
        if (l > 35)
            name = edName.getText().toString().substring(0, 35);
        tag = ByteBuffer.allocate(len);
        tag.putInt(Constants.tagId);
        tag.putLong(productUUID.getMostSignificantBits());
        tag.putLong(productUUID.getLeastSignificantBits());
        tag.putInt(Integer.parseInt(edEuros.getText().toString()));
        tag.putInt(Integer.parseInt(edCents.getText().toString()));
        tag.put((byte) name.length());
        tag.put(name.getBytes(StandardCharsets.ISO_8859_1));

        try {
            Cipher cipher = Cipher.getInstance(Constants.ENC_ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, pri);
            encTag = cipher.doFinal(tag.array());
            tvKey.setText(getResources().getString(R.string.msg_enctag, encTag.length, byteArrayToHex(encTag)));
            tvNoKey.setText("");
        } catch (Exception e) {
            tvNoKey.setText(e.toString());
        }
        Intent qrAct = new Intent(this, QRTag.class);
        qrAct.putExtra("data", encTag);
        startActivity(qrAct);
    }

    void decTag() {
        byte[] clearTag;

        if (encTag == null || encTag.length == 0) {
            tvNoKey.setText(R.string.msg_notag);
            return;
        }
        try {
            Cipher cipher = Cipher.getInstance(Constants.ENC_ALGO);
            cipher.init(Cipher.DECRYPT_MODE, pub);
            clearTag = cipher.doFinal(encTag);
        } catch (Exception e) {
            tvNoKey.setText(e.getMessage());
            return;
        }
        ByteBuffer tag = ByteBuffer.wrap(clearTag);
        int tId = tag.getInt();
        UUID id = new UUID(tag.getLong(), tag.getLong());
        int euros = tag.getInt();
        int cents = tag.getInt();
        byte[] bName = new byte[tag.get()];
        tag.get(bName);
        String name = new String(bName, StandardCharsets.ISO_8859_1);

        String text = "DecTag (" + clearTag.length + "):\n" + byteArrayToHex(clearTag) + "\n\n" +
                ((tId == Constants.tagId) ? "correct" : "wrong") + "\n" +
                "ID: " + id.toString() + "\n" +
                "Name: " + name + "\n" +
                "Price: €" + euros + "." + cents;
        tvNoKey.setText("");
        tvKey.setText(text);
    }

    void showKey() {
        String keys = "PRIVATE KEY: " + Constants.privateKeySupermarket + "\nPUBLIC KEY: " + Constants.publicKeySupermarket + "\n";
        tvKey.setText(keys);
    }

    String byteArrayToHex(byte[] ba) {
        StringBuilder sb = new StringBuilder(ba.length * 2);
        for (byte b : ba)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
