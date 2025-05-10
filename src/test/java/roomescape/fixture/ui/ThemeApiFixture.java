package roomescape.fixture.ui;

import java.util.HashMap;
import java.util.Map;

public class ThemeApiFixture {

    public static Map<String, String> themeParams1() {
        final Map<String, String> params = new HashMap<>();
        params.put("name", "우가의 레이어드 아키텍처");
        params.put("description", "우가우가 설명");
        params.put("thumbnail", "따봉우가.jpg");

        return params;
    }

    public static Map<String, String> themeParams2() {
        final Map<String, String> params = new HashMap<>();
        params.put("name", "헤일러의 디버깅 교실");
        params.put("description", "bug입니다.");
        params.put("thumbnail", "좋아용.jpg");

        return params;
    }
}
