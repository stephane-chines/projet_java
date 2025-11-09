/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tennis.personnages;

/**
 *
 * @author Int3g
 */
public class arbitre extends personne {
    public arbitre(String nom, String prenom, char genre) {
        super(nom, prenom, genre);
    }


    public void annoncerScore(String score) {
        System.out.println("Arbitre annonce : " + score);
    }


    public boolean prendreDecision(String motif, double reputation) {
        double random = Math.random();
        return random + reputation > 0.6;
    }
}