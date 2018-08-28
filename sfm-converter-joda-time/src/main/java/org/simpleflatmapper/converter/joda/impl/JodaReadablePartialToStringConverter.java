package org.simpleflatmapper.converter.joda.impl;

import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormatter;
import org.simpleflatmapper.converter.Context;
import org.simpleflatmapper.converter.Converter;


public class JodaReadablePartialToStringConverter implements Converter<ReadablePartial, String> {

    private final DateTimeFormatter dateTimeFormatter;

    public JodaReadablePartialToStringConverter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @Override
    public String convert(ReadablePartial in, Context context) throws Exception {
        return dateTimeFormatter.print(in);
    }
}
