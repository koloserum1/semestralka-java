package logika;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit testy herneho planu: nacitanie svet.txt, limit presunov, vyhra/prehra a pravidla.
 */
class HernyPlanTest {

    private HernyPlan plan;

    @BeforeEach
    void setUp() throws Exception {
        plan = new HernyPlan();
    }

    // ===== nacitanie sveta =====

    @Test
    void nacitajSvet_vytvori6LokalitANastavenia_poSpusteni() {
        assertEquals(6, plan.getLokality().size());
        assertEquals(8, plan.getLimit());
        assertEquals(4, plan.getBonus());
        assertEquals(HernyPlan.START, plan.getAktualnaLokalita().getId());
        assertEquals(4, plan.getInventar().size());   // mobil + 3 rezne
        assertNotNull(plan.getLokalita("business").getPredmety().get(0));
        assertFalse(plan.getUlohy().isEmpty());
        assertFalse(plan.getPravidla().isEmpty());
    }

    @Test
    void nacitajSvet_vratiHruDoPociatocnehoStavu() throws Exception {
        plan.pridajStav(Stav.NAJEDENY);
        plan.setPocitadlo(5);
        plan.setAktualnaLokalita(plan.getLokalita("wc"));
        plan.nacitajSvet();
        assertTrue(plan.getStavy().isEmpty());
        assertEquals(0, plan.getPocitadlo());
        assertEquals(HernyPlan.START, plan.getAktualnaLokalita().getId());
    }

    @Test
    void getLokalita_vyhodiVynimku_preNeznameId() {
        assertThrows(IllegalArgumentException.class, () -> plan.getLokalita("brno"));
    }

    // ===== limit a pocitadlo =====

    @ParameterizedTest
    @ValueSource(ints = {0, -3})
    void setLimit_vyhodiVynimku_kedLimitNieJeKladny(int limit) {
        assertThrows(IllegalArgumentException.class, () -> plan.setLimit(limit));
        assertEquals(8, plan.getLimit());
    }

    @ParameterizedTest
    @CsvSource({
            "7, presun_1",
            "6, presun_2",
            "4, presun_2",
            "3, presun_5",
            "0, presun_5"
    })
    void zostavajucePresuny_pouzijeSpravnyTvarSlova(int pocitadlo, String kluc) {
        plan.setPocitadlo(pocitadlo);
        int zostava = plan.getLimit() - pocitadlo;
        assertEquals(plan.text("zostava", zostava + " " + plan.text(kluc)), plan.zostavajucePresuny());
    }

    @Test
    void jePrehra_azKedPocitadloDosiahneLimit() {
        plan.setPocitadlo(7);
        assertFalse(plan.jePrehra());
        plan.setPocitadlo(8);
        assertTrue(plan.jePrehra());
    }

    @Test
    void jePrehra_nikdy_kedJeMobilNabity() {
        plan.setPocitadlo(20);
        plan.pridajStav(Stav.MOBIL_NABITY);
        assertFalse(plan.jePrehra());
    }

    @Test
    void jeVyhra_ibaVPrahe() {
        assertFalse(plan.jeVyhra());
        plan.setAktualnaLokalita(plan.getLokalita(HernyPlan.CIEL));
        assertTrue(plan.jeVyhra());
    }

    // ===== texty a pravidla =====

    @Test
    void text_nahradiZatvorkyDoplnkom() {
        plan.pridajText("test", "Nemas {} v inventari.");
        assertEquals("Nemas kava v inventari.", plan.text("test", "kava"));
    }

    @Test
    void najdiPravidlo_najdePravidloIbaVSpravnejLokalite() {
        assertNull(plan.najdiPravidlo("pouzi", "kelimok", "kavovar"));   // hrac je na nastupisti
        plan.setAktualnaLokalita(plan.getLokalita("standard"));
        assertNotNull(plan.najdiPravidlo("pouzi", "kelimok", "kavovar"));
    }
}
