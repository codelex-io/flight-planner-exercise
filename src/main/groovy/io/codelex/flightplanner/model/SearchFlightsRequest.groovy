package io.codelex.flightplanner.model

import java.time.LocalDate

class SearchFlightsRequest {
    String from
    String to
    LocalDate departureDate
}
