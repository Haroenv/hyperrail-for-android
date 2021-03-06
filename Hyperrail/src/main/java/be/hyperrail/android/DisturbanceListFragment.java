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

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.DateTime;

import be.hyperrail.android.adapter.DisturbanceCardAdapter;
import be.hyperrail.android.adapter.onRecyclerItemClickListener;
import be.hyperrail.android.irail.contracts.IrailDataResponse;
import be.hyperrail.android.irail.factories.IrailFactory;
import be.hyperrail.android.irail.implementation.Disturbance;
import be.hyperrail.android.util.ErrorDialogFactory;

/**
 * A list with disturbances
 */
public class DisturbanceListFragment extends Fragment implements onRecyclerItemClickListener<Disturbance> {

    private RecyclerView vRecyclerView;
    private SwipeRefreshLayout vRefreshLayout;
    private Disturbance[] disturbances;
    private DateTime lastUpdate;

    public DisturbanceListFragment() {
        // Required empty public constructor
    }

    public static DisturbanceListFragment newInstance() {
        return new DisturbanceListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_disturbance_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        vRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        loadDisturbances();
                    }
                }
        );

        vRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_primary);

        vRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        vRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (!PreferenceManager.getDefaultSharedPreferences(getActivity().getApplication()).getBoolean("use_card_layout", false)) {
            // Cards have their own division by margin, others need a divider
            vRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        }

        DisturbanceCardAdapter adapter = new DisturbanceCardAdapter(this.getActivity().getApplicationContext(), null);
        adapter.setOnItemClickListener(this);
        vRecyclerView.setAdapter(adapter);

        if (savedInstanceState != null) {
            this.disturbances = (Disturbance[]) savedInstanceState.getSerializable("disturbances");
            this.lastUpdate = new DateTime(savedInstanceState.getLong("updated"));
            this.setData(this.disturbances);
        } else {
            loadDisturbances();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("disturbances", disturbances);
        outState.putLong("updated", lastUpdate.getMillis());
    }

    private void loadDisturbances() {
        AsyncTask<Void, Void, IrailDataResponse<Disturbance[]>> t = new AsyncTask<Void, Void, IrailDataResponse<Disturbance[]>>() {

            @Override
            protected void onCancelled() {
                super.onCancelled();

                vRefreshLayout.setRefreshing(false);

            }

            @Override
            protected void onPostExecute(IrailDataResponse<Disturbance[]> response) {
                super.onPostExecute(response);
                vRefreshLayout.setRefreshing(false);

                if (response.isSuccess()) {
                    lastUpdate = new DateTime();
                    setData(response.getData());
                } else {
                    // Don't finish, this is the main activity
                    ErrorDialogFactory.showErrorDialog(response.getException(), DisturbanceListFragment.this.getActivity(), false);
                }

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                vRefreshLayout.setRefreshing(true);

            }

            @Override
            protected IrailDataResponse<Disturbance[]> doInBackground(Void... v) {
                return IrailFactory.getDataProviderInstance().getDisturbances();
            }
        };
        t.execute();
    }

    private void setData(Disturbance[] disturbances) {
        if (disturbances == null) {
            return;
        }

        this.disturbances = disturbances;
        ((DisturbanceCardAdapter) vRecyclerView.getAdapter()).updateDisturbances(disturbances);
    }

    @Override
    public void onRecyclerItemClick(RecyclerView.Adapter sender, Disturbance disturbance) {
        if (disturbance.getLink() != null && !disturbance.getLink().isEmpty()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(disturbance.getLink()));
            startActivity(browserIntent);
        }
    }
}
