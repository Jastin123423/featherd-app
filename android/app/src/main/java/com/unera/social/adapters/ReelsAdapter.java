package com.unera.social.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.unera.social.R;
import com.unera.social.models.Reel;
import com.unera.social.utils.ImageLoader;

import java.util.List;

public class ReelsAdapter extends RecyclerView.Adapter<ReelsAdapter.ReelViewHolder> {
    private final List<Reel> reels;

    public ReelsAdapter(List<Reel> reels) {
        this.reels = reels;
    }

    @NonNull
    @Override
    public ReelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reel, parent, false);
        return new ReelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReelViewHolder holder, int position) {
        Reel reel = reels.get(position);
        Context context = holder.itemView.getContext();

        holder.tvCreatorName.setText(reel.creatorName);
        holder.tvCaption.setText(reel.caption);
        holder.tvSound.setText(reel.soundTitle);
        holder.tvLikes.setText(String.valueOf(reel.likesCount));
        holder.tvComments.setText(String.valueOf(reel.commentsCount));

        if (reel.creatorAvatar != null && !reel.creatorAvatar.isEmpty()) {
            ImageLoader.getInstance().load(reel.creatorAvatar, holder.ivCreatorAvatar);
        } else {
            holder.ivCreatorAvatar.setImageResource(R.drawable.ic_profile);
        }

        if (reel.mediaUrl != null && !reel.mediaUrl.isEmpty()) {
            ImageLoader.getInstance().load(reel.mediaUrl, holder.ivMedia);
        }

        updateLikeState(holder, reel, context);

        holder.btnLike.setOnClickListener(v -> {
            reel.isLiked = !reel.isLiked;
            if (reel.isLiked) {
                reel.likesCount++;
            } else {
                reel.likesCount = Math.max(0, reel.likesCount - 1);
            }
            holder.tvLikes.setText(String.valueOf(reel.likesCount));
            updateLikeState(holder, reel, context);
        });

        holder.btnComment.setOnClickListener(v ->
                Toast.makeText(context, "Opening comments for reel...", Toast.LENGTH_SHORT).show()
        );

        holder.btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Watch this reel by " + reel.creatorName + " on Feathered:\n" + reel.caption);
            context.startActivity(Intent.createChooser(shareIntent, "Share Reel via"));
        });
    }

    private void updateLikeState(ReelViewHolder holder, Reel reel, Context context) {
        if (reel.isLiked) {
            holder.btnLike.setImageResource(R.drawable.ic_heart_filled);
            holder.btnLike.setColorFilter(ContextCompat.getColor(context, R.color.like_red));
        } else {
            holder.btnLike.setImageResource(R.drawable.ic_heart);
            holder.btnLike.setColorFilter(ContextCompat.getColor(context, R.color.text_primary));
        }
    }

    @Override
    public int getItemCount() {
        return reels.size();
    }

    static class ReelViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMedia;
        ImageView ivCreatorAvatar;
        TextView tvCreatorName;
        TextView tvCaption;
        TextView tvSound;
        ImageView btnLike;
        TextView tvLikes;
        ImageView btnComment;
        TextView tvComments;
        ImageView btnShare;

        ReelViewHolder(View itemView) {
            super(itemView);
            ivMedia = itemView.findViewById(R.id.iv_reel_media);
            ivCreatorAvatar = itemView.findViewById(R.id.iv_reel_creator_avatar);
            tvCreatorName = itemView.findViewById(R.id.tv_reel_creator_name);
            tvCaption = itemView.findViewById(R.id.tv_reel_caption);
            tvSound = itemView.findViewById(R.id.tv_reel_sound);
            btnLike = itemView.findViewById(R.id.btn_reel_like);
            tvLikes = itemView.findViewById(R.id.tv_reel_likes);
            btnComment = itemView.findViewById(R.id.btn_reel_comment);
            tvComments = itemView.findViewById(R.id.tv_reel_comments);
            btnShare = itemView.findViewById(R.id.btn_reel_share);
        }
    }
}
