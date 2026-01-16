# Pharmacy App

This project follows a layered architecture to keep responsibilities clean and separated. Below is an overview of the main packages.

---

## `Model`
Contains the core data classes of the application.

- Represents entities such as `Produit`, `Fournisseur`, `Client`, etc.
- Contains fields, constructors, getters/setters, and simple validation.
- **No database or business logic should exist here.**

---

## `DAO`
DAO stands for *Data Access Object*.

- Responsible for accessing and interacting with the database.
- Typical operations: `SELECT`, `INSERT`, `UPDATE`, `DELETE`.
- Converts database rows into model objects.
- **Should not contain business logic or UI code.**

---

## `Service`
The service layer contains the business logic of the application.

- Applies business rules (e.g., validate stock quantities).
- Coordinates operations across DAOs.
- Transforms or combines model data.
- **Should call DAO methods but not interact directly with the database.**

---

## `Utils`
Utility classes containing small reusable helper functions.

- Examples: formatting helpers, string/date conversions, shared static methods.
- **Should not contain business logic or database access.**

---

## `Main`
Application entry point.

- Starts the program.
- Creates required objects/services.
- Invokes the initial program flow.
- **Should not contain core logic, data access, or business rules.**

---

Following these responsibilities keeps the project modular, maintainable, and ready for scaling.

