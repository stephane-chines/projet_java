package tennis.jeu;

import tennis.personnages.Arbitre;
import tennis.personnages.Joueur;

/**
 * Représente un set complet de tennis, incluant la gestion d'un éventuel jeu décisif.
 */
public class Set 
{
    private Joueur joueur1;
    private Joueur joueur2;
    private Arbitre arbitre;
    
    private int scoreJeuxJoueur1 = 0;
    private int scoreJeuxJoueur2 = 0;
    
    private Joueur vainqueur;
    private Joueur serveurActuel;
    private final boolean tieBreakAutorise;
    private final StatistiquesMatch statsJoueur1;
    private final StatistiquesMatch statsJoueur2;

    public Set(Joueur joueur1, Joueur joueur2, Joueur premierServeur, Arbitre arbitre, boolean tieBreakAutorise,
            StatistiquesMatch statsJoueur1, StatistiquesMatch statsJoueur2) 
    {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.arbitre = arbitre;
        this.serveurActuel = premierServeur;
        this.tieBreakAutorise = tieBreakAutorise;
        this.statsJoueur1 = statsJoueur1;
        this.statsJoueur2 = statsJoueur2;
    }

    /**
     * Lance le set jusqu'à ce qu'un joueur remplisse les conditions de victoire.
     */
    public void jouerSet(ModeJeu mode, boolean afficherDetails) 
    {
        while (vainqueur == null) 
        {
            if (tieBreakAutorise && scoreJeuxJoueur1 == 6 && scoreJeuxJoueur2 == 6) 
            {
                JeuDecisif tieBreak = new JeuDecisif(joueur1, joueur2, serveurActuel, arbitre, statsJoueur1, statsJoueur2);
                tieBreak.jouer(mode, afficherDetails);
                vainqueur = tieBreak.getVainqueur();
                if (vainqueur == joueur1) 
                {
                    scoreJeuxJoueur1 = 7;
                } 
                else 
                {
                    scoreJeuxJoueur2 = 7;
                }
                if (mode == ModeJeu.MANUEL || afficherDetails)
                {
                    arbitre.annoncerFinJeu("Tie-break", scoreJeuxJoueur1, scoreJeuxJoueur2);
                }
                break;
            }

            Jeu jeu = new Jeu(joueur1, joueur2, serveurActuel, arbitre, statsJoueur1, statsJoueur2);
            jeu.jouerJeu(mode, afficherDetails);

            Joueur gagnantDuJeu = jeu.getVainqueur();
            if (gagnantDuJeu == joueur1) 
            {
                scoreJeuxJoueur1++;
            } 
            else 
            {
                scoreJeuxJoueur2++;
            }

            if (mode == ModeJeu.MANUEL || afficherDetails)
            {
                arbitre.annoncerFinJeu(gagnantDuJeu.getPrenom(), scoreJeuxJoueur1, scoreJeuxJoueur2);
            }

            verifierVainqueur();
            changerServeur();
        }
    }

    private void verifierVainqueur() 
    {
        if (tieBreakAutorise && scoreJeuxJoueur1 == 7 && scoreJeuxJoueur2 == 6) 
        {
            vainqueur = joueur1;
        } 
        else if (tieBreakAutorise && scoreJeuxJoueur2 == 7 && scoreJeuxJoueur1 == 6) 
        {
            vainqueur = joueur2;
        }
        else if (scoreJeuxJoueur1 >= 6 && scoreJeuxJoueur1 >= scoreJeuxJoueur2 + 2) 
        {
            vainqueur = joueur1;
        } 
        else if (scoreJeuxJoueur2 >= 6 && scoreJeuxJoueur2 >= scoreJeuxJoueur1 + 2) 
        {
            vainqueur = joueur2;
        }
    }

    private void changerServeur() 
    {
        serveurActuel = (serveurActuel == joueur1) ? joueur2 : joueur1;
    }

    /**
     * @return le vainqueur du set une fois terminé
     */
    public Joueur getVainqueur() 
    {
        return vainqueur;
    }

    public int getScoreJeuxJoueur1()
    {
        return scoreJeuxJoueur1;
    }

    public int getScoreJeuxJoueur2()
    {
        return scoreJeuxJoueur2;
    }
}

