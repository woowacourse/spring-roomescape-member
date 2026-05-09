package roomescape.theme.controller;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StringToSortColumnConverter implements Converter<String, SortColumn> {
    @Override
    public SortColumn convert(@NonNull String source) {
        return SortColumn.fromString(source);
    }
}