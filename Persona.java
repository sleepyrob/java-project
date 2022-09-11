import java.io.*;

// Classe generica per rappresentare chi prenota e i visitatori che fanno parte di una comitiva
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nome;

    public Persona(String nome) {
	this.nome = nome;
    }
    
    // Metodo per la restituzione del nome della singola Persona
    public String getNome() {
	return nome;
    }

}
