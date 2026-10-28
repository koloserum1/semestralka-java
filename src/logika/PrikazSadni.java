package logika;

/**
 * Prikaz "sadni" - iba pri sedadle a iba s nabitym mobilom.
 * Stevardka skontroluje listok, vlak dorazi do Prahy a otvori sa vychod z Low cost.
 */
public class PrikazSadni implements IPrikaz {

    private final HernyPlan plan;

    public PrikazSadni(HernyPlan plan) {
        this.plan = plan;
    }

    @Override
    public String vykonaj(String... parametre) {
        if (plan.getAktualnaLokalita().najdiObjekt("sedadlo") == null) {
            return plan.text("sadni_tu_nie");
        }
        if (plan.maStav(Stav.VLAK_V_PRAHE)) {
            return plan.text("uz_v_prahe");
        }
        if (!plan.maStav(Stav.MOBIL_NABITY)) {
            return plan.text("bez_listka");
        }
        plan.pridajStav(Stav.VLAK_V_PRAHE);
        return plan.text("sadnutie");
    }

    @Override
    public String getNazov() {
        return "sadni";
    }
}
