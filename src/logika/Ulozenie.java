package logika;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ulozenie a nacitanie rozohranej hry (jedna ulozena pozicia).
 * Uklada sa iba to, co sa pocas hrania meni. Popisy, repliky a pravidla
 * sa po nacitani znova nacitaju zo svet.txt.
 *
 * Format suboru - riadky "kluc=hodnota", zoznamy oddelene ciarkou, "-" = prazdne:
 *   lokalita=business
 *   pocitadlo=4
 *   limit=8
 *   stavy=NAJEDENY,KUFOR_ODPRATANY
 *   ulohy=nabijacka,kufor
 *   inventar=mobil,rezen,nabijacka
 *   predmety.business=kelimok
 */
public class Ulozenie {

    public static final String SUBOR = "ulozena_hra.txt";

    public static void uloz(HernyPlan plan) throws IOException {
        List<String> stavy = new ArrayList<>();
        for (Stav stav : plan.getStavy()) {
            stavy.add(stav.name());
        }
        List<String> ulohy = new ArrayList<>();
        for (Uloha uloha : plan.getUlohy()) {
            if (uloha.isPrijata()) {
                ulohy.add(uloha.getId());
            }
        }

        try (BufferedWriter zapisovac = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(SUBOR), "UTF-8"))) {
            zapis(zapisovac, "lokalita", plan.getAktualnaLokalita().getId());
            zapis(zapisovac, "pocitadlo", Integer.toString(plan.getPocitadlo()));
            zapis(zapisovac, "limit", Integer.toString(plan.getLimit()));
            zapis(zapisovac, "stavy", spoj(stavy));
            zapis(zapisovac, "ulohy", spoj(ulohy));
            zapis(zapisovac, "inventar", spoj(idPredmetov(plan.getInventar())));
            for (Lokalita lokalita : plan.getLokality()) {
                zapis(zapisovac, "predmety." + lokalita.getId(), spoj(idPredmetov(lokalita.getPredmety())));
            }
        }
    }

    /**
     * Nacita svet odznova zo svet.txt a prepise ho stavom z ulozenej hry.
     * Ak je ulozena hra poskodena, hra zacne odznova a vyhodi sa vynimka.
     */
    public static void nahraj(HernyPlan plan) throws IOException, ChybneDataException {
        Map<String, String> data = new HashMap<>();
        try (BufferedReader citac = new BufferedReader(
                new InputStreamReader(new FileInputStream(SUBOR), "UTF-8"))) {
            String riadok;
            while ((riadok = citac.readLine()) != null) {
                String[] casti = riadok.split("=");
                if (casti.length == 2) {
                    data.put(casti[0], casti[1]);
                }
            }
        }

        plan.nacitajSvet();
        try {
            plan.setAktualnaLokalita(plan.getLokalita(data.get("lokalita")));
            plan.setPocitadlo(Integer.parseInt(data.get("pocitadlo")));
            plan.setLimit(Integer.parseInt(data.get("limit")));
            for (String stav : rozdel(data.get("stavy"))) {
                plan.pridajStav(Stav.valueOf(stav));
            }
            for (String id : rozdel(data.get("ulohy"))) {
                plan.getUloha(id).prijmi();
            }
            nastavPredmety(plan, plan.getInventar(), data.get("inventar"));
            for (Lokalita lokalita : plan.getLokality()) {
                nastavPredmety(plan, lokalita.getPredmety(), data.get("predmety." + lokalita.getId()));
            }
        } catch (RuntimeException e) {
            plan.nacitajSvet();
            throw new ChybneDataException("Poskodena ulozena hra: " + e.getMessage());
        }
    }

    private static void zapis(BufferedWriter zapisovac, String kluc, String hodnota) throws IOException {
        zapisovac.write(kluc + "=" + hodnota);
        zapisovac.newLine();
    }

    private static List<String> idPredmetov(List<Predmet> predmety) {
        List<String> idcka = new ArrayList<>();
        for (Predmet predmet : predmety) {
            idcka.add(predmet.getId());
        }
        return idcka;
    }

    /** ["a", "b"] -> "a,b"; prazdny zoznam -> "-" */
    private static String spoj(List<String> hodnoty) {
        if (hodnoty.isEmpty()) {
            return "-";
        }
        StringBuilder sb = new StringBuilder();
        for (String hodnota : hodnoty) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(hodnota);
        }
        return sb.toString();
    }

    /** "a,b" -> ["a", "b"]; "-" -> prazdny zoznam */
    private static List<String> rozdel(String hodnota) {
        if (hodnota.equals("-")) {
            return new ArrayList<>();
        }
        return Arrays.asList(hodnota.split(","));
    }

    private static void nastavPredmety(HernyPlan plan, List<Predmet> zoznam, String hodnota) {
        zoznam.clear();
        for (String id : rozdel(hodnota)) {
            zoznam.add(plan.getPredmet(id));
        }
    }
}
