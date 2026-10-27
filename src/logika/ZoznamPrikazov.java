package logika;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Zoznam prikazov, ktore hra pozna. Kluc mapy je nazov prikazu.
 */
public class ZoznamPrikazov {

    private final Map<String, IPrikaz> prikazy = new HashMap<>();

    public void pridaj(IPrikaz prikaz) {
        prikazy.put(prikaz.getNazov(), prikaz);
    }

    /** @return prikaz so zadanym nazvom, alebo null, ak taky prikaz neexistuje */
    public IPrikaz vrat(String nazov) {
        return prikazy.get(nazov);
    }

    /** @return nazvy vsetkych prikazov podla abecedy, oddelene ciarkou */
    public String vratNazvy() {
        List<String> nazvy = new ArrayList<>(prikazy.keySet());
        Collections.sort(nazvy);
        StringBuilder sb = new StringBuilder();
        for (String nazov : nazvy) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(nazov);
        }
        return sb.toString();
    }
}
