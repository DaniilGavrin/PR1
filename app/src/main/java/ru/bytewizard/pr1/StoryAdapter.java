package ru.bytewizard.pr1;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private List<Story> stories;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Story story);
    }

    public StoryAdapter(Context context, List<Story> stories, OnItemClickListener listener) {
        this.stories = stories;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.story_item, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Story story = stories.get(position);
        holder.title.setText(story.getTitle());

        // Получаем URL изображения
        String imageUrl = story.getImageUrl();

        // Проверка на null и пустоту строки
        if (imageUrl == null || imageUrl.isEmpty()) {
            holder.image.setImageResource(R.drawable.placeholder); // Заглушка
        } else {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(holder.image);
        }

        // Обработка клика по элементу
        holder.itemView.setOnClickListener(v -> listener.onItemClick(story));
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public StoryViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_cover);
            title = itemView.findViewById(R.id.text_title);
        }
    }
}