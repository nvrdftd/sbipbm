package io.lungchen.sbipbm;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;
import java.util.UUID;

public class Account {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private UUID uuid;

    public Account(PrivateKey privateKey, PublicKey publicKey, UUID uuid) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.uuid = uuid;
    }

    public String sign(String msg) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");

        signature.initSign(privateKey);
        signature.update(msg.getBytes());

        return Base64.getEncoder().encodeToString(signature.sign());
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public UUID getUuid() {
        return uuid;
    }
}
