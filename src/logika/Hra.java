package logika;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hlavna trida logiky hry. Vytvori herny plan, zaregistruje prikazy
 * a spracuva riadky, ktore zada hrac.
 */
public class Hra implements IHra {

    private static final Logger LOG = Logger.getLogger("Hra");

    private final HernyPlan plan;
    private final ZoznamPrikazov prikazy = new ZoznamPrikazov();
    private boolean koniec = false;

    public Hra() throws IOException, ChybneDataException {
        plan = new HernyPlan();
        prikazy.pridaj(new PrikazChod(plan));
        prikazy.pridaj(new PrikazNastup(plan));
        prikazy.pridaj(new PrikazRozhliadni(plan));
        prikazy.pridaj(new PrikazPrezri(plan));
        prikazy.pridaj(new PrikazVezmi(plan));
        prikazy.pridaj(new PrikazPoloz(plan));
        prikazy.pridaj(new PrikazPouzi(plan));
        prikazy.pridaj(new PrikazDaj(plan));
        prikazy.pridaj(new PrikazHovor(plan));
        prikazy.pridaj(new PrikazZjedz(plan));
        prikazy.pridaj(new PrikazZdvihni(plan));
        prikazy.pridaj(new PrikazSadni(plan));
        prikazy.pridaj(new PrikazSchovaj(plan));
        prikazy.pridaj(new PrikazInventar(plan));
        prikazy.pridaj(new PrikazUlohy(plan));
        prikazy.pridaj(new PrikazNapoveda(plan, prikazy));
        prikazy.pridaj(new PrikazUloz(plan));
        prikazy.pridaj(new PrikazNahraj(plan));
        prikazy.pridaj(new PrikazKoniec(this, plan));
    }

    @Override
    public String vratUvitanie() {
        return plan.text("uvod", Integer.toString(plan.getLimit())) + "\n\n" + plan.popisLokality();
    }

    @Override
    public String vratEpilog() {
        if (plan.jeVyhra()) {
            return plan.text("vyhra");
        } else if (plan.jePrehra()) {
            return plan.text("prehra");
        } else {
            return plan.text("dakujeme");
        }
    }

    @Override
    public boolean konecHry() {
        return koniec || plan.jeVyhra() || plan.jePrehra();
    }

    void setKoniec(boolean koniec) {
        this.koniec = koniec;
    }

    @Override
    public String spracujPrikaz(String riadok) {
        // na velkosti pismen nezalezi, slova moze oddelovat viac medzier
        String[] slova = riadok.trim().toLowerCase().split("\\s+");
        IPrikaz prikaz = prikazy.vrat(slova[0]);
        if (prikaz == null) {
            return plan.text("nerozumiem");
        }
        String[] parametre = new String[slova.length - 1];
        for (int i = 1; i < slova.length; i++) {
            parametre[i - 1] = slova[i];
        }
        try {
            return prikaz.vykonaj(parametre);
        } catch (RuntimeException e) {
            LOG.log(Level.WARNING, "Chyba pri prikaze '" + riadok + "': " + e);
            return plan.text("chyba");
        }
    }

    @Override
    public HernyPlan getHernyPlan() {
        return plan;
    }
}
