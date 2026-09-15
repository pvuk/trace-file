package com.trace.file.config.converters;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions.StoreConversions;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;

@Configuration
public class R2dbcConvertersConfig {

    static class ByteBufferToBytesConverter implements Converter<ByteBuffer, byte[]> {
        @Override
        public byte[] convert(ByteBuffer source) {
            byte[] bytes = new byte[source.remaining()];
            source.get(bytes);
            return bytes;
        }
    }

    static class BytesToByteBufferConverter implements Converter<byte[], ByteBuffer> {
        @Override
        public ByteBuffer convert(byte[] source) {
            return ByteBuffer.wrap(source);
        }
    }

    @Bean
    public R2dbcCustomConversions r2dbcCustomByteConversions() {
        List<Converter<?, ?>> converters = Arrays.asList(
                new ByteBufferToBytesConverter(),
                new BytesToByteBufferConverter()
        );
        return new R2dbcCustomConversions(StoreConversions.NONE, converters);
    }
    
    static class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {
        @Override
        public Date convert(LocalDateTime source) {
            return Date.from(source.atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    static class DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {
        @Override
        public LocalDateTime convert(Date source) {
            return source.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
    }

    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Converter<?, ?>> converters = Arrays.asList(
                new LocalDateTimeToDateConverter(),
                new DateToLocalDateTimeConverter()
        );
        return new R2dbcCustomConversions(StoreConversions.NONE, converters);
    }
}
