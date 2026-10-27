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

    // ===== chybove situacie =====

    @ParameterizedTest
    @CsvSource({
            "skakaj, nerozumiem",
            "'', nerozumiem"
    })
    void spracujPrikaz_vratiChybu_priZlomVstupe(String vstup, String kluc) {
        assertEquals(plan.text(kluc), hraj(vstup));
        assertEquals(HernyPlan.START, plan.getAktualnaLokalita().getId());
    }

}
