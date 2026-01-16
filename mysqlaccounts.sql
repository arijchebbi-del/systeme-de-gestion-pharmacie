# Users
CREATE USER 'employee'@'localhost' IDENTIFIED BY 'employee123';
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin123';

# Admin Privileges
GRANT ALL PRIVILEGES ON pharmacy_db.* TO 'admin'@'localhost';

# Employee Privileges
GRANT INSERT, UPDATE, SELECT ON pharmacy_db.Stock TO 'employee'@'localhost';
GRANT INSERT, UPDATE, SELECT ON pharmacy_db.Composer TO 'employee'@'localhost';
GRANT INSERT, UPDATE, SELECT ON pharmacy_db.Gerer TO 'employee'@'localhost';
GRANT INSERT, UPDATE, SELECT ON pharmacy_db.Constituer TO 'employee'@'localhost';
GRANT INSERT, UPDATE, SELECT ON pharmacy_db.Vente TO 'employee'@'localhost';
GRANT INSERT, UPDATE, SELECT ON pharmacy_db.Commande TO 'employee'@'localhost';
GRANT SELECT ON pharmacy_db.Produit TO 'employee'@'localhost';
GRANT SELECT ON pharmacy_db.Fournisseur TO 'employee'@'localhost';
GRANT SELECT ON pharmacy_db.Employe TO 'employee'@'localhost';


# Apply Changes
FLUSH PRIVILEGES;
