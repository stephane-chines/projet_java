package tennis.personnages;

import java.time.LocalDate;
import tennis.stats.StatistiquesCarriere;

// Représentation d'un joueur avec ses infos et ses actions basiques.
public class Joueur extends Personne implements ActionsSpectateur
{

    // Enumération pour indiquer la main dominante d’un joueur.
    public enum MainDeJeu
    {
        DROITIER,
        GAUCHER
    }

    // Attributs spécifiques aux joueurs.
    private MainDeJeu mainDeJeu;
    private String sponsor;
    private String entraineur;
    private int classement;
    private String couleurTenue;
    private int reputation;
    private StatistiquesCarriere statsCarriere;

    // Compteur statique pour attribuer un classement selon l’ordre de création.
    private static int compteurClassement = 0;

    // Constructeur principal : initialise les données propres à un joueur.
    public Joueur(String nomNaissance, String prenom, Genre genre, LocalDate dateNaissance, String lieuNaissance,
                    MainDeJeu mainDeJeu, String sponsor, String entraineur, String couleurTenue, int reputation) 
    {
        super(nomNaissance, prenom, genre, dateNaissance, lieuNaissance);
        this.mainDeJeu = mainDeJeu;
        this.sponsor = sponsor;
        this.entraineur = entraineur;
        this.couleurTenue = couleurTenue;
        
        // Classement attribué automatiquement pour conserver l’ordre de création.
        this.classement = ++compteurClassement;

        // Réputation bornée entre 1 et 10.
        if (reputation > 10)
        {
            this.reputation = 10;
        }
        else if (reputation < 1)
        {
            this.reputation = 1;
        }
        else
        {
            this.reputation = reputation;
        }

        this.statsCarriere = new StatistiquesCarriere();
    }

    // Accesseurs et mutateurs classiques.
    public MainDeJeu getMainDeJeu() 
    {
        return mainDeJeu;
    }

    public void setMainDeJeu(MainDeJeu mainDeJeu) 
    {
        this.mainDeJeu = mainDeJeu;
    }

    public String getSponsor() 
    {
        return sponsor;
    }

    public void setSponsor(String sponsor) 
    {
        this.sponsor = sponsor;
    }

    public String getEntraineur() 
    {
        return entraineur;
    }

    public void setEntraineur(String entraineur) 
    {
        this.entraineur = entraineur;
    }

    public int getClassement() 
    {
        return classement;
    }

    public void setClassement(int classement) 
    {
        if (classement > 0) 
        {
            this.classement = classement;
        }
    }

    public String getCouleurTenue() 
    {
        return couleurTenue;
    }

    public void setCouleurTenue(String nouvelleCouleur) 
    {
        this.couleurTenue = nouvelleCouleur;
        
        if (getGenre() == Genre.FEMME) 
        {
            System.out.println(getPrenom() + " change la couleur de sa jupe en " + nouvelleCouleur + ".");
        } 
        else 
        {
            System.out.println(getPrenom() + " change la couleur de son short en " + nouvelleCouleur + ".");
        }
    }

    public int getReputation()
    {
        return reputation;
    }

    public StatistiquesCarriere getStatsCarriere()
    {
        return statsCarriere;
    }

    // Actions réalisables par un joueur lorsqu’il est spectateur.
    @Override
    public void applaudir() 
    {
        System.out.println("Le joueur " + getPrenom() + " applaudit un beau point depuis les gradins.");
    }

    @Override
    public void crier() 
    {
        System.out.println("Le joueur " + getPrenom() + " encourage un de ses compatriotes !");
    }

    @Override
    public void huer() 
    {
        System.out.println(getPrenom() + " (dans les gradins) n'est pas d'accord avec cette décision !");
    }

    // Actions en cours de match pour rendre la simulation plus vivante.
    // Fais semblant de lancer un service.
    public void servir() 
    {
        System.out.println(getPrenom() + " se prépare à servir...");
    }

    // Fais semblant de se préparer au retour.
    public void retournerService() 
    {
        System.out.println(getPrenom() + " se met en position pour retourner le service.");
    }
    
    // Même chose pour le renvoi de balle.
    public void renvoyerBalle() 
    {
        System.out.println(getPrenom() + " frappe un coup droit puissant !");
    }

    // Petite faute directe pour la forme.
    public void faireFauteDirecte() 
    {
        System.out.println("Faute directe de " + getPrenom() + " ! La balle est dans le filet.");
    }

    // Appelle l'arbitre quand ça râle.
    public void appelerArbitre(Arbitre arbitre, String motif) 
    {
        System.out.println(getPrenom() + " n'est pas d'accord et interpelle l'arbitre: '" + motif + "' !");
        arbitre.resoudreLitige(this, motif);
    }
    
    // Petit boost perso.
    public void sEncourager() 
    {
        System.out.println(getPrenom() + " se motive: 'Allez ! Come on !'");
    }

    // Pense à boire un coup.
    public void boire() 
    {
        System.out.println(getPrenom() + " profite de la pause pour s'hydrater.");
    }

    public void crierVictoire() 
    {
        System.out.println(getPrenom() + " lève les bras au ciel en signe de victoire !");
    }

    public void crierDefaite() 
    {
        System.out.println(getPrenom() + " jette sa raquette de frustration.");
    }
    
    @Override
    public String toString() 
    {
        return getPrenom() + " " + getNomCourant() + " (#" + classement + ")";
    }
}