package tennis.personnages;

import java.time.LocalDate;
import java.time.Period;

// Classe “socle” pour les personnages. Rassemble les informations de type état civil.
public class Personne {

    public enum Genre {
        HOMME,
        FEMME
    }

    // Informations figées une fois le constructeur exécuté.
    private final String nomNaissance;
    private final String prenom;
    private final Genre genre;
    private final LocalDate dateNaissance;
    private final String lieuNaissance;

    // Informations susceptibles d’évoluer au cours de la vie du personnage.
    private String nomCourant;
    private String surnom;
    private LocalDate dateDeces;
    private String nationalite;
    private int tailleCm;
    private int poidsKg;
    private boolean estMariee;
    private String nomDuConjoint;

    // Constructeur de base avec les informations essentielles.
    public Personne(String nomNaissance, String prenom, Genre genre, LocalDate dateNaissance, String lieuNaissance) {
        this.nomNaissance = nomNaissance;
        this.prenom = prenom;
        this.genre = genre;
        this.dateNaissance = dateNaissance;
        this.lieuNaissance = lieuNaissance;
    }

    // Constructeur plus complet pour initialiser également des détails optionnels.
    public Personne(String nomNaissance, String prenom, Genre genre, LocalDate dateNaissance, String lieuNaissance,
            String nationalite, int tailleCm, int poidsKg, String surnom) {
        this(nomNaissance, prenom, genre, dateNaissance, lieuNaissance);
        this.nationalite = nationalite;
        setTailleCm(tailleCm);
        setPoidsKg(poidsKg);
        this.surnom = surnom;
    }

    public String getNomNaissance() {
        return nomNaissance;
    }

    // Retourne le nom courant si disponible, sinon renvoie le nom de naissance.
    public String getNomCourant() {
        if (nomCourant != null && !nomCourant.isEmpty()) {
            return nomCourant;
        } else {
            return nomNaissance;
        }
    }

    // Setter privé pour maîtriser les changements de nom.
    private void setNomCourant(String nomCourant) {
        this.nomCourant = nomCourant;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getSurnom() {
        return surnom;
    }

    public void setSurnom(String surnom) {
        this.surnom = surnom;
    }

    public Genre getGenre() {
        return genre;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public LocalDate getDateDeces() {
        return dateDeces;
    }

    // La date de décès ne peut être enregistrée qu’une seule fois pour limiter les erreurs.
    public void setDateDeces(LocalDate dateDeces) {
        if (this.dateDeces == null && dateDeces != null && !dateDeces.isBefore(dateNaissance)) {
            this.dateDeces = dateDeces;
        }
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public int getTailleCm() {
        return tailleCm;
    }

    public void setTailleCm(int tailleCm) {
        if (tailleCm > 0) {
            this.tailleCm = tailleCm;
        }
    }

    public int getPoidsKg() {
        return poidsKg;
    }

    public void setPoidsKg(int poidsKg) {
        if (poidsKg > 0) {
            this.poidsKg = poidsKg;
        }
    }

    public boolean estMariee() {
        return estMariee;
    }

    public String getNomDuConjoint() {
        return nomDuConjoint;
    }

    // Lors d’un mariage, une femme adopte automatiquement le nom du conjoint.
    public void seMarie(String nomConjoint) {
        if (!estMariee && nomConjoint != null && !nomConjoint.isEmpty()) {
            estMariee = true;
            nomDuConjoint = nomConjoint;

            if (genre == Genre.FEMME) {
                setNomCourant(nomConjoint);
            }
        }
    }

    // Annule le mariage et remet les informations associées à zéro.
    public void divorcer() {
        if (estMariee) {
            estMariee = false;
            nomDuConjoint = null;
            if (genre == Genre.FEMME) {
                setNomCourant(null);
            }
        }
    }

    // Indique si la personne est considérée comme décédée.
    public boolean estDecede() {
        return dateDeces != null;
    }

    // Calcule l’âge en tenant compte de la date de décès si elle est renseignée.
    public int getAge() {
        LocalDate fin = (dateDeces == null) ? LocalDate.now() : dateDeces;
        return Period.between(dateNaissance, fin).getYears();
    }

    @Override
    public String toString() {
        return prenom + " " + getNomCourant();
    }
}
