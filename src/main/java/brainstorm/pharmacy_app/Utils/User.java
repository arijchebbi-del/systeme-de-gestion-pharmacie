package brainstorm.pharmacy_app.Utils;

import brainstorm.pharmacy_app.Model.Employe;

public class User{
    private static User instance;
    private static Employe loggedUser;

    private User(Employe user) {
        loggedUser = user;
    }

    // on appelle cette methode dans le login pour sauv l user
    public static void getInstace(Employe user) {
        if (instance == null) {
            instance = new User(user);
        }
    }

    public static User getInstance() {
        return instance;
    }

    public Employe getUser() {
        return loggedUser;
    }

    public static void cleanUser() {
        instance = null;
        loggedUser = null;
    }
}