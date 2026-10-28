package logika;

/**
 * Prikaz "vezmi <predmet>" - presunie predmet z lokality do inventara.
 */
public class PrikazVezmi implements IPrikaz {

    private final HernyPlan plan;

    public PrikazVezmi(HernyPlan plan) {
        this.plan = plan;
    }

    @Override
    public String vykonaj(String... parametre) {
        if (parametre.length == 0) {
            return plan.text("chyba_parametra", "vezmi kelimok");
        }
        Lokalita lokalita = plan.getAktualnaLokalita();
        Predmet predmet = Predmet.odober(lokalita.getPredmety(), parametre[0]);
        if (predmet != null) {
            plan.getInventar().add(predmet);
            return plan.text("vzal", predmet.getNazov());
        }
        if (lokalita.najdiObjekt(parametre[0]) != null || lokalita.najdiPostavu(parametre[0]) != null) {
            return plan.text("neda_sa_vziat");
        }
        return plan.text("nie_je_tu");
    }

    @Override
    public String getNazov() {
        return "vezmi";
    }
}
