import java.util.LinkedList;
import java.util.Objects;

public class Contact {

    private final int port;
    private String name;
    private volatile LinkedList<Contact> contacts = new LinkedList<>();

    public Contact(int port, String name) {
        this.port = port;
        this.name = name;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Contact> getContacts() {
        return contacts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Contact contact = (Contact) o;
        return port == contact.getPort() || name == contact.getName();
    }

    @Override
    public int hashCode() {
        return Objects.hash(port, name);
    }
}
