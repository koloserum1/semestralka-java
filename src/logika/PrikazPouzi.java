package logika;

/**
 * Prikaz "pouzi <predmet> <objekt>", napr. "pouzi kelimok kavovar".
 */
public class PrikazPouzi extends PrikazInterakcia {

    public PrikazPouzi(HernyPlan plan) {
        super(plan, "pouzi", "pouzi kelimok kavovar");
    }

    @Override
    public String skontrolujCiel(HernyPlan plan, String ciel) {
        if (plan.getAktualnaLokalita().najdiObjekt(ciel) == null) {
            return plan.text("nejde");
        }
        return null;
    }

    @Override
    public String odmietnutie(HernyPlan plan, String ciel) {
        return plan.text("nejde");
    }
}
