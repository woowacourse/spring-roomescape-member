package roomescape;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//TODO: Domain, DTO 단 검증로직 구현 spring-validation 없이
@SpringBootApplication
public class RoomescapeApplication {

    public static void main(final String[] args) {
        SpringApplication.run(RoomescapeApplication.class, args);
    }

}
