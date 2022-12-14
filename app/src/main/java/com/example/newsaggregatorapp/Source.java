package com.example.newsaggregatorapp;

import java.io.Serializable;

public class Source implements Serializable {

    private final String sourceID;
    private final String sourceName;
    private final String sourceCategory;

    // Source obj holds info regarding a particular news source (e.g., CNN, ABC News, etc.)
    public Source(String sourceID, String sourceName, String sourceCategory) {

        this.sourceID = sourceID;
        this.sourceName = sourceName;
        this.sourceCategory = sourceCategory;
    }

    String getSourceID() {
        return sourceID;
    }
    String getSourceName() {
        return sourceName;
    }
    String getSourceCategory() {
        return sourceCategory;
    }
}
