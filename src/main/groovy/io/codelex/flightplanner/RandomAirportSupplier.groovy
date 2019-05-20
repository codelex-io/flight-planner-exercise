package io.codelex.flightplanner

import io.codelex.flightplanner.model.Airport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import java.util.stream.Collectors

@Component
class RandomAirportSupplier {
    static final Random random = new Random()

    @Autowired
    ResourceLoader resourceLoader

    List<Airport> airports

    @PostConstruct
    void init() {
        def lines = resourceLoader.getResource('classpath:airports.csv').inputStream.text.readLines()
        lines = lines.subList(1, lines.size())
        airports = lines.stream()
                .map { it -> new Airport(country: it.split(',')[5], city: it.split(',')[7], airport: it.split(',')[9]) }
                .filter { it -> !it.country.empty && !it.city.empty && !it.airport.empty }
                .filter { it -> it.airport != '0' }
                .collect(Collectors.toList())
    }

    Airport get() {
        def index = random.nextInt(airports.size())
        return airports.get(index)
    }
}
