package logika;

import java.util.Set;

/**
 * Cesta z jednej lokality do druhej. Cesta ide vzdy iba jednym smerom,
 * obojsmerny prechod su dva vychody. Moze byt zatvorena, kym nenastane podmienka.
 */
public class Vychod {

    private final Lokalita ciel;
    private final Stav podmienka;          // null = cesta je vzdy otvorena
    private final String textZatvorene;    // co sa vypise, ked je cesta zatvorena

    public Vychod(Lokalita ciel, Stav podmienka, String textZatvorene) {
        this.ciel = ciel;
        this.podmienka = podmienka;
        this.textZatvorene = textZatvorene;
    }

    public Lokalita getCiel() {
        return ciel;
    }

    public String getTextZatvorene() {
        return textZatvorene;
    }

    public boolean jeOtvoreny(Set<Stav> stavy) {
        return podmienka == null || stavy.contains(podmienka);
    }
}
