import java.util.*;

/* Classe che implementa un comparatore per gli oggetti Prenotazione.
*	Permette il riordinamento dell'array in base alla data. */
public class RiordinaData implements Comparator<Prenotazione> {

    public int compare(Prenotazione Prenotazione1, Prenotazione Prenotazione2) {
	/*
	 * Si mettono a confronto le date delle due Prenotazioni da riordinare. Il
	 * metodo ritorna 0 quando sono uguali
	 */
	Date primaPrenotazione = Prenotazione1.getData();
	Date secondaPrenotazione = Prenotazione2.getData();
	return primaPrenotazione.compareTo(secondaPrenotazione);
    }
}