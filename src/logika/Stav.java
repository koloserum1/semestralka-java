package logika;

/**
 * Zmeny herneho sveta, ktore mozu nastat pocas hrania.
 * Hra si pamata mnozinu (Set) stavov, ktore uz nastali.
 * V subore svet.txt sa na ne odkazuje menom, napr. KUFOR_ODPRATANY.
 */
public enum Stav {
    NAJEDENY,           // hrac zjedol rezen
    NABIJACKA_ZISKANA,  // Jarda dal nabijacku
    KUFOR_ODPRATANY,    // cesta do Business je volna
    ZASUVKA_VOLNA,      // manazer uvolnil zasuvku
    MOBIL_NABITY,       // pocitadlo presunov sa zastavi
    WC_POUZITE,         // schovanie na WC sa uz pouzilo
    VLAK_V_PRAHE        // hrac si sadol, vychod do Prahy je otvoreny
}
