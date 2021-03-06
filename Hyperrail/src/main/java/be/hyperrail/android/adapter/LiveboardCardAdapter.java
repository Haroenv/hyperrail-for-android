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

package be.hyperrail.android.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

import be.hyperrail.android.infiniteScrolling.InfiniteScrollingAdapter;
import be.hyperrail.android.infiniteScrolling.InfiniteScrollingDataSource;
import be.hyperrail.android.irail.implementation.LiveBoard;
import be.hyperrail.android.irail.implementation.TrainStop;

/**
 * Recyclerview adapter to show train departures in a station
 */
public class LiveboardCardAdapter extends InfiniteScrollingAdapter<TrainStop> {

    private LiveBoard liveboard;
    private final Context context;

    private Object[] displayList;

    protected final static int VIEW_TYPE_DATE = 1;

    private onRecyclerItemClickListener<TrainStop> listener;

    public LiveboardCardAdapter(Context context, RecyclerView recyclerView, InfiniteScrollingDataSource listener) {
        super(context, recyclerView, listener);
        this.context = context;
    }

    public void updateLiveboard(LiveBoard liveBoard) {
        this.liveboard = liveBoard;

        ArrayList<Integer> daySeparatorPositions = new ArrayList<>();

        if (liveboard != null && liveboard.getStops() != null && liveboard.getStops().length > 0) {
            DateTime lastday = liveBoard.getStops()[0].getDepartureTime().withTimeAtStartOfDay();

            for (int i = 0; i < liveboard.getStops().length; i++) {
                TrainStop stop = liveBoard.getStops()[i];

                if (stop.getDepartureTime().withTimeAtStartOfDay().isAfter(lastday)) {
                    lastday = stop.getDepartureTime().withTimeAtStartOfDay();
                    daySeparatorPositions.add(i);
                }
            }

            Log.d("DateSeparator", "Detected " + daySeparatorPositions.size() + " day changes");
            this.displayList = new Object[daySeparatorPositions.size() + liveBoard.getStops().length];

            // Convert to array + take previous separators into account for position of next separator
            int dayPosition = 0;
            int stopPosition = 0;
            int resultPosition = 0;
            while (resultPosition < daySeparatorPositions.size() + liveBoard.getStops().length) {
                // Keep in mind that position shifts with the number of already placed date separators
                if (dayPosition < daySeparatorPositions.size() && resultPosition == daySeparatorPositions.get(dayPosition) + dayPosition) {
                    DateSeparator separator = new DateSeparator();
                    separator.separatorDate = liveBoard.getStops()[stopPosition].getDepartureTime();

                    this.displayList[resultPosition] = separator;

                    dayPosition++;
                } else {
                    this.displayList[resultPosition] = liveboard.getStops()[stopPosition];
                    stopPosition++;
                }

                resultPosition++;
            }
        }

        super.setLoaded();
        super.notifyDataSetChanged();
    }

    @Override
    protected int onGetItemViewType(int position) {
        if (displayList[position] instanceof DateSeparator) {
            return VIEW_TYPE_DATE;
        }
        return VIEW_TYPE_ITEM;

    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if (viewType == VIEW_TYPE_DATE) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(be.hyperrail.android.R.layout.listview_separator_date, parent, false);
            return new DateSeparatorViewHolder(itemView);
        }

