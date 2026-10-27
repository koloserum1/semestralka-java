package logika;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Nacita herny svet zo suboru svet.txt (priecinok resources).
 * Kazdy riadok je jeden zaznam, polia oddeluje ";", prve pole je typ zaznamu.
 * Novu lokalitu alebo predmet pridame iba upravou svet.txt, bez zmeny kodu.
 */
public class NacitavacSveta {

    private static final String SUBOR = "/svet.txt";

    public static void nacitaj(HernyPlan plan) throws IOException, ChybneDataException {
        InputStream vstup = NacitavacSveta.class.getResourceAsStream(SUBOR);
        if (vstup == null) {
            throw new FileNotFoundException("Subor " + SUBOR + " sa nenasiel.");
        }
        try (BufferedReader citac = new BufferedReader(new InputStreamReader(vstup, "UTF-8"))) {
            String riadok;
            int cislo = 0;
            while ((riadok = citac.readLine()) != null) {
                cislo++;
                riadok = riadok.trim();
                if (!riadok.isEmpty() && !riadok.startsWith("#")) {
                    try {
                        spracujRiadok(plan, riadok.split(";"));
                    } catch (RuntimeException e) {
                        // chybajuce pole, zle cislo, neznamy stav alebo lokalita...
                        throw new ChybneDataException("Chybny riadok " + cislo + " v " + SUBOR + ": " + riadok);
                    }
                }
            }
        }
    }

    private static void spracujRiadok(HernyPlan plan, String[] p) {
        switch (p[0]) {
            case "NASTAVENIE":
                if (p[1].equals("limit")) {
                    plan.setLimit(Integer.parseInt(p[2]));
                } else if (p[1].equals("bonus")) {
                    plan.setBonus(Integer.parseInt(p[2]));
                } else {
                    throw new IllegalArgumentException("Nezname nastavenie: " + p[1]);
                }
                break;
            case "LOKALITA":
                plan.pridajLokalitu(new Lokalita(p[1], p[2], p[3]));
                break;
            case "VYCHOD":
                Vychod vychod = new Vychod(plan.getLokalita(p[2]), stav(p[3]), p[4]);
                plan.getLokalita(p[1]).getVychody().add(vychod);
                break;
            case "PREDMET":
                nacitajPredmet(plan, p);
                break;
            case "OBJEKT":
                plan.getLokalita(p[3]).getObjekty().add(new Objekt(p[1], p[2], p[4]));
                break;
            case "POSTAVA":
                Postava postava = new Postava(p[1], p[2], p[4], stav(p[5]), p[6], p[7]);
                plan.getLokalita(p[3]).getPostavy().add(postava);
                break;
            case "ULOHA":
                plan.getUlohy().add(new Uloha(p[1], p[2], p[3], hodnota(p[4]), Stav.valueOf(p[5])));
                break;
            case "PRAVIDLO":
                plan.getPravidla().add(new Pravidlo(p[1], p[2], p[3], p[4], stav(p[5]), p[6],
                        p[7].equals("ano"), hodnota(p[8]), stav(p[9]), p[10]));
                break;
            case "TEXT":
                plan.pridajText(p[1], p[2]);
                break;
            default:
                throw new IllegalArgumentException("Neznamy typ zaznamu: " + p[0]);
        }
    }

    /** Vytvori predmet a polozi ho tolkokrat, kolko je kusov (do inventara alebo do lokality). */
    private static void nacitajPredmet(HernyPlan plan, String[] p) {
        Predmet predmet = new Predmet(p[1], p[2], p[3], p[6].equals("ano"));
        plan.pridajPredmet(predmet);
        int pocet = Integer.parseInt(p[5]);
        if (pocet > 0) {
            List<Predmet> kam = plan.getInventar();
            if (!p[4].equals("inventar")) {
                kam = plan.getLokalita(p[4]).getPredmety();
            }
            for (int i = 0; i < pocet; i++) {
                kam.add(predmet);
            }
        }
    }

    /** "-" znamena "nic" - vrati null, inak text. */
    private static String hodnota(String text) {
        if (text.equals("-")) {
            return null;
        }
        return text;
    }

    /** "-" znamena "ziadny stav", inak prevedie text na konstantu enumu Stav. */
    private static Stav stav(String text) {
        if (text.equals("-")) {
            return null;
        }
        return Stav.valueOf(text);
    }
}
