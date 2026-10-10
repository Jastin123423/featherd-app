package com.unera.social.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.unera.social.R;
import com.unera.social.adapters.CommentAdapter;
import com.unera.social.adapters.PostAdapter;
import com.unera.social.adapters.StoryAdapter;
import com.unera.social.api.ApiClient;
import com.unera.social.models.Comment;
import com.unera.social.models.Post;
import com.unera.social.models.Story;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment implements PostAdapter.OnCommentClickListener {
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvStories;
    private RecyclerView rvPosts;
    private ProgressBar pbLoading;
    private View cardCreatePost;

    private final List<Story> storiesList = new ArrayList<>();
    private final List<Post> postsList = new ArrayList<>();
    private StoryAdapter storyAdapter;
    private PostAdapter postAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        swipeRefresh = view.findViewById(R.id.swipe_refresh_feed);
        rvStories = view.findViewById(R.id.rv_stories);
        rvPosts = view.findViewById(R.id.rv_posts);
        pbLoading = view.findViewById(R.id.pb_feed_loading);
        cardCreatePost = view.findViewById(R.id.card_create_post);

        setupRecyclerViews();
        setupListeners();
        loadData();

        return view;
    }

    private void setupRecyclerViews() {
        rvStories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        storyAdapter = new StoryAdapter(storiesList);
        rvStories.setAdapter(storyAdapter);

        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        postAdapter = new PostAdapter(postsList, this);
        rvPosts.setAdapter(postAdapter);
    }

    private void setupListeners() {
        swipeRefresh.setOnRefreshListener(this::loadData);
        cardCreatePost.setOnClickListener(v -> showCreatePostDialog());
    }

    private void loadData() {
        pbLoading.setVisibility(View.VISIBLE);

        // Fetch Stories
        ApiClient.getInstance().fetchStories(new ApiClient.ApiCallback<List<Story>>() {
            @Override
            public void onSuccess(List<Story> stories) {
                if (!isAdded() || getContext() == null) return;
                storiesList.clear();
                storiesList.addAll(stories);
                if (storyAdapter != null) storyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception error) {}
        });

        // Fetch Live Feed
        ApiClient.getInstance().fetchFeed(new ApiClient.ApiCallback<List<Post>>() {
            @Override
            public void onSuccess(List<Post> posts) {
                if (!isAdded() || getContext() == null) return;
                if (pbLoading != null) pbLoading.setVisibility(View.GONE);
                if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
                postsList.clear();
                postsList.addAll(posts);
                if (postAdapter != null) postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception error) {
                if (!isAdded() || getContext() == null) return;
                if (pbLoading != null) pbLoading.setVisibility(View.GONE);
                if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void showCreatePostDialog() {
        if (getContext() == null) return;
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_create_post);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        EditText etContent = dialog.findViewById(R.id.et_new_post_content);
        EditText etImageUrl = dialog.findViewById(R.id.et_new_post_image_url);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel_post);
        Button btnSubmit = dialog.findViewById(R.id.btn_submit_post);

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnSubmit.setOnClickListener(v -> {
            String content = etContent.getText().toString().trim();
            String imageUrl = etImageUrl.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(getContext(), "Please write something to post", Toast.LENGTH_SHORT).show();
                return;
            }

            // Optimistic insert
            Post newPost = new Post(
                    "p_" + System.currentTimeMillis(),
                    "You",
                    "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
                    "Just now",
                    content,
                    imageUrl,
                    0,
                    0
            );
            postsList.add(0, newPost);
            postAdapter.notifyItemInserted(0);
            rvPosts.scrollToPosition(0);
            dialog.dismiss();

            // Submit to live backend API
            ApiClient.getInstance().createPost(content, imageUrl, new ApiClient.ApiCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    Toast.makeText(getContext(), "Post published successfully!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Exception error) {}
            });
        });

        dialog.show();
    }

    @Override
    public void onCommentClick(Post post) {
        if (getContext() == null) return;
        BottomSheetDialog sheetDialog = new BottomSheetDialog(getContext());
        sheetDialog.setContentView(R.layout.dialog_comments);

        RecyclerView rvComments = sheetDialog.findViewById(R.id.rv_comments);
        EditText etInput = sheetDialog.findViewById(R.id.et_comment_input);
        Button btnSend = sheetDialog.findViewById(R.id.btn_send_comment);

        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("c1", "Alex Rivera", "", "This looks incredible! Love the vibe.", "5m ago"));
        comments.add(new Comment("c2", "Maya Lin", "", "Totally agree with this! 🔥", "12m ago"));

        if (rvComments != null) {
            rvComments.setLayoutManager(new LinearLayoutManager(getContext()));
            CommentAdapter adapter = new CommentAdapter(comments);
            rvComments.setAdapter(adapter);

            if (btnSend != null && etInput != null) {
                btnSend.setOnClickListener(v -> {
                    String text = etInput.getText().toString().trim();
                    if (!text.isEmpty()) {
                        comments.add(new Comment("c_" + System.currentTimeMillis(), "You", "", text, "Just now"));
                        adapter.notifyItemInserted(comments.size() - 1);
                        rvComments.scrollToPosition(comments.size() - 1);
                        etInput.setText("");
                        post.commentsCount++;
                        postAdapter.notifyDataSetChanged();
                    }
                });
            }
        }

        sheetDialog.show();
    }
}
