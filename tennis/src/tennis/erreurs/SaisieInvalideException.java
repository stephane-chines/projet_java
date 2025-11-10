package tennis.erreurs;

// Petite exception pour signaler un choix de menu incohérent.
public class SaisieInvalideException extends Exception 
{
    public SaisieInvalideException(String message) 
    {
        super(message);
    }
}

