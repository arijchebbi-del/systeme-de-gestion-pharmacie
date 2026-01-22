package brainstorm.pharmacy_app.Model;

public class Employe {
    private int idEmploye;
    private String nom,prenom;
    private int numTel;
    private String email;
    private String motDePasse;
    private String role;
    private String horaire;
    private String username;
    private double salaire;


    //getter
    public int getIdEmploye(){return idEmploye;}
    public String getNom (){return nom;}
    public String getPrenom (){return prenom;}
    public int getNumTelephoneEmploye (){return numTel;}
    public String getEmail (){return email;}
    public String getMotdePasse() {return motDePasse;}
    public String getRole (){return role;}
    public String getHoraire (){return horaire;}
    public String getUsername(){return username;}
    public double getSalaire(){return salaire;}

    //setters

    public void setIdEmploye(int idEmploye) {this.idEmploye = idEmploye;}
    public void setNom (String nom){this.nom=nom;}
    public void setPrenom (String prenom){this.prenom=prenom;}
    public void setNumTelephone (int num){numTel=num;}
    public void setEmail (String mail){email=mail;}
    public void setMotdepasse(String mdp) {motDePasse=mdp;}
    public void setRole(String role){this.role=role;}
    public void setHoraire (String horaire ){this.horaire=horaire;}
    public void setIdemploye(int idEmploye) {this.idEmploye=idEmploye;}
    public void setUsername (String username ){this.username=username;}
    public void setSalaire (double s){this.salaire=s;}

}