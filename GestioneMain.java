import java.util.*;

// Programma per la gestione prenotazioni visite a un museo
// Realizzato da Roberto Cannarella (matr. 616400) per il corso di Programmazione in Java (6 CFU) 

// Classe contenente il main, che quindi si occupa di gestire l'input da tastiera
// (sia per l'esplorazione dell'interfaccia testuale che per l'inserimento di dati)
// e la stampa a schermo
public class GestioneMain {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
	// Dichiarazione della variabile 'prenotazioni'
	// cioè dell'oggetto che si occupa nel resto del programma di lavorare col
	// vettore delle Prenotazioni
	ElencoPrenotazioni prenotazioni = new ElencoPrenotazioni();

	// La variabile char (poi usata per controllare le decisioni dell'utente) è già
	// definita
	// perché usata nell'espressione booleana del ciclo do-while
	char c = (char) 0;
	System.out.println("PRENOTAZIONI VISITE MUSEO");

	do {
	    // Evocazione del metodo di stampa delle funzionalità programma
	    stampaFunzionalita();

	    try {
		// La variabile c è aggiornata in base al carattere dato in input dall'utente
		c = input.nextLine().charAt(0);
		// Il carattere dell'utente indica quale azione eseguire
		switch (c) {

		case 'A':
		case 'a':
		    aggiungiPrenotazione(prenotazioni);
		    break;

		case 'R':
		case 'r':
		    rimuoviPrenotazione(prenotazioni);
		    break;

		case 'V':
		case 'v':
		    visualizza(prenotazioni);
		    break;

		case 'C':
		case 'c':
		    visualizzaComitive(prenotazioni);
		    break;

		case 'N':
		case 'n':
		    ricercaNome(prenotazioni);
		    break;

		case 'D':
		case 'd':
		    ricercaData(prenotazioni);
		    break;

		/*
		 * Sia per il salvataggio che per l'import si richiede all'utente il nome del
		 * file da salvare/aprire e lo si passa come stringa ai relativi metodi di
		 * ElencoPrenotazioni
		 */
		case 'S':
		case 's':
		    System.out.println("Inserisci il nome da dare al file:");
		    String nomeUtenteSalvare = input.nextLine();
		    prenotazioni.salvaFile(nomeUtenteSalvare);
		    break;

		case 'I':
		case 'i':
		    System.out.println("Inserisci il nome del file da aprire:");
		    String nomeUtenteAprire = input.nextLine();
		    prenotazioni.apriFile(nomeUtenteAprire);
		    break;

		case 'M':
		case 'm':
		    daiInformazioniMuseo();
		    break;

		case 'X':
		case 'x':
		    System.out.println("Chiusura del programma.");
		    break;

		default:
		    System.out.println("Comando non riconosciuto.");

		}
	    } catch (Exception e) {
	    }
	    /**
	     * Evita il termine del programma per eccezioni come l'inserimento, da parte di
	     * un utente, di una riga vuota
	     */

	    // Comando per la chiusura del programma
	} while (c != 'X');
    }

    // [A] Metodo per la registrazione di una nuova prenotazione
    private static void aggiungiPrenotazione(ElencoPrenotazioni prenotazioni) {

	// Richiesta del nome della persona che prenota. Il nome viene usato per creare
	// un'istanza della classe Persona
	System.out.println("Inserisci il nome di chi prenota:");
	String nome = input.nextLine();
	Persona prenotante = new Persona(nome);

	/**
	 * L'inserimento della data è gestito attraverso un metodo apposito, che
	 * controlla sia il tipo di dato inserito dall'utente che la sua
	 * 'veridicibilità'. E' stato usato un metodo perché questi stessi controlli
	 * sono necessari in ricercaData()
	 */
	Date data = gestisciInputData();

	// Richiesta della guida
	System.out.println("Vuoi richiedere una guida? Il prezzo è di 50€. Scrivi [S] per Sì, oppure [N] per No:");
	boolean accettato;
	boolean guida = gestisciInputGuida();

	// Richiesta del numero di visitatori.
	int n = gestisciInputVisitatori();
	// Per evitare problemi di input dovuti a nextInt...
	input.nextLine();

	// Viene creato un vettore che sarà utilizzato nel caso delle Prenotazioni comitiva
	Vector<Persona> nomi = new Vector<Persona>();
	boolean comitiva = false;

	// Nel caso il numero di visitatori sia superiore a 10, si propone all'utente
	// la possibilità di prenotare come comitiva
	if (n > 10) {
	    accettato = false;
	    do {
		try {
		    System.out.println("Stai prenotando per più di dieci persone."
			    + " Vuoi richiedere lo sconto comitiva (la guida costa 40€ e ogni biglietto 3€)?"
			    + "\nRispondi [S] per sì, [N] per no:");

		    char risposta = input.nextLine().charAt(0);

		    if (risposta == 'S' || risposta == 's') {
			// Nel caso l'utente voglia lo sconto comitiva, si memorizza questa informazione
			// come boolean
			comitiva = true;
			System.out.println(
				"Per usufruire dello sconto comitiva è necessario dare i nominativi dei visitatori."
					+ " Inserisci un nome per volta e premi invio:");

			int i = 0;
			while (i < n) {
			    /*
			     * Registrazione dei nomi dei visitatori. Sono usati per la creazione di oggetti
			     * Persona, aggiunti al Vettore dichiarato precedentemente
			     */
			    String nomeVisitatore = input.nextLine();
			    Persona visitatore = new Persona(nomeVisitatore);
			    nomi.add(visitatore);
			    i++;
			}
			accettato = true;

		    } else if (risposta == 'N' || risposta == 'n') {
			comitiva = false;
			accettato = true;

			// Controllo dei casi in cui l'utente inserisce erroneamente altri caratteri
		    } else {
			System.out.println("Carattere non accettato. Riprova [S/N]:");
		    }
		} catch (Exception e) {
		    System.out.println("Inserisci un carattere: S per prenotare una guida, N per non farlo:");
		}

	    } while (!accettato);
	}

	/*
	 * Si calcola, alla luce delle informazioni date dall'utente, il costo della
	 * prenotazione per poi chiedere se conferma la prenotazione
	 */
	int costo = prenotazioni.calcolaCosto(n, guida, comitiva);
	System.out.println("La prenotazione costa " + costo + "€. Procedere? [S/N]");

	// Conferma finale
	boolean finito = false;
	do {
	    try {
		char conferma = input.nextLine().charAt(0);

		// Conferma positiva
		if (conferma == 'S' || conferma == 's') {
		    finito = true;
		    /*
		     * Si usa il boolean definito precedentemente per indirizzare l'inserimento in
		     * base al tipo di prenotazione (normale/comitiva). L'inserimento infatti
		     * implica la creazione di un oggetto con variabili diversi in base alla sua
		     * appartenenza alla superclasse Prenotazione o a PrenotazioneComitiva. In
		     * entrambi i casi, l'inserimento avviene attraverso l'invocazione del metodo
		     * dell'oggetto ElencoPrenotazioni.
		     */
		    if (comitiva) {
			prenotazioni.inserisciComitiva(prenotante, data, n, guida, nomi);
			comitiva = false;
		    } else {
			prenotazioni.inserisci(prenotante, data, n, guida);
		    }
		    System.out.println("Prenotazione registrata.");

		    // Conferma negativa
		} else if (conferma == 'N' || conferma == 'n') {
		    finito = true;
		    System.out.println("La prenotazione è stata annullata.");

		    // Controlli dei casi di input non corretti
		} else {
		    System.out.println("Carattere non accettato. Riprova [S/N]:");
		}

	    } catch (Exception e) {
		System.out.println("Inserisci un carattere: S per confermare, N per non annullare");
	    }
	} while (!finito);
    }

    // [R] Metodo per la rimozione di una prenotazione precedentemente registrata
    private static void rimuoviPrenotazione(ElencoPrenotazioni prenotazioni) {
	// Si richiede all'utente il nome sotto cui è stata registrata la prenotazione
	System.out.println("Inserisci il nome di chi ha prenotato:");
	String prenotazioneDaCancellare = input.nextLine();

	/*
	 * La ricerca della prenotazione e la relativa rimozione avvengono al livello
	 * dell'ElencoPrenotazioni, che lavora effettivamente con la struttura dati
	 * contenente le prenotazioni. Nel main si restituisce solo un boolean per
	 * informare l'utente circa l'esito della ricerca/rimozione.
	 */
	boolean trovato = prenotazioni.rimuovi(prenotazioneDaCancellare);
	if (trovato) {
	    System.out
		    .println("Rimozione della prenotazione di " + prenotazioneDaCancellare + " avvenuta con successo.");
	} else {
	    System.out.println("Contatto non trovato.");
	}
    }

    /*
     * [V] Metodo per la visualizzazione/stampa di tutte le prenotazioni registrate
     * (Nel caso delle P. Comitiva, non stampa l'elenco dei visitatori.)
     */
    private static void visualizza(ElencoPrenotazioni prenotazioni) {
	System.out.println("Ecco l'elenco delle prenotazioni:");
	// L'elenco è generato, come stringa, da un metodo di ElencoPrenotazioni, e
	// stampato nel main
	String elencoFormattato = prenotazioni.getElencoFormattato();
	System.out.println(elencoFormattato);
    }

    /*
     * [C] Metodo per la visualizzazione/stampa delle sole prenotazioni comitiva
     * (Nel caso delle P. Comitiva, non stampa l'elenco dei visitatori.)
     */
    private static void visualizzaComitive(ElencoPrenotazioni prenotazioni) {
	System.out.println("Ecco l'elenco delle prenotazioni delle comitive:");
	String elencoComitiveFormattato = prenotazioni.getElencoFormattatoComitiva();
	System.out.println(elencoComitiveFormattato);
    }

    /*
     * [N] Metodo per l'invocazione del metodo di ricerca in base al nome e relativa
     * stampa del risultato.
     */
    private static void ricercaNome(ElencoPrenotazioni prenotazioni) {
	System.out.println("Inserisci il nome da cercare:");
	String nomeRicerca = input.nextLine();
	String risultatoRicercaNome = prenotazioni.ricercaNome(nomeRicerca);
	System.out.println(risultatoRicercaNome);
    }

    /*
     * [D] Metodo per l'invocazione del metodo di ricerca per data e relativa stampa
     * del risultato.
     */
    private static void ricercaData(ElencoPrenotazioni prenotazioni) {
	// Si richiama il metodo che fa i necessari controlli di input per la
	// definizione della data
	Date data = gestisciInputData();
	String risultatoRicercaData = prenotazioni.ricercaData(data);
	System.out.println(risultatoRicercaData);
    }

    /*
     * [M] Metodo per la stampa delle informazioni riguardanti i costi di
     * prenotazione.
     */
    private static void daiInformazioniMuseo() {
	System.out.println("\nQuesto programma ti permette di prenotare la tua prossima visita al museo."
		+ "\nI prezzi base sono:\n\t5€: costo di ogni biglietto"
		+ "\n\t50€: costo della guida\n\nNel caso si prenoti per più di 10 persone, è possibile richiedere lo 'sconto comitiva'."
		+ "\nI prezzi in questo caso diventano:\n\t3€: costo di ogni biglietto" + "\n\t40€: costo della guida");
	System.out.println("");
    }

    // Controllo generico per la definizione di una data in input.
    // Richiama sia il controllo delle cifre con prendiInputData() [1]
    // che il controllo della semantica della data con validatore(...) [2]
    private static Date gestisciInputData() {
	boolean dataConfermata = false;
	Date data = new Date();
	do {
	    // [1] Tre valori, uno per ogni parte della data, tutti ottenuti usando
	    // l'apposito metodo di controllo del lv. char
	    System.out.println("Inserisci l'anno per cui è prevista la visita (in cifre):");
	    int y = prendiInputData();
	    System.out.println("Inserisci il mese (in cifre):");
	    int m = prendiInputData() - 1;
	    System.out.println("Inserisci il giorno (in cifre):");
	    int d = prendiInputData();
	    input.nextLine();

	    // [2] Controllo della data in input. Se valida(ta), è possibile salvarla in una
	    // variabile
	    boolean dataValidata = validatore(y, m, d);
	    if (dataValidata) {
		Calendar calendario = new GregorianCalendar(y, m, d);
		calendario.setLenient(false);
		data = calendario.getTime();
		dataConfermata = true;
	    } else {
		System.out.println("Data in ingresso non valida."
			+ " Probabilmente hai inserito dei valori troppo alti. Riproviamo:");
	    }
	} while (!dataConfermata);

	// Restituzione della data (controllata e validata)
	return data;
    }

    // [1] Controllo al livello "base", cioè dei caratteri dati in input dall'utente
    private static int prendiInputData() {
	int numeroInput = 0;
	boolean valoreOk;

	// Controlli a livello del semplice input (evitare caratteri testuali, ecc.)
	do {
	    valoreOk = true;
	    try {
		numeroInput = input.nextInt();
	    } catch (InputMismatchException e) {
		input.nextLine();
		System.out.println("Input non accettato. Riproviamo, ricorda di inserire solo cifre.");
		valoreOk = false;
	    }
	} while (!valoreOk);
	return numeroInput;
    }

    // [2] Controllo della 'semantica' della data inserita
    private static boolean validatore(int y, int m, int d) {
	try {
	    /*
	     * Creazione di un nuovo oggetto con le coordinate date dall'utente Setting come
	     * 'non lenient' (cioè non permissivo, controlla la 'veridicità' della data
	     * tentata dall'utente
	     */
	    Calendar calendario = new GregorianCalendar(y, m, d);
	    calendario.setLenient(false);
	    Date data = calendario.getTime();
	} catch (Exception e) {
	    /*
	     * Se la data non è possibile, lancia un'eccezione, il che restituisce false, di
	     * fatto definendo la data come 'non valida'
	     */
	    return false;
	}
	return true;
    }

    public static boolean gestisciInputGuida() {
	boolean accettato;
	boolean guida = false;

	do {
	    // Finché l'utente non inserisce qualcosa fra S e N (facendo diventare accettato
	    // true), il ciclo va avanti
	    accettato = false;
	    try {
		char rispostaGuida = input.nextLine().charAt(0);

		// Blocchi che gestiscono l'accettazione oppure rifiuto, da parte dell'utente,
		// della guida
		if (rispostaGuida == 'S' || rispostaGuida == 's') {
		    guida = true;
		    accettato = true;
		} else if (rispostaGuida == 'N' || rispostaGuida == 'n') {
		    guida = false;
		    accettato = true;

		} else {
		    // Nel caso di altre lettere come input...
		    System.out.println("Carattere non accettato. Riprova [S/N]:");
		}
	    } catch (Exception e) {
		// Nel caso ad esempio della riga vuota...
		System.out.println("Inserisci un carattere: S per prenotare una guida, N per non farlo:");
	    }
	} while (!accettato);

	return guida;
    }

    public static int gestisciInputVisitatori() {
	boolean numeroOk;
	int numeroVisitatori = 0;

	do {
	    // Il ciclo va avanti finché l'utente non inserisce un numero valido
	    numeroOk = true;
	    try {
		System.out.println("Inserisci il numero di persone per cui si sta prenotando:");
		numeroVisitatori = input.nextInt();
		
	    } catch (InputMismatchException e) {
		// Gestione dei casi in cui l'input non va bene (ad es. non è un intero)
		input.nextLine();
		System.out.println("Input non accettato. Riproviamo, ricorda di inserire solo cifre.");
		numeroOk = false;
	    }
	} while (!numeroOk);

	return numeroVisitatori;
    }

    // [M] Metodo che mostra a schermo le funzionalità del programma e i relativi comandi
    private static void stampaFunzionalita() {
	System.out.println("Decidi cosa fare, scrivendo il carattere che vedi fra [] e premendo Invio:\n");
	System.out.println("\t[A] - Aggiungi una prenotazione");
	System.out.println("\t[R] - Rimuovi una prenotazione");
	System.out.println("\t[V] - Visualizza l'elenco delle prenotazioni");
	System.out.println("\t[C] - Visualizza l'elenco delle prenotazioni di comitive");
	System.out.println("\t[N] - Cerca le prenotazioni associate a un nome");
	System.out.println("\t[D] - Cerca le prenotazioni per una particolare data");
	System.out.println("\t[S] - Salva l'elenco delle prenotazioni su un file");
	System.out
		.println("\t[I] - Importa l'elenco delle prenotazioni, caricandolo da un file salvato precedentemente");
	System.out.println("\t[M] - Leggi informazioni riguardanti il costo delle prenotazioni");
	System.out.println("\t[X] - Interrompi il programma");
    }

}