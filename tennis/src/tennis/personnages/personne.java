/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tennis.personnages;

/**
 *
 * @author Int3g
 */
import java.time.LocalDate;
import java.time.Period;


public class personne {
    private final String nomNaissance;
    private final String prenom;
    private final char genre;
    private final LocalDate dateNaissance;
    private final String lieuNaissance;
    private String nationalite;
    private LocalDate dateDeces;


    public personne(String nomNaissance, String prenom, char genre) {
        this.nomNaissance = nomNaissance;
        this.prenom = prenom;
        this.genre = genre;
        this.dateNaissance = LocalDate.of(1990, 1, 1);
        this.lieuNaissance = "";
    }


    public String getNomNaissance() { return nomNaissance; }
    public String getPrenom() { return prenom; }
    public char getGenre() { return genre; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public String getLieuNaissance() { return lieuNaissance; }
    public String getNationalite() { return nationalite; }
    public void setNationalite(String nationalite) { this.nationalite = nationalite; }
    public void setDateDeces(LocalDate dateDeces) { this.dateDeces = dateDeces; }


    public int getAge() {
        LocalDate end = (dateDeces == null) ? LocalDate.now() : dateDeces;
        return Period.between(dateNaissance, end).getYears();
    }


    @Override
    public String toString() {
        return prenom + " " + nomNaissance;
    }
}
