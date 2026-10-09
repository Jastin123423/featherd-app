package com.unera.social.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.unera.social.R;
import com.unera.social.models.Event;
import com.unera.social.utils.ImageLoader;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private final List<Event> events;

    public EventAdapter(List<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        Context context = holder.itemView.getContext();

        holder.tvDate.setText(event.date);
        holder.tvTitle.setText(event.title);
        holder.tvLocation.setText(event.location);
        holder.tvAttendees.setText(event.interestedCount + " interested • Public Event");

        if (event.imageUrl != null && !event.imageUrl.isEmpty()) {
            ImageLoader.getInstance().load(event.imageUrl, holder.ivCover);
        }

        updateInterestedButton(holder, event, context);

        holder.btnInterested.setOnClickListener(v -> {
            event.isInterested = !event.isInterested;
            if (event.isInterested) {
                event.interestedCount++;
                Toast.makeText(context, "Marked as interested in " + event.title, Toast.LENGTH_SHORT).show();
            } else {
                event.interestedCount = Math.max(0, event.interestedCount - 1);
            }
            holder.tvAttendees.setText(event.interestedCount + " interested • Public Event");
            updateInterestedButton(holder, event, context);
        });
    }

    private void updateInterestedButton(EventViewHolder holder, Event event, Context context) {
        if (event.isInterested) {
            holder.btnInterested.setText(R.string.going);
            holder.btnInterested.setBackgroundColor(ContextCompat.getColor(context, R.color.carrot_500));
            holder.btnInterested.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.btnInterested.setText(R.string.interested);
            holder.btnInterested.setBackgroundColor(ContextCompat.getColor(context, R.color.navy_700));
            holder.btnInterested.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvDate;
        TextView tvTitle;
        TextView tvLocation;
        TextView tvAttendees;
        Button btnInterested;

        EventViewHolder(View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_event_cover);
            tvDate = itemView.findViewById(R.id.tv_event_date);
            tvTitle = itemView.findViewById(R.id.tv_event_title);
            tvLocation = itemView.findViewById(R.id.tv_event_location);
            tvAttendees = itemView.findViewById(R.id.tv_event_attendees);
            btnInterested = itemView.findViewById(R.id.btn_event_interested);
        }
    }
}
