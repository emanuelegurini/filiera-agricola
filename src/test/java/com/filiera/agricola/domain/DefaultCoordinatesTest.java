package com.filiera.agricola.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCoordinatesTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create coordinates with valid latitude and longitude")
        void shouldCreateCoordinatesWithValidValues() {
            // Given
            Float latitude = 45.0f;
            Float longitude = 12.0f;

            // When
            DefaultCoordinates coordinates = new DefaultCoordinates(latitude, longitude);

            // Then
            assertNotNull(coordinates);
            assertEquals(latitude, coordinates.getLat());
            assertEquals(longitude, coordinates.getLng());
        }
    }
}