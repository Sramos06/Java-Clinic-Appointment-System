package clinic;
/**
 * Appointment Class
 * 
 * OOP Concepts Demonstrated:
 * - Encapsulation: Private attributes with getters and setters
 * - Objects: Represents appointment objects
 * - Constructors: Default and parameterized constructors
 */
public class Appointment {
    // Private attributes (Encapsulation)
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private String appointmentDate;
    private String appointmentTime;
    private String status;
    private String reasonForVisit;
    
    // Default Constructor
    public Appointment() {
        this.appointmentId = 0;
        this.patientId = 0;
        this.doctorId = 0;
        this.appointmentDate = "";
        this.appointmentTime = "";
        this.status = "scheduled";
        this.reasonForVisit = "";
    }
    
    // Parameterized Constructor
    public Appointment(int appointmentId, int patientId, int doctorId, 
                      String appointmentDate, String appointmentTime, 
                      String status, String reasonForVisit) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.reasonForVisit = reasonForVisit;
    }
    
    // Getters and Setters
    public int getAppointmentId() {
        return appointmentId;
    }
    
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
    
    public int getPatientId() {
        return patientId;
    }
    
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    
    public int getDoctorId() {
        return doctorId;
    }
    
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
    
    public String getAppointmentDate() {
        return appointmentDate;
    }
    
    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    
    public String getAppointmentTime() {
        return appointmentTime;
    }
    
    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getReasonForVisit() {
        return reasonForVisit;
    }
    
    public void setReasonForVisit(String reasonForVisit) {
        this.reasonForVisit = reasonForVisit;
    }
    
    // Method to display appointment information
    public void displayInfo() {
        System.out.println("\n=== APPOINTMENT INFORMATION ===");
        System.out.println("Appointment ID: " + appointmentId);
        System.out.println("Patient ID: " + patientId);
        System.out.println("Doctor ID: " + doctorId);
        System.out.println("Date: " + appointmentDate);
        System.out.println("Time: " + appointmentTime);
        System.out.println("Status: " + status);
        System.out.println("Reason: " + reasonForVisit);
        System.out.println("================================\n");
    }
    
    // Method to display summary
    public void displaySummary() {
        System.out.printf("ID: %-5d | Date: %-12s | Time: %-8s | Status: %-12s\n", 
                         appointmentId, appointmentDate, appointmentTime, status);
    }
}
