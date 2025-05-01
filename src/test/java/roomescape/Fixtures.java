package roomescape;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.model.Theme;
import roomescape.model.TimeSlot;

public class Fixtures {

    // TODO: id 명시
    public static final TimeSlot JUNK_TIME_SLOT = new TimeSlot(1L, LocalTime.of(10, 0));
    public static final Theme JUNK_THEME = new Theme(1L, "레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

    public static LocalDate ofYesterday() {
        return LocalDate.now().minusDays(1);
    }

    public static LocalDate ofToday() {
        return LocalDate.now();
    }

    public static LocalDate ofTomorrow() {
        return LocalDate.now().plusDays(1);
    }
}
