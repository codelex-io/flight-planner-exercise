package io.codelex.flightplanner

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import java.util.stream.Collectors

@Component
class RandomAirlineSupplier {
    static final Random random = new Random()

    @Autowired
    ResourceLoader resourceLoader

    List<String> airlines

    @PostConstruct
    void init() {
        def lines = resourceLoader.getResource('classpath:airlines.csv').inputStream.text.readLines()
        lines = lines.subList(1, lines.size())
        airlines = lines.stream()
                .map { it -> it.split(',')[1].replaceAll('"', '') }
                .collect(Collectors.toList())
    }

    String get() {
        def index = random.nextInt(airlines.size())
        return airlines.get(index)
    }
}
