package io.codelex.flightplanner.fixture

import io.codelex.flightplanner.model.Airport

class AirportFixture {
    static final Airport RIX = new Airport(country: 'Latvia', city: 'Riga', airport: 'RIX')
    static final Airport DME = new Airport(country: 'Russia', city: 'Moscow', airport: 'DME')
    static final Airport DXB = new Airport(country: 'United Arab Emirates', city: 'Dubai', airport: 'DXB')
    static final Airport ARN = new Airport(country: 'Sweden', city: 'Stockholm', airport: 'ARN')
}
