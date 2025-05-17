package roomescape.business.domain.member;

import roomescape.config.PasswordEncryptor;

public final class SignUpMember {

    private final Long id;
    private final MemberName name;
    private final MemberRole role;
    private final MemberCredential credential;

    public SignUpMember(Long id, MemberName name, MemberRole role, MemberCredential credential) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.credential = credential;
    }

    public SignUpMember(String name, String email, String password) {
        this(null, new MemberName(name), MemberRole.MEMBER,
                new MemberCredential(email, PasswordEncryptor.encrypt(password)));
    }

    public SignUpMember(String name, String email, String password, MemberRole role) {
        this(null, new MemberName(name), role, new MemberCredential(email, PasswordEncryptor.encrypt(password)));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public MemberRole getRole() {
        return role;
    }

    public String getEmail() {
        return credential.getEmail();
    }

    public String getPassword() {
        return credential.getPassword();
    }
}
