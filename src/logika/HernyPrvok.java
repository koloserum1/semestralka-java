package logika;

/**
 * Spolocny predok vsetkeho, co ma v hre id, nazov a popis
 * (lokalita, predmet, objekt, postava).
 * Je abstraktny - samotny "herny prvok" nevytvarame, vzdy iba konkretneho potomka.
 */
public abstract class HernyPrvok {

    private final String id;      // slovo, ktore pise hrac v prikaze (bez diakritiky)
    private final String nazov;   // nazov, ktory sa vypisuje hracovi
    private final String popis;

    public HernyPrvok(String id, String nazov, String popis) {
        this.id = id;
        this.nazov = nazov;
        this.popis = popis;
    }

    public String getId() {
        return id;
    }

    public String getNazov() {
        return nazov;
    }

    public String getPopis() {
        return popis;
    }
}
