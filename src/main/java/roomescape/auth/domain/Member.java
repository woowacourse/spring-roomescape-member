package roomescape.auth.domain;

import roomescape.reservation.domain.Name;

public class Member {

  private final Long id;
  private final Name name;
  private final String email;
  private final String password;
  private final Role role;

  public Member(Long id, Name name, String email, String password, Role role) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  public static Member of(String name, String email, String password) {
    return new Member(null, new Name(name), email, password, null);
  }

  public static Member of(Name name) {
    return new Member(null, name, null, null, null);
  }

  public static Member createInstance(long id, String name, String email, String password,
      String role) {
    return new Member(id, new Name(name), email, password, new Role(role));
  }

  public Long getId() {
    return id;
  }

  public Name getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public Role getRole() {
    return role;
  }
}
