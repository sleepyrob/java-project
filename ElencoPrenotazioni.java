import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

// Classe che lavora direttamente a contatto con gli oggetti Prenotazioni, inserendoli e rimuovendoli
// dalla struttura dati ArrayList, iterando sulla lista ecc.
public class ElencoPrenotazioni {

     // Viene creata la struttura dati che contiene le Prenotazioni (con una capienza
     // indicativa/aggiornabile via via)
    ArrayList<Prenotazione> elenco = new ArrayList<Prenotazione>(10);

    // Metodo che calcola, in base alle scelte dell'utente, il costo della prenotazione.
    public int calcolaCosto(int numero, boolean guida, boolean comitiva) {
	int costo = 0;

	// Nel caso della prenotazione comitiva, la guida costa 40€ e ogni biglietto 3€
	if (comitiva) {
	    if (guida) {
		costo = costo + 40;
	    }
	    costo += 3 * numero;
	}

	// Nel caso della prenotazione standard, la guida costa 50€ e ogni biglietto 5€
	else if (!comitiva) {
	    if (guida) {
		costo = 50;
	    }
	    costo += 5 * numero;
	}
	return costo;
    }

    
    // I tre metodi per inserimenti e rimozioni nella e dalla struttura dati.

    // Il metodo crea un oggetto Prenotazione usando il relativo costruttore e lo
    // inserisce nella struttura dati ArrayList
    public void inserisci(Persona prenotante, Date data, int n, boolean guida) {
	Prenotazione p = new Prenotazione(prenotante, data, n, guida);
	elenco.add(p);
    }

    /*
     * Il metodo crea un oggetto PrenotazioneComitiva usando il relativo costruttore
     * e lo inserisce nella struttura dati ArrayList
     */
    public void inserisciComitiva(Persona prenotante, Date data, int n, boolean guida, Vector<Persona> nomi) {
	Prenotazione p = new PrenotazioneComitiva(prenotante, data, n, guida, nomi);
	elenco.add(p);
    }

    // Metodo per la ricerca, in base al nome registrato, della prenotazione da rimuovere.
    public boolean rimuovi(String nome) {
	// L'int indice è dichiarato perché servirà, dentro il for, per memorizzare
	// 'dove' si trova la Prenotazione nell'ArrayList
	int indice = 0;
	int i = 0;
	boolean trovato = false;

	for (Prenotazione elem : elenco) {
	    String nomeTrovato = elem.getNome();
	    /*
	     * Se il 'nome' del prenotante di una Prenotazione corrisponde al nome inserito
	     * dall'utente, si memorizza l'indice della Prenotazione per rimuoverla subito
	     * dopo
	     */
	    if (nomeTrovato.equalsIgnoreCase(nome)) {
		indice = elenco.indexOf(elem);
		trovato = true;
	    }
	}

	if (trovato) {
	    elenco.remove(indice);
	}

	return trovato;
    }

    
    /*
     * I due metodi per la visualizzazione / stampa (nel main) dell'elenco.
     * "Corrispondono", rispettivamente, ai metodi visualizza() e
     * visualizzaComitiva() del GestioneMain
     */

    public String getElencoFormattato() {
	String elencoFormattato = "";

	/*
	 * Si crea un ArrayList copia di quello principale. In questo modo, il
	 * riordinamento per data non riguarda la struttura dati "elenco" in sé, che
	 * rimane uguale a prima
	 */
	ArrayList<Prenotazione> copiaElenco = new ArrayList<Prenotazione>(elenco);
	Collections.sort(copiaElenco, new RiordinaData());

	if (copiaElenco.size() == 0) {
	    elencoFormattato += "\n\tNon sono presenti prenotazioni.\n";
	} else {
	    /*
	     * Nel caso in cui l'elenco non sia vuota, per ciascuna Prenotazione dell'elenco
	     * si formatta una descrizione, concatenandola alla stringa elencoFormattato
	     */
	    for (Prenotazione prenotazioneElenco : copiaElenco) {
		elencoFormattato += prenotazioneElenco.formattaDescrizione();
	    }
	}

	return elencoFormattato;
    }

    // Metodo per la visualizzazione delle informazioni delle sole Prenotazioni comitiva
    public String getElencoFormattatoComitiva() {
	String elencoFormattato = "";

	// Come nel caso del metodo subito sopra, il riordinamento avviene su una copia
	// dell'ArrayList
	ArrayList<Prenotazione> copiaElenco = new ArrayList<Prenotazione>(elenco);
	Collections.sort(copiaElenco, new RiordinaData());

	// Indice che serve per la guardia successiva, che controlla se ci sono
	// effettivamente Prenotazioni comitiva
	int contatore = 0;
	for (Prenotazione singolaPrenotazione : copiaElenco) {
	    if (singolaPrenotazione instanceof PrenotazioneComitiva)
		contatore++;
	}

	// Nel caso in cui la struttura dati sia vuota oppure non ci siano Prenotazioni comitiva
	if (copiaElenco.size() == 0 || contatore == 0) {
	    elencoFormattato += "\n\tNon sono presenti prenotazioni comitive.\n";
	} else {
	    for (Prenotazione prenotazioneElenco : copiaElenco) {
		/*
		 * Accertato che ci sono prenotazioni comitiva, definisce un elenco formattato
		 * che considera solo queste, invocando il metodo formattaDescrizione() su ogni
		 * prenotazione e aggiornando la String poi restituita al main
		 */
		if (prenotazioneElenco instanceof PrenotazioneComitiva) {
		    elencoFormattato += prenotazioneElenco.formattaDescrizione();
		}
	    }
	}

	return elencoFormattato;
    }


