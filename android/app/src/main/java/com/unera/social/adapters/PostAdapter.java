package com.unera.social.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.unera.social.R;
import com.unera.social.api.ApiClient;
import com.unera.social.models.Post;
import com.unera.social.utils.ImageLoader;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    public interface OnCommentClickListener {
        void onCommentClick(Post post);
    }

    private final List<Post> posts;
    private final OnCommentClickListener commentClickListener;

    public PostAdapter(List<Post> posts, OnCommentClickListener listener) {
        this.posts = posts;
        this.commentClickListener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        Context context = holder.itemView.getContext();

        holder.tvAuthor.setText(post.authorName);
        holder.tvTime.setText(post.timestamp);
        holder.tvContent.setText(post.content);

        holder.tvLikesCount.setText(post.likesCount + (post.likesCount == 1 ? " like" : " likes"));
        holder.tvCommentsCount.setText(post.commentsCount + (post.commentsCount == 1 ? " comment" : " comments"));

        // Author Avatar
        if (post.authorAvatar != null && !post.authorAvatar.isEmpty()) {
            ImageLoader.getInstance().load(post.authorAvatar, holder.ivAvatar);
        } else {
            holder.ivAvatar.setImageResource(R.drawable.ic_profile);
        }

        // Media Image
        if (post.imageUrl != null && !post.imageUrl.isEmpty()) {
            holder.ivMedia.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().load(post.imageUrl, holder.ivMedia);
        } else {
            holder.ivMedia.setVisibility(View.GONE);
        }

        // Like Button State
        updateLikeState(holder, post, context);

        holder.btnLike.setOnClickListener(v -> {
            post.isLiked = !post.isLiked;
            if (post.isLiked) {
                post.likesCount++;
            } else {
                post.likesCount = Math.max(0, post.likesCount - 1);
            }
            holder.tvLikesCount.setText(post.likesCount + (post.likesCount == 1 ? " like" : " likes"));
            updateLikeState(holder, post, context);

            // Call API
            ApiClient.getInstance().reactToPost(post.id, new ApiClient.ApiCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {}

                @Override
                public void onError(Exception error) {}
            });
        });

        // Comment Button
        holder.btnComment.setOnClickListener(v -> {
            if (commentClickListener != null) {
                commentClickListener.onCommentClick(post);
            }
        });

        // Share Button (Native Android Share Intent)
        holder.btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared from Feathered");
            shareIntent.putExtra(Intent.EXTRA_TEXT, post.authorName + " on Feathered:\n" + post.content);
            context.startActivity(Intent.createChooser(shareIntent, "Share post via"));
        });
    }

    private void updateLikeState(PostViewHolder holder, Post post, Context context) {
        if (post.isLiked) {
            holder.ivLikeIcon.setImageResource(R.drawable.ic_heart_filled);
            holder.ivLikeIcon.setColorFilter(ContextCompat.getColor(context, R.color.like_red));
            holder.tvLikeLabel.setTextColor(ContextCompat.getColor(context, R.color.like_red));
        } else {
            holder.ivLikeIcon.setImageResource(R.drawable.ic_heart);
            holder.ivLikeIcon.setColorFilter(ContextCompat.getColor(context, R.color.text_secondary));
            holder.tvLikeLabel.setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvAuthor;
        TextView tvTime;
        TextView tvContent;
        ImageView ivMedia;
        TextView tvLikesCount;
        TextView tvCommentsCount;
        View btnLike;
        ImageView ivLikeIcon;
        TextView tvLikeLabel;
        View btnComment;
        View btnShare;

        PostViewHolder(View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_post_avatar);
            tvAuthor = itemView.findViewById(R.id.tv_post_author);
            tvTime = itemView.findViewById(R.id.tv_post_time);
            tvContent = itemView.findViewById(R.id.tv_post_content);
            ivMedia = itemView.findViewById(R.id.iv_post_media);
            tvLikesCount = itemView.findViewById(R.id.tv_likes_count);
            tvCommentsCount = itemView.findViewById(R.id.tv_comments_count);
            btnLike = itemView.findViewById(R.id.btn_like_post);
            ivLikeIcon = itemView.findViewById(R.id.iv_like_icon);
            tvLikeLabel = itemView.findViewById(R.id.tv_like_label);
            btnComment = itemView.findViewById(R.id.btn_comment_post);
            btnShare = itemView.findViewById(R.id.btn_share_post);
        }
    }
}
