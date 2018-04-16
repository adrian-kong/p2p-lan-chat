package me;

import java.io.FileOutputStream;
import java.util.Properties;
import java.util.UUID;

/**
 * @author Adrian
 * @since 16/04/2018
 */
public class User {

    private String name;
    private UUID uuid;
    private Properties properties;

    private int ID;
    private int PORT;

    public User(Properties properties) {
        loadFromProperties(properties);
    }

    public User(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public User(String name, int ID, int PORT) {
        this.name = name;
        this.ID = ID;
        this.PORT = PORT;
        uuid = UUID.randomUUID();

        properties = new Properties();
        properties.setProperty("name", name);
        properties.setProperty("id", String.valueOf(ID));
        properties.setProperty("port", String.valueOf(PORT));
        properties.setProperty("uuid", uuid.toString());

        try {
            properties.store(new FileOutputStream("user.properties"), null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadFromProperties(Properties properties) {
        this.properties = properties;
        this.name = properties.getProperty("name");
        this.ID = Integer.parseInt(properties.getProperty("id"));
        this.PORT = Integer.parseInt(properties.getProperty("port"));
        this.uuid = UUID.fromString(properties.getProperty("uuid"));
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getPORT() {
        return PORT;
    }

    public String getIdentifier() {
        return "{" + getName() + ":" + getUuid().toString() + "}";
    }

    /**
     * Multicast Address
     * @return address for socket
     */
    public String getINetAddress() {
        return "224.0.0." + ID;
    }

    /**
     * Overrides equals in list.contains method in {@link Chat}
     *
     * @param obj to check
     * @return whether they are equal
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof User ? getIdentifier().equals(((User) obj).getIdentifier()) : super.equals(obj);
    }
}
