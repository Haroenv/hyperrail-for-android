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

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import be.hyperrail.android.irail.contracts.IrailDataProvider;
import be.hyperrail.android.irail.contracts.IrailDataResponse;
import be.hyperrail.android.irail.db.Station;
import be.hyperrail.android.irail.factories.IrailFactory;

/**
 * Train information, except its stops.
 * This data is typically present in the API without requiring a second API call.
 */
public class TrainStub implements Serializable {

    protected final String id;
    private final Station direction;

    // Direction is required, since we need to display something
    TrainStub(String id, Station direction) {
        this.id = id;
        this.direction = direction;
    }

    public String getId() {
        return id;
    }

    public Station getDirection() {
        return direction;
    }

    public IrailDataResponse<Train> getTrain(DateTime day) {
        IrailDataProvider api = IrailFactory.getDataProviderInstance();
        return api.getTrain(id, day);
    }

    public String getName() {
        return getType() + " " + getNumber();
    }

    private String getReducedId() {
        return this.id.substring(8);
    }

    public String getType() {
        String rid = getReducedId();
        // S trains are special
        if (rid.startsWith("S")) {
            return rid.substring(0, rid.length() - 4);
        }

        String pattern = "(\\w+?)(\\d+)";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(rid);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }

    public String getNumber() {
        String rid = getReducedId();
        // S trains are special
        if (rid.startsWith("S")) {
            return rid.substring(rid.length() - 4);
        }

        String pattern = "(\\w+?)(\\d+)";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(rid);
        if (m.find()) {
            return m.group(2);
        }
        return "";
    }
}
