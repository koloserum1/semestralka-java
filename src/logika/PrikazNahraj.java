package logika;

import java.io.File;

/**
 * Prikaz "nahraj" - nacita ulozenu hru a hrac pokracuje tam, kde skoncil.
 */
public class PrikazNahraj implements IPrikaz {

    private final HernyPlan plan;

    public PrikazNahraj(HernyPlan plan) {
        this.plan = plan;
    }

    @Override
    public String vykonaj(String... parametre) {
        if (!new File(Ulozenie.SUBOR).exists()) {
            return plan.text("ziadne_ulozenie");
        }
        try {
            Ulozenie.nahraj(plan);
            return plan.text("nahrane") + "\n\n" + plan.popisLokality();
        } catch (Exception e) {
            return plan.text("chyba_nahratia") + "\n\n" + plan.popisLokality();
        }
    }

    @Override
    public String getNazov() {
        return "nahraj";
    }
}
