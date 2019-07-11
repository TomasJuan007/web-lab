package com.weblab.crawler;

public class PriorityURL implements Comparable<PriorityURL> {
    private String url;
    String getUrl()
    {
        return url;
    }

    private double priority;

    PriorityURL(String url, double priority) {
        super();
        this.url = url;
        this.priority = priority;
    }

    @Override
    public int compareTo(PriorityURL o) {
        return Double.compare(priority, o.priority);
    }

}
