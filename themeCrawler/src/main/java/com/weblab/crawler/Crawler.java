package com.weblab.crawler;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Crawler {
    private String title;
    private volatile static AtomicInteger threadNum = new AtomicInteger();
    private int urlCount = 1000;
    private volatile int visitedURL = 0;
    private int threadCount = 5;
    private double threshold = 0.91;
    private String startURL;

    private Hashtable<String, Integer> keyWords = new Hashtable<>();

    private PriorityBlockingQueue<PriorityURL> waitforHandling = new PriorityBlockingQueue<>();

    private HashSet<String> isWaiting = new HashSet<>();

    private Hashtable<String, String> wanted = new Hashtable<>();

    private HashSet<String> noneRelevant = new HashSet<>();

    private boolean stop = false;

    private JTextPane textpane;
    private JLabel label;
    private JButton button;

    private ExecutorService threadPool = Executors.newCachedThreadPool();

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    int getUrlCount() {
        return urlCount;
    }

    void setUrlCount(int urlCount) {
        this.urlCount = urlCount;
    }

    int getThreadCount() {
        return threadCount;
    }

    Enumeration<String> getKeyWords() {
        return keyWords.keys();
    }

    void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    String getStartURL() {
        return startURL;
    }

    void setStartURL(String startURL) {
        this.startURL = startURL;
    }

    double getThreshold() {
        return threshold;
    }

    void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    void addKeyWord(String word) {
        keyWords.put(word, 0);
    }

    void removeAllKeyWords() {
        keyWords.clear();
    }

    Crawler(String title, String start, JTextPane textpane, JLabel label,JButton button) {
        this.title = title;
        this.startURL = start;
        this.textpane = textpane;
        this.label = label;
        this.button=button;
    }

    void initialize() {
        stop = false;
        label.setText("total access: 0");
        Download download = new Download();
        try {
            String content = download.downloadHttp(new URL(startURL));
            String title = "";
            String regex = "<title>([^<]+)</title>";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(content);
            if (m.find())
                title = m.group(1);
            System.out.println("title is " + title);
            int count;
            for (String key : keyWords.keySet()) {
                count = content.split(key).length - 1;
                keyWords.put(key, count);
            }
            for (String key : keyWords.keySet()) {
                System.out.println(key + ":" + keyWords.get(key));
            }
            double cos = calRelevancy(keyWords);
            ArrayList<String> urls = getLink(content, new URL(startURL));
            int length = urls.size();
            for (String s : urls) {
                waitforHandling.add(new PriorityURL(s, cos / length));
                isWaiting.add(s);
            }

            wanted.put(startURL, title);
            visitedURL = 1;
            insertWeb(title + "----" + startURL);
            label.setText("total access: " + visitedURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void search() {
        String url, content, title = "";
        Download d = new Download();
        while (visitedURL < urlCount && !stop) {
            if (waitforHandling.size() > 0) {
                url = waitforHandling.remove().getUrl();
            } else {
                break;
            }

            try {
                content = d.downloadHttp(new URL(url));
                if (content != null) {
                    Hashtable<String, Integer> destination = new Hashtable<>();

                    int count;
                    for (String key : keyWords.keySet()) {
                        count = content.split(key).length - 1;
                        destination.put(key, count);
                    }

                    double cos = calRelevancy(destination);
                    synchronized (this) {
                        if (cos < threshold) {
                            noneRelevant.add(url);
                            continue;
                        }
                    }
                    ArrayList<String> urls = getLink(content, new URL(url));
                    int length = urls.size();
                    String regex = "<title>([^<]+)</title>";
                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher(content);
                    if (m.find())
                        title = m.group(1);
                    for (String s : urls) {
                        if (wanted.containsKey(s)) {
                            continue;
                        }
                        if (noneRelevant.contains(s)) {
                            continue;
                        }
                        synchronized (this) {
                            if (!isWaiting.contains(s)) {
                                int numberOfSlash = url.split("/").length - 1;

                                waitforHandling.add(new PriorityURL(s, cos
                                        / length / numberOfSlash));
                                isWaiting.add(s);
                            }
                        }
                    }
                    wanted.put(url, title);
                    insertWeb(title + "----" + url);
                    synchronized (this) {
                        isWaiting.remove(url);
                        visitedURL++;
                        label.setText("total access: " + visitedURL);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        threadNum.decrementAndGet();
        System.out.println("current thread num is " + threadNum + ",waiting size is " + waitforHandling.size());
        synchronized (this) {
            wanted.clear();
            waitforHandling.clear();
            noneRelevant.clear();
            isWaiting.clear();
        }
    }

    private void insertWeb(String web) {
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setUnderline(set, true);
        try {
            textpane.getDocument().insertString(
                    textpane.getDocument().getLength(), web + "\n", set);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    void stopSearch() {
        stop = true;
        synchronized (this) {
            wanted.clear();
            waitforHandling.clear();
            noneRelevant.clear();
            isWaiting.clear();
        }
    }

    private double calRelevancy(Hashtable<String, Integer> destination) {
        long sum1 = 0, sum2 = 0, sum3 = 0;
        for (String key : keyWords.keySet()) {
            sum1 += keyWords.get(key)
                    * destination.get(key);
            sum2 += keyWords.get(key) * keyWords.get(key);
            sum3 += destination.get(key)
                    * destination.get(key);
        }
        if (sum3 == 0) {
            return 0;
        }
        return sum1 * 1.0 / (Math.sqrt(sum2) * Math.sqrt(sum3));
    }

    private ArrayList<String> getLink(String content, URL url) {
        ArrayList<String> urls = new ArrayList<>();
        String regex = "<a\\s*href=\"([^>\"]*)\"[^>]*>";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        String s;
        while (m.find()) {
            s = m.group(1);
            if (s.length() == 1)
                continue;
            if (s.startsWith("/"))
                s = "http://" + url.getHost() + s;
            if (s.startsWith("http"))
                urls.add(s);
        }
        return urls;
    }

    void parallelHandle() {
        for (int i = 0; i < threadCount; i++) {
            new Task().start();
        }
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Runnable task = () -> {
            while (true) {
                if (threadNum.get() <= 0) break;

            }
            button.setEnabled(true);
            JOptionPane.showMessageDialog(null, "Finish searching the pages!",
                    "Done", JOptionPane.INFORMATION_MESSAGE);
        };
        threadPool.execute(task);


    }

    class Task extends Thread {

        Task() {
            threadNum.getAndIncrement();
        }

        public void run() {
            search();
        }
    }

}
