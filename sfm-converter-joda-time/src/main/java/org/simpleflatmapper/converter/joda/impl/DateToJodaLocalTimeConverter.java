package org.simpleflatmapper.converter.joda.impl;

import org.joda.time.LocalTime;
import org.simpleflatmapper.converter.Context;
import org.simpleflatmapper.converter.Converter;

import java.util.Date;


public class DateToJodaLocalTimeConverter implements Converter<Date, LocalTime> {

    @Override
    public LocalTime convert(Date in, Context context) throws Exception {
        if (in == null) return null;
        return LocalTime.fromDateFields(in);
    }
}
