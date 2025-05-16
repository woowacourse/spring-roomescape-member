package roomescape.domain.member.model;


public class Member {

    private Long id;
    private final Role role;
    private final String name;
    private final String email;
    private final String password;

    private Member(Long id, Role role, String name, String email, String password) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private Member(Role role, String name, String email, String password) {
        this.name = name;
        this.role = role;
        this.email = email;
        this.password = password;
    }

    public static Member createMember(Long id, Role role, String name, String email,
        String password) {
        return new Member(id, role, name, email, password);
    }

    public static Member createUserWithId(Long id, String name, String email, String password) {
        return new Member(id, Role.USER, name, email, password);
    }

    public static Member createUser(String name, String email, String password) {
        return new Member(Role.USER, name, email, password);
    }

    /**
     * 사장님 회원가입 페이지가 추가된다면, 사용할 정적 팩토리 메서드 코드
     */
    public static Member createAdminWithId(Long id, String name, String email, String password) {
        return new Member(id, Role.ADMIN, name, email, password);
    }

    public static Member createAdmin(String name, String email, String password) {
        return new Member(Role.ADMIN, name, email, password);
    }

    public Role getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
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
}
