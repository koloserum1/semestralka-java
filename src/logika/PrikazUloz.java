package logika;

import java.io.IOException;

/**
 * Prikaz "uloz" - ulozi rozohranu hru (jedna ulozena pozicia).
 */
public class PrikazUloz implements IPrikaz {

    private final HernyPlan plan;

    public PrikazUloz(HernyPlan plan) {
        this.plan = plan;
    }

    @Override
    public String vykonaj(String... parametre) {
        try {
            Ulozenie.uloz(plan);
            return plan.text("ulozene");
        } catch (IOException e) {
            return plan.text("chyba_ulozenia");
        }
    }

    @Override
    public String getNazov() {
        return "uloz";
    }
}
