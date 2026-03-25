# 💊 MyPharma: Pharmacy Management System

**MyPharma** is a comprehensive desktop application engineered to modernize and optimize daily pharmacy operations. By bridging a secure, structured relational database with a highly intuitive, custom-built interface, MyPharma provides pharmacy staff with the tools they need to manage their workflow efficiently.

## 📸 UI / UX Design
Great software starts with great design. The interface was meticulously planned and prototyped using Figma to ensure a seamless user experience. 
🎨 **[Explore the Full Figma Prototype Here](https://www.figma.com/design/0ybGjAhEVIpZY5rC00Bcn2/Gestion-Pharmacie?node-id=165-7&t=GttBvh3ozzo8F6kH-1)**

---

## ✨ Key Features

* **🗃️ Smart Inventory Control:** Effortlessly monitor medication stock, track critical product details, and prevent shortages.
* **🛒 Seamless Point of Sale (POS):** Process customer transactions swiftly and accurately for an improved checkout experience.
* **📦 Supply Chain Management:** Keep detailed logs of supplier orders, command tracking, and incoming stock deliveries.
* **👤 Secure Access Control:** Protect sensitive operational data with role-based user authentication for different pharmacy staff members.
* **🎨 Modern User Experience:** Navigate a beautifully crafted, responsive interface built completely from scratch using JavaFX and CSS.

---

## 🛠️ Technology Stack

| Architecture | Technology |
| :--- | :--- |
| **Core Language** | Java |
| **GUI Framework** | JavaFX |
| **Database Management** | MySQL |
| **Dependency Manager**| Maven |
| **Interface Design** | Figma |
| **Styling** | CSS |

---

## 🚀 Getting Started

Follow these instructions to configure the database and run the MyPharma application on your local machine.

### Prerequisites
* **Java JDK 11** (or higher)
* **MySQL Server**
* **Maven**

### Installation Guide

**1. Clone the repository**
```bash
git clone [https://github.com/moetez00/MyPharma.git](https://github.com/moetez00/MyPharma.git)
cd MyPharma

2. Initialize the Database
To set up the backend, execute the provided SQL scripts in your MySQL client in this exact sequence:
Bash

# 1. Generate the main database schema and tables
mysql -u root -p < pharmacy.sql

# 2. Populate the required user accounts and roles
mysql -u root -p < mysqlaccounts.sql

3. Resolve Dependencies
Ensure all required Maven libraries in the pom.xml are securely downloaded and synchronized:
Bash

mvn clean install

4. Launch the Application
You can run the app directly through your IDE by executing the main launcher:

    src/main/java/brainstorm/pharmacy_app/Main.java

Alternatively, start it from your terminal using the Maven JavaFX plugin:
Bash

mvn javafx:run

📁 Repository Structure
Plaintext

MyPharma/
├── src/
│   └── main/
│       └── java/
│           └── brainstorm/pharmacy_app/  # Core application source code
├── pharmacy.sql                          # Database schema generation script
├── mysqlaccounts.sql                     # Database user role setup script
├── pom.xml                               # Maven project configuration
└── README.md
