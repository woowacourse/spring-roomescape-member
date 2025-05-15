package roomescape.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import roomescape.auth.repository.AuthRepository;
import roomescape.entity.Member;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final AuthRepository authRepository;

    public AdminInitializer(final AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public void run(final String... args) throws Exception {
        Member member = Member.beforeAdminSave("어드민", "admin@admin.com", "admin");
        if (!authRepository.isExistEmail(member.getEmail())) {
            authRepository.save(member);
        }
    }
}
