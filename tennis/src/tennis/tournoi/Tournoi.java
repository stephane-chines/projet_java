package tennis.tournoi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tennis.erreurs.SaisieInvalideException;
import tennis.jeu.CategorieMatch;
import tennis.jeu.Match;
import tennis.jeu.ModeJeu;
import tennis.personnages.Arbitre;
import tennis.personnages.Joueur;
import tennis.personnages.Personne;
import tennis.personnages.Spectateur;
import tennis.stats.StatistiquesMatch;

/**
 * Représente un tournoi de tennis et conserve l'état des tours successifs.
 */
public class Tournoi 
{
    private final String nom;
    private final int annee;
    private final TournoiType type;

    private final List<Joueur> participantsHommes = new ArrayList<>();
    private final List<Joueur> participantsFemmes = new ArrayList<>();
    private final List<Arbitre> arbitres = new ArrayList<>();

    private final List<Joueur> joueursEnLiceHommes = new ArrayList<>();
    private final List<Joueur> joueursEnLiceFemmes = new ArrayList<>();
    private final List<Match> matchsAVenir = new ArrayList<>();
    private final List<Match> matchsJoues = new ArrayList<>();

    private int numeroTourHommes = 0;
    private int numeroTourFemmes = 0;
    private boolean tableauHommesTermine = false;
    private boolean tableauFemmesTermine = false;
    private boolean tournoiTermine = false;
    private Joueur championHommes;
    private Joueur championFemmes;

    /**
     * @param nom   nom officiel du tournoi
     * @param annee année d'édition
     * @param type  type du tournoi (surface, ville, règles tie-break)
     */
    public Tournoi(String nom, int annee, TournoiType type) 
    {
        this.nom = nom;
        this.annee = annee;
        this.type = type;
    }

    public String getNom() 
    {
        return nom + " " + annee;
    }

    /**
     * @return type de tournoi associé (ville, surface, règles)
     */
    public TournoiType getType()
    {
        return type;
    }

    /**
     * @return ville dans laquelle se déroule le tournoi
     */
    public String getVille()
    {
        return type.getVille();
    }

    /**
     * @return surface officielle du tournoi
     */
    public String getSurface()
    {
        return type.getSurface();
    }

    public boolean estTermine() 
    {
        return tournoiTermine;
    }

    public List<Match> getMatchsAVenir() 
    {
        return Collections.unmodifiableList(matchsAVenir);
    }

    public List<Match> getMatchsJoues() 
    {
        return Collections.unmodifiableList(matchsJoues);
    }

    /**
     * Inscrit un joueur au tableau principal (limité à 128 participants).
     *
     * @param joueur joueur à enregistrer
     */
    public void inscrireParticipant(Joueur joueur)
    {
        List<Joueur> participants = (joueur.getGenre() == Personne.Genre.HOMME) ? participantsHommes : participantsFemmes;

        if (participants.contains(joueur))
        {
            System.out.println(joueur + " est déjà inscrit au tournoi.");
            return;
        }

        if (participants.size() >= 128)
        {
            System.out.println("Il y a déjà 128 joueurs inscrits dans cette catégorie. " + joueur + " ne peut pas être ajouté.");
            return;
        }

        participants.add(joueur);
        joueur.getStatsCarriere().ajouterParticipationTournoi();
    }

    /**
     * Crée automatiquement le nombre de joueurs et d'arbitres manquants
     * et réinitialise l'état du tournoi.
     */
    public void creerParticipantsAutomatiquement() 
    {
        joueursEnLiceHommes.clear();
        joueursEnLiceFemmes.clear();
        matchsAVenir.clear();
        matchsJoues.clear();
        numeroTourHommes = 0;
        numeroTourFemmes = 0;
        tableauHommesTermine = false;
        tableauFemmesTermine = false;
        tournoiTermine = false;
        championHommes = null;
        championFemmes = null;

        ajusterParticipants(participantsHommes, Personne.Genre.HOMME, "AutoH");
        ajusterParticipants(participantsFemmes, Personne.Genre.FEMME, "AutoF");
        completerArbitres();

        System.out.println("Le tableau messieurs compte " + participantsHommes.size() + " joueurs.");
        System.out.println("Le tableau dames compte " + participantsFemmes.size() + " joueuses.");
        System.out.println(arbitres.size() + " arbitres disponibles.");
    }

