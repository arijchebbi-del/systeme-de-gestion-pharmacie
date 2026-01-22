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
	Email VARCHAR(60),
    Salaire DECIMAL(10,2)
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
CREATE TABLE Stock (
                       NumLot INT PRIMARY KEY AUTO_INCREMENT,
                       DerniereMiseAJour DATETIME,
                       Quantite INT,
                       Reference INT,
                       FOREIGN KEY (Reference) REFERENCES Produit(Reference),
                       CONSTRAINT CHK_StockCHECK CHECK (Quantite >= 0)
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
(Username, MotDePasse, Role, HoraireDeTravail, NumTel, Prenom, Nom, Email,Salaire)
VALUES
    ('taz', 'taz123', 'admin', '09:00-17:00', 223456781, 'Moetez', 'Zouari', 'zouaritaz@pharma.tn',2000.00),
    ('rkh', 'rkh123', 'employee', '14:00-22:00', 221334451, 'Rayen', 'Khammar', 'rayenkhammar@mypharma.tn',1500.00),
    ('ons', 'ons456', 'admin', '8:00-16:00', 525678901, 'Ons', 'Sassi', 'onssassi@pharma.tn',1500.00),
    ('arij', 'arij456', 'employee', '8:00-16:00', 924668201, 'Arij', 'Chebbi', 'arijchebbi@pharma.tn',1500.00),
    ('joseph', 'joseph123', 'employee', '8:00-16:00', 424668201, 'Youssef', 'Akermi', 'akermiyoussef@pharma.tn',1500.00);

INSERT INTO Fournisseur
(Nom, NumTel, Email, Adresse, TypeProduit)
VALUES
    ('AntibioPharm', '71200111', 'contact@antibiopharm.tn', 'Tunis', 'Antibiotiques'),
    ('CardioMed', '73400222', 'sales@cardiomed.tn', 'Sousse', 'Cardiologie'),
    ('NeuroCare', '71600333', 'info@neurocare.tn', 'Sfax', 'Neurologie'),
    ('DiabetPlus', '73500444', 'support@diabetplus.tn', 'Gabes', 'Diabetologie'),
    ('PediaPharma', '71800555', 'contact@pediapharma.tn', 'Bizerte', 'Pediatrie');

INSERT INTO Produit
(NomProduit, Categorie, Ordonnance, Type, ModeUtilisation, SeuilMinimal, PrixAchat, PrixVente)
VALUES
    ('Paracetamol 500mg', 'Antalgique', FALSE, 'Comprime', 'Orale', 50, 0.80, 1.50),
    ('Ibuprofene 400mg', 'Antalgique', FALSE, 'Comprime', 'Orale', 40, 0.90, 1.70),
    ('Amoxicilline 1g', 'Antibiotique', TRUE, 'Comprime', 'Orale', 30, 2.50, 4.00),
    ('Ceftriaxone 500mg', 'Antibiotique', TRUE, 'Injection', 'Intramusculaire', 20, 5.00, 8.50),
    ('Atenolol 50mg', 'Cardiologie', TRUE, 'Comprime', 'Orale', 25, 1.20, 2.50),
    ('Simvastatine 20mg', 'Cardiologie', TRUE, 'Comprime', 'Orale', 30, 1.10, 2.30),
    ('Insuline NPH', 'Diabetologie', TRUE, 'Injection', 'Sous-cutanée', 15, 15.00, 25.00),
    ('Metformine 500mg', 'Diabetologie', TRUE, 'Comprime', 'Orale', 40, 1.00, 1.80),
    ('Diazepam 5mg', 'Neurologie', TRUE, 'Comprime', 'Orale', 20, 1.50, 3.00),
    ('Paroxetine 20mg', 'Neurologie', TRUE, 'Comprime', 'Orale', 15, 2.00, 3.50),
    ('Vitamine C 1000mg', 'Complement', FALSE, 'Comprime', 'Orale', 60, 1.00, 2.00),
    ('Fer 80mg', 'Complement', FALSE, 'Comprime', 'Orale', 50, 0.90, 1.70),
    ('Sirop Toux Enfant', 'Pediatrie', FALSE, 'Sirop', 'Orale', 30, 3.00, 5.50),
    ('Doliprane 500mg', 'Pain Relief', FALSE, 'Comprime', 'Orale', 60, 0.85, 1.60),
    ('Doliprane 1000mg', 'Pain Relief', FALSE, 'Comprime', 'Orale', 40, 1.50, 2.80),
    ('Doliprane Sirop Enfant 120mg/5ml', 'Pain Relief', FALSE, 'Sirop', 'Orale', 30, 2.50, 4.50);



INSERT INTO Stock
(DerniereMiseAJour, Quantite, Reference)
VALUES
    ('2026-01-15 10:00:00', 120, 1),
    ('2026-01-15 10:00:00', 90, 2),
    ('2026-01-12 09:00:00', 60, 3),
    ('2026-01-12 09:00:00', 40, 4),
    ('2026-01-18 14:00:00', 70, 5),
    ('2026-01-18 14:00:00', 55, 6),
    ('2026-01-10 08:30:00', 20, 7),
    ('2026-01-10 08:30:00', 100, 8),
    ('2026-01-20 11:00:00', 35, 9),
    ('2026-01-20 11:00:00', 25, 10),
    ('2026-01-22 13:00:00', 150, 11),
    ('2026-01-22 13:00:00', 80, 12),
    ('2026-01-22 15:00:00', 50, 13),
    ('2026-01-22 15:00:00', 110, 14),
    ('2026-01-22 15:00:00', 70, 15),
    ('2026-01-22 15:00:00', 40, 16);


INSERT INTO Commande
(PrixTotal, DateCommande, DateArrivee, IdEmploye, IdFournisseur, Etat)
VALUES
    (150.00, '2025-12-10 10:00:00', '2025-12-12 14:00:00', 1, 1, 'recue'),
    (200.00, '2025-12-15 09:30:00', '2025-12-17 11:00:00', 2, 2, 'recue'),
    (300.00, '2025-12-20 11:00:00', '2025-12-22 13:00:00', 1, 3, 'recue'),
    (120.00, '2025-12-22 08:45:00', NULL, 3, 4, 'modifie'),
    (450.00, '2025-12-25 15:20:00', '2025-12-27 17:00:00', 2, 5, 'recue'),
    (80.00, '2025-12-28 10:30:00', '2025-12-30 14:00:00', 1, 5, 'recue'),
    (500.00, '2026-01-01 14:00:00', '2026-01-03 09:00:00', 3, 1, 'recue'),
    (250.00, '2026-01-02 16:45:00', NULL, 2, 3, 'annulee');

INSERT INTO Composer (Reference, IdCommande, Quantite) VALUES
                                                           (3, 1, 100),
                                                           (4, 1, 50),

                                                           (5, 2, 40),
                                                           (6, 2, 30),

                                                           (9, 3, 25),
                                                           (10, 3, 20),

                                                           (7, 4, 15),
                                                           (8, 4, 40),

                                                           (13, 5, 30),

                                                           (13, 6, 20),

                                                           (3, 7, 80),
                                                           (4, 7, 70);


INSERT INTO Vente
(DateVente, IdEmploye, PresenceOrd, PrixTotal)
VALUES
    ('2025-12-12 12:00:00', 1, TRUE, 45.00),
    ('2025-12-16 15:30:00', 2, FALSE, 30.00),
    ('2025-12-21 10:00:00', 3, TRUE, 60.00),
    ('2026-01-03 11:15:00', 1, FALSE, 25.00),
    ('2026-01-05 16:45:00', 2, TRUE, 90.00);


INSERT INTO Constituer (NumFacture, Reference, QuantiteVendu, PrixVente) VALUES
                                                                             (1, 1, 10, 1.50),
                                                                             (1, 2, 5, 1.70),

                                                                             (2, 14, 15, 1.60),

                                                                             (3, 3, 20, 4.00),
                                                                             (3, 4, 10, 8.50),

                                                                             (4, 11, 12, 2.00),

                                                                             (5, 5, 25, 2.50),
                                                                             (5, 6, 10, 2.30);


INSERT INTO Gerer (IdEmploye, NumLot) VALUES
                                          (1, 1),
                                          (2, 2),
                                          (3, 3),
                                          (1, 4),
                                          (2, 5),
                                          (3, 6),
                                          (1, 7),
                                          (2, 8);