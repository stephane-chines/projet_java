package tennis.jeu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import tennis.personnages.Arbitre;
import tennis.personnages.Joueur;
import tennis.personnages.Spectateur;
import tennis.stats.StatistiquesMatch;

/**
 * Représente une rencontre entre deux joueurs.
 * Un match est composé de plusieurs sets que l'on joue en mode manuel ou automatique.
 */
public class Match 
{
    private final Joueur joueur1;
    private final Joueur joueur2;
    private final Arbitre arbitre;
    private final CategorieMatch categorie;
    private final String niveauTournoi;
    private final int pointsATPgagnes;
    private final boolean tieBreakDernierSetAutorise;
    private final List<Spectateur> spectateurs;
    private final Map<Joueur, StatistiquesMatch> statistiquesParJoueur = new HashMap<>();

    private Joueur vainqueur;
    private Joueur perdant;

    private final int nombreSetsGagnants;
    private int scoreSetsJoueur1;
    private int scoreSetsJoueur2;

    public Match(Joueur joueur1, Joueur joueur2, Arbitre arbitre, CategorieMatch categorie, String niveauTournoi,
            int pointsATPgagnes, boolean tieBreakDernierSetAutorise, List<Spectateur> spectateurs) 
    {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.arbitre = arbitre;
        this.categorie = categorie;
        this.niveauTournoi = niveauTournoi;
        this.pointsATPgagnes = pointsATPgagnes;
        this.tieBreakDernierSetAutorise = tieBreakDernierSetAutorise;
        this.spectateurs = Collections.unmodifiableList(new ArrayList<>(spectateurs));
        this.nombreSetsGagnants = (categorie == CategorieMatch.SIMPLE_HOMMES) ? 3 : 2;
        statistiquesParJoueur.put(joueur1, new StatistiquesMatch());
        statistiquesParJoueur.put(joueur2, new StatistiquesMatch());
    }

    /**
     * Lance le match complet jusqu'à ce qu'un joueur atteigne le nombre de sets requis.
     *
     * @param mode             manuel ou automatique
     * @param afficherDetails  indique si l'on souhaite des commentaires détaillés
     */
    public void jouerMatch(ModeJeu mode, boolean afficherDetails) 
    {
        // Réinitialisation des compteurs au cas où le match serait relancé.
        scoreSetsJoueur1 = 0;
        scoreSetsJoueur2 = 0;
        vainqueur = null;
        perdant = null;

        if (afficherDetails || mode == ModeJeu.MANUEL)
        {
            System.out.println("Début du match (" + niveauTournoi + ") entre " + joueur1.getPrenom() + " et " + joueur2.getPrenom());
        }

        Random random = new Random();
        Joueur serveurActuel = random.nextBoolean() ? joueur1 : joueur2;

        if (afficherDetails || mode == ModeJeu.MANUEL)
        {
            System.out.println(serveurActuel.getPrenom() + " servira en premier.");
        }

        while (vainqueur == null) 
        {
            boolean estSetPotentiellementDecisif = (scoreSetsJoueur1 == nombreSetsGagnants - 1)
                    && (scoreSetsJoueur2 == nombreSetsGagnants - 1);
            boolean autoriseTieBreakDansCeSet = !estSetPotentiellementDecisif || tieBreakDernierSetAutorise;

            Set set = new Set(joueur1, joueur2, serveurActuel, arbitre, autoriseTieBreakDansCeSet,
                    statistiquesParJoueur.get(joueur1), statistiquesParJoueur.get(joueur2));
            set.jouerSet(mode, afficherDetails);
            Joueur gagnantDuSet = set.getVainqueur();

            if (gagnantDuSet == joueur1) 
            {
                scoreSetsJoueur1++;
            } 
            else 
            {
                scoreSetsJoueur2++;
            }

            serveurActuel = (serveurActuel == joueur1) ? joueur2 : joueur1;

            if (afficherDetails || mode == ModeJeu.MANUEL)
            {
                arbitre.annoncerFinSet(gagnantDuSet.getPrenom(), scoreSetsJoueur1, scoreSetsJoueur2);
            }

            verifierVainqueur();
        }

        arbitre.annoncerFinMatch(vainqueur.getPrenom());
        vainqueur.crierVictoire();
        if (perdant != null)
        {
            perdant.crierDefaite();
        }
    }

    private void verifierVainqueur() 
    {
        if (scoreSetsJoueur1 == nombreSetsGagnants) 
        {
            this.vainqueur = joueur1;
            this.perdant = joueur2;
        } 
        else if (scoreSetsJoueur2 == nombreSetsGagnants) 
        {
            this.vainqueur = joueur2;
            this.perdant = joueur1;
        }
    }

    public Joueur getJoueur1()
    {
        return joueur1;
    }

    public Joueur getJoueur2()
    {
        return joueur2;
    }

    public Joueur getPerdant()
    {
        return perdant;
    }

    /**
     * @return catégorie de match (simple hommes / femmes)
     */
    public CategorieMatch getCategorie()
    {
        return categorie;
    }

    public Joueur getVainqueur() 
    {
        return vainqueur;
    }
    
    /**
     * @return représentation textuelle du score final si le match est terminé
     */
    public String getScoreFinal()
    {
        if(vainqueur == null)
        {
            return "Match non terminé.";
        }
        return "Score: " + scoreSetsJoueur1 + " - " + scoreSetsJoueur2;
    }

    public List<Spectateur> getSpectateurs()
    {
        return spectateurs;
    }

    /**
     * @param joueur joueur concerné
     * @return statistiques du match pour le joueur
     */
    public StatistiquesMatch getStatistiques(Joueur joueur)
    {
        return statistiquesParJoueur.get(joueur);
    }

    /**
     * @return carte immuable des statistiques de match par joueur
     */
    public Map<Joueur, StatistiquesMatch> getStatistiquesParJoueur()
    {
        return Collections.unmodifiableMap(statistiquesParJoueur);
    }

    @Override
    public String toString() 
    {
        return categorie + " - " + joueur1.getPrenom() + " vs " + joueur2.getPrenom() + " (" + niveauTournoi + ")";
    }
}

