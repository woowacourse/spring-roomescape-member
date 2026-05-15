package roomescape.global.converter;

import org.springframework.core.convert.converter.Converter;
import roomescape.domain.vo.ReservationDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringToReservationDateConverter implements Converter<String, ReservationDate> {
    @Override
    public ReservationDate convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }

        return new ReservationDate(LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}
