package logika;

/**
 * Prikaz "zdvihni kufor" - iba tam, kde je kufor, a iba najedeny hrac.
 * Potom sa otvori cesta do Business a uloha pani Marty je splnena.
 */
public class PrikazZdvihni implements IPrikaz {

    private final HernyPlan plan;

    public PrikazZdvihni(HernyPlan plan) {
        this.plan = plan;
    }

    @Override
    public String vykonaj(String... parametre) {
        if (parametre.length == 0) {
            return plan.text("chyba_parametra", "zdvihni kufor");
        }
        if (!parametre[0].equals("kufor")) {
            return plan.text("nejde");
        }
        if (plan.getAktualnaLokalita().najdiObjekt("kufor") == null) {
            return plan.text("kufor_tu_nie");
        }
        if (plan.maStav(Stav.KUFOR_ODPRATANY)) {
            return plan.text("kufor_uz");
        }
        if (!plan.maStav(Stav.NAJEDENY)) {
            return plan.text("nevladzes");
        }
        plan.pridajStav(Stav.KUFOR_ODPRATANY);
        return plan.text("kufor_ok");
    }

    @Override
    public String getNazov() {
        return "zdvihni";
    }
}
