package logika;

/**
 * Prikaz "schovaj sa" - iba na WC a iba raz za hru. Limit presunov sa zvysi o bonus.
 */
public class PrikazSchovaj implements IPrikaz {

    private static final String WC = "wc";
    private final HernyPlan plan;

    public PrikazSchovaj(HernyPlan plan) {
        this.plan = plan;
    }

    @Override
    public String vykonaj(String... parametre) {
        if (!plan.getAktualnaLokalita().getId().equals(WC)) {
            return plan.text("wc_tu_nie");
        }
        if (plan.maStav(Stav.WC_POUZITE)) {
            return plan.text("wc_uz");
        }
        plan.pridajStav(Stav.WC_POUZITE);
        plan.setLimit(plan.getLimit() + plan.getBonus());
        return plan.text("wc_ok", Integer.toString(plan.getBonus()));
    }

    @Override
    public String getNazov() {
        return "schovaj";
    }
}
