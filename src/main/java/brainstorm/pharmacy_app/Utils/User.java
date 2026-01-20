package brainstorm.pharmacy_app.Utils;

import brainstorm.pharmacy_app.Model.Employe;

public class User{
    private static User instance;
    private Employe loggedUser;

    private User(Employe user) {
        this.loggedUser = user;
    }

    // ll jot3a illi naaytoulha fll login
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

    public void cleanUser() {
        instance = null;
    }
}