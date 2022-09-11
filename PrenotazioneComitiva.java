import java.util.*;

// Classe che estende Prenotazione. Rappresenta infatti un (sotto)tipo particolare di prenotazione,
// cioè le prenotazioni per comitive
public class PrenotazioneComitiva extends Prenotazione {

    private static final long serialVersionUID = 1L;
    private Vector<Persona> nomi;

    // Costruttore che usa il costruttore della superclasse e in più salva l'elenco
    // dei visitatori in 'nomi'
    public PrenotazioneComitiva(Persona prenotante, Date data, int numero, boolean guida, Vector<Persona> vettore) {
	super(prenotante, data, numero, guida);
	nomi = vettore;
    }

    // Metodo che fa overriding di quello di Prenotazione per aggiungere l'informazione
    // "Prenotazione comitiva"
    public String formattaDescrizione() {
	// Il metodo della superclasse ha un'ultima line (solitamente di 'separazione'
	// fra le descrizioni) in questo caso da rimuovere
	int ultimaRiga = super.formattaDescrizione().lastIndexOf("\n");
	String testoFormattato = super.formattaDescrizione().substring(0, ultimaRiga);

	testoFormattato = testoFormattato + "\n\tPrenotazione comitiva";
	testoFormattato = testoFormattato + "\n";

	/*
	 * Il testo formattato, oggetto String, è aggiornato via via con le varie info
	 * sulla specifica prenotazione per poi essere restituito
	 */
	return testoFormattato;
    }

    // Metodo che restituisce l'elenco dei visitatori. Invocato quando l'utente compie una ricerca
    public String getElencoNomi() {
	String listaVisitatori = "\tElenco dei visitatori:";
	Persona primaPersona = nomi.get(0);
	String elencoNomi = " " + primaPersona.getNome();
	
	// Una volta riempito "il primo posto" della stringa col primo nome ('primaPersona'),
	// è possibile inserire tutti gli altri separandoli con una virgola
	for (Persona visitatore : nomi) {
	    elencoNomi = elencoNomi + ", " + visitatore.getNome();
	}
	listaVisitatori = listaVisitatori + elencoNomi;
	return listaVisitatori;
    }
}
