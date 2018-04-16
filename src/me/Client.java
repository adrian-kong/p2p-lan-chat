package me;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author Adrian
 * @since 16/04/2018
 */
public class Client {

    private User user;

    public Client() throws Exception {
        File file = new File("user.properties");
        if (file.exists()) {
            Properties properties = new Properties();
            InputStream inputStream = new FileInputStream(file);
            properties.load(inputStream);
            inputStream.close();
            user = new User(properties);
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("ID (0 - 255): ");
            int ID = Integer.parseInt(scanner.nextLine());
            System.out.print("Port (0 - 65535): ");
            int PORT = Integer.parseInt(scanner.nextLine());
            user = new User(name, ID, PORT);
        }
        new Chat(user);
    }
}
