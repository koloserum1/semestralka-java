package logika;

/**
 * Prikaz "napoveda" - vypise ciel hry a zoznam prikazov.
 */
public class PrikazNapoveda implements IPrikaz {

    private final HernyPlan plan;
    private final ZoznamPrikazov prikazy;

    public PrikazNapoveda(HernyPlan plan, ZoznamPrikazov prikazy) {
        this.plan = plan;
        this.prikazy = prikazy;
    }

    @Override
    public String vykonaj(String... parametre) {
        return plan.text("napoveda", prikazy.vratNazvy());
    }

    @Override
    public String getNazov() {
        return "napoveda";
    }
}
