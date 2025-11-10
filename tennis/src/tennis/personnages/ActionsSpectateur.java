package tennis.personnages;

// Contrat décrivant les actions à disposition d’un spectateur.
public interface ActionsSpectateur 
{
    void applaudir();
    void crier();
    void huer();
    default void dormir() {
        // Implémentation par défaut au cas où une classe ne souhaite pas la surcharger.
    }
}

