package HashTable;

public class Libri {
    private String emri;
    private String permbajtja;
    private int data_publikimit;

    public Libri(String emri, int data, String permbajtja) {
        this.emri = emri;
        this.data_publikimit = data;
        this.permbajtja = permbajtja;
    }

    public String getEmri() {
        return this.emri;
    }

    public void setEmri(String emri) {
        this.emri = emri;
    }

    public int getData() {
        return this.data_publikimit;
    }

    public void setData(int data) {
        this.data_publikimit = data;
    }

    public String getPermbajtja() {
        return this.permbajtja;
    }

    public void setPermbajtja(String p) {
        this.permbajtja = p;
    }

    public String toString() {
        return "Emri: " + this.emri + " (" + this.data_publikimit + ")\n" + "Permbajtja: " + this.permbajtja;
    }
}
