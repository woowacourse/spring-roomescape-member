package roomescape.theme.controller;

import org.springframework.core.convert.converter.Converter; // ⚠️ 스프링 패키지 확인!
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StringToSortOrderConverter implements Converter<String, SortOrder> {

    @Override
    public SortOrder convert(@NonNull String source) {
        return SortOrder.fromString(source);
    }
}