# RegioJet do Prahy – textová adventúra (4IT107)

Hra presne podľa nášho GDD. Projekt sa zostavuje cez **Maven** (`pom.xml`, závislosť JUnit 5).

```
mvn test                                  # spustí všetky JUnit testy
mvn package                               # vytvorí target/regiojet-do-prahy-1.0.jar
java -jar target/regiojet-do-prahy-1.0.jar
```

V IntelliJ: *Open* → vybrať `pom.xml` → *Open as Project*. Spustenie: `src/main/Start.java` → Run.

## Štruktúra (rovnaká ako na cvičení „Kolo 4“)

```
pom.xml                 Maven: JUnit 5, priečinky src / test / resources, hlavná trieda main.Start
resources/svet.txt      VŠETOK obsah hry: lokality, východy, predmety, objekty, postavy,
                        úlohy, pravidlá pre pouzi/daj, texty, limit 8 a bonus 4
src/main/Start          main() – vytvorí Hra a TextoveRozhranie
src/uiText/             TextoveRozhranie – číta riadky z konzoly (Scanner), vypisuje odpovede
src/logika/
  IHra, Hra             rozhranie hry + implementácia: zaregistruje príkazy, spracuje riadok
  IPrikaz               rozhranie príkazu (vykonaj, getNazov)
  ZoznamPrikazov        HashMap názov → príkaz
  Prikaz*.java          jeden príkaz = jedna trieda
  HernyPlan             celý svet + stav hry (lokalita, inventár, stavy, počítadlo, limit)
  NacitavacSveta        načíta svet.txt
  Ulozenie              príkazy uloz / nahraj (súbor ulozena_hra.txt)
  HernyPrvok            abstraktný predok: Lokalita, Predmet, Objekt, Postava
  Vychod, Uloha, Pravidlo
  Stav, StavUlohy       enumy
  ChybneDataException   vlastná checked výnimka
test/logika/
  HernePrvkyTest        unit testy: Lokalita, Vychod, Predmet, Postava, Uloha
  HernyPlanTest         unit testy: načítanie sveta, limit, výhra/prehra, texty, pravidlá
  HraTest               testy príkazov a celej hry (výhra, prehra, uloženie)
```

## Ako prejde jeden príkaz (napr. `chod standard`)

1. `TextoveRozhranie.hraj()` prečíta riadok a zavolá `hra.spracujPrikaz(riadok)`.
2. `Hra.spracujPrikaz` → malé písmená, rozdelí slová → `prikazy.vrat("chod")` → `PrikazChod`.
3. `PrikazChod.vykonaj("standard")` → nájde `Vychod` → ak je zatvorený, vráti dôvod →
   inak presunie hráča, vo vlaku zvýši počítadlo a pripíše „Do kontroly zostáva…“.
4. Cyklus v `TextoveRozhranie` sa skončí, keď `konecHry()` = výhra / prehra / koniec → `vratEpilog()`.

## Kľúčová myšlienka: stav sveta = množina `Stav`

Všetko, čo sa počas hry zmení, je konštanta enumu `Stav` v `Set<Stav> stavy`
(NAJEDENY, NABIJACKA_ZISKANA, KUFOR_ODPRATANY, ZASUVKA_VOLNA, MOBIL_NABITY, WC_POUZITE, VLAK_V_PRAHE).
- zatvorený východ = východ s podmienkou (`KUFOR_ODPRATANY`)
- úloha je splnená, keď nastane jej stav
- postava hovorí repliku „po“, keď nastane jej stav
- `uloz` zapíše stavy + lokalitu + počítadlo + limit + inventár + predmety v lokalitách + prijaté úlohy

## Pravidlá pre `pouzi` a `daj`

Sú v `svet.txt` (riadky PRAVIDLO). `PrikazInterakcia` (abstraktná) robí spoločný postup:
chýba parameter? → má predmet? → je cieľ tu? → existuje pravidlo? → `Pravidlo.vykonaj`.
`PrikazPouzi` a `PrikazDaj` doplnia iba, ako hľadať cieľ a čo povedať pri odmietnutí.

## Kde je ktorý koncept z DataCampu / cvičení

| Koncept | Kde |
|---|---|
| trieda, konštruktor, `this`, gettery/settery, `private` | všetky triedy, napr. `Uloha`, `Vychod` |
| validácia v setteri (OOP 3 – Parcel) | `HernyPlan.setLimit` |
| `static final` konštanty | `HernyPlan.START`, `CIEL`, `Ulozenie.SUBOR` |
| statické metódy | `Predmet.najdi/odober`, `NacitavacSveta.nacitaj` |
| dedičnosť, `extends`, `super(...)` | `Lokalita/Predmet/Objekt/Postava extends HernyPrvok` |
| abstraktná trieda + abstraktné metódy | `HernyPrvok`, `PrikazInterakcia` |
| interface + polymorfizmus, `@Override` | `IHra`, `IPrikaz`; premenná `HernyPrvok prvok` v `PrikazPrezri` |
| prepísanie + volanie `super.metoda()` (OOP 3 – Drone) | `PrikazNastup extends PrikazChod` |
| preťaženie (overloading) | `HernyPlan.text(kluc)` a `text(kluc, doplnok)` |
| enum, `valueOf`, `name()` | `Stav`, `StavUlohy` |
| `if / else if / else`, `switch`, `for`, foreach, `while` | `zostavajucePresuny`, `NacitavacSveta`, `Predmet.odober` |
| ArrayList, HashMap, HashSet, `Collections.sort`, `Arrays.asList` | `HernyPlan`, `ZoznamPrikazov`, `Ulozenie` |
| Iterator (bezpečné mazanie) | `Predmet.odober` |
| Stream + lambda (`filter`, `count`) | `PrikazInventar` |
| wrapper triedy (`Integer.parseInt`, `Integer.toString`) | `NacitavacSveta`, `Ulozenie` |
| súbory: BufferedReader/Writer, UTF-8, `File.exists` | `NacitavacSveta`, `Ulozenie`, `PrikazNahraj` |
| try/catch, try-with-resources, checked vs runtime, `throws` | `NacitavacSveta`, `Ulozenie`, `Hra.spracujPrikaz` |
| vlastná výnimka | `ChybneDataException` |
| Logger | `Start`, `Hra` |
| JUnit 5: `@Test`, `@BeforeEach`, `@ParameterizedTest`, `@CsvSource`, `@ValueSource` | `HraTest`, `HernyPlanTest`, `HernePrvkyTest` |

## Rozhodnutia, ktoré GDD nehovorí presne

- Kontrola limitu podľa GDD: po každom presune; ak počítadlo dosiahne limit a mobil nie je nabitý → prehra.
- Na nástupišti sa nedá nič položiť (späť sa už nedá ísť, hra by sa zasekla – GDD: „Hra sa nesmie zaseknúť“).
- Texty sú v `svet.txt` (GDD kap. 7: „Texty a nastavenia… chybové hlášky“), preto v kóde nie je diakritika.
