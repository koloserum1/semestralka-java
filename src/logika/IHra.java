package logika;

/**
 * Rozhranie hry. Pouzivatelske rozhranie (uiText) pozna iba toto rozhranie,
 * nie konkretnu triedu Hra.
 */
public interface IHra {

    /** @return uvodny text hry */
    String vratUvitanie();

    /** @return zaverecny text hry (vyhra, prehra alebo podakovanie) */
    String vratEpilog();

    /** @return true, ak hra skoncila */
    boolean konecHry();

    /**
     * Spracuje riadok zadany hracom a vrati text, ktory sa ma vypisat.
     *
     * @param riadok text zadany hracom, napr. "chod standard"
     */
    String spracujPrikaz(String riadok);

    /** @return herny plan (svet a stav hry) */
    HernyPlan getHernyPlan();
}
