package roomescape;

import java.util.List;
import roomescape.controller.response.ThemeResponse;

public class TestFixtures {
    public static ThemeResponse theme1 = new ThemeResponse(1L, "name1", "description1", "thumbnail1");
    public static ThemeResponse theme2 = new ThemeResponse(2L, "name2", "description2", "thumbnail2");
    public static ThemeResponse theme3 = new ThemeResponse(3L, "name3", "description3", "thumbnail3");
    public static ThemeResponse theme4 = new ThemeResponse(4L, "name4", "description4", "thumbnail4");
    public static ThemeResponse theme5 = new ThemeResponse(5L, "name5", "description5", "thumbnail5");
    public static ThemeResponse theme6 = new ThemeResponse(6L, "name6", "description6", "thumbnail6");
    public static ThemeResponse theme7 = new ThemeResponse(7L, "name7", "description7", "thumbnail7");
    public static ThemeResponse theme8 = new ThemeResponse(8L, "name8", "description8", "thumbnail8");
    public static ThemeResponse theme9 = new ThemeResponse(9L, "name9", "description9", "thumbnail9");
    public static ThemeResponse theme10 = new ThemeResponse(10L, "name10", "description10", "thumbnail10");
    public static ThemeResponse theme11 = new ThemeResponse(11L, "name11", "description11", "thumbnail11");
    public static List<ThemeResponse> themeResponses1 = List.of(theme1, theme2, theme3, theme4, theme5, theme6, theme7, theme8, theme9);
    public static List<ThemeResponse> themeResponses2 = List.of(theme1, theme2, theme3, theme4, theme5, theme6, theme7, theme8, theme9, theme10);
    public static List<ThemeResponse> themeResponses3 = List.of(theme1, theme2, theme3, theme11, theme4, theme5, theme6, theme7, theme8, theme9);
}
