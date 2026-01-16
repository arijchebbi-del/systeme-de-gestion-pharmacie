CREATE database pharmacy_db;
use pharmacy_db;


CREATE TABLE Employe(
	IdEmploye INT PRIMARY KEY,
	MotDePasse VARCHAR(30),
	Role VARCHAR(15),
	HoraireDeTravail VARCHAR(20),
	NumTel INT,
	Prenom VARCHAR(30),
	Nom VARCHAR(30),
	Email VARCHAR(30),
);
