package logika;

/**
 * Prikaz "ulohy" - vypise hlavnu ulohu a ulohy, ktore hrac dostal od postav, so stavom.
 */
public class PrikazUlohy implements IPrikaz {

    private final HernyPlan plan;

    public PrikazUlohy(HernyPlan plan) {
        this.plan = plan;
    }

    @Override
    public String vykonaj(String... parametre) {
        StringBuilder sb = new StringBuilder(plan.text("ulohy"));
        for (Uloha uloha : plan.getUlohy()) {
            StavUlohy stav = uloha.getStav(plan.getStavy());
            if (stav != StavUlohy.NEPRIJATA) {
                sb.append("\n- ").append(uloha.getNazov()).append(": ").append(uloha.getZadanie())
                        .append(" [").append(plan.text(stav.name())).append("]");
            }
        }
        return sb.toString();
    }

    @Override
    public String getNazov() {
        return "ulohy";
    }
}
