package logika;

import java.util.Iterator;
import java.util.List;

/**
 * Predmet, ktory sa da nosit v inventari (mobil, rezen, kelimok...).
 * Predmet sa po vytvoreni nemeni, preto moze ten isty objekt lezat
 * v zozname viackrat (napr. 3 rezne).
 */
public class Predmet extends HernyPrvok {

    private final boolean daSaPolozit;

    public Predmet(String id, String nazov, String popis, boolean daSaPolozit) {
        super(id, nazov, popis);
        this.daSaPolozit = daSaPolozit;
    }

    public boolean isDaSaPolozit() {
        return daSaPolozit;
    }

    /**
     * @return prvy predmet so zadanym id zo zoznamu, alebo null, ak tam nie je
     */
    public static Predmet najdi(List<Predmet> zoznam, String id) {
        for (Predmet predmet : zoznam) {
            if (predmet.getId().equals(id)) {
                return predmet;
            }
        }
        return null;
    }

    /**
     * Odoberie zo zoznamu prvy predmet so zadanym id.
     * Pouziva Iterator, ktory dovoli bezpecne mazat pocas prechadzania.
     *
     * @return odobrany predmet, alebo null, ak v zozname nie je
     */
    public static Predmet odober(List<Predmet> zoznam, String id) {
        Iterator<Predmet> iterator = zoznam.iterator();
        while (iterator.hasNext()) {
            Predmet predmet = iterator.next();
            if (predmet.getId().equals(id)) {
                iterator.remove();
                return predmet;
            }
        }
        return null;
    }
}
