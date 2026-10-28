package logika;

/**
 * Spolocny predok prikazov "pouzi <predmet> <objekt>" a "daj <predmet> <postava>".
 * Postup je rovnaky, lisi sa iba to, ako sa hlada ciel a co sa vypise pri odmietnuti.
 * Tieto rozdiely si doplni kazdy potomok (abstraktne metody).
 */
public abstract class PrikazInterakcia implements IPrikaz {

    private final HernyPlan plan;
    private final String nazov;
    private final String priklad;

    public PrikazInterakcia(HernyPlan plan, String nazov, String priklad) {
        this.plan = plan;
        this.nazov = nazov;
        this.priklad = priklad;
    }

    @Override
    public String vykonaj(String... parametre) {
        if (parametre.length < 2) {
            return plan.text("chyba_parametra", priklad);
        }
        String predmet = parametre[0];
        String ciel = parametre[1];
        if (!plan.maVInventari(predmet)) {
            return plan.text("nemas", predmet);
        }
        String chyba = skontrolujCiel(plan, ciel);
        if (chyba != null) {
            return chyba;
        }
        Pravidlo pravidlo = plan.najdiPravidlo(nazov, predmet, ciel);
        if (pravidlo == null) {
            return odmietnutie(plan, ciel);
        }
        return pravidlo.vykonaj(plan);
    }

    /** @return chybovy text, ak ciel v lokalite nie je, inak null */
    public abstract String skontrolujCiel(HernyPlan plan, String ciel);

    /** @return text, ked pre tuto kombinaciu neexistuje pravidlo */
    public abstract String odmietnutie(HernyPlan plan, String ciel);

    @Override
    public String getNazov() {
        return nazov;
    }
}
