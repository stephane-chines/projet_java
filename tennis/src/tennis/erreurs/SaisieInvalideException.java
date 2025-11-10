package tennis.erreurs;

/**
 * Exception personnalisée levée lorsqu'une saisie est valide sur la forme
 * mais ne correspond pas à un choix logique dans l'application.
 */
public class SaisieInvalideException extends Exception 
{
    public SaisieInvalideException(String message) 
    {
        super(message);
    }
}

