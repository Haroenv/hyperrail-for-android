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

import java.io.Serializable;

import be.hyperrail.android.irail.db.Station;

/**
 * A trainstop, belonging to a certain train.
 * A trainstrop can either be shown in a liveboard (grouped by station) or a train (grouped by vehicle)
 */
public class TrainStop implements Serializable {

    private final TrainStub train;
    private final Station destination;
    private final Station station;

    private final boolean isPlatformNormal;
    private final DateTime departureTime;
    private final String platform;
    private boolean hasLeft;
    private final Duration departureDelay;
    private final boolean departureCanceled;

    private DateTime arrivalTime;
    private Duration arrivalDelay;
    private boolean arrivalCanceled;

    protected TrainStop(Station station, Station destination, TrainStub train, String platform, boolean isPlatformNormal, DateTime departureTime, DateTime arrivalTime, Duration departureDelay, Duration arrivalDelay, boolean departureCanceled, boolean arrivalCanceled, boolean hasLeft) {
        this(station, destination, train, platform, isPlatformNormal, departureTime, departureDelay, departureCanceled, hasLeft);

        this.arrivalTime = arrivalTime;
        this.arrivalDelay = arrivalDelay;
        this.arrivalCanceled = arrivalCanceled;
    }

    protected TrainStop(Station station, Station destination, TrainStub train, String platform, boolean isPlatformNormal, DateTime departureTime, Duration departureDelay, boolean departureCanceled, boolean hasLeft) {
        this.station = station;
        this.destination = destination;
        this.isPlatformNormal = isPlatformNormal;
        this.departureTime = departureTime;
        this.platform = platform;
        this.departureDelay = departureDelay;
        this.departureCanceled = departureCanceled;
        this.arrivalCanceled = departureCanceled;
        this.train = train;
        this.hasLeft = hasLeft;
    }

    public TrainStub getTrain() {
        return train;
    }

    public boolean hasLeft() {
        return hasLeft;
    }

    public boolean isPlatformNormal() {
        return isPlatformNormal;
    }

    public DateTime getDepartureTime() {
        return departureTime;
    }

    public String getPlatform() {
        return platform;
    }

    public Duration getDepartureDelay() {
        return departureDelay;
    }

    public DateTime getDelayedDepartureTime() {
        return departureTime.plus(departureDelay);
    }

    public boolean isDepartureCanceled() {
        return departureCanceled;
    }

    public Station getDestination() {
        return destination;
    }

    public Station getStation() {
        return station;
    }

    public boolean isArrivalCanceled() {
        return arrivalCanceled;
    }

    public Duration getArrivalDelay() {
        return arrivalDelay;
    }

    public DateTime getArrivalTime() {
        return arrivalTime;
    }

    public DateTime getDelayedArrivalTime() {
        return arrivalTime.plus(arrivalDelay);
    }

    public void setHasLeft(boolean hasLeft) {
        this.hasLeft = hasLeft;
    }
}

