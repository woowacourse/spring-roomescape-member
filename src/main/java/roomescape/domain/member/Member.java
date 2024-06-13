package roomescape.domain.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Member {
    private final Long id;

    private final MemberName name;

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "이메일 형식에 맞게 입력해 주세요.")
    private final String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Size(min = 4, max = 20, message = "비밀번호는 최소 4글자, 최대 20글자로 작성해주세요.")
    private final String password;

    private final MemberRole role;


    public Member(MemberName name, String email, String password, MemberRole role) {
        this.id = null;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member(long id, MemberName name, String email, String password, MemberRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public MemberName getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }

    public MemberRole getRole() {
        return role;
    }
}
