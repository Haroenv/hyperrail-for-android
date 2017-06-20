/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package android.irail.be.hyperrail.adapter;

import android.content.Context;
import android.irail.be.hyperrail.R;
import android.irail.be.hyperrail.irail.implementation.Disturbance;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Recyclerview adapter to show a list with disturbances on the net
 */
public class DisturbanceCardAdapter extends RecyclerView.Adapter<DisturbanceCardAdapter.DisturbanceViewHolder> {

    private Disturbance[] disturbances;
    private Context context;

    private onRecyclerItemClickListener<Disturbance> listener;

    public DisturbanceCardAdapter(Context context, Disturbance[] disturbances) {
        this.context = context;
        this.disturbances = disturbances;
    }

    public void updateDisturbances(Disturbance[] disturbances) {
        this.disturbances = disturbances;
        super.notifyDataSetChanged();
    }

    @Override
    public DisturbanceCardAdapter.DisturbanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if (! PreferenceManager.getDefaultSharedPreferences(context).getBoolean("use_card_layout", false)) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_disturbance, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_disturbance, parent, false);
        }

        return new DisturbanceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DisturbanceViewHolder holder, int position) {

        final Disturbance disturbance = disturbances[position];

        holder.vTitle.setText(disturbance.getTitle());

        holder.vDescription.setText(disturbance.getDescription());

        DateFormat df = new SimpleDateFormat("d/M/y - HH:mm");
        holder.vDate.setText(df.format(disturbance.getTime()));
    }

    @Override
    public int getItemCount() {
        if (disturbances == null){
            return 0;
        }
        return disturbances.length;
    }

    public void setOnItemClickListener(onRecyclerItemClickListener<Disturbance> listener) {
        this.listener = listener;
    }

    public void clearOnItemClickListener() {
        this.listener = null;
    }

    class DisturbanceViewHolder extends RecyclerView.ViewHolder {
        TextView vTitle;
        TextView vDescription;
        TextView vDate;


        DisturbanceViewHolder(View view) {
            super(view);
            vTitle = (TextView) view.findViewById(R.id.text_title);
            vDescription = ((TextView) view.findViewById(R.id.text_description));
            vDate = ((TextView) view.findViewById(R.id.text_date));
        }
    }
}

