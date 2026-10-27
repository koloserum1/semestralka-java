package logika;

/**
 * Kazdy prikaz hry je trida, ktora implementuje toto rozhranie.
 */
public interface IPrikaz {

    /**
     * Vykona prikaz.
     *
     * @param parametre slova, ktore hrac napisal za nazvom prikazu
     * @return text, ktory sa vypise hracovi
     */
    String vykonaj(String... parametre);

    /** @return slovo, ktorym hrac prikaz vyvola */
    String getNazov();
}
