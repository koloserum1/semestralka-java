package logika;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Herny plan - cely herny svet a stav rozohranej hry.
 * Obsah sveta sa nacita zo suboru svet.txt (trieda NacitavacSveta).
 */
public class HernyPlan {

    public static final String START = "nastupiste";
    public static final String CIEL = "praha";

    // obsah sveta - nacita sa zo svet.txt
    private final Map<String, Lokalita> lokality = new HashMap<>();
    private final Map<String, Predmet> predmety = new HashMap<>();   // vsetky predmety hry podla id
    private final List<Uloha> ulohy = new ArrayList<>();
    private final List<Pravidlo> pravidla = new ArrayList<>();
    private final Map<String, String> texty = new HashMap<>();
    private int limit;   // pocet presunov do kontroly
    private int bonus;   // presuny navyse za schovanie na WC

    // stav rozohranej hry - meni sa pocas hrania, preto sa uklada
    private final Set<Stav> stavy = new HashSet<>();
    private final List<Predmet> inventar = new ArrayList<>();
    private Lokalita aktualnaLokalita;
    private int pocitadlo;   // kolko presunov hrac od nastupenia urobil

    public HernyPlan() throws IOException, ChybneDataException {
        nacitajSvet();
    }

    /** Vsetko vymaze a nacita svet odznova - hra je v pociatocnom stave. */
    public void nacitajSvet() throws IOException, ChybneDataException {
        lokality.clear();
        predmety.clear();
        ulohy.clear();
        pravidla.clear();
        texty.clear();
        stavy.clear();
        inventar.clear();
        pocitadlo = 0;
        NacitavacSveta.nacitaj(this);
        aktualnaLokalita = getLokalita(START);
    }

    // ===== texty =====

    public void pridajText(String kluc, String text) {
        texty.put(kluc, text);
    }

    public String text(String kluc) {
        return texty.get(kluc);
    }

    /** Pretazena metoda: navyse nahradi "{}" v texte doplnkom. */
    public String text(String kluc, String doplnok) {
        return text(kluc).replace("{}", doplnok);
    }

    // ===== lokality, predmety, ulohy, pravidla =====

    public void pridajLokalitu(Lokalita lokalita) {
        lokality.put(lokalita.getId(), lokalita);
    }

    public Lokalita getLokalita(String id) {
        Lokalita lokalita = lokality.get(id);
        if (lokalita == null) {
            throw new IllegalArgumentException("Neznama lokalita: " + id);
        }
        return lokalita;
    }

    public Collection<Lokalita> getLokality() {
        return lokality.values();
    }

    public void pridajPredmet(Predmet predmet) {
        predmety.put(predmet.getId(), predmet);
    }

    public Predmet getPredmet(String id) {
        Predmet predmet = predmety.get(id);
        if (predmet == null) {
            throw new IllegalArgumentException("Neznamy predmet: " + id);
        }
        return predmet;
    }

    public List<Uloha> getUlohy() {
        return ulohy;
    }

    public Uloha getUloha(String id) {
        for (Uloha uloha : ulohy) {
            if (uloha.getId().equals(id)) {
                return uloha;
            }
        }
        throw new IllegalArgumentException("Neznama uloha: " + id);
    }

    public List<Pravidlo> getPravidla() {
        return pravidla;
    }

    /**
     * @return pravidlo pre prikaz, predmet a ciel v aktualnej lokalite,
     *         alebo null, ak take nie je alebo uz bolo vykonane
     */
    public Pravidlo najdiPravidlo(String prikaz, String predmet, String ciel) {
        for (Pravidlo pravidlo : pravidla) {
            if (pravidlo.sedi(prikaz, aktualnaLokalita.getId(), predmet, ciel) && !pravidlo.jeUzVykonane(this)) {
                return pravidlo;
            }
        }
        return null;
    }

    // ===== stav hry =====

    public boolean maStav(Stav stav) {
        return stavy.contains(stav);
    }

    public void pridajStav(Stav stav) {
        stavy.add(stav);
    }

    public Set<Stav> getStavy() {
        return stavy;
    }

    public List<Predmet> getInventar() {
        return inventar;
    }

    public boolean maVInventari(String id) {
        return Predmet.najdi(inventar, id) != null;
    }

    public Lokalita getAktualnaLokalita() {
        return aktualnaLokalita;
    }

    public void setAktualnaLokalita(Lokalita lokalita) {
        aktualnaLokalita = lokalita;
    }

    public int getPocitadlo() {
        return pocitadlo;
    }

    public void setPocitadlo(int pocitadlo) {
        this.pocitadlo = pocitadlo;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit musi byt kladny: " + limit);
        }
        this.limit = limit;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public boolean jeVyhra() {
        return aktualnaLokalita.getId().equals(CIEL);
    }

    public boolean jePrehra() {
        return pocitadlo >= limit && !maStav(Stav.MOBIL_NABITY);
    }

    // ===== vypisy =====

    public String stavMobilu() {
        if (maStav(Stav.MOBIL_NABITY)) {
            return text("mobil_nabity");
        }
        return text("mobil_vybity");
    }

    /** @return napr. "Do kontroly zostava 3 presuny." (so spravnym tvarom slova) */
    public String zostavajucePresuny() {
        int zostava = limit - pocitadlo;
        String slovo;
        if (zostava == 1) {
            slovo = text("presun_1");
        } else if (zostava >= 2 && zostava <= 4) {
            slovo = text("presun_2");
        } else {
            slovo = text("presun_5");
        }
        return text("zostava", zostava + " " + slovo);
    }

    /** @return nazov a popis aktualnej lokality, co v nej je a kam sa da ist */
    public String popisLokality() {
        StringBuilder sb = new StringBuilder();
        sb.append(aktualnaLokalita.getNazov().toUpperCase()).append("\n").append(aktualnaLokalita.getPopis());
        pridajZoznam(sb, "postavy", new ArrayList<>(aktualnaLokalita.getPostavy()));
        pridajZoznam(sb, "objekty", new ArrayList<>(aktualnaLokalita.getObjekty()));
        pridajZoznam(sb, "predmety", new ArrayList<>(aktualnaLokalita.getPredmety()));
        if (!aktualnaLokalita.getVychody().isEmpty()) {
            sb.append("\n").append(text("vychody")).append(": ");
            for (int i = 0; i < aktualnaLokalita.getVychody().size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(aktualnaLokalita.getVychody().get(i).getCiel().getId());
            }
        }
        return sb.toString();
    }

    private void pridajZoznam(StringBuilder sb, String kluc, List<HernyPrvok> prvky) {
        if (prvky.isEmpty()) {
            return;
        }
        sb.append("\n").append(text(kluc)).append(": ");
        for (int i = 0; i < prvky.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(prvky.get(i).getNazov());
        }
    }
}
