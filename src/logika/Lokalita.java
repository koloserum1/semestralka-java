package logika;

import java.util.ArrayList;
import java.util.List;

/**
 * Jedna lokalita (miestnost) hry. Ma vychody do dalsich lokalit,
 * moze v nej lezat predmet, stat objekt alebo byt postava.
 */
public class Lokalita extends HernyPrvok {

    private final List<Vychod> vychody = new ArrayList<>();
    private final List<Predmet> predmety = new ArrayList<>();
    private final List<Objekt> objekty = new ArrayList<>();
    private final List<Postava> postavy = new ArrayList<>();

    public Lokalita(String id, String nazov, String popis) {
        super(id, nazov, popis);
    }

    public List<Vychod> getVychody() {
        return vychody;
    }

    public List<Predmet> getPredmety() {
        return predmety;
    }

    public List<Objekt> getObjekty() {
        return objekty;
    }

    public List<Postava> getPostavy() {
        return postavy;
    }

    /** @return vychod do lokality so zadanym id, alebo null, ak taky nie je */
    public Vychod najdiVychod(String idCiela) {
        for (Vychod vychod : vychody) {
            if (vychod.getCiel().getId().equals(idCiela)) {
                return vychod;
            }
        }
        return null;
    }

    /** @return objekt so zadanym id, alebo null, ak tu nie je */
    public Objekt najdiObjekt(String id) {
        for (Objekt objekt : objekty) {
            if (objekt.getId().equals(id)) {
                return objekt;
            }
        }
        return null;
    }

    /** @return postava so zadanym id, alebo null, ak tu nie je */
    public Postava najdiPostavu(String id) {
        for (Postava postava : postavy) {
            if (postava.getId().equals(id)) {
                return postava;
            }
        }
        return null;
    }
}
