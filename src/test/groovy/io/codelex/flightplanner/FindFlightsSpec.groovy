package io.codelex.flightplanner

import io.codelex.flightplanner.model.SearchFlightsRequest
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Unroll

import java.time.LocalDate

import static io.codelex.flightplanner.fixture.AirportFixture.*
import static io.codelex.flightplanner.fixture.FlightsFixture.fromRigaToStockholm
import static org.springframework.http.HttpStatus.*

class FindFlightsSpec extends BaseSpecification {
    @Autowired
    AdminFlightApi adminFlightApi

    @Autowired
    CustomerFlightApi customerFlightApi

    static def baseDate = LocalDate.of(2019, 1, 1)

    def 'should get 200 response when no flights found'() {
        given:
            def req = new SearchFlightsRequest(
                    from: ARN.airport,
                    to: DXB.airport,
                    departureDate: baseDate
            )
        when:
            def response = customerFlightApi.searchFlights(req)
        then:
            response.statusCode == OK
        and:
            response.body.page == 0
            response.body.totalItems == 0
            response.body.items.empty
    }

    def 'GET on /api/flights/{id} should find flight and return 200 when flight is found'() {
        given:
            def rigaStockholm = adminFlightApi.addFlight(fromRigaToStockholm()).body
        when:
            def response = customerFlightApi.fetchFlight(rigaStockholm.id)
        then:
            response.statusCode == OK
        when:
            def flight = response.body
        then:
            rigaStockholm.id == flight.id
            rigaStockholm.from == flight.from
            rigaStockholm.to == flight.to
            rigaStockholm.carrier == flight.carrier
    }

    def 'GET on /api/flights/{id} should find flight and return 404 when flight is not found'() {
        expect:
            customerFlightApi.fetchFlight(666).statusCode == NOT_FOUND
    }

    @Unroll
    def 'POST on /api/flights/search should return 400 when passing empty values'() {
        expect:
            customerFlightApi.searchFlights(req, Void).statusCode == BAD_REQUEST
        where:
            req << [
                    new SearchFlightsRequest(),
                    new SearchFlightsRequest(
                            to: DXB,
                            departureDate: baseDate
                    ),
                    new SearchFlightsRequest(
                            from: RIX,
                            departureDate: baseDate
                    ),
                    new SearchFlightsRequest(
                            from: RIX,
                            to: DXB
                    )
            ]
    }

    @Unroll
    def 'POST on /api/flights/search should return 400 when from & to are same airports'() {
        given:
            def req = new SearchFlightsRequest(
                    from: from,
                    to: to,
                    departureDate: baseDate
            )
        expect:
            customerFlightApi.searchFlights(req, Void).statusCode == BAD_REQUEST
        where:
            from        | to
            RIX.airport | RIX.airport
            RIX.airport | 'rIx'
            RIX.airport | ' RIX'
            RIX.airport | 'RIX '
            RIX.airport | 'rix'
    }
}
