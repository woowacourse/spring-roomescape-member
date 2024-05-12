package roomescape.auth.domain;

import java.util.Objects;

public class Role {

  public static final String ADMIN_ROLE_NAME = "ADMIN";
  private final String name;

  public Role(String name) {
    this.name = name;
  }

  public boolean isAdmin() {
    return name.equals(ADMIN_ROLE_NAME);
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Role role = (Role) o;
    return Objects.equals(name, role.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
