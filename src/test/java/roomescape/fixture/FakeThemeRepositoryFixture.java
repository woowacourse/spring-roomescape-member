package roomescape.fixture;

import java.util.List;
import roomescape.domain.Theme;
import roomescape.repository.FakeThemeRepository;

public class FakeThemeRepositoryFixture {

    public static FakeThemeRepository create() {
        return new FakeThemeRepository(List.of(
                new Theme(1L, "우테코", "방탈출", "https://"),
                new Theme(2L, "우테코2", "방탈출2", "https://")
        ));
    }
}
