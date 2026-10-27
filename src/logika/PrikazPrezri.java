package logika;

/**
 * Prikaz "prezri <vec>" - popis predmetu (v inventari alebo v lokalite), objektu alebo postavy.
 */
public class PrikazPrezri implements IPrikaz {

    private final HernyPlan plan;

    public PrikazPrezri(HernyPlan plan) {
        this.plan = plan;
    }

    @Override
    public String vykonaj(String... parametre) {
        if (parametre.length == 0) {
            return plan.text("chyba_parametra", "prezri kufor");
        }
        String id = parametre[0];
        Lokalita lokalita = plan.getAktualnaLokalita();

        // premenna typu HernyPrvok moze odkazovat na predmet, objekt aj postavu
        HernyPrvok prvok = Predmet.najdi(plan.getInventar(), id);
        if (prvok == null) {
            prvok = Predmet.najdi(lokalita.getPredmety(), id);
        }
        if (prvok == null) {
            prvok = lokalita.najdiObjekt(id);
        }
        if (prvok == null) {
            prvok = lokalita.najdiPostavu(id);
        }
        if (prvok == null) {
            return plan.text("nevidis");
        }

        if (id.equals("mobil")) {
            return prvok.getPopis() + " " + plan.stavMobilu();
        }
        return prvok.getPopis();
    }

    @Override
    public String getNazov() {
        return "prezri";
    }
}
