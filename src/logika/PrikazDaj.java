package logika;

/**
 * Prikaz "daj <predmet> <postava>", napr. "daj rezen jarda".
 * Pri nespravnej kombinacii postava odmietne a predmet ostane hracovi.
 */
public class PrikazDaj extends PrikazInterakcia {

    public PrikazDaj(HernyPlan plan) {
        super(plan, "daj", "daj rezen jarda");
    }

    @Override
    public String skontrolujCiel(HernyPlan plan, String ciel) {
        if (plan.getAktualnaLokalita().najdiPostavu(ciel) == null) {
            return plan.text("postava_nie_je", ciel);
        }
        return null;
    }

    @Override
    public String odmietnutie(HernyPlan plan, String ciel) {
        return plan.text("odmieta", plan.getAktualnaLokalita().najdiPostavu(ciel).getNazov());
    }
}
