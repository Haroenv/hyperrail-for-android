/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package be.hyperrail.android.irail.implementation;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;

import java.io.Serializable;

import be.hyperrail.android.irail.db.Station;

/**
 * A route between 2 stations, which might consist of multiple vehicles with transfers in between
 */
public class Route implements Serializable {

    private final Station departureStation;
    private final Station arrivalStation;
    private final DateTime departureTime;
    private final DateTime arrivalTime;

    private final Duration departureDelay;
    private final Duration arrivalDelay;

    private final String departurePlatform;
    private final boolean isDeparturePlatformNormal;
    private final String arrivalPlatform;
    private final boolean isArrivalDeparturePlatformNormal;

    private final TrainStub[] trains;
    private final Transfer[] transfers;

    Route(Station departureStation, Station arrivalStation, DateTime departureTime, Duration departureDelay, String departurePlatform, boolean isDeparturePlatformNormal, DateTime arrivalTime, Duration arrivalDelay, String arrivalPlatform, boolean isArrivalDeparturePlatformNormal, TrainStub[] trains, Transfer[] transfers) {
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;

        this.departureTime = departureTime;
        this.departureDelay = departureDelay;
        this.isDeparturePlatformNormal = isDeparturePlatformNormal;
        this.arrivalTime = arrivalTime;
        this.arrivalDelay = arrivalDelay;

        this.departurePlatform = departurePlatform;
        this.arrivalPlatform = arrivalPlatform;

        this.isArrivalDeparturePlatformNormal = isArrivalDeparturePlatformNormal;
        this.trains = trains;
        this.transfers = transfers;
    }

    public Duration getDuration() {
        return new Period(getDepartureTime(), getArrivalTime()).toStandardDuration();
    }

    public Duration getDurationIncludingDelays() {
        return new Period(getDepartureTime().plus(departureDelay), getArrivalTime().plus(arrivalDelay)).toStandardDuration();
    }

    public DateTime getDepartureTime() {
        return departureTime;
    }

    public DateTime getArrivalTime() {
        return arrivalTime;
    }

    public Transfer getOrigin() {
        return transfers[0];
    }

    public Transfer getDestination() {
        return transfers[transfers.length - 1];
    }

    public int getTransferCount() {
        // minus origin and destination
        return transfers.length - 2;
    }

    public int getStationCount() {
        return transfers.length;
    }

    public TrainStub[] getTrains() {
        return trains;
    }

    public Transfer[] getTransfers() {
        return transfers;
    }

    public Duration getArrivalDelay() {
        return arrivalDelay;
    }

    public Duration getDepartureDelay() {
        return departureDelay;
    }

    public String getDeparturePlatform() {
        return departurePlatform;
    }

    public String getArrivalPlatform() {
        return arrivalPlatform;
    }

    public boolean isArrivalDeparturePlatformNormal() {
        return isArrivalDeparturePlatformNormal;
    }

    public boolean isDeparturePlatformNormal() {
        return isDeparturePlatformNormal;
    }

    public Station getDepartureStation() {
        return departureStation;
    }

    public Station getArrivalStation() {
        return arrivalStation;
    }
}
