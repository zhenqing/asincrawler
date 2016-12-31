package com.kber.crawler;

import com.kber.crawler.service.ApplicationContext;
import com.kber.crawler.ui.ConfigDialog;

import javax.swing.*;

/**
 * <a href="mailto:tmtlindsay@gmail.com">Lindsay Zhao</a> 11/15/2016 4:21 PM
 */
public class Crawler {
    public static void main(String[] args) {
        ConfigDialog configDialog = ApplicationContext.getBean(ConfigDialog.class);
    }

}
