package roomescape.theme.controller;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import roomescape.theme.domain.SortType;

@Component
public class StringToSortColumnConverter implements Converter<String, SortType> {
    @Override
    public SortType convert(@NonNull String source) {
        return SortType.fromString(source);
    }
}