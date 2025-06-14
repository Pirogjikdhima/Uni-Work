package HashTable;

public class Test {
    public static void main(String[] args) {
        Libri libri1 = new Libri("Abetare", 1920, "Shkronjat");
        Libri libri2 = new Libri("Matematika", 1500, "Formula");

        Librari libraria = new Librari();

        libraria.shtoLiber(libri1);
        libraria.shtoLiber(libri2);


        System.out.println("Merr libri1 " + libraria.getLibri("Abetare").getEmri());
        System.out.println("Merr libri2 " + libraria.getLibri("Matematika").getEmri());

        libraria.fshiLibri("Abetare");
        System.out.println("Merr libri1 " + libraria.getLibri("Abetare").getEmri());
    }
}
