package bike.rental.system.model;

import java.sql.Timestamp;

/**
 * Represents a user in the Bike Rental System.
 * 
 * <p>This class stores user information including personal details,
 * authentication credentials, and account information. Users can be
 * either administrators or customers with different system permissions.
 * 
 * @author Shivam
 * @version 1.0
 * @since 2024
 */
public class User {
    /** Unique identifier for the user */
    private int userId;
    
    /** Full name of the user */
    private String name;
    
    /** Email address used for login and communication */
    private String email;
    
    /** Hashed password for secure authentication */
    private String passwordHash;
    
    /** Contact phone number */
    private String phone;
    
    /** User role: 'admin' or 'customer' */
    private String role;
    
    /** Timestamp when the user account was created */
    private Timestamp createdAt;

    /**
     * Default constructor.
     */
    public User() {}

    /**
     * Parameterized constructor to create a user with all attributes.
     *
     * @param userId the unique user identifier
     * @param name the full name of the user
     * @param email the email address
     * @param passwordHash the hashed password
     * @param phone the contact phone number
     * @param role the user role ('admin' or 'customer')
     * @param createdAt the account creation timestamp
     */
    public User(int userId, String name, String email, String passwordHash, String phone, String role, java.sql.Timestamp createdAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phone = phone;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Getters and setters with Javadoc

    /**
     * Gets the unique user identifier.
     *
     * @return the user ID
     */
    public int getUserId() { return userId; }

    /**
     * Sets the unique user identifier.
     *
     * @param userId the user ID to set
     */
    public void setUserId(int userId) { this.userId = userId; }

    /**
     * Gets the user's full name.
     *
     * @return the user's name
     */
    public String getName() { return name; }

    /**
     * Sets the user's full name.
     *
     * @param name the name to set
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gets the user's email address.
     *
     * @return the email address
     */
    public String getEmail() { return email; }

    /**
     * Sets the user's email address.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Gets the hashed password.
     *
     * @return the password hash
     */
    public String getPasswordHash() { return passwordHash; }

    /**
     * Sets the hashed password.
     *
     * @param passwordHash the password hash to set
     */
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    /**
     * Gets the user's phone number.
     *
     * @return the phone number
     */
    public String getPhone() { return phone; }

    /**
     * Sets the user's phone number.
     *
     * @param phone the phone number to set
     */
    public void setPhone(String phone) { this.phone = phone; }

    /**
     * Gets the user's role.
     *
     * @return the user role ('admin' or 'customer')
     */
    public String getRole() { return role; }

    /**
     * Sets the user's role.
     *
     * @param role the role to set ('admin' or 'customer')
     */
    public void setRole(String role) { this.role = role; }

    /**
     * Gets the account creation timestamp.
     *
     * @return the creation timestamp
     */
    public java.sql.Timestamp getCreatedAt() { return createdAt; }

    /**
     * Sets the account creation timestamp.
     *
     * @param createdAt the creation timestamp to set
     */
    public void setCreatedAt(java.sql.Timestamp createdAt) { this.createdAt = createdAt; }
}