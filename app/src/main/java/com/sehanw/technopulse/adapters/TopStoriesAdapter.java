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

/**
 * Adapter for displaying top stories in a RecyclerView with featured card layout.
 * Supports click interactions and efficient view recycling.
 */
public class TopStoriesAdapter extends RecyclerView.Adapter<TopStoriesAdapter.TopStoryViewHolder> {

    private final OnTopStoryClickListener clickListener;
    private List<NewsItem> topStories;

    /**
     * @param topStories    List of featured news stories
     * @param clickListener Callback for story click events
     */
    public TopStoriesAdapter(List<NewsItem> topStories, OnTopStoryClickListener clickListener) {
        this.topStories = topStories;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public TopStoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_top_story, parent, false);
        return new TopStoryViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TopStoryViewHolder holder, int position) {
        holder.bind(topStories.get(position));
    }

    @Override
    public int getItemCount() {
        return topStories != null ? topStories.size() : 0;
    }

    /**
     * Updates the stories list and notifies adapter
     *
     * @param newStories Updated list of top stories
     */
    public void updateStories(List<NewsItem> newStories) {
        this.topStories = newStories;
        notifyDataSetChanged();
    }

    /**
     * Interface for handling top story click events
     */
    public interface OnTopStoryClickListener {
        void onTopStoryClick(NewsItem topStory);
    }

    /**
     * ViewHolder for top story items that handles view binding and click events
     */
    static class TopStoryViewHolder extends RecyclerView.ViewHolder {
        private final ImageView backgroundImage;
        private final TextView categoryChip;
        private final TextView sourceText;
        private final TextView titleText;
        private final OnTopStoryClickListener clickListener;

        public TopStoryViewHolder(@NonNull View itemView, OnTopStoryClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            backgroundImage = itemView.findViewById(R.id.story_background_image);
            categoryChip = itemView.findViewById(R.id.story_category);
            sourceText = itemView.findViewById(R.id.story_source);
            titleText = itemView.findViewById(R.id.story_title);

            setupClickListeners();
        }

        private void setupClickListeners() {
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    NewsItem story = (NewsItem) itemView.getTag();
                    if (story != null && clickListener != null) {
                        clickListener.onTopStoryClick(story);
                    }
                }
            });
        }

        /**
         * Binds story data to the views
         *
         * @param topStory The story item to display
         */
        public void bind(NewsItem topStory) {
            itemView.setTag(topStory);

            // Set visual elements
            backgroundImage.setImageResource(topStory.getImageResource());
            categoryChip.setText(topStory.getCategory());
            sourceText.setText(topStory.getDate());
            titleText.setText(topStory.getTitle());

        }
    }
}