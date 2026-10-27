package logika;

/**
 * Objekt v lokalite, ktory sa neda vziat (kufor, kavovar, zasuvka, sedadlo).
 * Hrac ho moze prezriet alebo na neho pouzit predmet.
 */
public class Objekt extends HernyPrvok {

    public Objekt(String id, String nazov, String popis) {
        super(id, nazov, popis);
    }
}
