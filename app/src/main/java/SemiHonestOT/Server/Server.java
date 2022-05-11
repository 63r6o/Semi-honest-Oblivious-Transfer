package SemiHonestOT.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class Server {
    private static PublicKey[] publicKeys;
    private static boolean choiceBitBoolean;
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Receiver receiver = new Receiver();

        // estabilishing the connection
        ServerSocket serverSocket = new ServerSocket(4999);
        System.out.println("Waiting for the client to connect");
        Socket socket = serverSocket.accept();
        System.out.println("Client connected");

        // getting the choicebit
        Scanner scanner = new Scanner(System.in);
        System.out.println("Which message do you want to read? Type 0 or 1:");
        choiceBitBoolean = (scanner.nextInt() == 1) ? true : false;
        scanner.close();

        // create the public keys and send them to the client
        publicKeys = receiver.getPublicKeys(choiceBitBoolean);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        for (PublicKey publicKey : publicKeys) {
            String encodedKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            printWriter.println(encodedKey);
            printWriter.flush();
        }

        // read the and decrypt the messages from the client
        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        byte[][] messages = new byte[2][];
        for (int i = 0; i < messages.length; i++) {
            String base64EncodedMessage = bufferedReader.readLine();
            messages[i] = Base64.getDecoder().decode(base64EncodedMessage);
        }
        System.out.println("got the encrypted texts");
        byte[][] decryptedMessages = receiver.decryptMessages(messages);
        for (byte[] message: decryptedMessages) {
            System.out.println(new String(message));
        }

        // not sure if needed, but bettter safe than sorry
        serverSocket.close();
    }
}