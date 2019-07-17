package io.codelex.flightplanner

import io.codelex.flightplanner.model.AddFlightRequest
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Unroll

import java.time.LocalDate

import static io.codelex.flightplanner.fixture.AirportFixture.*
import static io.codelex.flightplanner.fixture.CarrierFixture.RYANAIR
import static org.springframework.http.HttpStatus.OK

class AirportTypeaheadSpec extends BaseSpecification {
    @Autowired
    AdminFlightApi adminFlightApi

    @Autowired
    CustomerAirportApi customerAirportApi

    static def baseDateTime = LocalDate.of(2019, 1, 1).atStartOfDay()

    @Unroll
    def 'GET on /api/airports should return 200 and matched airports'() {
        given:
            adminFlightApi.addFlight(new AddFlightRequest(
                    from: RIX,
                    to: ARN,
                    carrier: RYANAIR,
                    departureTime: baseDateTime,
                    arrivalTime: baseDateTime.plusDays(1)
            ))
            adminFlightApi.addFlight(new AddFlightRequest(
                    from: DXB,
                    to: ARN,
                    carrier: RYANAIR,
                    departureTime: baseDateTime,
                    arrivalTime: baseDateTime.plusDays(1)
            ))
            adminFlightApi.addFlight(new AddFlightRequest(
                    from: DME,
                    to: ARN,
                    carrier: RYANAIR,
                    departureTime: baseDateTime,
                    arrivalTime: baseDateTime.plusDays(1)
            ))
        when:
            def response = customerAirportApi.search(search)
        then:
            response.statusCode == OK
        and:
            response.body.size() == 1
            response.body.first() == RIX
        where:
            search << [
                    'RIX',
                    'rix',
                    ' RIx',
                    'RI ',
                    'Rig',
                    'Latv',
                    'Latvia',
                    'Riga'
            ]
    }
}
