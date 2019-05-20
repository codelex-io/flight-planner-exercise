package io.codelex.flightplanner

import io.codelex.flightplanner.model.AddFlightRequest
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDate

import static io.codelex.flightplanner.fixture.AirportFixture.getARN
import static io.codelex.flightplanner.fixture.AirportFixture.getRIX
import static io.codelex.flightplanner.fixture.CarrierFixture.getRYANAIR
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

class DeletingFlightsSpec extends BaseSpecification {
    @Autowired
    AdminFlightApi adminFlightApi

    static def baseDateTime = LocalDate.of(2019, 1, 1).atStartOfDay()

    def 'DELETE on /admin-api/flights/{id} should return 200 when flight exists'() {
        given:
            def request = new AddFlightRequest(
                    from: RIX,
                    to: ARN,
                    carrier: RYANAIR,
                    departureTime: baseDateTime,
                    arrivalTime: baseDateTime.plusDays(1)
            )
            def flight = adminFlightApi.addFlight(request).body
        expect:
            adminFlightApi.deleteFlight(flight.id).statusCode == OK
        and:
            adminFlightApi.fetchFlight(flight.id).statusCode == NOT_FOUND
    }

    def 'DELETE on /admin-api/flights/{id} should return 200 when flight does not exist'() {
        expect:
            adminFlightApi.deleteFlight(666).statusCode == OK
    }
}
