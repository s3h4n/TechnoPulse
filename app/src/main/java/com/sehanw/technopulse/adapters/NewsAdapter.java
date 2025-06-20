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
 * Adapter for displaying a list of news items in a RecyclerView.
 * Handles item click events and view recycling.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsList;
    private OnNewsItemClickListener clickListener;

    /**
     * Constructor for NewsAdapter.
     *
     * @param newsList      List of news items to display.
     * @param clickListener Listener for news item click events.
     */
    public NewsAdapter(List<NewsItem> newsList, OnNewsItemClickListener clickListener) {
        this.newsList = newsList;
        this.clickListener = clickListener;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     */
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem newsItem = newsList.get(position);
        holder.bind(newsItem);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     */
    @Override
    public int getItemCount() {
        return newsList.size();
    }

    /**
     * Updates the list of news items and notifies the adapter to refresh the view.
     *
     * @param newNewsList The new list of news items.
     */
    public void updateNews(List<NewsItem> newNewsList) {
        this.newsList = newNewsList;
        notifyDataSetChanged();
    }

    /**
     * Interface definition for a callback to be invoked when a news item is clicked.
     */
    public interface OnNewsItemClickListener {
        /**
         * Called when a news item has been clicked.
         *
         * @param newsItem The news item that was clicked.
         */
        void onNewsItemClick(NewsItem newsItem);
    }

    /**
     * ViewHolder for news items. Handles view binding and click events.
     */
    class NewsViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView categoryText;
        private TextView titleText;
        private TextView dateText;

        /**
         * Constructor for NewsViewHolder.
         *
         * @param itemView The view of the news item.
         */
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views from the layout
            imageView = itemView.findViewById(R.id.news_image);
            categoryText = itemView.findViewById(R.id.news_category);
            titleText = itemView.findViewById(R.id.news_title);
            dateText = itemView.findViewById(R.id.news_date);

            // Set up click listener for the item view
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && clickListener != null) {
                    clickListener.onNewsItemClick(newsList.get(position));
                }
            });
        }

        /**
         * Binds the news item data to the views in the ViewHolder.
         *
         * @param newsItem The news item to display.
         */
        public void bind(NewsItem newsItem) {
            // Set data to views
            imageView.setImageResource(newsItem.getImageResource());
            categoryText.setText(newsItem.getCategory());
            titleText.setText(newsItem.getTitle());
            dateText.setText(newsItem.getDate());
        }
    }
}
