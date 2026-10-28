# RegioJet do Prahy

Textová adventúra v Jave, semestrálna práca z predmetu 4IT107.

Je nedeľa večer, hráč stojí na nástupišti v Bratislave a chce sa dostať vlakom do Prahy.
Lístok má iba v mobile a mobil je vybitý. Stevardka vo vlaku kontroluje lístky a kto ho
neukáže, v Břeclavi vystupuje. Hráč musí nabiť mobil skôr, ako príde kontrola.

## Spustenie

Projekt používa Maven (Java 17, JUnit 5).

```
mvn test
mvn package
java -jar target/regiojet-do-prahy-1.0.jar
```

V IntelliJ: Open, vybrať `pom.xml`, Open as Project. Hra sa spúšťa triedou `src/main/Start.java`.

## Príkazy

| Príkaz | Čo robí |
|---|---|
| `nastup` | nastúpi z nástupišťa do vlaku (vozeň Low cost) |
| `chod <lokalita>` | presun do susednej lokality |
| `rozhliadni sa` | popis aktuálnej lokality |
| `prezri <vec>` | popis predmetu, objektu alebo postavy |
| `vezmi <predmet>` / `poloz <predmet>` | práca s predmetmi |
| `inventar` | čo má hráč pri sebe a či je mobil nabitý |
| `zjedz rezen` | najesť sa (bez toho hráč nezdvihne kufor) |
| `zdvihni kufor` | vyloží kufor pani Marty na policu |
| `hovor <postava>` | rozhovor, postava môže zadať úlohu |
| `daj <predmet> <postava>` | daruje predmet postave |
| `pouzi <predmet> <objekt>` | použije predmet na objekt (kávovar, zásuvka) |
| `schovaj sa` | na WC, raz za hru, pridá presuny navyše |
| `sadni` | sadnutie na miesto č. 45, iba s nabitým mobilom |
| `ulohy` | zoznam úloh a ich stav |
| `uloz` / `nahraj` | uloženie a načítanie rozohranej hry |
| `napoveda` / `koniec` | nápoveda a ukončenie hry |

Na veľkosti písmen nezáleží a slová môžu byť oddelené aj viacerými medzerami.

## Herný svet

```
nástupište -> Low cost <-> Standard <-> Business
                 |            |
               Praha          WC
```

- Do vozňa Business sa dá prejsť až po vyložení kufra.
- Do Prahy sa dá vystúpiť z vozňa Low cost až potom, ako si hráč sadne na svoje miesto.
- Na nástupište sa už vrátiť nedá.

Postavy: stevardka (nástupište), Jarda (Low cost), pani Marta (Standard), manažér (Business).

## Výhra a prehra

**Výhra:** hráč dorazí do lokality Praha.

**Prehra:** hráč urobí 8 presunov vo vlaku a mobil stále nie je nabitý. Nastúpenie z nástupišťa
sa nepočíta, neúspešný pokus o presun tiež nie. Po nabití mobilu sa presuny už nepočítajú vôbec.
Schovanie na WC pridá jedenkrát 4 presuny.

Najkratšia cesta k výhre:

```
nastup, daj rezen jarda, chod standard, zjedz rezen, zdvihni kufor,
chod business, vezmi kelimok, chod standard, pouzi kelimok kavovar,
chod business, daj kava manazer, pouzi nabijacka zasuvka,
chod standard, sadni, chod lowcost, chod praha
```

## Ako je hra postavená

### Obsah hry je v súbore `resources/svet.txt`

Lokality, východy, predmety, objekty, postavy, úlohy, pravidlá pre `pouzi` a `daj`, všetky texty
a nastavenia (limit 8, bonus 4) sú v jednom textovom súbore. Každý riadok je jeden záznam, polia sú
oddelené `;` a prvé pole hovorí, o aký typ ide (`LOKALITA`, `VYCHOD`, `PREDMET`, ...).
Súbor načíta trieda `NacitavacSveta` pri štarte hry. Ak sú dáta chybné, vyhodí `ChybneDataException`.

Vďaka tomu sa dá svet upraviť bez zmeny kódu.

### Hlavné triedy

