package SemiHonestOT.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class Client {
    public static void main(String[] args) throws UnknownHostException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        
        // create our messages
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the first secret message:");
        String message0 = scanner.nextLine();
        System.out.println("Enter the second secret message:");
        String message1 = scanner.nextLine();
        scanner.close();
        Sender sender = new Sender(message0, message1);

        // connect to the server
        Socket socket = new Socket("localhost", 4999);

        // get the public keys from the server
        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        PublicKey[] publicKeys = new PublicKey[2];
        KeyFactory kf = KeyFactory.getInstance("ElGamal");

        for (int i = 0; i < publicKeys.length; i++) {
            String base64EncodedKey = bufferedReader.readLine();
            byte[] publicKeyData = Base64.getDecoder().decode(base64EncodedKey);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyData);
            publicKeys[i] = kf.generatePublic(spec);
        }
        
        // encrypt the messages and send them to the server
        System.out.println("Encrypting messages...");
        byte[][] encryptedMessages = sender.getEncrypted(publicKeys);

        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        for (byte[] message : encryptedMessages) {
            String encodedMessage = Base64.getEncoder().encodeToString(message);
            printWriter.println(encodedMessage);
            printWriter.flush();
        }
        System.out.println("The encrypted messages were sent to the server");

        // not sure if needed, but better safe than sorry
        socket.close();
    }
}