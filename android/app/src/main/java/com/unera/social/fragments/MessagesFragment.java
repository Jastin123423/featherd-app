package com.unera.social.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unera.social.R;
import com.unera.social.adapters.ChatAdapter;
import com.unera.social.models.ChatConversation;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {
    private RecyclerView rvMessages;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        rvMessages = view.findViewById(R.id.rv_messages);
        rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ChatConversation> chats = new ArrayList<>();
        chats.add(new ChatConversation("c1", "Elena Vance", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150", "Are we still meeting at the studio today?", "5m", true));
        chats.add(new ChatConversation("c2", "Marcus Sterling", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150", "Check out the new audio sample on F-Music!", "18m", true));
        chats.add(new ChatConversation("c3", "Sophia Chen", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150", "The photos from the exhibition look fantastic! 🌟", "1h", false));
        chats.add(new ChatConversation("c4", "Alex Rivera", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150", "See you at the Creator Summit on Saturday.", "3h", false));

        ChatAdapter adapter = new ChatAdapter(chats);
        rvMessages.setAdapter(adapter);

        return view;
    }
}
