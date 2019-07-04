package io.codelex.flightplanner

import io.codelex.flightplanner.model.AddFlightRequest
import io.codelex.flightplanner.model.Airport
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Unroll

import java.time.LocalDate

import static io.codelex.flightplanner.fixture.AirportFixture.*
import static io.codelex.flightplanner.fixture.CarrierFixture.RYANAIR
import static org.springframework.http.HttpStatus.*

class AddingFlightsSpec extends BaseSpecification {
    @Autowired
    AdminFlightApi adminFlightApi

    static def baseDateTime = LocalDate.of(2019, 1, 1).atStartOfDay()

    def 'PUT on /admin-api/flights should return 201 and flight with added id'() {
        given:
            def req = new AddFlightRequest(
                    from: RIX,
                    to: ARN,
                    carrier: RYANAIR,
                    departureTime: baseDateTime,
                    arrivalTime: baseDateTime.plusDays(1)
            )
        when:
            def response = adminFlightApi.addFlight(req)
        then:
            response.statusCode == CREATED
        when:
            def flight = response.body
        then:
            flight.id != null
            flight.from == req.from
            flight.to == req.to
            flight.carrier == req.carrier
            flight.departureTime.isEqual(req.departureTime)
            flight.arrivalTime.isEqual(req.arrivalTime)
    }

    def 'PUT on /admin-api/flights should return different id for new flight'() {
        given:
            def req = new AddFlightRequest(
                    from: RIX,
                    to: ARN,
                    carrier: RYANAIR,
                    departureTime: baseDateTime,
                    arrivalTime: baseDateTime.plusDays(1)
            )
        when:
            def firstFlight = adminFlightApi.addFlight(req).body
            def secondFlight = adminFlightApi.addFlight(req.tap { it.from = DME }).body
        then:
            firstFlight.id != secondFlight.id
    }

    def 'PUT on /admin-api/flights should return 409 when adding duplicated flight'() {
        given:
            def req = new AddFlightRequest(
                    from: RIX,
                    to: ARN,
                    carrier: RYANAIR,
                    departureTime: baseDateTime,
                    arrivalTime: baseDateTime.plusDays(1)
            )
        expect:
            adminFlightApi.addFlight(req).statusCode == CREATED
            adminFlightApi.addFlight(req, Void).statusCode == CONFLICT
    }

    @Unroll
    def 'PUT on /admin-api/flights should return 400 when passing wrong values'() {
        expect:
            adminFlightApi.addFlight(req, Void).statusCode == BAD_REQUEST
        where:
            req << [
                    new AddFlightRequest(),
                    new AddFlightRequest(
                            from: null,
                            to: DXB,
                            carrier: RYANAIR,
                            departureTime: baseDateTime,
                            arrivalTime: baseDateTime.plusDays(1)
                    ),
                    new AddFlightRequest(
                            from: RIX,
                            to: null,
                            carrier: RYANAIR,
                            departureTime: baseDateTime,
                            arrivalTime: baseDateTime.plusDays(1)
                    ),
                    new AddFlightRequest(
                            from: RIX,
                            to: DXB,
                            carrier: null,
                            departureTime: baseDateTime,
                            arrivalTime: baseDateTime.plusDays(1)
                    ),
                    new AddFlightRequest(
                            from: RIX,
                            to: DXB,
                            carrier: RYANAIR,
                            departureTime: null,
                            arrivalTime: baseDateTime.plusDays(1)
                    ),
                    new AddFlightRequest(
                            from: RIX,
                            to: DXB,
                            carrier: RYANAIR,
                            departureTime: baseDateTime,
                            arrivalTime: null
                    ),
                    new AddFlightRequest(
                            from: RIX,
                            to: DXB,
                            carrier: '',
                            departureTime: baseDateTime,
                            arrivalTime: baseDateTime.plusDays(1)
                    ),
                    new AddFlightRequest(
                            from: new Airport(),
                            to: DXB,
                            carrier: RYANAIR,
                            departureTime: baseDateTime,
                            arrivalTime: baseDateTime.plusDays(1)
                    ),
                    new AddFlightRequest(
                            from: RIX,
                            to: new Airport(),
                            carrier: RYANAIR,
                            departureTime: baseDateTime,
                            arrivalTime: baseDateTime.plusDays(1)
                    ),
                    new AddFlightRequest(
                            from: new Airport(country: '', city: '', airport: ''),
                            to: DXB,
                            carrier: RYANAIR,
                            departureTime: baseDateTime,
                            arrivalTime: baseDateTime.plusDays(1)
                    ),
                    new AddFlightRequest(
                            from: RIX,
                            to: new Airport(country: '', city: '', airport: ''),
                            carrier: RYANAIR,
                            departureTime: baseDateTime,
                            arrivalTime: baseDateTime.plusDays(1)
                    )
            ]
    }

    @Unroll
    def 'PUT on /admin-api/flights should return 400 when from & to are the same airports'() {
        expect:
            adminFlightApi.addFlight(req, Void).statusCode == BAD_REQUEST
        where:
            req << [
                    new AddFlightRequest(
                            from: DXB,
                            to: DXB,
                            carrier: RYANAIR,
                            departureTime: baseDateTime,
                            arrivalTime: baseDateTime.plusDays(1)
                    ),
                    new AddFlightRequest(
                            from: DXB,
                            to: new Airport(country: 'united arab emirates', city: 'dubai', airport: 'dxb'),
                            carrier: RYANAIR,
                            departureTime: baseDateTime,
                            arrivalTime: baseDateTime.plusDays(1)
                    ),
                    new AddFlightRequest(
                            from: DXB,
                            to: new Airport(country: 'United Arab Emirates', city: 'Dubai', airport: 'DXB '),
                            carrier: RYANAIR,
                            departureTime: baseDateTime,
                            arrivalTime: baseDateTime.plusDays(1)
                    )
            ]
    }

    @Unroll
    def 'PUT on /admin-api/flights should return 400 when strange times are added'() {
        expect:
            adminFlightApi.addFlight(req, Void).statusCode == BAD_REQUEST
        where:
            req << [
                    new AddFlightRequest(
                            from: RIX,
                            to: DXB,
                            carrier: RYANAIR,
                            departureTime: baseDateTime,
                            arrivalTime: baseDateTime.minusDays(100)
                    ),
                    new AddFlightRequest(
                            from: RIX,
                            to: DXB,
                            carrier: RYANAIR,
                            departureTime: baseDateTime,
                            arrivalTime: baseDateTime
                    )
            ]
    }
}
