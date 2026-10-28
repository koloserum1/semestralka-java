package logika;

/**
 * Prikaz "zjedz rezen" - funguje kdekolvek, ale hrac zje iba jeden rezen.
 */
public class PrikazZjedz implements IPrikaz {

    private final HernyPlan plan;

    public PrikazZjedz(HernyPlan plan) {
        this.plan = plan;
    }

    @Override
    public String vykonaj(String... parametre) {
        if (parametre.length == 0) {
            return plan.text("chyba_parametra", "zjedz rezen");
        }
        if (!parametre[0].equals("rezen")) {
            return plan.text("nejedlo");
        }
        if (!plan.maVInventari("rezen")) {
            return plan.text("nemas", "rezen");
        }
        if (plan.maStav(Stav.NAJEDENY)) {
            return plan.text("plny");
        }
        Predmet.odober(plan.getInventar(), "rezen");
        plan.pridajStav(Stav.NAJEDENY);
        return plan.text("zjedol");
    }

    @Override
    public String getNazov() {
        return "zjedz";
    }
}
