CREATE database pharmacy_db;
use pharmacy_db;


CREATE TABLE Employe(
	IdEmploye INT PRIMARY KEY auto_increment,
    Username VARCHAR(30),
	MotDePasse VARCHAR(30),
	Role VARCHAR(15),
	HoraireDeTravail VARCHAR(20),
	NumTel INT,
	Prenom VARCHAR(30),
	Nom VARCHAR(30),
	Email VARCHAR(60)
);
CREATE TABLE Fournisseur(
	IdFournisseur INT PRIMARY KEY auto_increment,
	Nom VARCHAR(30),
	NumTel VARCHAR(30),
	Email VARCHAR(60),
	Adresse VARCHAR(50),
	TypeProduit VARCHAR(20)
);
CREATE TABLE Produit(
	Reference INT PRIMARY KEY auto_increment,
	NomProduit VARCHAR(50),
	Categorie VARCHAR(20),
	Ordonnance BOOLEAN,
	Type VARCHAR(20),
	ModeUtilisation VARCHAR(20),
    SeuilMinimal INT,
    PrixAchat DECIMAL(10,2),
	PrixVente DECIMAL(10,2),
	CONSTRAINT CHK_Produit
	CHECK ( PrixAchat>0 AND PrixVente>0 AND SeuilMinimal>0 )
);
CREATE TABLE Commande(
	IdCommande INT PRIMARY KEY auto_increment,
	PrixTotal DECIMAL(10,2),
	DateCommande DATETIME,
	DateArrivee DATETIME,
	IdEmploye INT,
	IdFournisseur INT,
	Etat VARCHAR(10),
	FOREIGN KEY (IdEmploye) REFERENCES Employe(IdEmploye),
	FOREIGN KEY (IdFournisseur) REFERENCES Fournisseur(IdFournisseur),
	CONSTRAINT CHK_Commande 
	CHECK (Etat in ('cree','modifie','annulee','recue') AND PrixTotal>=0)
);
CREATE TABLE Stock(
	NumLot INT PRIMARY KEY auto_increment,
	DerniereMiseAJour DATETIME,
	Quantite INT,
	Reference INT,
	FOREIGN KEY (Reference) REFERENCES Produit(Reference),
	CONSTRAINT CHK_Stock
	CHECK (Quantite>=0 )
);
CREATE TABLE Vente(
	NumFacture INT PRIMARY KEY auto_increment,
	DateVente DATETIME,
	IdEmploye INT,
    PresenceOrd BOOLEAN,
    PrixTotal DECIMAL(10,2),
	FOREIGN KEY (IdEmploye) REFERENCES Employe(IdEmploye)
);
CREATE TABLE Composer(
	Reference INT,
	IdCommande INT,
	Quantite INT,
	CONSTRAINT CHK_Composer
	CHECK (Quantite>0),
	PRIMARY KEY(Reference,IdCommande),
	FOREIGN KEY (Reference) REFERENCES Produit(Reference),
	FOREIGN KEY (IdCommande) REFERENCES Commande(IdCommande)
);
CREATE TABLE Constituer (
    NumFacture INT,
    Reference INT,
    QuantiteVendu INT NOT NULL,
    PrixVente FLOAT NOT NULL,
    PRIMARY KEY (NumFacture, Reference),
    CONSTRAINT FK_Vente FOREIGN KEY (NumFacture)
    REFERENCES Vente(NumFacture)
    ON DELETE CASCADE,

    CONSTRAINT FK_Produit FOREIGN KEY (Reference)
    REFERENCES Produit(Reference)
    ON UPDATE CASCADE
);
CREATE TABLE Gerer(
	IdEmploye INT,
	NumLot INT,
	FOREIGN KEY (IdEmploye) REFERENCES Employe(IdEmploye),
	FOREIGN KEY (NumLot) REFERENCES Stock(NumLot),
	PRIMARY KEY(IdEmploye,NumLot)
);

INSERT INTO Employe
(Username, MotDePasse, Role, HoraireDeTravail, NumTel, Prenom, Nom, Email)
VALUES
    ('taz', 'taz123', 'admin', '09:00-17:00', 223456781, 'Zouari', 'Moetaz', 'zouaritaz@pharma.tn'),
    ('rkh', 'rkh123', 'employee', '14:00-22:00', 221334451, 'Khammar', 'Rayen', 'rayenkhammar@mypharma.tn'),
    ('ons', 'ons456', 'employee', '8:00-16:00', 525678901, 'Sassi', 'Ons', 'onssassi@pharma.tn'),
    ('arij', 'arij456', 'employee', '8:00-16:00', 924668201, 'Chebbi', 'Arij', 'arijchebbi@pharma.tn'),
    ('joseph', 'joseph123', 'employee', '8:00-16:00', 424668201, 'Akermi', 'Yousseef', 'akermiyoussef@pharma.tn');



