package com.weblab.crawler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.Enumeration;

public class CrawlerFrame extends JFrame implements ActionListener {
    private JTabbedPane tabbedPane;
    private JPanel panel, panel1, panel2, panel3, panel4, panel5, panel6, panel7,
            panel8;
    private JLabel label1, label2, label3, label4, label5, label6, label7;
    private JTextField textField, textField2, textField3, textField4, textField5;
    private JTextPane textPane;
    private JTextArea textArea;
    private JButton button, button1, button2, button3, button4;

    private GridLayout gridlayout;
    private GridBagLayout gridbaglayout;
    private GridBagConstraints constraints;

    private Crawler crawler;

    private static final String defaultTitle = "cloud";
    private static final int defaultUrlCount = 1000;
    private static final int defaultThreadCount = 5;
    private static final double defaultThreshold = 0.91;
    private static final String defaultStartURL = "https://www.csdn.net/nav/cloud";

    private CrawlerFrame() {
        textPane = new JTextPane();
        label1 = new JLabel("total access:");
        button = new JButton("start");
        crawler = new Crawler("cloud native", "https://www.csdn.net/nav/cloud", textPane,
                label1, button);
        crawler.addKeyWord("cloud native");
        crawler.addKeyWord("data center");
        crawler.addKeyWord("platform");
        crawler.addKeyWord("architecture");
        crawler.addKeyWord("database");
        crawler.addKeyWord("security");
        crawler.addKeyWord("Hadoop");
        crawler.addKeyWord("storage");
        crawler.addKeyWord("virtual");
        crawler.addKeyWord("privacy");
        crawler.addKeyWord("hacker");
        crawler.addKeyWord("distributed");
        crawler.addKeyWord("MapReduce");
        crawler.addKeyWord("cloud");

        setTitle("theme crawler");
        setBounds(50, 50, 1000, 600);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        panel1 = new JPanel(new BorderLayout());

        panel1.add(new JScrollPane(textPane), BorderLayout.CENTER);

        panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));

        button.addActionListener(this);
        button1 = new JButton("access");
        button1.addActionListener(this);

        button2 = new JButton("stop");
        button2.addActionListener(this);
        panel3.add(button);
        panel3.add(button1);
        panel3.add(button2);
        panel1.add(panel3, BorderLayout.EAST);

        gridlayout = new GridLayout(1, 2);
        panel4 = new JPanel(gridlayout);

        panel4.add(label1);
        panel1.add(panel4, BorderLayout.SOUTH);

        gridbaglayout = new GridBagLayout();
        constraints = new GridBagConstraints();

        panel2 = new JPanel(gridbaglayout);

        panel5 = new JPanel();
        label2 = new JLabel("theme:");
        textField = new JTextField(44);
        textField.setText(crawler.getTitle());
        panel5.add(label2);
        panel5.add(textField);

        panel6 = new JPanel();
        label3 = new JLabel("first page:");
        textField2 = new JTextField(44);
        textField2.setText(crawler.getStartURL());
        panel6.add(label3);
        panel6.add(textField2);

        panel7 = new JPanel();
        label4 = new JLabel("url count:");
        textField3 = new JTextField(11);
        textField3.setText(String.valueOf(crawler.getUrlCount()));
        label5 = new JLabel("thread number:");
        textField4 = new JTextField(11);
        textField4.setText(String.valueOf(crawler.getThreadCount()));
        label6 = new JLabel("threshold:");
        textField5 = new JTextField(11);
        textField5.setText(String.valueOf(crawler.getThreshold()));
        panel7.add(label4);
        panel7.add(textField3);
        panel7.add(label5);
        panel7.add(textField4);
        panel7.add(label6);
        panel7.add(textField5);

        label7 = new JLabel(" keyword:");

        textArea = new JTextArea();
        textArea.setColumns(50);
        textArea.setRows(10);
        textArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textArea);

        Enumeration<String> keywords = crawler.getKeyWords();
        while (keywords.hasMoreElements()) {
            textArea.append(keywords.nextElement() + "|");
        }

        panel8 = new JPanel();
        button3 = new JButton("apply");
        button3.addActionListener(this);
        button4 = new JButton("reset");
        button4.addActionListener(this);
        panel8.add(button3);
        panel8.add(button4);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(0, 10, 0, 10);
        gridbaglayout.setConstraints(panel5, constraints);
        panel2.add(panel5);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(0, 10, 0, 10);
        gridbaglayout.setConstraints(panel6, constraints);
        panel2.add(panel6);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(0, 10, 0, 10);
        gridbaglayout.setConstraints(panel7, constraints);
        panel2.add(panel7);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(0, 10, 0, 10);
        gridbaglayout.setConstraints(label7, constraints);
        panel2.add(label7);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(0, 10, 0, 10);
        gridbaglayout.setConstraints(scrollPane, constraints);
        panel2.add(scrollPane);

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(0, 10, 0, 10);
        gridbaglayout.setConstraints(panel8, constraints);
        panel2.add(panel8);

        tabbedPane.addTab("home", panel1);

        tabbedPane.addTab("setting", panel2);

        panel.add(tabbedPane, BorderLayout.CENTER);
        this.add(panel);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(String.valueOf(new UIManager()));
        } catch (Exception e) {
            System.out.println("Substance Raven Graphite failed to initialize");
        }
        new CrawlerFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            textPane.setText("");
            crawler.initialize();
            crawler.parallelHandle();
            button.setEnabled(false);
        } else if (e.getSource() == button1) {
            String web = textPane.getSelectedText();
            URI uri;
            try {
                uri = new URI(web);
                Desktop.getDesktop().browse(uri);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "Wrong URL address!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == button2) {
            crawler.stopSearch();
            button.setEnabled(true);
        } else if (e.getSource() == button3) {
            crawler.setTitle(textField.getText());
            crawler.setStartURL(textField2.getText());
            try {
                int urlCount = Integer.parseInt(textField3.getText());
                crawler.setUrlCount(urlCount);
            } catch (NumberFormatException ee) {
                JOptionPane.showMessageDialog(null,
                        "Format Error of Url Count!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            try {
                int threadCount = Integer.parseInt(textField4.getText());
                crawler.setThreadCount(threadCount);
            } catch (NumberFormatException ee) {
                JOptionPane.showMessageDialog(null,
                        "Format Error of thread Count!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            try {
                double threshold = Double.parseDouble(textField5.getText());
                crawler.setThreshold(threshold);
            } catch (NumberFormatException ee) {
                JOptionPane.showMessageDialog(null,
                        "Format Error of threshold!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            crawler.removeAllKeyWords();
            String[] keywords = textArea.getText().split("\\|");
            for (String keyword : keywords) {
                crawler.addKeyWord(keyword);
            }
            JOptionPane.showMessageDialog(null,
                    "Succeed to set the configuration!", "Set",
                    JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == button4) {
            crawler.setStartURL(defaultStartURL);
            crawler.setTitle(defaultTitle);
            crawler.setThreadCount(defaultThreadCount);
            crawler.setThreshold(defaultThreshold);
            crawler.setUrlCount(defaultUrlCount);
            crawler.removeAllKeyWords();
            crawler.addKeyWord("cloud native");
            crawler.addKeyWord("data center");
            crawler.addKeyWord("platform");
            crawler.addKeyWord("architecture");
            crawler.addKeyWord("database");
            crawler.addKeyWord("security");
            crawler.addKeyWord("Hadoop");
            crawler.addKeyWord("storage");
            crawler.addKeyWord("virtual");
            crawler.addKeyWord("privacy");
            crawler.addKeyWord("hacker");
            crawler.addKeyWord("distributed");
            crawler.addKeyWord("MapReduce");
            crawler.addKeyWord("cloud");

            textArea.setText("");
            Enumeration<String> keywords = crawler.getKeyWords();
            while (keywords.hasMoreElements()) {
                textArea.append(keywords.nextElement() + "|");
            }

            textField.setText(defaultTitle);
            textField2.setText(defaultStartURL);
            textField3.setText(String.valueOf(defaultUrlCount));
            textField4.setText(String.valueOf(defaultThreadCount));
            textField5.setText(String.valueOf(defaultThreshold));

            JOptionPane.showMessageDialog(null,
                    "Set to the default configuration!", "Default",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

