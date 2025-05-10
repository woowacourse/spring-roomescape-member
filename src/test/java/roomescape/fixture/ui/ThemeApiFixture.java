package roomescape.fixture.ui;

import java.util.HashMap;
import java.util.Map;

public class ThemeApiFixture {

    public static Map<String, String> themeParams() {
        final Map<String, String> params = new HashMap<>();
        params.put("name", "우가우가");
        params.put("description", "우가우가 설명");
        params.put("thumbnail", "따봉우가.jpg");

        return params;
    }
}
