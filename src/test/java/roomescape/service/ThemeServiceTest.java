package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
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
                        1L,
                        name,
                        description,
                        thumbnailUrl
                )
        );
    }

    @Test
    void readAllTest() {
        themeService.create(new ThemeRequestDto(
                "피즈의 모험",
                "모험 이야기",
                "url.jpg"
        ));
        themeService.create(new ThemeRequestDto(
                "나무의 일대기",
                "모험 이야기",
                "url.jpg"
        ));

        List<ThemeResponseDto> responseDtos = themeService.readAll();

        assertThat(responseDtos.size()).isEqualTo(2);
        assertThat(responseDtos.get(0)).isEqualTo(new ThemeResponseDto(
                1L,
                "피즈의 모험",
                "모험 이야기",
                "url.jpg"
        ));
        assertThat(responseDtos.get(1)).isEqualTo(new ThemeResponseDto(
                2L,
                "나무의 일대기",
                "모험 이야기",
                "url.jpg"
        ));
    }

    @Test
    void deleteTest() {
        themeService.create(new ThemeRequestDto(
                "피즈의 모험",
                "모험 이야기",
                "url.jpg"
        ));

        themeService.delete(1L);

        List<ThemeResponseDto> responseDtos = themeService.readAll();
        assertThat(responseDtos.size()).isEqualTo(0);
    }

}
