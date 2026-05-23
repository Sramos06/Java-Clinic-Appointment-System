package clinic;
/**
 * Patient Class
 * 
 * OOP Concepts Demonstrated:
 * - Inheritance: Extends Person class
 * - Polymorphism: Overrides displayInfo() method
 * - Encapsulation: Private attributes with getters and setters
 */
public class Patient extends Person {
    // Additional private attributes specific to Patient
    private String email;
    private String address;
    private String dateOfBirth;
    private String medicalHistory;
    
    // Default Constructor
    public Patient() {
        super(); // Call parent constructor
        this.email = "";
        this.address = "";
        this.dateOfBirth = "";
        this.medicalHistory = "";
    }
    
    // Parameterized Constructor
    public Patient(int id, String firstName, String lastName, String contactNumber,
                   String email, String address, String dateOfBirth, String medicalHistory) {
        super(id, firstName, lastName, contactNumber); // Call parent constructor
        this.email = email;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.medicalHistory = medicalHistory;
    }
    
    // Getters and Setters
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getMedicalHistory() {
        return medicalHistory;
    }
    
    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
    
    // Polymorphism: Override abstract method from Person
    @Override
    public void displayInfo() {
        System.out.println("\n=== PATIENT INFORMATION ===");
        System.out.println("Patient ID: " + getId());
        System.out.println("Name: " + getFullName());
        System.out.println("Contact: " + getContactNumber());
        System.out.println("Email: " + email);
        System.out.println("Address: " + address);
        System.out.println("Date of Birth: " + dateOfBirth);
        System.out.println("Medical History: " + medicalHistory);
        System.out.println("===========================\n");
    }
    
    // Additional method specific to Patient
    public void displaySummary() {
        System.out.printf("ID: %-5d | Name: %-30s | Contact: %-15s\n", 
                         getId(), getFullName(), getContactNumber());
    }
}
