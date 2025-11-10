package tennis;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import tennis.erreurs.SaisieInvalideException;
import tennis.jeu.Match;
import tennis.jeu.ModeJeu;
import tennis.personnages.Joueur;
import tennis.personnages.Personne;
import tennis.tournoi.Tournoi;
import tennis.tournoi.TournoiType;

// Point d'entrée console : on fait simple, style projet étudiant.
public class Application 
{
    private final Scanner scanner = new Scanner(System.in);
    private final List<Joueur> joueursCrees = new ArrayList<>();
    private Tournoi tournoiEnCours = null;

    // main pour lancer tout ça.
    public static void main(String[] args) 
    {
        Application app = new Application();
        app.lancer();
    }

    // Menu principal en boucle jusqu'à ce que l'utilisateur quitte.
    public void lancer() 
    {
        System.out.println("Bienvenue dans le gestionnaire de tournoi de tennis !");

        while (true) 
        {
            afficherMenuPrincipal();
            try 
            {
                int choix = demanderChoixUtilisateur();
                switch (choix) 
                {
                    case 1 -> creerPersonnage();
                    case 2 -> creerTournoi();
                    case 3 -> {
                        if (tournoiEnCours != null) 
                        {
                            gererTournoi();
                        } 
                        else 
                        {
                            System.out.println("Erreur : aucun tournoi n'a été créé pour le moment.");
                        }
                    }
                    case 4 -> afficherInfosJoueurs();
                    case 0 -> {
                        System.out.println("Merci d'avoir utilisé l'application. À bientôt !");
                        return;
                    }
                    default -> throw new SaisieInvalideException("Ce choix n'existe pas dans le menu.");
                }
            } 
            catch (InputMismatchException e) 
            {
                System.out.println("Erreur : veuillez entrer un nombre entier.");
                scanner.nextLine(); // nettoie le buffer
            } 
            catch (SaisieInvalideException e) 
            {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
    }

    private void afficherMenuPrincipal() 
    {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Créer un personnage (joueur)");
        System.out.println("2. Créer un nouveau tournoi");
        System.out.println("3. Gérer le tournoi en cours");
        System.out.println("4. Afficher les informations des joueurs créés");
        System.out.println("0. Quitter");
    }

    private int demanderChoixUtilisateur() 
    {
        System.out.print("Votre choix : ");
        int choix = scanner.nextInt();
        scanner.nextLine(); // consomme le retour chariot
        return choix;
    }

    // Création d'un joueur maison.
    private void creerPersonnage() 
    {
        System.out.println("\n--- Création d'un joueur ---");
        System.out.print("Prénom : ");
        String prenom = scanner.nextLine();
        System.out.print("Nom : ");
        String nom = scanner.nextLine();
        Personne.Genre genre = demanderGenreJoueur();

        Joueur joueur = new Joueur(nom, prenom, genre, LocalDate.now(), "Inconnu",
                Joueur.MainDeJeu.DROITIER, "Sponsor", "Entraîneur", "Blanc", 5);
        joueursCrees.add(joueur);
        System.out.println("Joueur " + joueur + " créé avec succès.");
    }

    // Création d'un tournoi et préparation du tableau.
    private void creerTournoi() 
    {
        System.out.println("\n--- Création d'un tournoi ---");
        System.out.print("Nom du tournoi (ex: Roland-Garros) : ");
        String nom = scanner.nextLine();

        TournoiType type = demanderTypeTournoi();

        tournoiEnCours = new Tournoi(nom, LocalDate.now().getYear(), type);

        if (!joueursCrees.isEmpty())
        {
            System.out.println("Inscription des joueurs créés manuellement...");
            joueursCrees.forEach(tournoiEnCours::inscrireParticipant);
        }

        tournoiEnCours.creerParticipantsAutomatiquement();
        tournoiEnCours.lancerProchainTour();

        System.out.println("Le tournoi " + tournoiEnCours.getNom() + " (" + tournoiEnCours.getVille() + ", surface "
                + tournoiEnCours.getSurface() + ") est prêt. Utilisez le menu \"Gérer le tournoi\" pour lancer les matchs.");
    }

    // Sous-menu pour piloter le tournoi déjà créé.
    private void gererTournoi() throws SaisieInvalideException 
    {
        while (true) 
        {
            System.out.println("\n--- Gestion du tournoi : " + tournoiEnCours.getNom() + " ---");
            System.out.println("1. Afficher les matchs à venir");
            System.out.println("2. Jouer un match");
            System.out.println("3. Afficher les matchs déjà joués");
            System.out.println("4. Lancer le tour suivant");
            System.out.println("0. Retour au menu principal");

            int choix = demanderChoixUtilisateur();
            switch (choix) 
            {
                case 1 -> tournoiEnCours.afficherMatchsAVenir();
                case 2 -> jouerUnMatchDuTournoi();
                case 3 -> tournoiEnCours.afficherMatchsJoues();
                case 4 -> tournoiEnCours.lancerProchainTour();
                case 0 -> { return; }
                default -> throw new SaisieInvalideException("Choix invalide dans la gestion du tournoi.");
            }
        }
    }

    // Choisir un match et le jouer (manuel ou auto).
    private void jouerUnMatchDuTournoi() throws SaisieInvalideException 
    {
        List<Match> matchsAVenir = tournoiEnCours.getMatchsAVenir();
        if (matchsAVenir.isEmpty()) 
        {
            System.out.println("Aucun match disponible. Lancez le prochain tour.");
            return;
        }

        System.out.println("\nMatchs à jouer :");
        for (int i = 0; i < matchsAVenir.size(); i++) 
        {
            System.out.println((i + 1) + ". " + matchsAVenir.get(i));
        }

        System.out.print("Sélectionnez un match : ");
        int index = demanderChoixUtilisateur() - 1;
        if (index < 0 || index >= matchsAVenir.size()) 
        {
            throw new SaisieInvalideException("Numéro de match invalide.");
        }

        System.out.print("Mode de jeu (1: Manuel, 2: Automatique) : ");
        int choixMode = demanderChoixUtilisateur();
        if (choixMode != 1 && choixMode != 2)
        {
            throw new SaisieInvalideException("Mode de jeu inexistant.");
        }
        ModeJeu mode = (choixMode == 1) ? ModeJeu.MANUEL : ModeJeu.AUTOMATIQUE;

        tournoiEnCours.jouerMatch(matchsAVenir.get(index), mode);

        if (!tournoiEnCours.estTermine() && tournoiEnCours.getMatchsAVenir().isEmpty())
        {
            System.out.println("Fin du tour. Préparation du tour suivant...");
            tournoiEnCours.lancerProchainTour();
        }
    }

    // Petit menu pour choisir le type de tournoi.
    private TournoiType demanderTypeTournoi()
    {
        System.out.println("Choisissez le type de tournoi :");
        TournoiType[] types = TournoiType.values();
        for (int i = 0; i < types.length; i++)
        {
            System.out.println((i + 1) + ". " + types[i].name() + " (" + types[i].getVille() + " - "
                    + types[i].getSurface() + ")");
        }

        while (true)
        {
            System.out.print("Votre choix : ");
            String saisie = scanner.nextLine();
            try
            {
                int choix = Integer.parseInt(saisie);
                if (choix >= 1 && choix <= types.length)
                {
                    return types[choix - 1];
                }
            }
            catch (NumberFormatException e)
            {
                // boucle
            }
            System.out.println("Choix invalide, merci de recommencer.");
        }
    }

    // Retourne le genre suivant la saisie (H/F).
    private Personne.Genre demanderGenreJoueur()
    {
        while (true)
        {
            System.out.print("Genre (1: Homme, 2: Femme) : ");
            String saisie = scanner.nextLine();
            if ("1".equals(saisie))
            {
                return Personne.Genre.HOMME;
            }
            if ("2".equals(saisie))
            {
                return Personne.Genre.FEMME;
            }
            System.out.println("Saisie invalide, merci de recommencer.");
        }
    }

    // Petit listing des joueurs déjà créés.
    private void afficherInfosJoueurs() 
    {
        if (joueursCrees.isEmpty()) 
        {
            System.out.println("Aucun joueur créé pour le moment.");
            return;
        }

        System.out.println("\n--- Joueurs créés ---");
        joueursCrees.forEach(joueur -> {
            System.out.println(joueur);
            joueur.getStatsCarriere().afficher();
        });
    }
}

