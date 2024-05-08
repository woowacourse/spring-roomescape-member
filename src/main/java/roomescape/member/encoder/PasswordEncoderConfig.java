package roomescape.member.encoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
public class PasswordEncoderConfig {

    @Value("${custom.security.password-encode.iteration-count}")
    private int iterationCount;
    @Value("${custom.security.password-encode.key-length}")
    private int keyLength;

    @Bean
    public PasswordEncoder passwordEncoder() {
        System.out.println("iterationCount = " + iterationCount);
        System.out.println("keyLength = " + keyLength);
        return new Pbkdf2PasswordEncoder(
                generateSalt(),
                iterationCount,
                keyLength
        );
    }

    private byte[] generateSalt() {
        final SecureRandom random = new SecureRandom();
        final byte[] salt = new byte[16];
        random.nextBytes(salt);

        return salt;
    }
}
