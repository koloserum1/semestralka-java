package logika;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit testy hernych prvkov (Lokalita, Vychod, Predmet, Postava, Uloha)
 * bez nacitania celeho sveta - prvky si vytvorime priamo v teste.
 */
class HernePrvkyTest {

    private Lokalita standard;
    private Lokalita business;
    private Set<Stav> stavy;

    @BeforeEach
    void setUp() {
        standard = new Lokalita("standard", "Vozen Standard", "popis");
        business = new Lokalita("business", "Vozen Business", "popis");
        standard.getVychody().add(new Vychod(business, Stav.KUFOR_ODPRATANY, "Ulicku blokuje kufor."));
        stavy = new HashSet<>();
    }

    // ===== Lokalita a Vychod =====

    @Test
    void najdiVychod_vratiVychod_kedLokalitySusedia() {
        assertSame(business, standard.najdiVychod("business").getCiel());
    }

    @Test
    void najdiVychod_vratiNull_kedLokalityNesusedia() {
        assertNull(standard.najdiVychod("praha"));
        assertNull(business.najdiVychod("standard"));   // vychod ide iba jednym smerom
    }

    @Test
    void jeOtvoreny_otvoriSa_azPoSplneniPodmienky() {
        Vychod vychod = standard.najdiVychod("business");
        assertFalse(vychod.jeOtvoreny(stavy));
        stavy.add(Stav.KUFOR_ODPRATANY);
        assertTrue(vychod.jeOtvoreny(stavy));
    }

    @Test
    void jeOtvoreny_vzdyOtvoreny_bezPodmienky() {
        assertTrue(new Vychod(standard, null, null).jeOtvoreny(stavy));
    }

    @Test
    void najdiObjektAPostavu_vratiNull_kedVLokaliteNieSu() {
        standard.getObjekty().add(new Objekt("kavovar", "Kavovar", "popis"));
        assertEquals("Kavovar", standard.najdiObjekt("kavovar").getNazov());
        assertNull(standard.najdiObjekt("zasuvka"));
        assertNull(standard.najdiPostavu("marta"));
    }

    // ===== Predmet =====

    @Test
    void odober_odoberieIbaPrvyPredmet_kedJeIchViac() {
        Predmet rezen = new Predmet("rezen", "Rezen", "popis", true);
        Predmet mobil = new Predmet("mobil", "Mobil", "popis", false);
        List<Predmet> inventar = new ArrayList<>(Arrays.asList(mobil, rezen, rezen));

        assertSame(rezen, Predmet.odober(inventar, "rezen"));
        assertEquals(2, inventar.size());
        assertSame(rezen, Predmet.najdi(inventar, "rezen"));
    }

    @Test
    void odober_vratiNull_kedPredmetVZoznameNieJe() {
        List<Predmet> inventar = new ArrayList<>();
        assertNull(Predmet.odober(inventar, "kava"));
        assertNull(Predmet.najdi(inventar, "kava"));
    }

    // ===== Postava =====

    @Test
    void hovor_zmeniRepliku_poSplneniStavu() {
        Postava jarda = new Postava("jarda", "Jarda", "popis", Stav.NABIJACKA_ZISKANA, "Mam hlad.", "Diky!");
        assertEquals("Jarda: Mam hlad.", jarda.hovor(stavy));
        stavy.add(Stav.NABIJACKA_ZISKANA);
        assertEquals("Jarda: Diky!", jarda.hovor(stavy));
    }

    // ===== Uloha =====

    @Test
    void getStav_prechadzaNeprijataAktivnaSplnena() {
        Uloha uloha = new Uloha("kufor", "Kufor", "Odprac kufor.", "marta", Stav.KUFOR_ODPRATANY);
        assertEquals(StavUlohy.NEPRIJATA, uloha.getStav(stavy));
        uloha.prijmi();
        assertEquals(StavUlohy.AKTIVNA, uloha.getStav(stavy));
        stavy.add(Stav.KUFOR_ODPRATANY);
        assertEquals(StavUlohy.SPLNENA, uloha.getStav(stavy));
    }

    @Test
    void hlavnaUloha_jePrijataOdZaciatku() {
        Uloha hlavna = new Uloha("praha", "Do Prahy", "Dostan sa do Prahy.", null, Stav.VLAK_V_PRAHE);
        assertTrue(hlavna.isPrijata());
        assertEquals(StavUlohy.AKTIVNA, hlavna.getStav(stavy));
    }
}