    // I due metodi per la ricerca

    // Metodo per la ricerca nella struttura dati in base al nome dato in input
    public String ricercaNome(String nome) {
	String risultatoRicerca = "";
	int i = 0;

	for (Prenotazione singolaPrenotazione : elenco) {
	    String nomePrenotazione = singolaPrenotazione.getNome();

	    /*
	     * Il confronto delle String senza la considerazione delle maiuscole fa sì che
	     * non siano distintive, ad es. l'utente può cercare 'roberto' (o anche solo 'robe')
	     * e trovare sia 'Roberto' che 'ROBERTO', ecc.
	     * In generale, il confronto avviene fra il 'Nome' di ogni signola prenotazione
	     * (variabile "nomePrenotazione") e il nome dato in input dall'utente
	     * per la ricerca (variabile "nome")
	     */
	    if (nomePrenotazione.toLowerCase().contains(nome.toLowerCase())) {
		// Indice per la guardia successiva
		i++;
		risultatoRicerca += singolaPrenotazione.formattaDescrizione();

		// Nel caso in cui una prenotazione sia di tipo Comitiva, alla sua descrizione
		// si aggiunge l'elenco visitatori
		if (singolaPrenotazione instanceof PrenotazioneComitiva) {
		    PrenotazioneComitiva prenotazioneC = (PrenotazioneComitiva) singolaPrenotazione;
		    String lista = prenotazioneC.getElencoNomi();
		    risultatoRicerca += lista + "\n";
		}
	    }
	}

	if (i == 0) {
	    risultatoRicerca = "\n\tNon esiste nessuna prenotazione sotto il nome indicato.\n";
	}

	return risultatoRicerca;
    }

    // Metodo per la ricerca nella struttura dati in base alla data (già
    // controllata) data in input
    public String ricercaData(Date data) {
	String risultatoRicerca = "";
	int i = 0;

	for (Prenotazione singolaPrenotazione : elenco) {
	    Date dataPrenotazione = singolaPrenotazione.getData();

	    /*
	     * Il compareTo restituisce 0 nel caso in cui le due date (data di
	     * singolaPrenotazione e data definita dall'utente) siano uguali
	     */
	    if (dataPrenotazione.compareTo(data) == 0) {
		i++;
		risultatoRicerca += singolaPrenotazione.formattaDescrizione();

		// Nel caso in cui una prenotazione sia di tipo Comitiva, alla sua descrizione
		// si aggiunge l'elenco visitatori
		if (singolaPrenotazione instanceof PrenotazioneComitiva) {
		    PrenotazioneComitiva prenotazioneC = (PrenotazioneComitiva) singolaPrenotazione;
		    String lista = prenotazioneC.getElencoNomi();
		    risultatoRicerca += lista + "\n";
		}
	    }

	}
	if (i == 0) {
	    risultatoRicerca = "\n\tNon esiste nessuna prenotazione per la data indicata.\n";
	}

	return risultatoRicerca;
    }


    // I due metodi per salvataggio su file e caricamento da file

    public void salvaFile(String nomeInput) {
	// Si definisce un nome file costituito dal nome dato in input più l'estensione
	// file (.dat)
	String nomeFile = nomeInput + ".dat";
	// Si fa una copia dell'elenco
	ArrayList<Prenotazione> elencoDaSalvare = elenco;

	try {
	    // Si definisce e apre uno stream in output a partire da uno stream bufferizzato
	    BufferedOutputStream bufferedS = new BufferedOutputStream(new FileOutputStream(nomeFile));
	    ObjectOutputStream outputStream = new ObjectOutputStream(bufferedS);
	    // Si scrive e salva la copia dell'elenco
	    outputStream.writeObject(elencoDaSalvare);
	    System.out.println("File salvato con successo.");
	    // Si chiude lo stream
	    outputStream.close();

	} catch (IOException e) {
	    System.out.println("Errore nella scrittura: " + e);
	}
    }

    public void apriFile(String nomeInput) {
	// Si definisce un nome file costituito dal nome dato in input più l'estensione
	// file (.dat)
	String nomeFile = nomeInput + ".dat";

	try {
	    // Si definisce e apre uno stream in input a partire da uno stream bufferizzato
	    BufferedInputStream bufferedS = new BufferedInputStream(new FileInputStream(nomeFile));
	    ObjectInputStream inputStream = new ObjectInputStream(bufferedS);
	    
	    /*
	     * Si legge il contenuto di inputStream (salvato precedentemente), che è un
	     * oggetto di tipo ArrayList il che permette di fare un type cast e di salvarlo
	     * in quanto tale
	     */
	    ArrayList<Prenotazione> elencoAperto = (ArrayList<Prenotazione>) inputStream.readObject();

	    // Si aggiorna la variabile elenco con l'elenco importato/aperto
	    elenco = elencoAperto;
	    System.out.println("File importato con successo.");
	    // Si chiude lo stream
	    inputStream.close();

	    // Gestione delle eccezioni
	} catch (ClassNotFoundException e) {
	    System.out.println("Manca l'oggetto nel	file: " + e);
	} catch (IOException e) {
	    System.out.println("Errore I/O: " + e);
	}
    }

}