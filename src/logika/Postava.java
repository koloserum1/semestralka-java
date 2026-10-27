package logika;

import java.util.Set;

/**
 * Postava (NPC). Povie jednu repliku podla stavu hry:
 * kym nenastal jej stav "po splneni", hovori repliku "pred", potom repliku "po".
 */
public class Postava extends HernyPrvok {

    private final Stav stavPoSplneni;   // null = postava hovori stale to iste
    private final String replikaPred;
    private final String replikaPo;

    public Postava(String id, String nazov, String popis, Stav stavPoSplneni,
                   String replikaPred, String replikaPo) {
        super(id, nazov, popis);
        this.stavPoSplneni = stavPoSplneni;
        this.replikaPred = replikaPred;
        this.replikaPo = replikaPo;
    }

    public String hovor(Set<Stav> stavy) {
        if (stavPoSplneni != null && stavy.contains(stavPoSplneni)) {
            return getNazov() + ": " + replikaPo;
        }
        return getNazov() + ": " + replikaPred;
    }
}