        if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("use_card_layout", false)) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(be.hyperrail.android.R.layout.listview_liveboard, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(be.hyperrail.android.R.layout.cardview_liveboard, parent, false);
        }

        return new LiveboardStopViewHolder(itemView);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder genericHolder, int position) {
        if (genericHolder instanceof DateSeparatorViewHolder) {
            DateSeparatorViewHolder holder = (DateSeparatorViewHolder) genericHolder;

            DateTimeFormatter df = DateTimeFormat.forPattern("EEE dd MMMMMMMM yyyy");
            holder.vDateText.setText(df.print(((DateSeparator) displayList[position]).separatorDate));
            return;
        }

        LiveboardStopViewHolder holder = (LiveboardStopViewHolder) genericHolder;

        final TrainStop stop = (TrainStop) displayList[position];

        holder.vDestination.setText(stop.getDestination().getLocalizedName());

        holder.vTrainNumber.setText(stop.getTrain().getNumber());
        holder.vTrainType.setText(stop.getTrain().getType());

        DateTimeFormatter df = DateTimeFormat.forPattern("HH:mm");

        holder.vDeparture.setText(df.print(stop.getDepartureTime()));
        if (stop.getDepartureDelay().getStandardSeconds() > 0) {
            holder.vDepartureDelay.setText(context.getString(be.hyperrail.android.R.string.delay, stop.getDepartureDelay().getStandardMinutes()));
            holder.vDelayTime.setText(df.print(stop.getDelayedDepartureTime()));
        } else {
            holder.vDepartureDelay.setText("");
            holder.vDelayTime.setText("");
        }

        holder.vPlatform.setText(String.valueOf(stop.getPlatform()));

        if (stop.isDepartureCanceled()) {
            holder.vPlatform.setText("");
            holder.vPlatformContainer.setBackground(ContextCompat.getDrawable(context, be.hyperrail.android.R.drawable.platform_train_canceled));
            holder.vStatusText.setText(be.hyperrail.android.R.string.status_cancelled);
        } else {
            holder.vStatusContainer.setVisibility(View.GONE);
            holder.vPlatformContainer.setBackground(ContextCompat.getDrawable(context, be.hyperrail.android.R.drawable.platform_train));
            if (!stop.isPlatformNormal()) {
                Drawable drawable = holder.vPlatformContainer.getBackground();
                drawable.mutate();
                drawable.setColorFilter(ContextCompat.getColor(context, be.hyperrail.android.R.color.colorDelay), PorterDuff.Mode.SRC_ATOP);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRecyclerItemClick(LiveboardCardAdapter.this, stop);
                }
            }
        });
    }

    @Override
    public int getListItemCount() {
        if (liveboard == null || liveboard.getStops() == null || displayList == null) {
            return 0;
        }
        return displayList.length;
    }

    public void setOnItemClickListener(onRecyclerItemClickListener<TrainStop> listener) {
        this.listener = listener;
    }

    private class LiveboardStopViewHolder extends RecyclerView.ViewHolder {

        final TextView vDestination;
        final TextView vTrainType;
        final TextView vTrainNumber;
        final TextView vDeparture;
        final TextView vDepartureDelay;
        final TextView vDelayTime;
        final TextView vPlatform;
        final LinearLayout vPlatformContainer;

        final LinearLayout vStatusContainer;
        final TextView vStatusText;

        LiveboardStopViewHolder(View view) {
            super(view);
            vDestination = (TextView) view.findViewById(be.hyperrail.android.R.id.text_destination);
            vTrainNumber = ((TextView) view.findViewById(be.hyperrail.android.R.id.text_train_number));
            vTrainType = ((TextView) view.findViewById(be.hyperrail.android.R.id.text_train_type));
            vDeparture = (TextView) view.findViewById(be.hyperrail.android.R.id.text_departure_time);
            vDepartureDelay = (TextView) view.findViewById(be.hyperrail.android.R.id.text_departure_delay);
            vDelayTime = (TextView) view.findViewById(be.hyperrail.android.R.id.text_delay_time);
            vPlatform = (TextView) view.findViewById(be.hyperrail.android.R.id.text_platform);
            vPlatformContainer = (LinearLayout) view.findViewById(be.hyperrail.android.R.id.layout_platform_container);

            vStatusContainer = (LinearLayout) view.findViewById(be.hyperrail.android.R.id.layout_train_status_container);
            vStatusText = (TextView) view.findViewById(be.hyperrail.android.R.id.text_train_status);
        }
    }

    private class DateSeparator {

        DateTime separatorDate;
    }
}