    public void lancerProchainTour() 
    {
        if (tournoiTermine) 
        {
            System.out.println("Le tournoi est déjà terminé.");
            return;
        }

        if (!matchsAVenir.isEmpty()) 
        {
            System.out.println("Terminez d'abord les matchs du tour en cours.");
            return;
        }

        boolean aCreeDesMatchs = false;

        if (!tableauHommesTermine)
        {
            if (numeroTourHommes == 0)
            {
                joueursEnLiceHommes.addAll(participantsHommes);
            }
            if (joueursEnLiceHommes.size() < 2)
            {
                tableauHommesTermine = true;
                if (!joueursEnLiceHommes.isEmpty())
                {
                    championHommes = joueursEnLiceHommes.get(0);
                }
            }
            else
            {
                numeroTourHommes++;
                System.out.println("Préparation du tour messieurs " + numeroTourHommes + " (" + joueursEnLiceHommes.size() + " joueurs).");
                aCreeDesMatchs |= preparerMatchesCategorie(joueursEnLiceHommes, CategorieMatch.SIMPLE_HOMMES, numeroTourHommes);
            }
        }

        if (!tableauFemmesTermine)
        {
            if (numeroTourFemmes == 0)
            {
                joueursEnLiceFemmes.addAll(participantsFemmes);
            }
            if (joueursEnLiceFemmes.size() < 2)
            {
                tableauFemmesTermine = true;
                if (!joueursEnLiceFemmes.isEmpty())
                {
                    championFemmes = joueursEnLiceFemmes.get(0);
                }
            }
            else
            {
                numeroTourFemmes++;
                System.out.println("Préparation du tour dames " + numeroTourFemmes + " (" + joueursEnLiceFemmes.size() + " joueuses).");
                aCreeDesMatchs |= preparerMatchesCategorie(joueursEnLiceFemmes, CategorieMatch.SIMPLE_FEMMES, numeroTourFemmes);
            }
        }

        if (!aCreeDesMatchs)
        {
            tournoiTermine = true;
            afficherChampions();
        }
    }

    public void jouerMatch(Match match, ModeJeu mode) throws SaisieInvalideException
    {
        if (!matchsAVenir.contains(match)) 
        {
            throw new SaisieInvalideException("Le match sélectionné n'est pas prévu dans le tour en cours.");
        }

        match.jouerMatch(mode, true);
        matchsAVenir.remove(match);
        matchsJoues.add(match);

        CategorieMatch categorie = match.getCategorie();
        double multiplicateurTour = (categorie == CategorieMatch.SIMPLE_HOMMES) ? numeroTourHommes : numeroTourFemmes;
        double gainVainqueur = 10000.0 * Math.max(1, multiplicateurTour);
        double gainPerdant = 5000.0 * Math.max(1, multiplicateurTour);

        match.getVainqueur().getStatsCarriere().enregistrerMatch(true, gainVainqueur);
        match.getPerdant().getStatsCarriere().enregistrerMatch(false, gainPerdant);
        match.getStatistiquesParJoueur().forEach((joueur, stats) -> joueur.getStatsCarriere().enregistrerStatsMatch(stats));

        if (categorie == CategorieMatch.SIMPLE_HOMMES)
        {
            joueursEnLiceHommes.remove(match.getPerdant());
            if (!resteMatchPourCategorie(CategorieMatch.SIMPLE_HOMMES) && joueursEnLiceHommes.size() == 1)
            {
                tableauHommesTermine = true;
                championHommes = joueursEnLiceHommes.get(0);
            }
        }
        else
        {
            joueursEnLiceFemmes.remove(match.getPerdant());
            if (!resteMatchPourCategorie(CategorieMatch.SIMPLE_FEMMES) && joueursEnLiceFemmes.size() == 1)
            {
                tableauFemmesTermine = true;
                championFemmes = joueursEnLiceFemmes.get(0);
            }
        }

        if (tableauHommesTermine && tableauFemmesTermine)
        {
            tournoiTermine = true;
            afficherChampions();
        }
    }

    public void afficherMatchsAVenir() 
    {
        if (matchsAVenir.isEmpty()) 
        {
            System.out.println("Aucun match à venir pour le moment.");
            return;
        }

        for (int i = 0; i < matchsAVenir.size(); i++) 
        {
            System.out.println((i + 1) + ". " + matchsAVenir.get(i));
        }
    }

    public void afficherMatchsJoues() 
    {
        if (matchsJoues.isEmpty()) 
        {
            System.out.println("Aucun match joué pour le moment.");
            return;
        }

        matchsJoues.forEach(match -> System.out.println(match + " -> " + match.getScoreFinal()));
    }

    private void ajusterParticipants(List<Joueur> participants, Personne.Genre genre, String prefix)
    {
        while (participants.size() > 128)
        {
            participants.remove(participants.size() - 1);
        }

        int indexAuto = participants.size() + 1;
        while (participants.size() < 128)
        {
            Joueur joueur = new Joueur(prefix + "Nom" + indexAuto, prefix + " " + indexAuto, genre,
                    LocalDate.now(), "Ville", Joueur.MainDeJeu.DROITIER, "Sponsor", "Entraîneur", "Blanc", 5);
            joueur.getStatsCarriere().ajouterParticipationTournoi();
            participants.add(joueur);
            indexAuto++;
        }
    }

