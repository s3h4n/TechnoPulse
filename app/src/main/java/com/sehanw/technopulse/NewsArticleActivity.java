package com.sehanw.technopulse;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sehanw.technopulse.models.NewsItem;

/**
 * Activity for displaying a full news article.
 * Handles data retrieval, view initialization, and user interactions.
 */
public class NewsArticleActivity extends BaseActivity {

    // UI elements
    private Button backButton;

    // News item details
    private NewsItem newsItem;
    private ImageView headerImage;
    private TextView titleTextView;
    private TextView dateTextView;
    private TextView categoryChip;
    private TextView contentTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article);

        // Retrieve NewsItem from intent
        newsItem = (NewsItem) getIntent().getSerializableExtra("news_item");

        // Initialize UI and data
        initViews();
        setupData();
        setupClickListeners();
    }

    /**
     * Initializes views by finding them from the layout.
     */
    private void initViews() {
        backButton = findViewById(R.id.back_button);
        // Article details
        headerImage = findViewById(R.id.header_image);
        titleTextView = findViewById(R.id.title_text);
        dateTextView = findViewById(R.id.date_text);
        categoryChip = findViewById(R.id.category_chip);
        contentTextView = findViewById(R.id.content_text);
    }

    /**
     * Sets up data for the views from the NewsItem.
     */
    private void setupData() {
        if (newsItem != null) {
            headerImage.setImageResource(newsItem.getImageResource());
            titleTextView.setText(newsItem.getTitle());
            dateTextView.setText(newsItem.getDate());
            categoryChip.setText(newsItem.getCategory());

            // Load detailed content
            String content = getDetailedContent(newsItem);
            contentTextView.setText(content);
        }
    }

    /**
     * Sets up click listeners for interactive elements.
     */
    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Retrieves detailed content based on the news item's title.
     * In a real application, this would fetch content from a source.
     *
     * @param item The NewsItem for which to get content.
     * @return A string containing the detailed content.
     */
    private String getDetailedContent(NewsItem item) {
        if (item.getTitle().contains("Artificial Intelligence")) {
            return "The Faculty of Computing has confirmed the introduction of a new undergraduate course titled Foundations of Artificial Intelligence, set to commence in the upcoming semester. The course aims to provide students with a structured introduction to core AI concepts, covering machine learning, neural networks, natural language processing, and ethical considerations.\n\n" +
                    "According to Dr. Anura Perera, Head of the Department of Computer Science, the curriculum was developed in collaboration with industry experts to ensure students gain practical, hands-on experience with current AI technologies. The course will include both theoretical foundations and practical applications, preparing students for careers in the rapidly evolving field of artificial intelligence.\n\n" +
                    "Students will have access to state-of-the-art computing resources and will work on real-world projects throughout the semester. The course is expected to attract significant interest from both current students and industry professionals looking to upskill in AI technologies.";
        } else if (item.getTitle().contains("University Rises")) {
            return "The university has achieved a significant milestone by climbing 15 positions in the latest QS World University Rankings, now ranking among the top 200 institutions globally. This achievement reflects the institution's commitment to academic excellence, research innovation, and student satisfaction.\n\n" +
                    "The improvement in rankings can be attributed to several factors, including increased research output, improved faculty-to-student ratios, and enhanced international collaboration. The university's strong performance in the employer reputation category also contributed significantly to the overall score.\n\n" +
                    "Vice-Chancellor Professor Sarah Johnson expressed her satisfaction with the results, stating that this recognition validates the university's strategic investments in research infrastructure and faculty development over the past five years.";
        } else if (item.getTitle().contains("Career Fair")) {
            return "The annual Career Fair has exceeded expectations, attracting over 100 companies from various industries including technology, finance, healthcare, and manufacturing. The event, held over three days, provided students with unprecedented opportunities to connect with potential employers and explore career paths.\n\n" +
                    "Major technology companies such as Google, Microsoft, and local tech startups set up booths to recruit fresh graduates and interns. The fair also featured workshops on resume writing, interview skills, and networking strategies, helping students prepare for their job search.\n\n" +
                    "Career Services Director Mark Thompson noted that this year's fair saw a 40% increase in participating companies compared to last year, reflecting the strong reputation of the university's graduates in the job market. Over 2,000 students attended the event, with many securing interviews and job offers on the spot.";
        } else {
            return "This is a sample news article content. In a real application, this content would be fetched from a content management system or API. The article provides detailed information about the news topic, including background context, quotes from relevant sources, and analysis of the implications.\n\n" +
                    "The content is structured to be easily readable on mobile devices, with appropriate paragraph breaks and clear, concise language that engages the reader while providing comprehensive coverage of the topic.\n\n" +
                    "Additional sections might include related articles, social media sharing options, and comment sections for reader engagement.";
        }
    }
}