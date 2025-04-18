package dev.book.accountbook.type;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToFrequencyConverter implements Converter<String, Frequency> {
    @Override
    public Frequency convert(String source) {
        return Frequency.valueOf(source.toUpperCase());
    }
}
