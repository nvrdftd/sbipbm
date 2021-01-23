package io.lungchen.sbipbm;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;

public class AccountFactory {

    private static Integer id;
    private static KeyPairGenerator keyPairGenerator;
    private static AccountFactory accountFactory;

    private AccountFactory() throws Exception {
        this.id = 0;

        try {
            this.keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            this.keyPairGenerator.initialize(2048);
        } catch (Exception e) {
            throw new InstantiationException();
        }

    }

    public static AccountFactory getAccountFactory() throws Exception {
        if (accountFactory == null) {
            accountFactory = new AccountFactory();
        }
        return accountFactory;
    }

    public Account newAccount() {
        UUID uuid = UUID.randomUUID();
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        return new Account(privateKey, publicKey, uuid);
    }
}
