package brainstorm.pharmacy_app.Model;

public class Employe {
    private int idEmploye;
    private String nom,prenom;
    private int numTel;
    private String email;
    private String motDePasse;
    private String role;
    private String horaire;


    //getter
    public String getNom (){return nom;}
    public String getPrenom (){return prenom;}
    public int getNumTelephoneEmploye (){return numTel;}
    public String getEmail (){return email;}
    public String getMotdePasse() {return motDePasse;}
    public String getRole (){return role;}
    public String getHoraire (){return horaire;}
    public int getIdEmploye(){return idEmploye;}

    //setters
    public void setNom (String nom){this.nom=nom;}
    public void setPrenom (String prenom){prenom=prenom;}
    public void setNumTelephone (int num){numTel=num;}
    public void setEmail (String mail){email=mail;}
    public void setMotdepasse(String mdp) {motDePasse=mdp;}
    public void setRole(String role){role=role;}
    public void setHoraire (String horaire ){this.horaire=horaire;}}