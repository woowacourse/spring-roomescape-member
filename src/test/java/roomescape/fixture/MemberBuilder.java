package roomescape.fixture;

import static roomescape.domain.Role.ADMIN;
import static roomescape.domain.Role.MEMBER;

import roomescape.domain.Member;
import roomescape.domain.Sha256Encryptor;

public class MemberBuilder {
    private static final Sha256Encryptor ENCRYPTOR = new Sha256Encryptor();
    public static final Member DEFAULT_MEMBER = asMember(1L, "name", "email@email.com", "1234");
    public static final Member DEFAULT_ADMIN = asAdmin(2L, "admin", "email@email.com", "1234");

    public static Member asAdmin(String name, String email, String password) {
        return new Member(null, name, email, ENCRYPTOR.encrypt(password), ADMIN);
    }

    public static Member asAdmin(long id, String name, String email, String password) {
        return new Member(id, name, email, ENCRYPTOR.encrypt(password), ADMIN);
    }

    public static Member asMember(long id, String name, String email, String password) {
        return new Member(id, name, email, ENCRYPTOR.encrypt(password), MEMBER);
    }

    public static Member asMember(String name, String email, String password) {
        return new Member(null, name, email, ENCRYPTOR.encrypt(password), MEMBER);
    }
}
