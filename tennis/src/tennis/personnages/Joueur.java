/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tennis.personnages;

/**
 *
 * @author Int3g
 */
public class Joueur extends personne {
    public static final int DROITIER = 0;
    public static final int GAUCHER = 1;


    private int main;
    private String sponsor;
    private int classement;
    


    public Joueur(String nom, String prenom, char genre, String lieuNaissance, String nationalite, int main) {
        super(nom, prenom, genre);
        this.main = main;
        setNationalite(nationalite);
    }


    public int getMain() { return main; }
    public void setMain(int main) { this.main = main; }
    public String getSponsor() { return sponsor; }
    public void setSponsor(String sponsor) { this.sponsor = sponsor; }
    public int getClassement() { return classement; }
    public void setClassement(int classement) { this.classement = classement; }
    


    public void encourager() { System.out.println(this + " s'encourage !"); }
    public void crierVictoire() { System.out.println(this + " crie victoire !"); }
    public void crierDefaite() { System.out.println(this + " crie défaite..."); }
}
