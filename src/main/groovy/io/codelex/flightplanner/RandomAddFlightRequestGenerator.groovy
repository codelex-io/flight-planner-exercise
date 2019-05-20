package io.codelex.flightplanner

import io.codelex.flightplanner.model.AddFlightRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.time.LocalDateTime

@Component
class RandomAddFlightRequestGenerator {
    static final Random random = new Random()

    @Autowired
    RandomAirportSupplier airportSupplier

    @Autowired
    RandomAirlineSupplier airlineSupplier

    AddFlightRequest randomRequest() {
        def departureTime = LocalDateTime.of(
                2019 + random.nextInt(10),
                1 + random.nextInt(12),
                1 + random.nextInt(27),
                random.nextInt(24),
                random.nextInt(60)
        )

        def from = airportSupplier.get()
        def to = airportSupplier.get()

        while (from.airport == to.airport) {
            to = airportSupplier.get()
        }

        return new AddFlightRequest(
                from: from,
                to: to,
                carrier: airlineSupplier.get(),
                departureTime: departureTime,
                arrivalTime: departureTime.plusDays(random.nextInt(3)).plusHours(1 + random.nextInt(24))
        )
    }
}
