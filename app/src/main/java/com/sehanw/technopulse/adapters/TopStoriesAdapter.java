package com.sehanw.technopulse.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sehanw.technopulse.R;
import com.sehanw.technopulse.models.NewsItem;

import java.util.List;

public class TopStoriesAdapter extends RecyclerView.Adapter<TopStoriesAdapter.TopStoryViewHolder> {

    private final OnTopStoryClickListener clickListener;
    private List<NewsItem> topStories;

    public TopStoriesAdapter(List<NewsItem> topStories, OnTopStoryClickListener clickListener) {
        this.topStories = topStories;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public TopStoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_story, parent, false);
        return new TopStoryViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TopStoryViewHolder holder, int position) {
        NewsItem topStory = topStories.get(position);
        holder.bind(topStory);
    }

    @Override
    public int getItemCount() {
        return topStories.size();
    }

    public interface OnTopStoryClickListener {
        void onTopStoryClick(NewsItem topStory);
    }

    static class TopStoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView backgroundImage;
        private TextView categoryChip;
        private TextView sourceText;
        private TextView titleText;
        private OnTopStoryClickListener clickListener;

        public TopStoryViewHolder(@NonNull View itemView, OnTopStoryClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            backgroundImage = itemView.findViewById(R.id.story_background_image);
            categoryChip = itemView.findViewById(R.id.story_category);
            sourceText = itemView.findViewById(R.id.story_source);
            titleText = itemView.findViewById(R.id.story_title);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && clickListener != null) {
                    clickListener.onTopStoryClick((NewsItem) itemView.getTag());
                }
            });
        }

        public void bind(NewsItem topStory) {
            itemView.setTag(topStory);
            backgroundImage.setImageResource(topStory.getImageResource());
            categoryChip.setText(topStory.getCategory());
            sourceText.setText(topStory.getDate());
            titleText.setText(topStory.getTitle());
        }
    }
}