import java.util.*;
import java.io.*;

// Le istanze di questa classe sono gli oggetti effettivamente memorizzati a partire dall'input utente
public class Prenotazione implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date data;
    private int numero;
    private boolean guida;
    private Persona prenotante;

    /*
     * Costruttore di prenotazione. Lo "stato interno" di ogni prenotazione è dato
     * dalla persona che ha prenotato, dalla data per cui è prevista la visita, dal
     * n di persone per cui ha prenotato e dalla richiesta (o meno) della guida
     */
    public Prenotazione(Persona prenotante, Date data, int numero, boolean guida) {
	this.prenotante = prenotante;
	this.data = data;
	this.numero = numero;
	this.guida = guida;
    }

    // Metodo che restituisce il nome sotto cui è registrata la prenotazione
    public String getNome() {
	return prenotante.getNome();
    }

    // Metodo che restituisce la data per cui è prevista la visita
    public Date getData() {
	return data;
    }

    // Metodo che restituisce il numero di persone per cui è stata registrata la
    // prenotazione
    public int getNumero() {
	return numero;
    }

    // Metodo che restituisce 'true' se la prenotazione è con guida, 'false'
    // altrimenti
    public boolean getGuida() {
	return guida;
    }

    public String formattaDescrizione() {
	String testoFormattato = "\n\tPrenotazione a nome di: " + getNome();

	/*
	 * La data è riformattata per rimuovere l'orario (sarebbe 00:00:00) e per
	 * inserire l'anno fra parentesi
	 */
	String dataFormattata = data.toString().substring(4, 10) + " (" + tagliaAnno() + ")";
	testoFormattato += "\n\tData: " + dataFormattata;

	testoFormattato += "\n\tNumero di persone: " + numero;

	if (guida) {
	    testoFormattato += "\n\tGuida: Sì";
	} else {
	    testoFormattato += "\n\tGuida: No";
	}

	testoFormattato += "\n";
	/*
	 * Il testo formattato, oggetto String, è aggiornato via via con le varie info
	 * sulla specifica prenotazione per poi essere restituito
	 */
	return testoFormattato;
    }

    // Metodo che 'taglia fuori' (per restituirlo) l'anno della data in considerazione
    private String tagliaAnno() {
	String dataStr = data.toString();
	return dataStr.substring(24, dataStr.length());
    }
}