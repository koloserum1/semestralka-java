package logika;

/**
 * Pravidlo interakcie pre prikazy "pouzi" a "daj", napr.
 * "pouzi kelimok na kavovar v lokalite standard -> kelimok zmizne, ziskas kavu".
 * Vsetky pravidla su v svet.txt, kod ich iba vyhladava a vykonava.
 */
public class Pravidlo {

    private final String prikaz;          // "pouzi" alebo "daj"
    private final String lokalita;
    private final String predmet;
    private final String ciel;            // objekt alebo postava
    private final Stav potrebnyStav;      // null = netreba nic
    private final String textBezStavu;    // text, ked potrebny stav este nenastal
    private final boolean spotrebujeSa;
    private final String ziskaSa;         // id predmetu, null = nic
    private final Stav nastavi;           // null = nic
    private final String text;

    public Pravidlo(String prikaz, String lokalita, String predmet, String ciel, Stav potrebnyStav,
                    String textBezStavu, boolean spotrebujeSa, String ziskaSa, Stav nastavi, String text) {
        this.prikaz = prikaz;
        this.lokalita = lokalita;
        this.predmet = predmet;
        this.ciel = ciel;
        this.potrebnyStav = potrebnyStav;
        this.textBezStavu = textBezStavu;
        this.spotrebujeSa = spotrebujeSa;
        this.ziskaSa = ziskaSa;
        this.nastavi = nastavi;
        this.text = text;
    }

    public boolean sedi(String prikaz, String lokalita, String predmet, String ciel) {
        return this.prikaz.equals(prikaz) && this.lokalita.equals(lokalita)
                && this.predmet.equals(predmet) && this.ciel.equals(ciel);
    }

    /** @return true, ak pravidlo uz raz zmenilo svet a druhykrat sa nepouzije (Jarda zoberie iba jeden rezen) */
    public boolean jeUzVykonane(HernyPlan plan) {
        return nastavi != null && plan.maStav(nastavi);
    }

    public String vykonaj(HernyPlan plan) {
        if (potrebnyStav != null && !plan.maStav(potrebnyStav)) {
            return textBezStavu;
        }
        if (spotrebujeSa) {
            Predmet.odober(plan.getInventar(), predmet);
        }
        if (ziskaSa != null) {
            plan.getInventar().add(plan.getPredmet(ziskaSa));
        }
        if (nastavi != null) {
            plan.pridajStav(nastavi);
        }
        return text;
    }
}
