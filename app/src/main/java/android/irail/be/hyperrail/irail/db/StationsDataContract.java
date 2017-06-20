/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package android.irail.be.hyperrail.irail.db;

import android.provider.BaseColumns;

import static android.irail.be.hyperrail.irail.db.StationsDataContract.StationsDataColumns.COLUMN_NAME_ALTERNATIVE_DE;
import static android.irail.be.hyperrail.irail.db.StationsDataContract.StationsDataColumns.COLUMN_NAME_ALTERNATIVE_EN;
import static android.irail.be.hyperrail.irail.db.StationsDataContract.StationsDataColumns.COLUMN_NAME_ALTERNATIVE_FR;
import static android.irail.be.hyperrail.irail.db.StationsDataContract.StationsDataColumns.COLUMN_NAME_ALTERNATIVE_NL;
import static android.irail.be.hyperrail.irail.db.StationsDataContract.StationsDataColumns.COLUMN_NAME_AVG_STOP_TIMES;
import static android.irail.be.hyperrail.irail.db.StationsDataContract.StationsDataColumns.COLUMN_NAME_COUNTRY_CODE;
import static android.irail.be.hyperrail.irail.db.StationsDataContract.StationsDataColumns.COLUMN_NAME_LATITUDE;
import static android.irail.be.hyperrail.irail.db.StationsDataContract.StationsDataColumns.COLUMN_NAME_LONGITUDE;
import static android.irail.be.hyperrail.irail.db.StationsDataContract.StationsDataColumns.COLUMN_NAME_NAME;
import static android.irail.be.hyperrail.irail.db.StationsDataContract.StationsDataColumns.TABLE_NAME;
import static android.irail.be.hyperrail.irail.db.StationsDataContract.StationsDataColumns._ID;

/**
 * Created by Bert on 19-1-2017.
 */

public class StationsDataContract {

    private StationsDataContract() {
        // don't instantiate
    }

    public static final class StationsDataColumns implements BaseColumns {
        public static final String TABLE_NAME = "stations";
        public static final String _ID = "station_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_ALTERNATIVE_NL = "alternative_nl";
        public static final String COLUMN_NAME_ALTERNATIVE_FR = "alternative_fr";
        public static final String COLUMN_NAME_ALTERNATIVE_DE = "alternative_de";
        public static final String COLUMN_NAME_ALTERNATIVE_EN = "alternative_en";
        public static final String COLUMN_NAME_COUNTRY_CODE = "country_code";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_AVG_STOP_TIMES = "avg_stop_times";
    }

    /**
     * Create table + index for lookup by name. No index for sort since little performance gains.
     */
    static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " TEXT PRIMARY KEY," +
            COLUMN_NAME_NAME + " TEXT COLLATE NOCASE," +
            COLUMN_NAME_ALTERNATIVE_NL + " TEXT COLLATE NOCASE," +
            COLUMN_NAME_ALTERNATIVE_FR + " TEXT COLLATE NOCASE," +
            COLUMN_NAME_ALTERNATIVE_DE + " TEXT COLLATE NOCASE," +
            COLUMN_NAME_ALTERNATIVE_EN + " TEXT COLLATE NOCASE," +
            COLUMN_NAME_COUNTRY_CODE + " TEXT," +
            COLUMN_NAME_LONGITUDE + " REAL," +
            COLUMN_NAME_LATITUDE + " REAL," +
            COLUMN_NAME_AVG_STOP_TIMES + " REAL); " +
            "CREATE INDEX index_name ON " + TABLE_NAME + " (" + COLUMN_NAME_NAME + ");";

    static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

}