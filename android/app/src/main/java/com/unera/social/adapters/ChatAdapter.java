package com.unera.social.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unera.social.R;
import com.unera.social.models.ChatConversation;
import com.unera.social.utils.ImageLoader;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private final List<ChatConversation> chats;

    public ChatAdapter(List<ChatConversation> chats) {
        this.chats = chats;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatConversation chat = chats.get(position);
        holder.tvName.setText(chat.name);
        holder.tvLastMessage.setText(chat.lastMessage);
        holder.tvTime.setText(chat.time);
        holder.dotOnline.setVisibility(chat.isOnline ? View.VISIBLE : View.GONE);

        if (chat.avatarUrl != null && !chat.avatarUrl.isEmpty()) {
            ImageLoader.getInstance().load(chat.avatarUrl, holder.ivAvatar);
        } else {
            holder.ivAvatar.setImageResource(R.drawable.ic_profile);
        }

        holder.itemView.setOnClickListener(v ->
                Toast.makeText(v.getContext(), "Opening chat with " + chat.name, Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        View dotOnline;
        TextView tvName;
        TextView tvLastMessage;
        TextView tvTime;

        ChatViewHolder(View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_chat_avatar);
            dotOnline = itemView.findViewById(R.id.dot_chat_online);
            tvName = itemView.findViewById(R.id.tv_chat_name);
            tvLastMessage = itemView.findViewById(R.id.tv_chat_last_message);
            tvTime = itemView.findViewById(R.id.tv_chat_time);
        }
    }
}
