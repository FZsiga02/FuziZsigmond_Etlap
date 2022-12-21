package hu.petrik.etlap;

public class Etlap {

    private int id;
    private String nev;
    private String leiras;
    private int ar;
    private String kategoria;

    public Etlap(int id, String nev, String leiras, int ar, String kategoria) {
        this.id = id;
        this.nev = nev;
        this.leiras = leiras;
        this.ar = ar;
        this.kategoria = kategoria;
    }

    public Etlap(String nev, String leiras, int ar, String kategoria){
        this.nev = nev;
        this.leiras = leiras;
        this.ar = ar;
        this.kategoria = kategoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public String getLeiras() {
        return leiras;
    }

    public void setLeiras(String leiras) {
        this.leiras = leiras;
    }

    public int getAr() {
        return ar;
    }

    public void setAr(int ar) {
        this.ar = ar;
    }

    public String getKategoria() {
        return kategoria;
    }

    public void setKategoria(String kategoria) {
        this.kategoria = kategoria;
    }

    @Override
    public String toString() {
        return String.format("id: %d Név: %s Leírás: %s Ár: %d Kategória %s", this.id, this.nev, this,leiras, this.ar, this.kategoria);
    }
}
