package clinic;
/**
 * Doctor Class
 * 
 * OOP Concepts Demonstrated:
 * - Inheritance: Extends Person class
 * - Polymorphism: Overrides displayInfo() method
 * - Encapsulation: Private attributes with getters and setters
 */
public class Doctor extends Person {
    // Additional private attributes specific to Doctor
    private String specialization;
    private String schedule;
    
    // Default Constructor
    public Doctor() {
        super(); // Call parent constructor
        this.specialization = "";
        this.schedule = "";
    }
    
    // Parameterized Constructor
    public Doctor(int id, String firstName, String lastName, String contactNumber,
                  String specialization, String schedule) {
        super(id, firstName, lastName, contactNumber); // Call parent constructor
        this.specialization = specialization;
        this.schedule = schedule;
    }
    
    // Getters and Setters
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public String getSchedule() {
        return schedule;
    }
    
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    
    // Polymorphism: Override abstract method from Person
    @Override
    public void displayInfo() {
        System.out.println("\n=== DOCTOR INFORMATION ===");
        System.out.println("Doctor ID: " + getId());
        System.out.println("Name: " + getFullName());
        System.out.println("Specialization: " + specialization);
        System.out.println("Contact: " + getContactNumber());
        System.out.println("Schedule: " + schedule);
        System.out.println("==========================\n");
    }
    
    // Additional method specific to Doctor
    public void displaySummary() {
        System.out.printf("ID: %-5d | Name: %-30s | Specialization: %-25s\n", 
                         getId(), getFullName(), specialization);
    }
}
