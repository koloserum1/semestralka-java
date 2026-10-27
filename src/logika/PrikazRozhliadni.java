package logika;

/**
 * Prikaz "rozhliadni sa" - znova vypise popis aktualnej lokality.
 */
public class PrikazRozhliadni implements IPrikaz {

    private final HernyPlan plan;

    public PrikazRozhliadni(HernyPlan plan) {
        this.plan = plan;
    }

    @Override
    public String vykonaj(String... parametre) {
        return plan.popisLokality();
    }

    @Override
    public String getNazov() {
        return "rozhliadni";
    }
}
