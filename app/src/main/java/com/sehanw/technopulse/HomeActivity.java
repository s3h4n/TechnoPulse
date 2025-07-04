package com.sehanw.technopulse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.sehanw.technopulse.adapters.NewsAdapter;
import com.sehanw.technopulse.adapters.TopStoriesAdapter;
import com.sehanw.technopulse.data.NewsDataRepository;
import com.sehanw.technopulse.models.NewsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Main activity for the home screen, displaying top stories and a list of news articles.
 * Allows users to filter news by category and navigate to individual articles.
 */
public class HomeActivity extends BaseActivity {

    // Constants
    private static final int CAROUSEL_DELAY_MS = 3000;
    private static final int TOP_STORIES_LIMIT = 5;
    private static final String DEFAULT_CATEGORY = "Latest";
    // Carousel Handler
    private final Handler carouselHandler = new Handler(Looper.getMainLooper()); // Handler for managing the top stories carousel
    // UI Components
    private ViewPager2 topStoriesViewPager;
    private RecyclerView newsRecyclerView;

    // Data
    private ChipGroup categoryChipGroup;
    private TextView filterTitleTextView;
    // Adapters
    private NewsAdapter newsAdapter;
    private TopStoriesAdapter topStoriesAdapter;
    // Data
    private List<NewsItem> newsList;
    private List<NewsItem> topStories;
    private String selectedCategory = DEFAULT_CATEGORY;
    private Runnable carouselRunnable; // Runnable for the carousel auto-scroll

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeNavigation();
        initializeViews();
        initializeData();
        setupAdapters();
        setupClickListeners();
    }

    /**
     * Initializes navigation menu item click listeners.
     */
    private void initializeNavigation() {
        findViewById(R.id.menu_about).setOnClickListener(v ->
                navigateToDevInfo());

        findViewById(R.id.menu_profile).setOnClickListener(v ->
                navigateToProfile());
    }

    /**
     * Initializes UI views by finding them in the layout and configuring the ViewPager.
     */
    private void initializeViews() {
        topStoriesViewPager = findViewById(R.id.top_stories_viewpager);
        newsRecyclerView = findViewById(R.id.news_recyclerview);
        categoryChipGroup = findViewById(R.id.category_chip_group);
        filterTitleTextView = findViewById(R.id.filter_title);

        configureViewPager();
    }

    /**
     * Loads initial data for news articles and top stories from the repository.
     */
    private void initializeData() {
        newsList = NewsDataRepository.getDummyNewsData();
        topStories = NewsDataRepository.getTopStories(newsList, TOP_STORIES_LIMIT);
    }

    /**
     * Configures the ViewPager2 for the top stories carousel with page transformers and overscroll behavior.
     */
    private void configureViewPager() {
        topStoriesViewPager.setOffscreenPageLimit(3);
        topStoriesViewPager.setClipToPadding(false);
        topStoriesViewPager.setClipChildren(false);

        RecyclerView rv = (RecyclerView) topStoriesViewPager.getChildAt(0);
        rv.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(dpToPx(16)));
        transformer.addTransformer((page, pos) -> {
            float scale = 0.85f + (1 - Math.abs(pos)) * 0.15f;
            page.setScaleY(scale);
        });
        topStoriesViewPager.setPageTransformer(transformer);
    }

    /**
     * Sets up the adapters for the top stories ViewPager and the news RecyclerView.
     */
    private void setupAdapters() {
        setupTopStoriesAdapter();
        setupNewsAdapter();
    }

    /**
     * Initializes and sets the TopStoriesAdapter for the ViewPager.
     */
    private void setupTopStoriesAdapter() {
        topStoriesAdapter = new TopStoriesAdapter(topStories, this::onTopStoryClick);
        topStoriesViewPager.setAdapter(topStoriesAdapter);
    }

    /**
     * Initializes and sets the NewsAdapter for the RecyclerView.
     */
    private void setupNewsAdapter() {
        newsAdapter = new NewsAdapter(newsList, this::onNewsItemClick);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsRecyclerView.setAdapter(newsAdapter);
        updateFilterTitle();
    }

    /**
     * Sets up click listeners for UI elements, specifically the category chip group.
     */
    private void setupClickListeners() {
        categoryChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                Chip selectedChip = findViewById(checkedIds.get(0));
                selectedCategory = selectedChip.getText().toString();
                updateFilterTitle();
                filterNewsList();
            }
        });
    }


    /**
     * Updates the filter title TextView based on the currently selected category.
     */
    private void updateFilterTitle() {
        filterTitleTextView.setText(String.format("%s news", selectedCategory));
    }

    /**
     * Filters the news list based on the selected category and updates the NewsAdapter.
     */
    private void filterNewsList() {
        List<NewsItem> filteredNews = new ArrayList<>();
        if (selectedCategory.equals(DEFAULT_CATEGORY)) {
            filteredNews.addAll(newsList);
        } else {
            for (NewsItem item : newsList) {
                if (item.getCategory().equals(selectedCategory)) {
                    filteredNews.add(item);
                }
            }
        }
        newsAdapter.updateNews(filteredNews);
    }

    /**
     * Handles click events on top story items.
     *
     * @param topStory The clicked NewsItem.
     */
    private void onTopStoryClick(NewsItem topStory) {
        navigateToArticle(topStory);
    }

    /**
     * Handles click events on regular news items.
     *
     * @param newsItem The clicked NewsItem.
     */
    private void onNewsItemClick(NewsItem newsItem) {
        navigateToArticle(newsItem);
    }

    /**
     * Navigates to the NewsArticleActivity to display the details of a news item.
     *
     * @param newsItem The NewsItem to display.
     */
    private void navigateToArticle(NewsItem newsItem) {
        Intent intent = new Intent(this, NewsArticleActivity.class);
        intent.putExtra("news_item", newsItem);
        startActivity(intent);
    }

    /**
     * Helper method to navigate to a specified activity.
     *
     * @param cls The class of the activity to navigate to.
     */
    private void navigateToActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
        finish();
    }

    /**
     * Navigates to the Developer Information activity.
     */
    private void navigateToDevInfo() {
        Intent intent = new Intent(this, DevInfoActivity.class);
        startActivity(intent);
    }

    /**
     * Navigates to the Profile activity.
     */
    private void navigateToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    // Utility method
    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}