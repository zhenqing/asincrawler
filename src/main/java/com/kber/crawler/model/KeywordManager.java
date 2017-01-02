package com.kber.crawler.model;

import com.kber.crawler.CrawlerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <a href="mailto:tmtlindsay@gmail.com">Lindsay Zhao</a> 12/31/2016 12:05 PM
 */
public class KeywordManager {
    private List<String> keywords = new ArrayList<>();
    private final Logger LOGGER = LoggerFactory.getLogger(KeywordManager.class);

    public KeywordManager() {
        initKeywords();
    }

    public List<String> getKeywords() {
        return this.keywords;
    }

    public void removeKeywords(String keyword) {
        synchronized (keywords){
            keywords.remove(keyword);
        }
        LOGGER.info("关键词{}从列表中移除",keyword);
    }

    public void initKeywords() {
        File keywordFile = new File("keywords.txt");
        BufferedInputStream fis = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(keywordFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if (line == null) {
                    continue;
                }
                keywords.add(line);
            }
            LOGGER.info("There are {} keywords", keywords.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveLeftKeywords() {
        File keywordFile = new File("keywords.txt");
        StringBuffer stringBuffer = new StringBuffer();
        BufferedWriter bwr = null;
        try {
            bwr = new BufferedWriter(new FileWriter(keywordFile));
            for (String keyword : keywords) {
                stringBuffer.append(keyword + "\n");
            }
            bwr.write(stringBuffer.toString());
            bwr.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bwr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        KeywordManager keywordManager = new KeywordManager();
        keywordManager.initKeywords();
        keywordManager.removeKeywords("ihad");
        keywordManager.saveLeftKeywords();
    }

}
