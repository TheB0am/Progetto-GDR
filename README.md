[README.md](https://github.com/user-attachments/files/32163150/README.md)
# WBC - World Boxing Championship

Gioco di ruolo a turni ambientato nel mondo della boxe: si crea un personaggio
scegliendo uno stile di combattimento (Brawler, In-Fighter, Defense Lab,
Out-Boxer), lo si fa combattere contro una sequenza di avversari e infine
contro un boss finale. È disponibile sia una versione con interfaccia
grafica (JavaFX, combattimento a turni con bottone "Attacca") sia una
versione da riga di comando (combattimento automatico).

Il progetto include un sistema di salvataggi multipli in formato JSON: ogni
salvataggio ha un nome scelto dal giocatore e conserva sia il personaggio
sia lo stato esatto di tutti i nemici incontrati (compreso l'eventuale
boss), così da poter riprendere la partita esattamente da dove è stata
interrotta.

## Struttura del progetto

Il codice è organizzato in package secondo la responsabilità di ciascuna
classe:

```
org.it.unicam.cs.mpgc.rpg125943.model        classi di dominio (Entity, Player, Enemy, Boss, Styles, Opponents, ...)
org.it.unicam.cs.mpgc.rpg125943.engine       motore di combattimento condiviso (BattleEngine)
org.it.unicam.cs.mpgc.rpg125943.engine.auto  modalità automatica, da console (GameSession, Main)
org.it.unicam.cs.mpgc.rpg125943.engine.turns modalità a turni, per la UI (Turns)
org.it.unicam.cs.mpgc.rpg125943.persistence  salvataggio/caricamento partite (PlayerData, EnemyData, GameSaveData, SaveManager)
org.it.unicam.cs.mpgc.rpg125943.ui           interfaccia grafica JavaFX (App, MenuScreen, SaveListScreen, GameScreen)
```

## Requisiti

- JDK 17 o superiore
- Non serve installare Gradle: il progetto include il wrapper (`gradlew`)

## Come eseguirlo

**Versione con interfaccia grafica (JavaFX):**

```bash
./gradlew run
```

**Versione da riga di comando:**

Eseguire la classe `Main` (`org.it.unicam.cs.mpgc.rpg125943.engine.auto.Main`)
dal proprio IDE, oppure tramite Gradle indicando la main class alternativa.


## Uso di strumenti di intelligenza artificiale

Durante la realizzazione del progetto è stato utilizzato Claude
come assistente di programmazione, in particolare per:

- discutere scelte di progettazione (separazione tra logica di dominio,
  motore di combattimento e interfaccia grafica; organizzazione in package;
  applicazione dei principi SOLID)
- individuare e correggere errori di compilazione e di comportamento
- rivedere il codice segnalando duplicazioni e possibili violazioni dei
  principi di buona progettazione

La scrittura, l'integrazione e il testing del codice nel progetto sono stati
svolti dall'autore.
