package HashTable;

import java.util.HashMap;

public class Librari {
    private HashMap<String, Libri> librari;

    public Librari() {
        librari = new HashMap<>();
    }

    public void shtoLiber(Libri l) {
        if (librari.containsKey(l.getEmri())) {
            System.out.println("Libri gjendet në librari");
            return;
        }
        librari.put(l.getEmri(), l);
    }

    public Libri getLibri(String titulli) {
        if (librari.containsKey(titulli)) {
            return librari.get(titulli);
        }
        System.out.println("Libri nuk gjendet në librari");
        return null;
    }

    public void fshiLibri(String titulli) {
        if (librari.containsKey(titulli)) {
            librari.remove(titulli);
            return;
        }
        System.out.println("Libri me këtë titull nuk ekziston");
    }
}
