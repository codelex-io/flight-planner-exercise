package io.codelex.flightplanner

import io.codelex.flightplanner.model.Flight
import io.codelex.flightplanner.model.PageResult
import io.codelex.flightplanner.model.SearchFlightsRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

import static io.codelex.flightplanner.model.Flight.PAGE_TYPE
import static org.springframework.http.HttpEntity.EMPTY
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.POST

@Component
class CustomerFlightApi {
    @Autowired
    TestRestTemplate publicApiTemplate

    ResponseEntity<PageResult<Flight>> searchFlights(SearchFlightsRequest req, def responseType = PAGE_TYPE) {
        return publicApiTemplate.exchange('/flights/search', POST, new HttpEntity<>(req), responseType)
    }

    ResponseEntity<Flight> fetchFlight(Long id, Class responseType = Flight) {
        return publicApiTemplate.exchange('/flights/' + id, GET, EMPTY, responseType)
    }
}
