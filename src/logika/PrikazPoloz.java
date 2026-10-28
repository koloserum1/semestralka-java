package logika;

/**
 * Prikaz "poloz <predmet>" - necha predmet v aktualnej lokalite, odkial sa da znova vziat.
 * Na nastupisti sa nic polozit neda, lebo sa tam hrac uz nevrati (hra by sa zasekla).
 */
public class PrikazPoloz implements IPrikaz {

    private final HernyPlan plan;

    public PrikazPoloz(HernyPlan plan) {
        this.plan = plan;
    }

    @Override
    public String vykonaj(String... parametre) {
        if (parametre.length == 0) {
            return plan.text("chyba_parametra", "poloz kelimok");
        }
        Predmet predmet = Predmet.najdi(plan.getInventar(), parametre[0]);
        if (predmet == null) {
            return plan.text("nemas", parametre[0]);
        }
        if (!predmet.isDaSaPolozit()) {
            return plan.text("nepolozis", predmet.getNazov());
        }
        if (plan.getAktualnaLokalita().getId().equals(HernyPlan.START)) {
            return plan.text("nenechavaj");
        }
        Predmet.odober(plan.getInventar(), predmet.getId());
        plan.getAktualnaLokalita().getPredmety().add(predmet);
        return plan.text("polozil", predmet.getNazov());
    }

    @Override
    public String getNazov() {
        return "poloz";
    }
}
