package roomescape.theme.controller;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import roomescape.theme.domain.SortOrder;

@Component
public class StringToSortOrderConverter implements Converter<String, SortOrder> {

    @Override
    public SortOrder convert(@NonNull String source) {
        return SortOrder.fromString(source);
    }
}
