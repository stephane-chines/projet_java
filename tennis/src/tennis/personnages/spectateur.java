/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tennis.personnages;

/**
 *
 * @author Int3g
 */
public class spectateur extends personne {
    private String tribune;
    private int place;


    public spectateur(String nom, String prenom, char genre, String tribune, int place) {
        super(nom, prenom, genre);
        this.tribune = tribune;
        this.place = place;
    }


    public void applaudir(){ System.out.println(toString() + " applaudit"); }
    public void crier(){ System.out.println(toString() + " crie"); }
}
