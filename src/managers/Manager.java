package managers;

public class Manager {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String adress;
    private Genre genre;
    private String email;
    private String password;

    public enum Genre {
        MAN,
        WOMAN
    }

    public Manager(String firstName, String lastName, String phoneNumber, String adress, Genre genre, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.adress = adress;
        this.genre = genre;
        this.email = email;
        this.password = password;
    }


}
