package roomescape.model;

public class Customer {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    public Customer(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Customer(String email, String password){
        this.id = null;
        this.email = email;
        this.name = null;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }
}
