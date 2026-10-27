package logika;

/**
 * Vyhadzuje sa, ked je subor so svetom alebo s ulozenou hrou chybny.
 * Je to kontrolovana (checked) vynimka - dedi z Exception.
 */
public class ChybneDataException extends Exception {

    public ChybneDataException(String sprava) {
        super(sprava);
    }
}
