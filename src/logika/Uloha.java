package logika;

import java.util.Set;

/**
 * Uloha (quest). Hrac ju dostane rozhovorom so zadavatelom,
 * splnena je, ked nastane jej stav (napr. KUFOR_ODPRATANY).
 */
public class Uloha {

    private final String id;
    private final String nazov;
    private final String zadanie;
    private final String zadavatel;   // id postavy, null = hlavna uloha, plati od zaciatku
    private final Stav podmienka;
    private boolean prijata;

    public Uloha(String id, String nazov, String zadanie, String zadavatel, Stav podmienka) {
        this.id = id;
        this.nazov = nazov;
        this.zadanie = zadanie;
        this.zadavatel = zadavatel;
        this.podmienka = podmienka;
        this.prijata = zadavatel == null;
    }

    public String getId() {
        return id;
    }

    public String getNazov() {
        return nazov;
    }

    public String getZadanie() {
        return zadanie;
    }

    public String getZadavatel() {
        return zadavatel;
    }

    public boolean isPrijata() {
        return prijata;
    }

    public void prijmi() {
        prijata = true;
    }

    public StavUlohy getStav(Set<Stav> stavy) {
        if (stavy.contains(podmienka)) {
            return StavUlohy.SPLNENA;
        } else if (prijata) {
            return StavUlohy.AKTIVNA;
        } else {
            return StavUlohy.NEPRIJATA;
        }
    }
}
