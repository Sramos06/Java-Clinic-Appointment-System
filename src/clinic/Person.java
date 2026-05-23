package clinic;
/**
 * Abstract Person Class
 * 
 * OOP Concepts Demonstrated:
 * - Abstraction: Abstract class with abstract method
 * - Encapsulation: Private attributes with getters and setters
 * 
 * This is the parent class for Patient and Doctor classes.
 */
public abstract class Person {
    // Private attributes (Encapsulation)
    private int id;
    private String firstName;
    private String lastName;
    private String contactNumber;
    
    // Default Constructor
    public Person() {
        this.id = 0;
        this.firstName = "";
        this.lastName = "";
        this.contactNumber = "";
    }
    
    // Parameterized Constructor
    public Person(int id, String firstName, String lastName, String contactNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
    }
    
    // Getters and Setters (Encapsulation)
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getContactNumber() {
        return contactNumber;
    }
    
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    
    // Method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    // Abstract method (must be implemented by child classes)
    public abstract void displayInfo();
    
    // Method overloading example
    public void displayInfo(boolean detailed) {
        if (detailed) {
            displayInfo();
        } else {
            System.out.println("Name: " + getFullName());
        }
    }
}
