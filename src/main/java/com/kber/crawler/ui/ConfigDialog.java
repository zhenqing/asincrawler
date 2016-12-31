package com.kber.crawler.ui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kber.crawler.Crawler;
import com.kber.crawler.CrawlerHandler;
import com.kber.crawler.model.Config;
import com.kber.crawler.model.Country;
import com.kber.crawler.utils.Constants;
import com.kber.crawler.utils.Customize;
import com.kber.crawler.utils.Tools;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import static com.kber.crawler.utils.Constants.CONFIG_CUSTOMIZE;

/**
 * <a href="mailto:tmtlindsay@gmail.com">Lindsay Zhao</a> 11/15/2016 4:19 PM
 */
@Singleton
public class ConfigDialog extends JDialog {
    private JPanel pane;
    private GroupLayout layout;
    private JComboBox market;
    private JCheckBox useProxy;
    private JCheckBox multiThread;
    private JTextField jt_seller;
    private JTextField threadCount;
    private JTextField startLine;
    private Config config = Tools.loadCustomize(CONFIG_CUSTOMIZE);
    @Inject private CrawlerHandler crawlerHandler = new CrawlerHandler(Tools.loadCustomize(CONFIG_CUSTOMIZE));
    private static String comboBoxItems[] = {Country.US.name(), Country.UK.name(), Country.CA.name(), Country.ES.name(), Country.JP.name(), Country.FR.name(), Country.DE.name(), Country.IT.name(), Country.IN.name()};

    public ConfigDialog() {
        super();
        init();
    }

    public ConfigDialog(Frame frame) {
        super(frame);
        init();
    }

    public void init() {
        pane = new JPanel();
        layout = new GroupLayout(pane);
        final JLabel label = new JLabel("market:");
        market = new JComboBox(comboBoxItems);
        market.setSelectedIndex(Country.getIndex(config.getCountry()));
        useProxy = new JCheckBox("Use Proxy", false);
        useProxy.setSelected(config.isNeedProxy());
        multiThread = new JCheckBox("Multi Thread", false);
        multiThread.setSelected(config.isMultiThread());
        final JLabel category = new JLabel("category:");
        jt_seller = new JTextField(10);
        jt_seller.setText(config.getCategory());
        final JLabel threadLable = new JLabel("Thread count:");
        threadCount = new JTextField(10);
        threadCount.setText(Integer.toString(config.getThreadCount()));
        final JLabel startLable = new JLabel("Start line:");
        startLine = new JTextField(2);
        startLine.setText(Integer.toString(config.getStartLine()));
        final JButton jb_saveConfig = new JButton("Save Config");
        final JButton jb_run = new JButton("Run");
        pane.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addComponent(label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(market, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10)
                        .addComponent(useProxy, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10)
                        .addComponent(multiThread, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10)
                        .addComponent(category, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jt_seller, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10)
                        .addComponent(threadLable, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(threadCount, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10)
                        .addComponent(startLable, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(startLine, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10)
                        .addComponent(jb_saveConfig, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10)
                        .addComponent(jb_run, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(market, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(useProxy, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(multiThread, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(category, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jt_seller, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(threadLable, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(threadCount, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(startLable, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(startLine, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jb_saveConfig, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jb_run, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jb_run, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pane.setBorder(new EmptyBorder(15, 50, 50, 50));
        jb_saveConfig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveConfig();
            }
        });
        jb_run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveConfig();
                System.out.println("start to run");
                jb_run.setText("Running");
                try {
                    crawlerHandler.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        this.setSize(new Dimension(800, 400));
        this.add(pane);
        pack();
        this.setVisible(true);
    }

    private void saveConfig() {
        config.setCategory(jt_seller.getText());
        config.setCountry(Country.getCountry(comboBoxItems[market.getSelectedIndex()]));
        config.setMultiThread(multiThread.isSelected());
        config.setNeedProxy(useProxy.isSelected());
        config.setStartLine(Integer.parseInt(startLine.getText()));
        config.setThreadCount(Integer.parseInt(threadCount.getText()));
        Tools.saveCustomize(CONFIG_CUSTOMIZE, config);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Get Amazon Asin by category. All rights reserved by E-business");
        ConfigDialog configDialog = new ConfigDialog(frame);
    }

}
