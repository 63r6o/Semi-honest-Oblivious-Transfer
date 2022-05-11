package SemiHonestOT.Server;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jcajce.provider.asymmetric.elgamal.BCElGamalPublicKey;

import SemiHonestOT.Crypto.ElGamal;
import SemiHonestOT.Crypto.FakeKey;

public class Receiver {
    private ElGamal elGamal;
    private KeyPair keyPair;
    private FakeKey fakeKey;

    public Receiver() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
        elGamal = new ElGamal();
        keyPair = null;
        fakeKey = null;
    }

    // generating the public keys, so the we can only encrypt the choosen message
    public PublicKey[] getPublicKeys(boolean choiceBitBoolean) {
        PublicKey[] publicKeys = new PublicKey[2];

        int choosen = (choiceBitBoolean) ? 1 : 0;
        int notChoosen = (choiceBitBoolean) ? 0 : 1;

        keyPair = elGamal.generateKeyPair();
        BCElGamalPublicKey publicKey = (BCElGamalPublicKey) keyPair.getPublic();
        fakeKey = elGamal.generateRandomPublicKey(publicKey);

        publicKeys[choosen] = publicKey;
        publicKeys[notChoosen] = fakeKey;
        return publicKeys;
    }

    public byte[][] decryptMessages(byte[][] encryptedMessages) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[][] decryptedMessages = new byte[2][];
        decryptedMessages[0] = elGamal.decrypt(keyPair.getPrivate(), encryptedMessages[0]);
        decryptedMessages[1] = elGamal.decrypt(keyPair.getPrivate(), encryptedMessages[1]);
        return decryptedMessages;
    }
}