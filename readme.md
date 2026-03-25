🏥 My Pharma: Pharmacy Management App

Welcome to the My Pharma repository! This application is designed to streamline pharmacy management, offering a user-friendly interface and robust database integration.
🎨 UI / UX Design

The interface was carefully planned and designed using Figma. You can view the complete wireframes and high-fidelity prototypes here:
👉 View My Pharma Design on Figma
⚙️ Prerequisites

Before you begin, ensure you have the following installed:

    Java Development Kit (JDK) (Specify version, e.g., JDK 11 or 17)

    Maven for dependency management

    MySQL Server for the database

🚀 Getting Started

Follow these instructions to set up and run the project on your local machine.
1. Database Setup

You will need to initialize the local database using the provided SQL scripts. Open your MySQL client and execute the following scripts in order:

    Run pharmacy.sql to create the schema and tables.

    Run mysqlaccounts.sql to set up the necessary user credentials and permissions.

2. Install Dependencies

    ⚠️ Important: Please ensure that all Maven dependencies listed in the pom.xml file are fully downloaded and synchronized before attempting to run the application.

If you are using a terminal, navigate to the project root and run:
Bash

mvn clean install

(If you are using an IDE like IntelliJ or Eclipse, simply click "Reload All Maven Projects" to sync dependencies).
3. Run the Application

Once the database is running and dependencies are downloaded, you can start the application by executing the main launcher class:

    Target Class: brainstorm.pharmacy_app.Main

    File Path: src/main/java/brainstorm/pharmacy_app/Main.java
