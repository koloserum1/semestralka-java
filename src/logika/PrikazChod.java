package logika;

/**
 * Prikaz "chod <lokalita>" - presunie hraca do susednej lokality.
 * Po nastupeni do vlaku sa kazdy presun pocita, kym hrac nema nabity mobil.
 */
public class PrikazChod implements IPrikaz {

    private final HernyPlan plan;

    public PrikazChod(HernyPlan plan) {
        this.plan = plan;
    }

    @Override
    public String vykonaj(String... parametre) {
        if (parametre.length == 0) {
            return plan.text("chyba_parametra", "chod standard");
        }
        Lokalita odkial = plan.getAktualnaLokalita();
        Vychod vychod = odkial.najdiVychod(parametre[0]);
        if (vychod == null) {
            return plan.text("nesusedi");
        }
        if (!vychod.jeOtvoreny(plan.getStavy())) {
            return vychod.getTextZatvorene();
        }

        plan.setAktualnaLokalita(vychod.getCiel());
        String vysledok = plan.popisLokality();
        // nastupenie z nastupista sa nepocita, s nabitym mobilom sa uz nepocita nic
        if (!odkial.getId().equals(HernyPlan.START) && !plan.maStav(Stav.MOBIL_NABITY)) {
            plan.setPocitadlo(plan.getPocitadlo() + 1);
            if (!plan.jePrehra()) {
                vysledok += "\n" + plan.zostavajucePresuny();
            }
        }
        return vysledok;
    }

    @Override
    public String getNazov() {
        return "chod";
    }
}