    private void completerArbitres()
    {
        while (arbitres.size() < 10)
        {
            int idx = arbitres.size() + 1;
            arbitres.add(new Arbitre("ArbitreAuto" + idx, "Arbitre " + idx, Personne.Genre.HOMME, LocalDate.now(), "Ville"));
        }
    }

    private boolean preparerMatchesCategorie(List<Joueur> joueursEnLice, CategorieMatch categorie, int numeroTour)
    {
        if (joueursEnLice.size() < 2)
        {
            return false;
        }

        if (arbitres.isEmpty())
        {
            System.out.println("Aucun arbitre disponible pour organiser les matchs.");
            tournoiTermine = true;
            return false;
        }

        Collections.shuffle(joueursEnLice);

        for (int i = 0; i < joueursEnLice.size(); i += 2) 
        {
            Joueur j1 = joueursEnLice.get(i);
            Joueur j2 = joueursEnLice.get(i + 1);
            Arbitre arbitre = arbitres.get((matchsAVenir.size() + i / 2) % arbitres.size());
            List<Spectateur> spectateurs = genererSpectateursPourMatch(categorie);
            Match match = new Match(j1, j2, arbitre, categorie,
                    (categorie == CategorieMatch.SIMPLE_HOMMES ? "Tour messieurs " : "Tour dames ") + numeroTour,
                    10, type.isTieBreakDernierSetAutorise(), spectateurs);
            matchsAVenir.add(match);
        }
        return true;
    }

    private List<Spectateur> genererSpectateursPourMatch(CategorieMatch categorie)
    {
        List<Spectateur> liste = new ArrayList<>();
        int nombreSpectateurs = 120;
        for (int i = 0; i < nombreSpectateurs; i++)
        {
            Personne.Genre genre = (i % 2 == 0) ? Personne.Genre.HOMME : Personne.Genre.FEMME;
            LocalDate naissance = LocalDate.of(1970 + (i % 30), 1 + (i % 12), 1 + (i % 28));
            Spectateur spectateur = new Spectateur("Spec" + categorie.name() + i, "Fan" + i, genre,
                    naissance, type.getVille(), 40 + (i % 20), "Tribune " + (char)('A' + (i % 4)), 1 + i);
            liste.add(spectateur);
        }
        return liste;
    }

    private boolean resteMatchPourCategorie(CategorieMatch categorie)
    {
        return matchsAVenir.stream().anyMatch(match -> match.getCategorie() == categorie);
    }

    private void afficherChampions()
    {
        if (championHommes != null)
        {
            System.out.println("Vainqueur messieurs : " + championHommes);
        }
        if (championFemmes != null)
        {
            System.out.println("Vainqueur dames : " + championFemmes);
        }
        if (championHommes != null)
        {
            championHommes.getStatsCarriere().afficher();
        }
        if (championFemmes != null)
        {
            championFemmes.getStatsCarriere().afficher();
        }
        afficherSynthese();
    }

    private void afficherSynthese()
    {
        if (matchsJoues.isEmpty())
        {
            System.out.println("Aucun match joué, pas de synthèse disponible.");
            return;
        }

        int totalSpectateurs = matchsJoues.stream()
                .mapToInt(match -> match.getSpectateurs().size())
                .sum();
        double moyenneSpectateurs = totalSpectateurs / (double) matchsJoues.size();

        int totalPoints = matchsJoues.stream()
                .mapToInt(match -> match.getStatistiquesParJoueur().values().stream()
                        .mapToInt(StatistiquesMatch::getTotalPointsRemportes)
                        .sum())
                .sum();
        int ballesUtilisees = totalPoints; // approximation : 1 balle par échange

        long spectateursHommes = matchsJoues.stream()
                .flatMap(match -> match.getSpectateurs().stream())
                .filter(spectateur -> spectateur.getGenre() == Personne.Genre.HOMME)
                .count();
        long spectatricesFemmes = matchsJoues.stream()
                .flatMap(match -> match.getSpectateurs().stream())
                .filter(spectateur -> spectateur.getGenre() == Personne.Genre.FEMME)
                .count();

        System.out.println("\n--- Synthèse du tournoi ---");
        System.out.printf("Matches disputés : %d%n", matchsJoues.size());
        System.out.printf("Spectateurs totaux : %d (moyenne %.1f par match)%n", totalSpectateurs, moyenneSpectateurs);
        System.out.printf("Nombre total de points joués : %d%n", totalPoints);
        System.out.printf("Estimation des balles utilisées : %d%n", ballesUtilisees);
        System.out.printf("Public : %d spectateurs, %d spectatrices%n", spectateursHommes, spectatricesFemmes);
    }
}

