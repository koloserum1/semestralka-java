package main;

import java.util.logging.Level;
import java.util.logging.Logger;
import logika.Hra;
import logika.IHra;
import uiText.TextoveRozhranie;

/**
 * Spustacia trieda adventury RegioJet do Prahy.
 */
public class Start {

    private static final Logger LOG = Logger.getLogger("Start");

    public static void main(String[] args) {
        try {
            IHra hra = new Hra();
            new TextoveRozhranie(hra).hraj();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Hru sa nepodarilo spustit: " + e.getMessage());
        }
    }
}