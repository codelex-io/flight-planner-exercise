package io.codelex.flightplanner

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Component

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS

@Component
class RestTemplateFactory {
    static final ObjectMapper mapper = new ObjectMapper()

    static {
        def javaTimeModule = new JavaTimeModule()
        javaTimeModule.addDeserializer(
                LocalDateTime,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        )
        javaTimeModule.addDeserializer(
                LocalDate,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        )
        javaTimeModule.addSerializer(
                LocalDateTime,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        )
        javaTimeModule.addSerializer(
                LocalDate,
                new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        )

        mapper.registerModule(javaTimeModule)
        mapper.disable(WRITE_DATES_AS_TIMESTAMPS)
    }

    @Value('${flight-planner-app.api-url}')
    String apiUrl

    TestRestTemplate create(String path) {
        return new TestRestTemplate(restTemplateBuilder(path))
    }

    private RestTemplateBuilder restTemplateBuilder(String path) {
        return new RestTemplateBuilder()
                .rootUri(apiUrl + path)
                .messageConverters([new MappingJackson2HttpMessageConverter(mapper)])
    }
}
