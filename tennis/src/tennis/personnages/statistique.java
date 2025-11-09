/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tennis.personnages;

/**
 *
 * @author Int3g
 */
public class statistique {
    private int setsGagnes = 0;
    private int jeuxGagnes = 0;
    private int pointsGagnes = 0;
    private int matchsGagnes = 0;
    private int matchsJoues = 0;


    public void addSetWon(){ setsGagnes++; }
    public void addJeuWon(){ jeuxGagnes++; }
    public void addPointWon(){ pointsGagnes++; }
    public void addMatchWon(){ matchsGagnes++; }
    public void addMatchPlayed(){ matchsJoues++; }


    @Override
    public String toString(){
        return "Matches: " + matchsJoues + " | Won: " + matchsGagnes + " | Sets: " + setsGagnes + " | Jeux: " + jeuxGagnes + " | Points: " + pointsGagnes;
    }
}
