package logika;

/**
 * Prikaz "nastup" - skratka pre presun z nastupista do vozna Low cost.
 * Dedi z PrikazChod a vyuziva jeho presun cez super.vykonaj(...).
 */
public class PrikazNastup extends PrikazChod {

    private static final String VOZEN = "lowcost";
    private final HernyPlan plan;

    public PrikazNastup(HernyPlan plan) {
        super(plan);
        this.plan = plan;
    }

    @Override
    public String vykonaj(String... parametre) {
        if (!plan.getAktualnaLokalita().getId().equals(HernyPlan.START)) {
            return plan.text("uz_vo_vlaku");
        }
        return super.vykonaj(VOZEN);
    }

    @Override
    public String getNazov() {
        return "nastup";
    }
}
