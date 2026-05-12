package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.ThemeRequestDto;
import roomescape.dto.ThemeResponseDto;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Test
    void createTest() {
        String name = "피즈의 모험";
        String description = "모험 이야기";
        String thumbnailUrl = "url.jpg";
        ThemeRequestDto requestDto = new ThemeRequestDto(name, description, thumbnailUrl);

        ThemeResponseDto responseDto = themeService.create(requestDto);

        assertThat(responseDto).isEqualTo(
                new ThemeResponseDto(
                        responseDto.id(),
                        name,
                        description,
                        thumbnailUrl
                )
        );
    }

    @Test
    void readAllTest() {
        ThemeResponseDto firstResponse = themeService.create(new ThemeRequestDto(
                "피즈의 모험",
                "모험 이야기",
                "url.jpg"
        ));
        ThemeResponseDto secondResponse = themeService.create(new ThemeRequestDto(
                "나무의 일대기",
                "모험 이야기",
                "url.jpg"
        ));

        List<ThemeResponseDto> responseDtos = themeService.readAll();

        assertThat(responseDtos.size()).isEqualTo(2);
        assertThat(responseDtos.get(0)).isEqualTo(firstResponse);
        assertThat(responseDtos.get(1)).isEqualTo(secondResponse);
    }

    @Test
    void deleteTest() {
        ThemeResponseDto responseDto = themeService.create(new ThemeRequestDto(
                "피즈의 모험",
                "모험 이야기",
                "url.jpg"
        ));

        themeService.delete(responseDto.id());

        List<ThemeResponseDto> responseDtos = themeService.readAll();
        assertThat(responseDtos.size()).isEqualTo(0);
    }

    @Test
    @Sql(scripts = "/ranking-test-data.sql")
    void readRankingTest() {
        List<ThemeResponseDto> responseDtos = themeService.readRanking(LocalDate.of(2026, 5, 1),
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
}
