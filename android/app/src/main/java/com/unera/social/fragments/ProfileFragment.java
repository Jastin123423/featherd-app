package com.unera.social.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unera.social.R;
import com.unera.social.adapters.PostAdapter;
import com.unera.social.api.ApiClient;
import com.unera.social.models.Post;
import com.unera.social.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements PostAdapter.OnCommentClickListener {
    private ImageView ivCover;
    private ImageView ivAvatar;
    private TextView tvName;
    private TextView tvHandle;
    private TextView tvBio;
    private Button btnEditProfile;
    private RecyclerView rvPosts;

    private final List<Post> userPosts = new ArrayList<>();
    private PostAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ivCover = view.findViewById(R.id.iv_profile_cover);
        ivAvatar = view.findViewById(R.id.iv_profile_avatar);
        tvName = view.findViewById(R.id.tv_profile_name);
        tvHandle = view.findViewById(R.id.tv_profile_handle);
        tvBio = view.findViewById(R.id.tv_profile_bio);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        rvPosts = view.findViewById(R.id.rv_profile_posts);

        // Load images
        ImageLoader.getInstance().load("https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=1000", ivCover);
        ImageLoader.getInstance().load("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200", ivAvatar);

        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostAdapter(userPosts, this);
        rvPosts.setAdapter(adapter);

        btnEditProfile.setOnClickListener(v ->
                Toast.makeText(getContext(), "Edit Profile opened", Toast.LENGTH_SHORT).show()
        );

        loadUserPosts();

        return view;
    }

    private void loadUserPosts() {
        ApiClient.getInstance().fetchFeed(new ApiClient.ApiCallback<List<Post>>() {
            @Override
            public void onSuccess(List<Post> posts) {
                userPosts.clear();
                if (!posts.isEmpty()) {
                    userPosts.add(posts.get(0));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception error) {}
        });
    }

    @Override
    public void onCommentClick(Post post) {
        Toast.makeText(getContext(), "Comments for " + post.authorName, Toast.LENGTH_SHORT).show();
    }
}
