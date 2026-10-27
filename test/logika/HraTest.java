package logika;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Testy hry. Ocakavane texty berieme zo svet.txt cez plan.text(...),
 * aby testy nezaviseli od presneho znenia hlasok.
 * Nazvy testov: metoda_ocakavaneSpravanie_podmienky.
 */
class HraTest {

    private Hra hra;
    private HernyPlan plan;

    @BeforeEach
    void setUp() throws Exception {
        hra = new Hra();
        plan = hra.getHernyPlan();
    }

    /** Zada hre viac prikazov za sebou, vrati odpoved na posledny. */
    private String hraj(String... prikazy) {
        String odpoved = "";
        for (String prikaz : prikazy) {
            odpoved = hra.spracujPrikaz(prikaz);
        }
        return odpoved;
    }

    // ===== cela hra =====

    @Test
    void chod_prehra_poOsmomPresuneBezNabitehoMobilu() {
        hraj("nastup");
        for (int i = 1; i <= 8; i++) {
            assertFalse(plan.jePrehra());
            if (i % 2 == 1) {
                hraj("chod standard");
            } else {
                hraj("chod lowcost");
            }
        }
        assertEquals(8, plan.getPocitadlo());
        assertTrue(plan.jePrehra());
        assertTrue(hra.konecHry());
        assertEquals(plan.text("prehra"), hra.vratEpilog());
    }

    // ===== pohyb a casovy limit =====

    @Test
    void nastup_nepocitaSaDoPresunov() {
        hraj("nastup");
        assertEquals("lowcost", plan.getAktualnaLokalita().getId());
        assertEquals(0, plan.getPocitadlo());
    }

    @Test
    void chod_vypiseZostavajucePresuny_poPrvomPresuneVoVlaku() {
        String odpoved = hraj("nastup", "chod standard");
        assertTrue(odpoved.endsWith(plan.text("zostava", "7 " + plan.text("presun_5"))));
    }

    @Test
    void chod_vypiseDovod_kedJeCestaZatvorena() {
        String odpoved = hraj("nastup", "chod standard", "chod business");
        assertEquals(plan.getLokalita("standard").najdiVychod("business").getTextZatvorene(), odpoved);
        assertEquals("standard", plan.getAktualnaLokalita().getId());
        assertEquals(1, plan.getPocitadlo());   // neuspesny presun sa nepocita
    }

    @Test
    void chod_pocitadloSaZastavi_kedJeMobilNabity() {
        hraj("nastup");
        plan.pridajStav(Stav.MOBIL_NABITY);
        hraj("chod standard", "chod lowcost");
        assertEquals(0, plan.getPocitadlo());
    }

    // ===== chybove situacie =====

    @ParameterizedTest
    @CsvSource({
            "skakaj, nerozumiem",
            "'', nerozumiem",
            "chod business, nesusedi",
            "chod praha, nesusedi"
    })
    void spracujPrikaz_vratiChybu_priZlomVstupe(String vstup, String kluc) {
        assertEquals(plan.text(kluc), hraj(vstup));
        assertEquals(HernyPlan.START, plan.getAktualnaLokalita().getId());
    }

    @ParameterizedTest
    @CsvSource({
            "chod, chod standard",
            "prezri, prezri kufor"
    })
    void vykonaj_vratiChybuParametra_kedChybaParameter(String vstup, String priklad) {
        assertEquals(plan.text("chyba_parametra", priklad), hraj(vstup));
    }

    @Test
    void spracujPrikaz_ignorujeVelkostPismenAMedzery() {
        hraj("  NASTUP  ", "Chod    STANDARD");
        assertEquals("standard", plan.getAktualnaLokalita().getId());
    }

}
