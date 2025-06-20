package com.sehanw.technopulse.data;

import com.sehanw.technopulse.R;
import com.sehanw.technopulse.models.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class NewsDataRepository {

    public static List<NewsItem> getDummyNewsData() {
        List<NewsItem> newsList = new ArrayList<>();

        newsList.add(new NewsItem("Sports",
                "University basketball team wins national championship",
                "20 Jun 2025",
                R.drawable.sports_basketball_championship
        ));
        newsList.add(new NewsItem("Academic",
                "University ranked among top 100 globally",
                "20 Jun 2025",
                R.drawable.academic_top100_ranking
        ));
        newsList.add(new NewsItem("Research",
                "New quantum computing lab inaugurated",
                "19 Jun 2025",
                R.drawable.research_quantum_lab
        ));
        newsList.add(new NewsItem("Events",
                "Annual cultural festival draws thousands on campus",
                "19 Jun 2025",
                R.drawable.events_cultural_festival
        ));
        newsList.add(new NewsItem("Technology",
                "Campus Wi-Fi network upgrade completed",
                "18 Jun 2025",
                R.drawable.tech_wifi_upgrade
        ));
        newsList.add(new NewsItem("Academic",
                "New interdisciplinary program launched",
                "18 Jun 2025",
                R.drawable.academic_interdisciplinary
        ));
        newsList.add(new NewsItem("Academic",
                "Scholarship opportunities expanded for undergraduates",
                "17 Jun 2025",
                R.drawable.academic_scholarship_check
        ));
        newsList.add(new NewsItem("Academic",
                "Faculty publishes groundbreaking climate study",
                "16 Jun 2025",
                R.drawable.research_climate_study
        ));
        newsList.add(new NewsItem("Academic",
                "Engineering department unveils solar car",
                "15 Jun 2025",
                R.drawable.tech_solar_car
        ));
        newsList.add(new NewsItem("Events",
                "Guest lecture on AI ethics scheduled",
                "14 Jun 2025",
                R.drawable.events_ai_lecture
        ));
        newsList.add(new NewsItem("Events",
                "Open day attracts record applicants",
                "13 Jun 2025",
                R.drawable.events_open_day
        ));
        newsList.add(new NewsItem("Events",
                "Career fair set for July 1",
                "12 Jun 2025",
                R.drawable.events_career_fair
        ));
        newsList.add(new NewsItem("Events",
                "Alumni reunion to be held virtually",
                "11 Jun 2025",
                R.drawable.events_virtual_reunion
        ));
        newsList.add(new NewsItem("Sports",
                "Campus soccer team advances to finals",
                "10 Jun 2025",
                R.drawable.sports_soccer_finals
        ));
        newsList.add(new NewsItem("Sports",
                "Track and field trials begin next week",
                "09 Jun 2025",
                R.drawable.sports_track_trials
        ));
        newsList.add(new NewsItem("Sports",
                "Swimming pool renovation completed",
                "08 Jun 2025",
                R.drawable.sports_pool_renovation
        ));
        newsList.add(new NewsItem("Sports",
                "Chess club wins intercollegiate tournament",
                "07 Jun 2025",
                R.drawable.sports_chess_win
        ));
        newsList.add(new NewsItem("Technology",
                "University launches coding bootcamp",
                "06 Jun 2025",
                R.drawable.tech_coding_bootcamp
        ));
        newsList.add(new NewsItem("Technology",
                "AI-driven library catalog rolled out",
                "05 Jun 2025",
                R.drawable.tech_ai_library
        ));
        newsList.add(new NewsItem("Technology",
                "Cybersecurity workshop for students",
                "04 Jun 2025",
                R.drawable.tech_cybersecurity_workshop
        ));
        return newsList;
    }

    public static List<NewsItem> getTopStories(List<NewsItem> allNews, int limit) {
        return new ArrayList<>(allNews.subList(0, Math.min(limit, allNews.size())));
    }
}