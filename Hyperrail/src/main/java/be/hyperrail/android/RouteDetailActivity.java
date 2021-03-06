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

package be.hyperrail.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.joda.time.DateTime;

import java.text.DateFormat;

import be.hyperrail.android.adapter.RouteDetailCardAdapter;
import be.hyperrail.android.adapter.onRecyclerItemClickListener;
import be.hyperrail.android.irail.db.Station;
import be.hyperrail.android.irail.implementation.Route;
import be.hyperrail.android.irail.implementation.TrainStub;
import be.hyperrail.android.irail.implementation.Transfer;

/**
 * Activity to show one specific route
 */
public class RouteDetailActivity extends RecyclerViewActivity<Route> {

    /**
     * The route to show
     */
    private Route route;

    public static Intent createIntent(Context c, Route r) {
        Intent i = new Intent(c, RouteDetailActivity.class);
        i.putExtra("route", r);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        route = (Route) getIntent().getSerializableExtra("route");
        this.mShowDividers = false;

        super.onCreate(savedInstanceState);

        setTitle(route.getDepartureStation().getLocalizedName() + " - " + route.getArrivalStation().getLocalizedName());

        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);

        setSubTitle(df.format(route.getDepartureTime().toString()));

        // disable pull-to-refresh
        // TODO: support refreshing
        this.vRefreshLayout.setEnabled(false);

        this.vWarningNotRealtime.setVisibility(View.GONE);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_route_detail;
    }

    @Override
    protected int getMenuLayout() {
        // TODO: include notification options
        return R.menu.actionbar_main;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        RouteDetailCardAdapter adapter = new RouteDetailCardAdapter(this.getApplicationContext(), route, false);

        // Launch intents to view details / click through
        adapter.setOnItemClickListener(new onRecyclerItemClickListener<Object>() {
            @Override
            public void onRecyclerItemClick(RecyclerView.Adapter sender, Object object) {
                Intent i = null;
                if (object instanceof Bundle) {
                    i = TrainActivity.createIntent(RouteDetailActivity.this,
                            (TrainStub) ((Bundle) object).getSerializable("train"),
                            (Station) ((Bundle) object).getSerializable("from"),
                            (Station) ((Bundle) object).getSerializable("to"),
                            (DateTime) ((Bundle) object).getSerializable("date"));

                } else if (object instanceof Transfer) {
                    i = LiveboardActivity.createIntent(RouteDetailActivity.this, ((Transfer) object).getStation());
                }
                startActivity(i);
            }
        });
        return adapter;
    }

    @Override
    protected void getData() {
        // Not supported
    }

    @Override
    protected void getInitialData() {
       route = (Route) getIntent().getSerializableExtra("route");
    }

    @Override
    protected void getNextData() {
        // Not supported
    }

    @Override
    protected void showData(Route data) {
        // Not supported, already showing data by setting route on create
    }

    @Override
    public void markFavorite(boolean favorite) {
        // Not supported
    }

    @Override
    public boolean isFavorite() {
        // Not supported
        return false;
    }
}
