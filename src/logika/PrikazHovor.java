package logika;

/**
 * Prikaz "hovor <postava>" - postava povie repliku podla stavu hry.
 * Ak postava zadava ulohu, hrac ju tymto dostane.
 */
public class PrikazHovor implements IPrikaz {

    private final HernyPlan plan;

    public PrikazHovor(HernyPlan plan) {
        this.plan = plan;
    }

    @Override
    public String vykonaj(String... parametre) {
        if (parametre.length == 0) {
            return plan.text("chyba_parametra", "hovor jarda");
        }
        Postava postava = plan.getAktualnaLokalita().najdiPostavu(parametre[0]);
        if (postava == null) {
            return plan.text("postava_nie_je", parametre[0]);
        }
        for (Uloha uloha : plan.getUlohy()) {
            if (postava.getId().equals(uloha.getZadavatel())) {
                uloha.prijmi();
            }
        }
        return postava.hovor(plan.getStavy());
    }

    @Override
    public String getNazov() {
        return "hovor";
    }
}
