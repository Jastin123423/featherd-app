package com.unera.social.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.unera.social.R;
import com.unera.social.adapters.EventAdapter;
import com.unera.social.api.ApiClient;
import com.unera.social.models.Event;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment {
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvEvents;
    private ProgressBar pbLoading;
    private final List<Event> eventsList = new ArrayList<>();
    private EventAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        swipeRefresh = view.findViewById(R.id.swipe_refresh_events);
        rvEvents = view.findViewById(R.id.rv_events);
        pbLoading = view.findViewById(R.id.pb_events_loading);

        rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventAdapter(eventsList);
        rvEvents.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(this::loadEvents);
        loadEvents();

        return view;
    }

    private void loadEvents() {
        pbLoading.setVisibility(View.VISIBLE);
        ApiClient.getInstance().fetchEvents(new ApiClient.ApiCallback<List<Event>>() {
            @Override
            public void onSuccess(List<Event> events) {
                pbLoading.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                eventsList.clear();
                eventsList.addAll(events);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception error) {
                pbLoading.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
            }
        });
    }
}
