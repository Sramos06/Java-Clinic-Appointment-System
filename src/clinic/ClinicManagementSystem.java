package clinic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clinic Management System - Main Class
 *
 * OOP Concepts Applied:
 * - Classes and Objects  : Patient, Doctor, Appointment objects
 * - Encapsulation        : Private attributes with getters/setters
 * - Inheritance          : Patient and Doctor extend Person
 * - Polymorphism         : Overridden displayInfo() in child classes
 * - Abstraction          : Person is an abstract class
 * - Exception Handling   : try-catch blocks throughout
 * - Collections          : ArrayList for storing objects
 */
public class ClinicManagementSystem {

    private static Scanner scanner       = new Scanner(System.in);
    private static String  loggedInUser  = "";
    private static String  userRole      = "";

    // =========================================================
    // MAIN
    // =========================================================
    public static void main(String[] args) {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   CLINIC APPOINTMENT MANAGEMENT SYSTEM    ║");
        System.out.println("╚═══════════════════════════════════════════╝");

        if (login()) {
            mainMenu();
        } else {
            System.out.println("\nToo many failed attempts. Exiting...");
        }

        scanner.close();
    }

    // =========================================================
    // LOGIN
    // =========================================================
    private static boolean login() {
        System.out.println("\n=== LOGIN ===");
        int attempts = 0;

        while (attempts < 3) {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                conn  = DatabaseConnection.getConnection();
                if (conn == null) {
                    System.out.println("\nCannot connect to database. Check DatabaseConnection.java settings.");
                    return false;
                }
                String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    loggedInUser = rs.getString("username");
                    userRole     = rs.getString("role");
                    System.out.println("\nLogin successful! Welcome, " + loggedInUser + " (" + userRole + ")");
                    return true;
                } else {
                    attempts++;
                    System.out.println("\nInvalid username or password! Attempts remaining: " + (3 - attempts));
                }
            } catch (SQLException e) {
                System.out.println("Database error during login: " + e.getMessage());
                return false;
            } finally {
                DatabaseConnection.closeResultSet(rs);
                DatabaseConnection.closeStatement(pstmt);
                DatabaseConnection.closeConnection(conn);
            }
        }
        return false;
    }

    // =========================================================
    // MAIN MENU
    // =========================================================
    private static void mainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔═══════════════════════════════════════════╗");
            System.out.println("║               MAIN MENU                   ║");
            System.out.println("╠═══════════════════════════════════════════╣");
            System.out.println("║  1. Patient Management                    ║");
            System.out.println("║  2. Doctor Management                     ║");
            System.out.println("║  3. Appointment Management                ║");
            System.out.println("║  4. Reports                               ║");
            System.out.println("║  5. Logout                                ║");
            System.out.println("╚═══════════════════════════════════════════╝");
            System.out.print("Enter choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1: patientManagementMenu();     break;
                    case 2: doctorManagementMenu();      break;
                    case 3: appointmentManagementMenu(); break;
                    case 4: reportsMenu();               break;
                    case 5: System.out.println("\nLogging out... Goodbye!"); running = false; break;
                    default: System.out.println("\nInvalid choice! Enter 1-5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input! Please enter a number.");
            }
        }
    }

    // =========================================================
    // PATIENT MANAGEMENT MENU
    // =========================================================
    private static void patientManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n╔═══════════════════════════════════════════╗");
            System.out.println("║          PATIENT MANAGEMENT               ║");
            System.out.println("╠═══════════════════════════════════════════╣");
            System.out.println("║  1. Add New Patient                       ║");
            System.out.println("║  2. View All Patients                     ║");
            System.out.println("║  3. Search Patient                        ║");
            System.out.println("║  4. Update Patient                        ║");
            System.out.println("║  5. Delete Patient                        ║");
            System.out.println("║  6. Back                                  ║");
            System.out.println("╚═══════════════════════════════════════════╝");
            System.out.print("Enter choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1: addPatient();      break;
                    case 2: viewAllPatients(); break;
                    case 3: searchPatient();   break;
                    case 4: updatePatient();   break;
                    case 5: deletePatient();   break;
                    case 6: back = true;       break;
                    default: System.out.println("\nInvalid choice! Enter 1-6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input!");
            }
        }
    }

    // ----- ADD PATIENT -----
    private static void addPatient() {
        System.out.println("\n=== ADD NEW PATIENT ===");
        System.out.print("First Name       : "); String firstName     = scanner.nextLine();
        System.out.print("Last Name        : "); String lastName      = scanner.nextLine();
        System.out.print("Contact Number   : "); String contact       = scanner.nextLine();
        System.out.print("Email            : "); String email         = scanner.nextLine();
        System.out.print("Address          : "); String address       = scanner.nextLine();
        System.out.print("Date of Birth (YYYY-MM-DD): "); String dob  = scanner.nextLine();
        System.out.print("Medical History  : "); String medHistory    = scanner.nextLine();

        if (firstName.isEmpty() || lastName.isEmpty() || contact.isEmpty()) {
            System.out.println("\nFirst name, last name, and contact are required!");
            return;
        }

        Connection conn   = null;
        PreparedStatement pstmt = null;
        try {
            conn  = DatabaseConnection.getConnection();
            String sql = "INSERT INTO patients (first_name, last_name, contact_number, email, address, date_of_birth, medical_history) VALUES (?,?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, contact);
            pstmt.setString(4, email);
            pstmt.setString(5, address);
            pstmt.setString(6, dob.isEmpty() ? null : dob);
            pstmt.setString(7, medHistory);
            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0 ? "\nPatient added successfully!" : "\nFailed to add patient.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } finally {
            DatabaseConnection.closeStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }
    }

    // ----- VIEW ALL PATIENTS -----
    private static void viewAllPatients() {
        System.out.println("\n=== ALL PATIENTS ===");
        Connection conn  = null;
        Statement  stmt  = null;
        ResultSet  rs    = null;
        ArrayList<Patient> list = new ArrayList<>();
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs   = stmt.executeQuery("SELECT * FROM patients ORDER BY patient_id");
            while (rs.next()) {
                list.add(new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("first_name"), rs.getString("last_name"),
                    rs.getString("contact_number"), rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("date_of_birth") == null ? "" : rs.getString("date_of_birth"),
                    rs.getString("medical_history") == null ? "" : rs.getString("medical_history")
                ));
            }
            if (list.isEmpty()) {
                System.out.println("No patients found.");
            } else {
                System.out.println("Total Patients: " + list.size());
                System.out.println("-".repeat(70));
                for (Patient p : list) p.displaySummary();
                System.out.println("-".repeat(70));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving patients: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
            DatabaseConnection.closeConnection(conn);
        }
    }

    // ----- SEARCH PATIENT -----
    private static void searchPatient() {
        System.out.println("\n=== SEARCH PATIENT ===");
        System.out.print("Enter Patient ID or Last Name: ");
        String term = scanner.nextLine().trim();

        Connection conn  = null;
        PreparedStatement pstmt = null;
        ResultSet  rs    = null;
        try {
            conn  = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM patients WHERE patient_id = ? OR last_name LIKE ?";
            pstmt = conn.prepareStatement(sql);
            try { pstmt.setInt(1, Integer.parseInt(term)); }
            catch (NumberFormatException e) { pstmt.setInt(1, 0); }
            pstmt.setString(2, "%" + term + "%");
            rs = pstmt.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("first_name"), rs.getString("last_name"),
                    rs.getString("contact_number"), rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("date_of_birth") == null ? "" : rs.getString("date_of_birth"),
                    rs.getString("medical_history") == null ? "" : rs.getString("medical_history")
                ).displayInfo();
            }
            if (!found) System.out.println("No patient found.");
        } catch (SQLException e) {
            System.out.println("Error searching patient: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(pstmt);
            DatabaseConnection.closeConnection(conn);
        }
    }

    // ----- UPDATE PATIENT -----
    private static void updatePatient() {
        System.out.println("\n=== UPDATE PATIENT ===");
        System.out.print("Enter Patient ID to update: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Connection conn  = null;
            PreparedStatement pstmt = null;
            ResultSet  rs    = null;
            try {
                conn  = DatabaseConnection.getConnection();
                pstmt = conn.prepareStatement("SELECT * FROM patients WHERE patient_id = ?");
                pstmt.setInt(1, id);
                rs = pstmt.executeQuery();
                if (!rs.next()) { System.out.println("Patient not found!"); return; }

                Patient cur = new Patient(
                    rs.getInt("patient_id"), rs.getString("first_name"), rs.getString("last_name"),
                    rs.getString("contact_number"), rs.getString("email"), rs.getString("address"),
                    rs.getString("date_of_birth") == null ? "" : rs.getString("date_of_birth"),
                    rs.getString("medical_history") == null ? "" : rs.getString("medical_history")
                );
                cur.displayInfo();
                rs.close(); pstmt.close();

                System.out.println("Press Enter to keep current value.");
                System.out.print("First Name   [" + cur.getFirstName()      + "]: "); String fn  = scanner.nextLine(); if (fn.isEmpty())  fn  = cur.getFirstName();
                System.out.print("Last Name    [" + cur.getLastName()       + "]: "); String ln  = scanner.nextLine(); if (ln.isEmpty())  ln  = cur.getLastName();
                System.out.print("Contact      [" + cur.getContactNumber()  + "]: "); String cn  = scanner.nextLine(); if (cn.isEmpty())  cn  = cur.getContactNumber();
                System.out.print("Email        [" + cur.getEmail()          + "]: "); String em  = scanner.nextLine(); if (em.isEmpty())  em  = cur.getEmail();
                System.out.print("Address      [" + cur.getAddress()        + "]: "); String ad  = scanner.nextLine(); if (ad.isEmpty())  ad  = cur.getAddress();
                System.out.print("Date of Birth[" + cur.getDateOfBirth()    + "]: "); String dob = scanner.nextLine(); if (dob.isEmpty()) dob = cur.getDateOfBirth();
                System.out.print("Medical Hist.[" + cur.getMedicalHistory() + "]: "); String mh  = scanner.nextLine(); if (mh.isEmpty())  mh  = cur.getMedicalHistory();

                String sql = "UPDATE patients SET first_name=?,last_name=?,contact_number=?,email=?,address=?,date_of_birth=?,medical_history=? WHERE patient_id=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, fn); pstmt.setString(2, ln); pstmt.setString(3, cn);
                pstmt.setString(4, em); pstmt.setString(5, ad); pstmt.setString(6, dob);
                pstmt.setString(7, mh); pstmt.setInt(8, id);
                System.out.println(pstmt.executeUpdate() > 0 ? "\nPatient updated successfully!" : "\nFailed to update.");
            } catch (SQLException e) {
                System.out.println("Error updating patient: " + e.getMessage());
            } finally {
                DatabaseConnection.closeResultSet(rs);
                DatabaseConnection.closeStatement(pstmt);
                DatabaseConnection.closeConnection(conn);
            }
        } catch (NumberFormatException e) { System.out.println("Invalid ID!"); }
    }

    // ----- DELETE PATIENT -----
    private static void deletePatient() {
        System.out.println("\n=== DELETE PATIENT ===");
        System.out.print("Enter Patient ID to delete: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Connection conn  = null;
            PreparedStatement pstmt = null;
            ResultSet  rs    = null;
            try {
                conn  = DatabaseConnection.getConnection();
                pstmt = conn.prepareStatement("SELECT * FROM patients WHERE patient_id = ?");
                pstmt.setInt(1, id);
                rs = pstmt.executeQuery();
                if (!rs.next()) { System.out.println("Patient not found!"); return; }

                new Patient(rs.getInt("patient_id"), rs.getString("first_name"), rs.getString("last_name"),
                    rs.getString("contact_number"), rs.getString("email"), rs.getString("address"),
                    rs.getString("date_of_birth") == null ? "" : rs.getString("date_of_birth"),
                    rs.getString("medical_history") == null ? "" : rs.getString("medical_history")
                ).displayInfo();
                rs.close(); pstmt.close();

                System.out.print("Are you sure you want to delete? (yes/no): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                    pstmt = conn.prepareStatement("DELETE FROM patients WHERE patient_id = ?");
                    pstmt.setInt(1, id);
                    System.out.println(pstmt.executeUpdate() > 0 ? "\nPatient deleted successfully!" : "\nFailed to delete.");
                } else { System.out.println("Deletion cancelled."); }
            } catch (SQLException e) {
                System.out.println("Error deleting patient: " + e.getMessage());
            } finally {
                DatabaseConnection.closeResultSet(rs);
                DatabaseConnection.closeStatement(pstmt);
                DatabaseConnection.closeConnection(conn);
            }
        } catch (NumberFormatException e) { System.out.println("Invalid ID!"); }
    }

    // =========================================================
    // DOCTOR MANAGEMENT MENU
    // =========================================================
    private static void doctorManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n╔═══════════════════════════════════════════╗");
            System.out.println("║          DOCTOR MANAGEMENT                ║");
            System.out.println("╠═══════════════════════════════════════════╣");
            System.out.println("║  1. Add New Doctor                        ║");
            System.out.println("║  2. View All Doctors                      ║");
            System.out.println("║  3. Search Doctor                         ║");
            System.out.println("║  4. Update Doctor                         ║");
            System.out.println("║  5. Delete Doctor                         ║");
            System.out.println("║  6. Back                                  ║");
            System.out.println("╚═══════════════════════════════════════════╝");
            System.out.print("Enter choice: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1: addDoctor();      break;
                    case 2: viewAllDoctors(); break;
                    case 3: searchDoctor();   break;
                    case 4: updateDoctor();   break;
                    case 5: deleteDoctor();   break;
                    case 6: back = true;      break;
                    default: System.out.println("\nInvalid choice!");
                }
            } catch (NumberFormatException e) { System.out.println("\nInvalid input!"); }
        }
    }

    // ----- ADD DOCTOR -----
    private static void addDoctor() {
        System.out.println("\n=== ADD NEW DOCTOR ===");
        System.out.print("First Name       : "); String fn   = scanner.nextLine();
        System.out.print("Last Name        : "); String ln   = scanner.nextLine();
        System.out.print("Specialization   : "); String spec = scanner.nextLine();
        System.out.print("Contact Number   : "); String cn   = scanner.nextLine();
        System.out.print("Schedule (e.g. Mon-Fri 9AM-5PM): "); String sched = scanner.nextLine();

        if (fn.isEmpty() || ln.isEmpty() || spec.isEmpty() || cn.isEmpty()) {
            System.out.println("All fields except schedule are required!"); return;
        }
        Connection conn = null; PreparedStatement pstmt = null;
        try {
            conn  = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement("INSERT INTO doctors (first_name,last_name,specialization,contact_number,schedule) VALUES (?,?,?,?,?)");
            pstmt.setString(1, fn); pstmt.setString(2, ln); pstmt.setString(3, spec);
            pstmt.setString(4, cn); pstmt.setString(5, sched);
            System.out.println(pstmt.executeUpdate() > 0 ? "\nDoctor added successfully!" : "\nFailed to add doctor.");
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage());
        } finally { DatabaseConnection.closeStatement(pstmt); DatabaseConnection.closeConnection(conn); }
    }

    // ----- VIEW ALL DOCTORS -----
    private static void viewAllDoctors() {
        System.out.println("\n=== ALL DOCTORS ===");
        Connection conn = null; Statement stmt = null; ResultSet rs = null;
        ArrayList<Doctor> list = new ArrayList<>();
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs   = stmt.executeQuery("SELECT * FROM doctors ORDER BY doctor_id");
            while (rs.next()) {
                list.add(new Doctor(rs.getInt("doctor_id"), rs.getString("first_name"),
                    rs.getString("last_name"), rs.getString("contact_number"),
                    rs.getString("specialization"),
                    rs.getString("schedule") == null ? "" : rs.getString("schedule")));
            }
            if (list.isEmpty()) { System.out.println("No doctors found."); }
            else {
                System.out.println("Total Doctors: " + list.size());
                System.out.println("-".repeat(80));
                for (Doctor d : list) d.displaySummary();
                System.out.println("-".repeat(80));
            }
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage());
        } finally { DatabaseConnection.closeResultSet(rs); DatabaseConnection.closeStatement(stmt); DatabaseConnection.closeConnection(conn); }
    }

    // ----- SEARCH DOCTOR -----
    private static void searchDoctor() {
        System.out.println("\n=== SEARCH DOCTOR ===");
        System.out.print("Enter Doctor ID or Last Name: ");
        String term = scanner.nextLine().trim();
        Connection conn = null; PreparedStatement pstmt = null; ResultSet rs = null;
        try {
            conn  = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM doctors WHERE doctor_id = ? OR last_name LIKE ?");
            try { pstmt.setInt(1, Integer.parseInt(term)); } catch (NumberFormatException e) { pstmt.setInt(1, 0); }
            pstmt.setString(2, "%" + term + "%");
            rs = pstmt.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                new Doctor(rs.getInt("doctor_id"), rs.getString("first_name"), rs.getString("last_name"),
                    rs.getString("contact_number"), rs.getString("specialization"),
                    rs.getString("schedule") == null ? "" : rs.getString("schedule")).displayInfo();
            }
            if (!found) System.out.println("No doctor found.");
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage());
        } finally { DatabaseConnection.closeResultSet(rs); DatabaseConnection.closeStatement(pstmt); DatabaseConnection.closeConnection(conn); }
    }

    // ----- UPDATE DOCTOR -----
    private static void updateDoctor() {
        System.out.println("\n=== UPDATE DOCTOR ===");
        System.out.print("Enter Doctor ID to update: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Connection conn = null; PreparedStatement pstmt = null; ResultSet rs = null;
            try {
                conn  = DatabaseConnection.getConnection();
                pstmt = conn.prepareStatement("SELECT * FROM doctors WHERE doctor_id = ?");
                pstmt.setInt(1, id); rs = pstmt.executeQuery();
                if (!rs.next()) { System.out.println("Doctor not found!"); return; }
                Doctor cur = new Doctor(rs.getInt("doctor_id"), rs.getString("first_name"), rs.getString("last_name"),
                    rs.getString("contact_number"), rs.getString("specialization"),
                    rs.getString("schedule") == null ? "" : rs.getString("schedule"));
                cur.displayInfo(); rs.close(); pstmt.close();

                System.out.println("Press Enter to keep current value.");
                System.out.print("First Name     [" + cur.getFirstName()      + "]: "); String fn   = scanner.nextLine(); if (fn.isEmpty())   fn   = cur.getFirstName();
                System.out.print("Last Name      [" + cur.getLastName()       + "]: "); String ln   = scanner.nextLine(); if (ln.isEmpty())   ln   = cur.getLastName();
                System.out.print("Specialization [" + cur.getSpecialization() + "]: "); String spec = scanner.nextLine(); if (spec.isEmpty()) spec = cur.getSpecialization();
                System.out.print("Contact        [" + cur.getContactNumber()  + "]: "); String cn   = scanner.nextLine(); if (cn.isEmpty())   cn   = cur.getContactNumber();
                System.out.print("Schedule       [" + cur.getSchedule()       + "]: "); String sc   = scanner.nextLine(); if (sc.isEmpty())   sc   = cur.getSchedule();

                pstmt = conn.prepareStatement("UPDATE doctors SET first_name=?,last_name=?,specialization=?,contact_number=?,schedule=? WHERE doctor_id=?");
                pstmt.setString(1,fn); pstmt.setString(2,ln); pstmt.setString(3,spec);
                pstmt.setString(4,cn); pstmt.setString(5,sc); pstmt.setInt(6,id);
                System.out.println(pstmt.executeUpdate() > 0 ? "\nDoctor updated!" : "\nFailed.");
            } catch (SQLException e) { System.out.println("Error: " + e.getMessage());
            } finally { DatabaseConnection.closeResultSet(rs); DatabaseConnection.closeStatement(pstmt); DatabaseConnection.closeConnection(conn); }
        } catch (NumberFormatException e) { System.out.println("Invalid ID!"); }
    }

    // ----- DELETE DOCTOR -----
    private static void deleteDoctor() {
        System.out.println("\n=== DELETE DOCTOR ===");
        System.out.print("Enter Doctor ID to delete: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Connection conn = null; PreparedStatement pstmt = null; ResultSet rs = null;
            try {
                conn  = DatabaseConnection.getConnection();
                pstmt = conn.prepareStatement("SELECT * FROM doctors WHERE doctor_id = ?");
                pstmt.setInt(1, id); rs = pstmt.executeQuery();
                if (!rs.next()) { System.out.println("Doctor not found!"); return; }
                new Doctor(rs.getInt("doctor_id"), rs.getString("first_name"), rs.getString("last_name"),
                    rs.getString("contact_number"), rs.getString("specialization"),
                    rs.getString("schedule") == null ? "" : rs.getString("schedule")).displayInfo();
                rs.close(); pstmt.close();

                System.out.print("Are you sure? (yes/no): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                    pstmt = conn.prepareStatement("DELETE FROM doctors WHERE doctor_id = ?");
                    pstmt.setInt(1, id);
                    System.out.println(pstmt.executeUpdate() > 0 ? "\nDoctor deleted!" : "\nFailed.");
                } else { System.out.println("Cancelled."); }
            } catch (SQLException e) { System.out.println("Error: " + e.getMessage());
            } finally { DatabaseConnection.closeResultSet(rs); DatabaseConnection.closeStatement(pstmt); DatabaseConnection.closeConnection(conn); }
        } catch (NumberFormatException e) { System.out.println("Invalid ID!"); }
    }

    // =========================================================
    // APPOINTMENT MANAGEMENT MENU
    // =========================================================
    private static void appointmentManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n╔═══════════════════════════════════════════╗");
            System.out.println("║       APPOINTMENT MANAGEMENT              ║");
            System.out.println("╠═══════════════════════════════════════════╣");
            System.out.println("║  1. Book New Appointment                  ║");
            System.out.println("║  2. View All Appointments                 ║");
            System.out.println("║  3. Search Appointment                    ║");
            System.out.println("║  4. Update Appointment                    ║");
            System.out.println("║  5. Cancel Appointment                    ║");
            System.out.println("║  6. Back                                  ║");
            System.out.println("╚═══════════════════════════════════════════╝");
            System.out.print("Enter choice: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1: bookAppointment();      break;
                    case 2: viewAllAppointments();  break;
                    case 3: searchAppointment();    break;
                    case 4: updateAppointment();    break;
                    case 5: cancelAppointment();    break;
                    case 6: back = true;            break;
                    default: System.out.println("\nInvalid choice!");
                }
            } catch (NumberFormatException e) { System.out.println("\nInvalid input!"); }
        }
    }

    // ----- BOOK APPOINTMENT -----
    private static void bookAppointment() {
        System.out.println("\n=== BOOK NEW APPOINTMENT ===");
        viewAllPatients();
        System.out.print("\nEnter Patient ID: ");
        try {
            int pid = Integer.parseInt(scanner.nextLine().trim());
            viewAllDoctors();
            System.out.print("\nEnter Doctor ID: ");
            int did = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Date (YYYY-MM-DD): "); String date   = scanner.nextLine().trim();
            System.out.print("Time (HH:MM)     : "); String time   = scanner.nextLine().trim();
            System.out.print("Reason for Visit : "); String reason = scanner.nextLine();

            if (date.isEmpty() || time.isEmpty()) { System.out.println("Date and time required!"); return; }

            Connection conn = null; PreparedStatement pstmt = null;
            try {
                conn  = DatabaseConnection.getConnection();
                pstmt = conn.prepareStatement("INSERT INTO appointments (patient_id,doctor_id,appointment_date,appointment_time,status,reason_for_visit) VALUES (?,?,?,?,'scheduled',?)");
                pstmt.setInt(1,pid); pstmt.setInt(2,did);
                pstmt.setString(3,date); pstmt.setString(4,time); pstmt.setString(5,reason);
                System.out.println(pstmt.executeUpdate() > 0 ? "\nAppointment booked!" : "\nFailed.");
            } catch (SQLException e) {
                System.out.println("Error (check Patient/Doctor ID exists): " + e.getMessage());
            } finally { DatabaseConnection.closeStatement(pstmt); DatabaseConnection.closeConnection(conn); }
        } catch (NumberFormatException e) { System.out.println("Invalid ID!"); }
    }

    // ----- VIEW ALL APPOINTMENTS -----
    private static void viewAllAppointments() {
        System.out.println("\n=== ALL APPOINTMENTS ===");
        Connection conn = null; Statement stmt = null; ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT a.appointment_id, " +
                "CONCAT(p.first_name,' ',p.last_name) AS patient_name, " +
                "CONCAT(d.first_name,' ',d.last_name) AS doctor_name, " +
                "d.specialization, a.appointment_date, a.appointment_time, a.status " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.patient_id " +
                "JOIN doctors  d ON a.doctor_id  = d.doctor_id " +
                "ORDER BY a.appointment_date, a.appointment_time";
            rs = stmt.executeQuery(sql);
            int count = 0;
            System.out.println("-".repeat(110));
            System.out.printf("%-5s %-22s %-22s %-18s %-12s %-8s %-12s%n",
                "ID","Patient","Doctor","Specialization","Date","Time","Status");
            System.out.println("-".repeat(110));
            while (rs.next()) {
                count++;
                System.out.printf("%-5d %-22s %-22s %-18s %-12s %-8s %-12s%n",
                    rs.getInt("appointment_id"),
                    rs.getString("patient_name"), rs.getString("doctor_name"),
                    rs.getString("specialization"), rs.getString("appointment_date"),
                    rs.getString("appointment_time"), rs.getString("status"));
            }
            System.out.println("-".repeat(110));
            System.out.println(count == 0 ? "No appointments found." : "Total: " + count);
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage());
        } finally { DatabaseConnection.closeResultSet(rs); DatabaseConnection.closeStatement(stmt); DatabaseConnection.closeConnection(conn); }
    }

    // ----- SEARCH APPOINTMENT -----
    private static void searchAppointment() {
        System.out.println("\n=== SEARCH APPOINTMENT ===");
        System.out.print("Enter Appointment ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Connection conn = null; PreparedStatement pstmt = null; ResultSet rs = null;
            try {
                conn  = DatabaseConnection.getConnection();
                String sql = "SELECT a.*, CONCAT(p.first_name,' ',p.last_name) AS patient_name, " +
                    "CONCAT(d.first_name,' ',d.last_name) AS doctor_name, d.specialization " +
                    "FROM appointments a " +
                    "JOIN patients p ON a.patient_id = p.patient_id " +
                    "JOIN doctors  d ON a.doctor_id  = d.doctor_id " +
                    "WHERE a.appointment_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id); rs = pstmt.executeQuery();
                if (rs.next()) {
                    System.out.println("\n=== APPOINTMENT DETAILS ===");
                    System.out.println("ID            : " + rs.getInt("appointment_id"));
                    System.out.println("Patient       : " + rs.getString("patient_name"));
                    System.out.println("Doctor        : " + rs.getString("doctor_name"));
                    System.out.println("Specialization: " + rs.getString("specialization"));
                    System.out.println("Date          : " + rs.getString("appointment_date"));
                    System.out.println("Time          : " + rs.getString("appointment_time"));
                    System.out.println("Status        : " + rs.getString("status"));
                    System.out.println("Reason        : " + rs.getString("reason_for_visit"));
                    System.out.println("===========================\n");
                } else { System.out.println("Appointment not found."); }
            } catch (SQLException e) { System.out.println("Error: " + e.getMessage());
            } finally { DatabaseConnection.closeResultSet(rs); DatabaseConnection.closeStatement(pstmt); DatabaseConnection.closeConnection(conn); }
        } catch (NumberFormatException e) { System.out.println("Invalid ID!"); }
    }

    // ----- UPDATE APPOINTMENT -----
    private static void updateAppointment() {
        System.out.println("\n=== UPDATE APPOINTMENT ===");
        System.out.print("Enter Appointment ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Connection conn = null; PreparedStatement pstmt = null; ResultSet rs = null;
            try {
                conn  = DatabaseConnection.getConnection();
                pstmt = conn.prepareStatement("SELECT * FROM appointments WHERE appointment_id = ?");
                pstmt.setInt(1, id); rs = pstmt.executeQuery();
                if (!rs.next()) { System.out.println("Appointment not found!"); return; }
                Appointment cur = new Appointment(
                    rs.getInt("appointment_id"), rs.getInt("patient_id"), rs.getInt("doctor_id"),
                    rs.getString("appointment_date"), rs.getString("appointment_time"),
                    rs.getString("status"), rs.getString("reason_for_visit") == null ? "" : rs.getString("reason_for_visit"));
                cur.displayInfo(); rs.close(); pstmt.close();

                System.out.println("1. Date & Time  2. Status  3. Reason");
                System.out.print("What to update? ");
                int opt = Integer.parseInt(scanner.nextLine().trim());
                String sql = ""; Object[] params;

                if (opt == 1) {
                    System.out.print("New Date (YYYY-MM-DD) [" + cur.getAppointmentDate() + "]: "); String nd = scanner.nextLine(); if (nd.isEmpty()) nd = cur.getAppointmentDate();
                    System.out.print("New Time (HH:MM)      [" + cur.getAppointmentTime() + "]: "); String nt = scanner.nextLine(); if (nt.isEmpty()) nt = cur.getAppointmentTime();
                    sql = "UPDATE appointments SET appointment_date=?,appointment_time=? WHERE appointment_id=?";
                    pstmt = conn.prepareStatement(sql); pstmt.setString(1,nd); pstmt.setString(2,nt); pstmt.setInt(3,id);
                } else if (opt == 2) {
                    System.out.print("New Status (scheduled/completed/cancelled) [" + cur.getStatus() + "]: "); String ns = scanner.nextLine(); if (ns.isEmpty()) ns = cur.getStatus();
                    sql = "UPDATE appointments SET status=? WHERE appointment_id=?";
                    pstmt = conn.prepareStatement(sql); pstmt.setString(1,ns); pstmt.setInt(2,id);
                } else if (opt == 3) {
                    System.out.print("New Reason [" + cur.getReasonForVisit() + "]: "); String nr = scanner.nextLine(); if (nr.isEmpty()) nr = cur.getReasonForVisit();
                    sql = "UPDATE appointments SET reason_for_visit=? WHERE appointment_id=?";
                    pstmt = conn.prepareStatement(sql); pstmt.setString(1,nr); pstmt.setInt(2,id);
                } else { System.out.println("Invalid option."); return; }
                System.out.println(pstmt.executeUpdate() > 0 ? "\nAppointment updated!" : "\nFailed.");
            } catch (SQLException e) { System.out.println("Error: " + e.getMessage());
            } finally { DatabaseConnection.closeResultSet(rs); DatabaseConnection.closeStatement(pstmt); DatabaseConnection.closeConnection(conn); }
        } catch (NumberFormatException e) { System.out.println("Invalid ID!"); }
    }

    // ----- CANCEL APPOINTMENT -----
    private static void cancelAppointment() {
        System.out.println("\n=== CANCEL APPOINTMENT ===");
        System.out.print("Enter Appointment ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Connection conn = null; PreparedStatement pstmt = null; ResultSet rs = null;
            try {
                conn  = DatabaseConnection.getConnection();
                pstmt = conn.prepareStatement("SELECT * FROM appointments WHERE appointment_id = ?");
                pstmt.setInt(1, id); rs = pstmt.executeQuery();
                if (!rs.next()) { System.out.println("Appointment not found!"); return; }
                new Appointment(
                    rs.getInt("appointment_id"), rs.getInt("patient_id"), rs.getInt("doctor_id"),
                    rs.getString("appointment_date"), rs.getString("appointment_time"),
                    rs.getString("status"), rs.getString("reason_for_visit") == null ? "" : rs.getString("reason_for_visit")
                ).displayInfo();
                rs.close(); pstmt.close();

                System.out.print("Confirm cancel? (yes/no): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                    pstmt = conn.prepareStatement("DELETE FROM appointments WHERE appointment_id = ?");
                    pstmt.setInt(1, id);
                    System.out.println(pstmt.executeUpdate() > 0 ? "\nAppointment cancelled!" : "\nFailed.");
                } else { System.out.println("Cancelled."); }
            } catch (SQLException e) { System.out.println("Error: " + e.getMessage());
            } finally { DatabaseConnection.closeResultSet(rs); DatabaseConnection.closeStatement(pstmt); DatabaseConnection.closeConnection(conn); }
        } catch (NumberFormatException e) { System.out.println("Invalid ID!"); }
    }

    // =========================================================
    // REPORTS MENU
    // =========================================================
    private static void reportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n╔═══════════════════════════════════════════╗");
            System.out.println("║               REPORTS                     ║");
            System.out.println("╠═══════════════════════════════════════════╣");
            System.out.println("║  1. Daily Appointments Report             ║");
            System.out.println("║  2. Patient Appointment History           ║");
            System.out.println("║  3. Doctor Schedule Report                ║");
            System.out.println("║  4. System Summary                        ║");
            System.out.println("║  5. Back                                  ║");
            System.out.println("╚═══════════════════════════════════════════╝");
            System.out.print("Enter choice: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1: dailyReport();         break;
                    case 2: patientHistory();       break;
                    case 3: doctorScheduleReport(); break;
                    case 4: systemSummary();        break;
                    case 5: back = true;            break;
                    default: System.out.println("\nInvalid choice!");
                }
            } catch (NumberFormatException e) { System.out.println("\nInvalid input!"); }
        }
    }

    // ----- DAILY REPORT -----
    private static void dailyReport() {
        System.out.println("\n=== DAILY APPOINTMENTS REPORT ===");
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = scanner.nextLine().trim();
        Connection conn = null; PreparedStatement pstmt = null; ResultSet rs = null;
        try {
            conn  = DatabaseConnection.getConnection();
            String sql = "SELECT a.appointment_time, a.status, a.reason_for_visit, " +
                "CONCAT(p.first_name,' ',p.last_name) AS patient_name, " +
                "CONCAT(d.first_name,' ',d.last_name) AS doctor_name, d.specialization " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.patient_id " +
                "JOIN doctors  d ON a.doctor_id  = d.doctor_id " +
                "WHERE a.appointment_date = ? ORDER BY a.appointment_time";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, date); rs = pstmt.executeQuery();
            System.out.println("\nAppointments on " + date);
            System.out.println("=".repeat(80));
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("[" + rs.getString("appointment_time") + "] " +
                    rs.getString("patient_name") + " → " + rs.getString("doctor_name"));
                System.out.println("  Specialization : " + rs.getString("specialization"));
                System.out.println("  Reason         : " + rs.getString("reason_for_visit"));
                System.out.println("  Status         : " + rs.getString("status"));
                System.out.println();
            }
            System.out.println("=".repeat(80));
            System.out.println("Total: " + count);
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage());
        } finally { DatabaseConnection.closeResultSet(rs); DatabaseConnection.closeStatement(pstmt); DatabaseConnection.closeConnection(conn); }
    }

    // ----- PATIENT HISTORY -----
    private static void patientHistory() {
        System.out.println("\n=== PATIENT APPOINTMENT HISTORY ===");
        System.out.print("Enter Patient ID: ");
        try {
            int pid = Integer.parseInt(scanner.nextLine().trim());
            Connection conn = null; PreparedStatement pstmt = null; ResultSet rs = null;
            try {
                conn  = DatabaseConnection.getConnection();
                pstmt = conn.prepareStatement("SELECT CONCAT(first_name,' ',last_name) AS name FROM patients WHERE patient_id=?");
                pstmt.setInt(1, pid); rs = pstmt.executeQuery();
                if (!rs.next()) { System.out.println("Patient not found!"); return; }
                String name = rs.getString("name"); rs.close(); pstmt.close();

                String sql = "SELECT a.appointment_date, a.appointment_time, a.status, a.reason_for_visit, " +
                    "CONCAT(d.first_name,' ',d.last_name) AS doctor_name, d.specialization " +
                    "FROM appointments a JOIN doctors d ON a.doctor_id = d.doctor_id " +
                    "WHERE a.patient_id = ? ORDER BY a.appointment_date DESC";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, pid); rs = pstmt.executeQuery();
                System.out.println("\nHistory for: " + name);
                System.out.println("=".repeat(80));
                int count = 0;
                while (rs.next()) {
                    count++;
                    System.out.println("#" + count + "  " + rs.getString("appointment_date") + " at " + rs.getString("appointment_time"));
                    System.out.println("  Doctor : " + rs.getString("doctor_name") + " (" + rs.getString("specialization") + ")");
                    System.out.println("  Reason : " + rs.getString("reason_for_visit"));
                    System.out.println("  Status : " + rs.getString("status"));
                    System.out.println();
                }
                System.out.println("=".repeat(80));
                System.out.println("Total appointments: " + count);
            } catch (SQLException e) { System.out.println("Error: " + e.getMessage());
            } finally { DatabaseConnection.closeResultSet(rs); DatabaseConnection.closeStatement(pstmt); DatabaseConnection.closeConnection(conn); }
        } catch (NumberFormatException e) { System.out.println("Invalid ID!"); }
    }

    // ----- DOCTOR SCHEDULE REPORT -----
    private static void doctorScheduleReport() {
        System.out.println("\n=== DOCTOR SCHEDULE REPORT ===");
        System.out.print("Enter Doctor ID: ");
        try {
            int did = Integer.parseInt(scanner.nextLine().trim());
            Connection conn = null; PreparedStatement pstmt = null; ResultSet rs = null;
            try {
                conn  = DatabaseConnection.getConnection();
                pstmt = conn.prepareStatement("SELECT CONCAT(first_name,' ',last_name) AS name, specialization, schedule FROM doctors WHERE doctor_id=?");
                pstmt.setInt(1, did); rs = pstmt.executeQuery();
                if (!rs.next()) { System.out.println("Doctor not found!"); return; }
                String dName = rs.getString("name"), spec = rs.getString("specialization"), sched = rs.getString("schedule");
                rs.close(); pstmt.close();

                System.out.println("\n" + dName + " | " + spec + " | " + sched);
                System.out.println("=".repeat(80));
                String sql = "SELECT a.appointment_date, a.appointment_time, a.status, a.reason_for_visit, " +
                    "CONCAT(p.first_name,' ',p.last_name) AS patient_name " +
                    "FROM appointments a JOIN patients p ON a.patient_id = p.patient_id " +
                    "WHERE a.doctor_id = ? ORDER BY a.appointment_date, a.appointment_time";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, did); rs = pstmt.executeQuery();
                int count = 0;
                while (rs.next()) {
                    count++;
                    System.out.println(rs.getString("appointment_date") + " at " + rs.getString("appointment_time"));
                    System.out.println("  Patient : " + rs.getString("patient_name"));
                    System.out.println("  Reason  : " + rs.getString("reason_for_visit"));
                    System.out.println("  Status  : " + rs.getString("status"));
                    System.out.println();
                }
                System.out.println("=".repeat(80));
                System.out.println("Total: " + count);
            } catch (SQLException e) { System.out.println("Error: " + e.getMessage());
            } finally { DatabaseConnection.closeResultSet(rs); DatabaseConnection.closeStatement(pstmt); DatabaseConnection.closeConnection(conn); }
        } catch (NumberFormatException e) { System.out.println("Invalid ID!"); }
    }

    // ----- SYSTEM SUMMARY -----
    private static void systemSummary() {
        System.out.println("\n=== SYSTEM SUMMARY ===");
        Connection conn = null; Statement stmt = null; ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery("SELECT COUNT(*) FROM patients");     rs.next(); int pts  = rs.getInt(1); rs.close();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM doctors");      rs.next(); int drs  = rs.getInt(1); rs.close();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM appointments"); rs.next(); int appt = rs.getInt(1); rs.close();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM appointments WHERE status='scheduled'");  rs.next(); int sched  = rs.getInt(1); rs.close();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM appointments WHERE status='completed'");  rs.next(); int comp   = rs.getInt(1); rs.close();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM appointments WHERE status='cancelled'");  rs.next(); int cancel = rs.getInt(1); rs.close();

            System.out.println("=".repeat(45));
            System.out.println("Total Patients         : " + pts);
            System.out.println("Total Doctors          : " + drs);
            System.out.println("Total Appointments     : " + appt);
            System.out.println("  - Scheduled          : " + sched);
            System.out.println("  - Completed          : " + comp);
            System.out.println("  - Cancelled          : " + cancel);
            System.out.println("=".repeat(45));
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage());
        } finally { DatabaseConnection.closeResultSet(rs); DatabaseConnection.closeStatement(stmt); DatabaseConnection.closeConnection(conn); }
    }
}