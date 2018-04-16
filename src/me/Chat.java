package me;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

/**
 * @author Adrian
 * @since 16/04/2018
 */
public class Chat {

    private ArrayList<User> connectedUsers;

    private InetAddress inetAddress;
    private MulticastSocket socket;
    private User user;

    private final byte[] buffer = new byte[256];

    public Chat(User user) throws Exception {
        this.connectedUsers = new ArrayList();
        this.user = user;
        connectedUsers.add(user);

        (socket = new MulticastSocket(user.getPORT())).joinGroup(inetAddress = InetAddress.getByName(user.getINetAddress()));
        new Thread(() -> receivePacket()).start();
        new Thread(() -> sendMessage()).start();
        new Thread(() -> keepAlive()).start();

        print(">> " + user.getName() + " has joined the chat!");
    }

    public void keepAlive() {
        while (true) {
            try {
                print(user.getIdentifier() + "~keepAlive");
                Thread.sleep(5000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sendMessage() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String message = scanner.nextLine();
                if (!message.isEmpty()) {
                    if (message.equalsIgnoreCase("/list")) {
                        connectedUsers.stream().map(User::getName).forEach(System.out::println);
                        continue;
                    }
                    sendClientMessage(message);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void receivePacket() {
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());

                if (message.startsWith("{") && message.contains("}") && message.contains("~")) {
                    String identifier = message.split("~")[0];
                    if (identifier.endsWith("}")) {
                        String nameUUID = identifier.substring(1, identifier.length() - 1);
                        String[] split = nameUUID.split(":");
                        User user = new User(split[0], UUID.fromString(split[1]));
                        if (!connectedUsers.contains(user)) {
                            connectedUsers.add(user);
                        }
                        if (message.endsWith("keepAlive")) {
                            continue;
                        }
                        message = message.replace(identifier + "~", "[" + user.getName() + "] >> ");
                    }
                }
                System.out.println(message);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void print(String message) throws Exception {
        socket.send(new DatagramPacket(message.getBytes(), message.getBytes().length, inetAddress, user.getPORT()));
    }

    public void sendClientMessage(String message) throws Exception {
        print(user.getIdentifier() + "~" + message);
    }
}
