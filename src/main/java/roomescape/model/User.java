package roomescape.model;

import java.util.Objects;

import roomescape.exception.BadRequestException;

public class User {

    private Long id;
    private String name;
    private String email;
    private String password;

    private User() {
    }

    public User(Long id, String name, String email, String password) {
        validateNullOrBlank(name, "name");
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(Long id, String name) {
        this(id, name, null, null);
    }

    private void validateNullOrBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new BadRequestException(value, fieldName);
        }
    }

    public Long getId() {
        return id;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        User user = (User) object;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getName(), user.getName())
                && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getPassword(),
                user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getEmail(), getPassword());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
