package io.codelex.flightplanner

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile('!test')
@Component
class FlightGenerator implements CommandLineRunner {
    @Autowired
    AdminFlightApi adminFlightApi

    @Autowired
    RandomAddFlightRequestGenerator addFlightRequestGenerator

    @Override
    void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in)
        println 'Enter amount of flights you would wish to add:'
        long amountOfFlights = scanner.nextLong()

        amountOfFlights.times {
            def request = addFlightRequestGenerator.randomRequest()
            adminFlightApi.addFlight(request)
            if (it % 100 == 0) {
                println "${amountOfFlights - it} left..."
            }
        }

        println 'Done!'
    }
}
