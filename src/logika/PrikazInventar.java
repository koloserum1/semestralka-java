package logika;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Prikaz "inventar" - vypise predmety pri hracovi (rovnake spolu s poctom) a stav mobilu.
 */
public class PrikazInventar implements IPrikaz {

    private final HernyPlan plan;

    public PrikazInventar(HernyPlan plan) {
        this.plan = plan;
    }

    @Override
    public String vykonaj(String... parametre) {
        List<Predmet> inventar = plan.getInventar();
        Set<String> vypisane = new HashSet<>();
        StringBuilder sb = new StringBuilder(plan.text("inventar"));
        for (Predmet predmet : inventar) {
            if (!vypisane.contains(predmet.getId())) {
                vypisane.add(predmet.getId());
                long pocet = inventar.stream().filter(p -> p.getId().equals(predmet.getId())).count();
                sb.append("\n- ").append(predmet.getNazov());
                if (pocet > 1) {
                    sb.append(" (").append(pocet).append("x)");
                }
            }
        }
        sb.append("\n").append(plan.stavMobilu());
        return sb.toString();
    }

    @Override
    public String getNazov() {
        return "inventar";
    }
}