- `Start` vytvorí hru a textové rozhranie.
- `TextoveRozhranie` číta riadky z konzoly, posiela ich hre a vypisuje odpovede, kým hra neskončí.
- `Hra` (implementuje `IHra`) zaregistruje všetky príkazy a spracuje zadaný riadok.
- `HernyPlan` drží celý svet a stav rozohranej hry: aktuálnu lokalitu, inventár, počítadlo
  presunov, limit a množinu stavov. Rozhoduje aj o výhre a prehre.
- `HernyPrvok` je spoločný abstraktný predok pre `Lokalita`, `Predmet`, `Objekt` a `Postava`.
- `Vychod`, `Uloha`, `Pravidlo` sú ďalšie prvky sveta.

### Príkazy

Každý príkaz je samostatná trieda, ktorá implementuje rozhranie `IPrikaz` (`vykonaj`, `getNazov`).
`ZoznamPrikazov` ich drží v `HashMap` podľa názvu.

Spracovanie riadku, napr. `chod standard`:

1. `Hra.spracujPrikaz` prevedie text na malé písmená a rozdelí ho na slová.
2. Podľa prvého slova nájde v `ZoznamPrikazov` objekt `PrikazChod`.
3. `PrikazChod.vykonaj("standard")` nájde východ, skontroluje, či je otvorený, presunie hráča
   a zvýši počítadlo.
4. `TextoveRozhranie` odpoveď vypíše a po každom príkaze sa pýta `konecHry()`.

Niektoré príkazy zdieľajú kód:

- `PrikazNastup` dedí z `PrikazChod` a volá `super.vykonaj("lowcost")`.
- `PrikazPouzi` a `PrikazDaj` dedia z abstraktnej triedy `PrikazInterakcia`, ktorá robí spoločný
  postup (je zadaný predmet aj cieľ, má ho hráč, je cieľ v lokalite, existuje pravidlo).
  Potomkovia doplnia iba hľadanie cieľa a text pri odmietnutí.

### Stav hry

Všetko, čo sa počas hry zmení, je uložené ako hodnota enumu `Stav` v množine `Set<Stav>`:

`NAJEDENY`, `NABIJACKA_ZISKANA`, `KUFOR_ODPRATANY`, `ZASUVKA_VOLNA`, `MOBIL_NABITY`, `WC_POUZITE`, `VLAK_V_PRAHE`

Na tieto stavy sa odkazuje aj zo `svet.txt`:

- východ môže mať podmienku (do Business až po `KUFOR_ODPRATANY`),
- úloha je splnená, keď nastane jej stav,
- postava povie inú repliku, keď nastane jej stav,
- pravidlo môže vyžadovať stav a po vykonaní nový stav nastaviť.

### Pravidlá pre `pouzi` a `daj`

Pravidlo určuje príkaz, lokalitu, predmet a cieľ. Pri vykonaní sa môže predmet spotrebovať,
hráč môže získať nový predmet a môže sa nastaviť stav. Príklad zo `svet.txt`:

```
PRAVIDLO;daj;lowcost;rezen;jarda;-;-;ano;nabijacka;NABIJACKA_ZISKANA;Jarda ti podá nabíjačku...
```

Pravidlo, ktoré už nastavilo svoj stav, sa druhýkrát nepoužije (Jarda zoberie iba jeden rezeň).

### Ukladanie hry

Trieda `Ulozenie` zapíše do súboru `ulozena_hra.txt` iba to, čo sa počas hrania mení: lokalitu,
počítadlo, limit, stavy, prijaté úlohy, inventár a predmety v lokalitách. Pri načítaní sa svet
najprv načíta odznova zo `svet.txt` a potom sa naň aplikuje uložený stav.

## Testy

JUnit 5, priečinok `test/logika`:

- `HernePrvkyTest` – lokality, východy, predmety, postavy, úlohy
- `HernyPlanTest` – načítanie sveta, limit, výhra a prehra, texty, pravidlá
- `HraTest` – jednotlivé príkazy, prehra po 8 presunoch, uloženie a načítanie, celá hra najkratšou cestou

## Rozhodnutia nad rámec GDD

- Limit sa kontroluje po každom presune. Keď počítadlo dosiahne limit a mobil nie je nabitý, hráč prehral.
- Na nástupišti sa nedá nič položiť, pretože sa tam hráč už nevráti a hra by sa mohla zaseknúť.
- Texty sú iba v `svet.txt`, preto v zdrojovom kóde nie je diakritika.
