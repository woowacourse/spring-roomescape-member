package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import roomescape.service.dto.ServiceThemeRequest;
import roomescape.service.dto.ServiceThemeResponse;
import roomescape.support.DatabaseCleanUp;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ThemeServiceTest {

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    private ThemeService themeService;

    @Test
    void createTest() {
        String name = "피즈의 모험";
        String description = "모험 이야기";
        String thumbnailUrl = "url.jpg";
        ServiceThemeRequest requestDto = new ServiceThemeRequest(name, description, thumbnailUrl);

        ServiceThemeResponse responseDto = themeService.create(requestDto);

        assertThat(responseDto).isEqualTo(
                new ServiceThemeResponse(
                        1L,
                        name,
                        description,
                        thumbnailUrl
                )
        );
    }

    @Test
    void readAllTest() {
        themeService.create(new ServiceThemeRequest(
                "피즈의 모험",
                "모험 이야기",
                "url.jpg"
        ));
        themeService.create(new ServiceThemeRequest(
                "나무의 일대기",
                "모험 이야기",
                "url.jpg"
        ));

        List<ServiceThemeResponse> responseDtos = themeService.readAll();

        assertThat(responseDtos.size()).isEqualTo(2);
        assertThat(responseDtos.get(0)).isEqualTo(new ServiceThemeResponse(
                1L,
                "피즈의 모험",
                "모험 이야기",
                "url.jpg"
        ));
        assertThat(responseDtos.get(1)).isEqualTo(new ServiceThemeResponse(
                2L,
                "나무의 일대기",
                "모험 이야기",
                "url.jpg"
        ));
    }

    @Test
    void deleteTest() {
        themeService.create(new ServiceThemeRequest(
                "피즈의 모험",
                "모험 이야기",
                "url.jpg"
        ));

        themeService.delete(1L);

        List<ServiceThemeResponse> responseDtos = themeService.readAll();
        assertThat(responseDtos.size()).isEqualTo(0);
    }

    @Test
    @Sql(scripts = "/ranking-test-data.sql")
    void readRankingTest() {
        List<ServiceThemeResponse> responseDtos = themeService.readRanking(LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 7));

        assertThat(responseDtos.get(0).id()).isEqualTo(1);
        assertThat(responseDtos.get(1).id()).isEqualTo(2);
        assertThat(responseDtos.get(2).id()).isEqualTo(3);
        assertThat(responseDtos.get(3).id()).isEqualTo(4);
        assertThat(responseDtos.get(4).id()).isEqualTo(5);
        assertThat(responseDtos.get(5).id()).isEqualTo(6);
        assertThat(responseDtos.get(6).id()).isEqualTo(7);
        assertThat(responseDtos.get(7).id()).isEqualTo(8);
        assertThat(responseDtos.get(8).id()).isEqualTo(9);
        assertThat(responseDtos.get(9).id()).isEqualTo(10);
    }

    @AfterEach
    void afterEach() {
        databaseCleanUp.execute();
    }
}
