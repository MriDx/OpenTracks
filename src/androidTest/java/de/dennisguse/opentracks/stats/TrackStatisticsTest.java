/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.dennisguse.opentracks.stats;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link TrackStatistics}.
 * This only tests non-trivial pieces of that class.
 *
 * @author Rodrigo Damazio
 */
@RunWith(JUnit4.class)
public class TrackStatisticsTest {

    private TrackStatistics statistics;

    @Before
    public void setUp() {
        statistics = new TrackStatistics();
    }

    @Test
    public void testMerge() {
        TrackStatistics statistics2 = new TrackStatistics();
        statistics.setStartTime_ms(1000L);  // Resulting start time
        statistics.setStopTime_ms(2500L);
        statistics2.setStartTime_ms(3000L);
        statistics2.setStopTime_ms(4000L);  // Resulting stop time
        statistics.setTotalTime(1500L);
        statistics2.setTotalTime(1000L);  // Result: 1500+1000
        statistics.setMovingTime(700L);
        statistics2.setMovingTime(600L);  // Result: 700+600
        statistics.setTotalDistance(750.0);
        statistics2.setTotalDistance(350.0);  // Result: 750+350
        statistics.setTotalElevationGain(50.0f);
        statistics2.setTotalElevationGain(850.0f);  // Result: 850+50
        statistics.setMaxSpeed(60.0);  // Resulting max speed
        statistics2.setMaxSpeed(30.0);
        statistics.setMaxElevation(1250.0);
        statistics.setMinElevation(1200.0);  // Resulting min elevation
        statistics2.setMaxElevation(3575.0);  // Resulting max elevation
        statistics2.setMinElevation(2800.0);

        statistics.merge(statistics2);

        assertEquals(1000L, statistics.getStartTime_ms());
        assertEquals(4000L, statistics.getStopTime_ms());
        assertEquals(2500L, statistics.getTotalTime());
        assertEquals(1300L, statistics.getMovingTime());
        assertEquals(1100.0, statistics.getTotalDistance(), 0.001);
        assertEquals(900.0, statistics.getTotalElevationGain(), 0.001);
        assertEquals(statistics.getTotalDistance() / (statistics.getMovingTime() / 1000.0), statistics.getMaxSpeed(), 0.001);
        assertEquals(1200.0, statistics.getMinElevation(), 0.001);
        assertEquals(3575.0, statistics.getMaxElevation(), 0.001);
    }

    @Test
    public void testGetAverageSpeed() {
        statistics.setTotalDistance(1000.0);
        statistics.setTotalTime(50000);  // in milliseconds
        assertEquals(20.0, statistics.getAverageSpeed(), 0.001);
    }

    @Test
    public void testGetAverageMovingSpeed() {
        statistics.setTotalDistance(1000.0);
        statistics.setMovingTime(20000);  // in milliseconds
        assertEquals(50.0, statistics.getAverageMovingSpeed(), 0.001);
    }
}
