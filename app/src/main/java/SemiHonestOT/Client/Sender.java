package SemiHonestOT.Client;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import SemiHonestOT.Crypto.ElGamal;

public class Sender {
    private byte[][] message;
    private ElGamal elGamal;

    public Sender(String message0, String message1) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
        message = new byte[2][];
        message[0] = message0.getBytes();
        message[1] = message1.getBytes();

        elGamal = new ElGamal();
    }

    // encrypt the messages using ElGamal with th public keys supplied
    public byte[][] getEncrypted(PublicKey[] publicKeys) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[][] encryptedMessages = new byte[2][];
        encryptedMessages[0] = elGamal.encrypt(publicKeys[0], message[0]);
        encryptedMessages[1] = elGamal.encrypt(publicKeys[1], message[1]);
        return encryptedMessages;
    }
}
