package uiText;

import java.util.Scanner;
import logika.IHra;

/**
 * Textove pouzivatelske rozhranie - cita prikazy z konzoly a vypisuje odpovede hry.
 */
public class TextoveRozhranie {

    private final IHra hra;

    public TextoveRozhranie(IHra hra) {
        this.hra = hra;
    }

    public void hraj() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(hra.vratUvitanie());
        while (!hra.konecHry()) {
            System.out.print("\n> ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String riadok = scanner.nextLine();
            System.out.println(hra.spracujPrikaz(riadok));
        }
        System.out.println("\n" + hra.vratEpilog());
    }
}
