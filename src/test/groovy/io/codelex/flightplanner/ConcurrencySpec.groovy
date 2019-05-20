package io.codelex.flightplanner

import groovy.util.logging.Slf4j
import io.codelex.flightplanner.model.SearchFlightsRequest
import org.springframework.beans.factory.annotation.Autowired
import spock.util.concurrent.AsyncConditions

@Slf4j
class ConcurrencySpec extends BaseSpecification {
    @Autowired
    AdminFlightApi adminFlightApi

    @Autowired
    CustomerFlightApi customerFlightApi

    @Autowired
    RandomAddFlightRequestGenerator addFlightRequestGenerator

    static final int SIXTY_SECONDS = 60

    def 'should handle concurrent adding & deleting'() {
        given:
            def async = new AsyncConditions(100)
        when:
            100.times {
                Thread.start {
                    async.evaluate {
                        def request = addFlightRequestGenerator.randomRequest()
                        def response = adminFlightApi.addFlight(request)
                        log.info("Received status code: ${response.statusCode}")
                        if (response.statusCode.is2xxSuccessful()) {
                            adminFlightApi.deleteFlight(response.body.id)
                        }
                    }
                }
            }
        then:
            async.await(SIXTY_SECONDS)
    }

    def 'should not be able to add same flight twice'() {
        given:
            def async = new AsyncConditions(100)
            def addFlightRequest = addFlightRequestGenerator.randomRequest()
        when:
            100.times {
                Thread.start {
                    async.evaluate {
                        try {
                            adminFlightApi.addFlight(addFlightRequest)
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        then:
            async.await(SIXTY_SECONDS)
        when:
            def flights = customerFlightApi.searchFlights(new SearchFlightsRequest(
                    from: addFlightRequest.from.airport,
                    to: addFlightRequest.to.airport,
                    departureDate: addFlightRequest.departureTime.toLocalDate()
            )).body
        then:
            flights.totalItems == 1
    }
}
