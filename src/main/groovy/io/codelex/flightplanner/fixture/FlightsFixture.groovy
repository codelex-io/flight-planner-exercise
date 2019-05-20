package io.codelex.flightplanner.fixture

import io.codelex.flightplanner.model.AddFlightRequest

import java.time.LocalDate

import static io.codelex.flightplanner.fixture.AirportFixture.*
import static io.codelex.flightplanner.fixture.CarrierFixture.RYANAIR
import static io.codelex.flightplanner.fixture.CarrierFixture.TURKISH_AIRLINES

class FlightsFixture {
    static final def baseDateTime = LocalDate.of(2019, 1, 1).atStartOfDay()

    static AddFlightRequest fromRigaToStockholm() {
        return new AddFlightRequest(
                from: RIX,
                to: ARN,
                carrier: RYANAIR,
                departureTime: baseDateTime,
                arrivalTime: baseDateTime.plusDays(1)
        )
    }

    static AddFlightRequest fromMoscowToDubai() {
        return new AddFlightRequest(
                from: DME,
                to: DXB,
                carrier: TURKISH_AIRLINES,
                departureTime: baseDateTime,
                arrivalTime: baseDateTime.plusDays(1)
        )
    }
}
