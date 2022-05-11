package SemiHonestOT.Crypto;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.elgamal.BCElGamalPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.BigIntegers;

public class ElGamal {
    private Cipher cipher;
    private KeyPairGenerator generator;
    private SecureRandom random;

    public ElGamal() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
        Security.addProvider(new BouncyCastleProvider());

        cipher = Cipher.getInstance("ELGamal/None/NoPadding", "BC");
        generator = KeyPairGenerator.getInstance("ElGamal", "BC");

        random = new SecureRandom();
        // is 1024 reasonable?
        generator.initialize(2048, random);
    }


    public KeyPair generateKeyPair() {
        return generator.generateKeyPair();
    }


    // Creating a random public key, for which we don't know the private key for
    public FakeKey generateRandomPublicKey(BCElGamalPublicKey pubKey) {
        BigInteger g = pubKey.getParameters().getG();
        BigInteger p = pubKey.getParameters().getP();
        BigInteger pMinusOne = p.subtract(BigInteger.ONE);
        BigInteger y = BigIntegers.createRandomInRange(BigInteger.ONE, pMinusOne, random);

        ElGamalParameters params = new ElGamalParameters(p, g);
        ElGamalPublicKeyParameters pubParams = new ElGamalPublicKeyParameters(y, params);
        return new FakeKey(pubParams);
    }

    public byte[] encrypt(Key publicKey, byte[] message) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey, random);
        return cipher.doFinal(message);
    }

    public byte[] decrypt(Key privateKey, byte[] cipherText) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(cipherText);
    }
    
}
