package io.codelex.flightplanner.model

import java.time.LocalDateTime

class AddFlightRequest {
    Airport from
    Airport to
    String carrier
    LocalDateTime departureTime
    LocalDateTime arrivalTime
}
