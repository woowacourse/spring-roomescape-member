package roomescape.auth.domain;

import java.util.Objects;
import java.util.regex.Pattern;

import roomescape.exception.RoomEscapeException;

public class Users {
    private static final Pattern EMAIL_FORMAT = Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$");
    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    public Users(final Long id, final String name, final String email, final String password) {
        validateInvalidName(name);
        validateInvalidEmail(email);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Users(final String email, final String password, final String name) {
        this(null, name, email, password);
    }

    private void validateInvalidName(final String name) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new RoomEscapeException("사용자 명이 null 이거나 공백으로 이루어질 수 없습니다.");
        }
    }

    private void validateInvalidEmail(final String email) {
        if (!EMAIL_FORMAT.matcher(email).matches()) {
            throw new RoomEscapeException("이메일이 이메일 형식에 맞게 이루어지지 않았습니다.");
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(id, users.id) && Objects.equals(name, users.name) && Objects.equals(email, users.email) && Objects.equals(password, users.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password);
    }
}
