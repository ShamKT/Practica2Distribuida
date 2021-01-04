import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Clase que modela el contacto de un usuario.
 * Almacena el puerto de su servidor, su nombre y un hashmap con sus contactos.
 *
 * @author Orlando Ledesma Rincon
 */
public class Contact implements Serializable {

    private static final long serialVersionUID = -3108864461492601916L;

    private final int port;
    private String name;
    private volatile HashMap<String, Contact> contacts = new HashMap<>();


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

    public HashMap<String, Contact> getContacts() {
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
