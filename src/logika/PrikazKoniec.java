package logika;

/**
 * Prikaz "koniec" - ukonci hru (pred tym si ju hrac moze ulozit prikazom "uloz").
 */
public class PrikazKoniec implements IPrikaz {

    private final Hra hra;
    private final HernyPlan plan;

    public PrikazKoniec(Hra hra, HernyPlan plan) {
        this.hra = hra;
        this.plan = plan;
    }

    @Override
    public String vykonaj(String... parametre) {
        hra.setKoniec(true);
        return plan.text("ukoncenie");
    }

    @Override
    public String getNazov() {
        return "koniec";
    }
}
