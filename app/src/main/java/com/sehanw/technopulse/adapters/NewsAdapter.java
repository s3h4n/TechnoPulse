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

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsList;
    private OnNewsItemClickListener clickListener;

    public NewsAdapter(List<NewsItem> newsList, OnNewsItemClickListener clickListener) {
        this.newsList = newsList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem newsItem = newsList.get(position);
        holder.bind(newsItem);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void updateNews(List<NewsItem> newNewsList) {
        this.newsList = newNewsList;
        notifyDataSetChanged();
    }

    public interface OnNewsItemClickListener {
        void onNewsItemClick(NewsItem newsItem);
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView categoryText;
        private TextView titleText;
        private TextView dateText;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.news_image);
            categoryText = itemView.findViewById(R.id.news_category);
            titleText = itemView.findViewById(R.id.news_title);
            dateText = itemView.findViewById(R.id.news_date);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && clickListener != null) {
                    clickListener.onNewsItemClick(newsList.get(position));
                }
            });
        }

        public void bind(NewsItem newsItem) {
            imageView.setImageResource(newsItem.getImageResource());
            categoryText.setText(newsItem.getCategory());
            titleText.setText(newsItem.getTitle());
            dateText.setText(newsItem.getDate());
        }
    }
}
