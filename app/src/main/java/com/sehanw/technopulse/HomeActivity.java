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

public class HomeActivity extends BaseActivity {

    // Constants
    private static final int CAROUSEL_DELAY_MS = 3000;
    private static final int TOP_STORIES_LIMIT = 5;
    private static final String DEFAULT_CATEGORY = "Latest";
    // Carousel Handler
    private final Handler carouselHandler = new Handler(Looper.getMainLooper());
    // UI Components
    private ViewPager2 topStoriesViewPager;
    private RecyclerView newsRecyclerView;
    private ChipGroup categoryChipGroup;
    private TextView filterTitleTextView;
    // Adapters
    private NewsAdapter newsAdapter;
    private TopStoriesAdapter topStoriesAdapter;
    // Data
    private List<NewsItem> newsList;
    private List<NewsItem> topStories;
    private String selectedCategory = DEFAULT_CATEGORY;
    private Runnable carouselRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeNavigation();
        initializeViews();
        initializeData();
        setupAdapters();
        setupClickListeners();
        startCarousel();
    }

    private void initializeNavigation() {
        findViewById(R.id.menu_about).setOnClickListener(v ->
                navigateToDevInfo());

        findViewById(R.id.menu_profile).setOnClickListener(v ->
                navigateToProfile());
    }

    private void initializeViews() {
        topStoriesViewPager = findViewById(R.id.top_stories_viewpager);
        newsRecyclerView = findViewById(R.id.news_recyclerview);
        categoryChipGroup = findViewById(R.id.category_chip_group);
        filterTitleTextView = findViewById(R.id.filter_title);

        configureViewPager();
    }

    private void initializeData() {
        newsList = NewsDataRepository.getDummyNewsData();
        topStories = NewsDataRepository.getTopStories(newsList, TOP_STORIES_LIMIT);
    }

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

    private void setupAdapters() {
        setupTopStoriesAdapter();
        setupNewsAdapter();
    }

    private void setupTopStoriesAdapter() {
        topStoriesAdapter = new TopStoriesAdapter(topStories, this::onTopStoryClick);
        topStoriesViewPager.setAdapter(topStoriesAdapter);

        topStoriesViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                handleCarouselStateChange(state);
            }
        });
    }

    private void setupNewsAdapter() {
        newsAdapter = new NewsAdapter(newsList, this::onNewsItemClick);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsRecyclerView.setAdapter(newsAdapter);
        updateFilterTitle();
    }

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

    private void startCarousel() {
        carouselRunnable = new Runnable() {
            @Override
            public void run() {
                int current = topStoriesViewPager.getCurrentItem();
                int next = (current + 1) % topStoriesAdapter.getItemCount();
                topStoriesViewPager.setCurrentItem(next, true);
                carouselHandler.postDelayed(this, CAROUSEL_DELAY_MS);
            }
        };
        carouselHandler.postDelayed(carouselRunnable, CAROUSEL_DELAY_MS);
    }

    private void handleCarouselStateChange(int state) {
        if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
            carouselHandler.removeCallbacks(carouselRunnable);
        } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
            carouselHandler.postDelayed(carouselRunnable, CAROUSEL_DELAY_MS);
        }
    }

    private void updateFilterTitle() {
        filterTitleTextView.setText(String.format("%s news", selectedCategory));
    }

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

    private void onTopStoryClick(NewsItem topStory) {
        navigateToArticle(topStory);
    }

    private void onNewsItemClick(NewsItem newsItem) {
        navigateToArticle(newsItem);
    }

    private void navigateToArticle(NewsItem newsItem) {
        Intent intent = new Intent(this, NewsArticleActivity.class);
        intent.putExtra("news_item", newsItem);
        startActivity(intent);
    }

    private void navigateToActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
        finish();
    }


    private void navigateToDevInfo() {
        Intent intent = new Intent(this, DevInfoActivity.class);
        startActivity(intent);
    }

    private void navigateToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }


    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onPause() {
        super.onPause();
        carouselHandler.removeCallbacks(carouselRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carouselHandler.postDelayed(carouselRunnable, CAROUSEL_DELAY_MS);
    }
}