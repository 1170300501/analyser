package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import parser.Parser;
import unit.Production;
import utils.FrameUtil;
import utils.LineUtil;
import utils.ReadFileUtil;

public class ParserGUI {
  
  private Parser parser;
  
  public ParserGUI() {
    parser = new Parser();
    parser.init();
  }
  
  public void mainWindow() {
    // 创建新框架对象
    JFrame frame = new JFrame("语法分析器");
    // 调用框架初始化方法
    FrameUtil.initFrame(frame, 1400, 950, true);
    // 创建新的面
    JPanel panel = new JPanel();
    panel.setBackground(new Color(255,250,205));
    frame.add(panel);
    // 不使用布局管理
    panel.setLayout(null);
    Font b = new Font(Font.MONOSPACED, Font.PLAIN, 15);
    Font l = new Font("微软雅黑", Font.BOLD, 18);
    
    // 文件路径
    JTextField fileField = new JTextField();
    fileField.setFont(b);
    fileField.setOpaque(false);
    panel.add(fileField);
    fileField.setBounds(100, 50, 360, 30);
    
    // 输入标签
    JLabel in = new JLabel("输入:");
    in.setFont(l);
    panel.add(in);
    in.setBounds(100, 100, 60, 30);
    
    // 源码内容
    JTextArea codeArea = new JTextArea();
    codeArea.setFont(b);
    codeArea.setBorder(new LineBorder(new java.awt.Color(127,157,185), 1, false));
    JScrollPane jsp = new JScrollPane(codeArea);
    jsp.setOpaque(false);
    jsp.setRowHeaderView(new LineUtil());
    panel.add(jsp);
    jsp.setBounds(100, 160, 500, 250);
    
    // 词法分析结果内容
    JTextArea tokenArea = new JTextArea();
    tokenArea.setFont(b);
    tokenArea.setBorder(new LineBorder(new java.awt.Color(127,157,185), 1, false));
    JScrollPane jsp1 = new JScrollPane(tokenArea);
    jsp1.setOpaque(false);
    jsp1.setRowHeaderView(new LineUtil());
    panel.add(jsp1);
    jsp1.setBounds(100, 460, 500, 230);
    
    // 语法分析错误内容
    JTextArea errArea = new JTextArea();
    errArea.setFont(b);
    errArea.setBorder(new LineBorder(new java.awt.Color(127,157,185), 1, false));
    JScrollPane jsp2 = new JScrollPane(errArea);
    jsp2.setOpaque(false);
    jsp2.setRowHeaderView(new LineUtil());
    panel.add(jsp2);
    jsp2.setBounds(680, 540, 600, 150);
    
    // 语法分析树内容
    JTextArea treeArea = new JTextArea();
    treeArea.setFont(b);
    treeArea.setBorder(new LineBorder(new java.awt.Color(127,157,185), 1, false));
    JScrollPane jsp3 = new JScrollPane(treeArea);
    jsp3.setOpaque(false);
    jsp3.setRowHeaderView(new LineUtil());
    panel.add(jsp3);
    jsp3.setBounds(680, 90, 600, 400);
    
    // 输出TOKEN标签
    JLabel outToken = new JLabel("输出TOKEN:");
    outToken.setFont(l);
    panel.add(outToken);
    outToken.setBounds(100, 420, 140, 30);
    
    // 输出语法错误标签
    JLabel outErr = new JLabel("输出ERROR:");
    outErr.setFont(l);
    panel.add(outErr);
    outErr.setBounds(680, 500, 140, 30);
    
    // 输出语法分析树标签
    JLabel outTree = new JLabel("输出语法分析树:");
    outTree.setFont(l);
    panel.add(outTree);
    outTree.setBounds(680, 50, 140, 30);

    // 读入按钮
    JButton read = new JButton("浏览...");
    read.setFont(b);
    read.setBackground(new Color(100,149,237));

    read.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser(new File("example"));
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.showDialog(new JLabel(), "选择");
        String filepath = chooser.getSelectedFile().getAbsolutePath();
        fileField.setText(filepath);
        
        String content = null;
        try {
          content = ReadFileUtil.read(filepath);
        } catch (UnsupportedEncodingException e1) {
          e1.printStackTrace();
        }
        
        codeArea.setText(content);
      }
    });

    panel.add(read);
    read.setBounds(500, 50, 100, 30);
    
    // 分析开始按钮
    JButton action = new JButton("开始分析");
    action.setFont(b);
    action.setBackground(new Color(100,149,237));

    action.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String content = codeArea.getText();
        parser = new Parser();
        parser.init();
        parser.analyse(String.valueOf(content));
        
        tokenArea.setText(parser.getTokens()[0]);
        treeArea.setText(parser.treePreToString());
        errArea.setText(parser.getPErrors());
      }
    });

    panel.add(action);
    action.setBounds(500, 100, 100, 30);
    
    // FIRST和FOLLOW按钮
    JButton ffset = new JButton("查看FIRST集和FOLLOW集");
    ffset.setFont(b);
    ffset.setBackground(new Color(100,149,237));

    ffset.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showFF();
      }
    });

    panel.add(ffset);
    ffset.setBounds(350, 730, 250, 30);
    
    // SLR分析表按钮
    JButton table = new JButton("查看SLR分析表");
    table.setFont(b);
    table.setBackground(new Color(100,149,237));

    table.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showSLR();
      }
    });

    panel.add(table);
    table.setBounds(680, 730, 200, 30);
  }
  
  public void showFF() {
    // 创建新框架对象
    JFrame frame = new JFrame("FIRST集和FOLLOW集");
    // 调用框架初始化方法
    FrameUtil.initFrame(frame, 1000, 800, false);
    // 创建新的面
    JPanel panel = new JPanel();
    panel.setBackground(new Color(255,250,205));
    frame.add(panel);
    // 不使用布局管理
    panel.setLayout(null);
    Font l = new Font("微软雅黑", Font.BOLD, 14);
    
    // FIRST标签
    JLabel first = new JLabel("FIRST集");
    first.setFont(l);
    panel.add(first);
    first.setBounds(20, 30, 140, 30);
    
    // FOLLOW标签
    JLabel follow = new JLabel("FOLLOW集");
    follow.setFont(l);
    panel.add(follow);
    follow.setBounds(20, 380, 140, 30);
    
    Map<String, Set<String>> firsts = parser.getFirsts();
    Map<String, Set<String>> follows = parser.getFollows();
    
    List<String> terminals = parser.getTerminals();
    List<String> unTerminals = parser.getUnTerminals();
    
    int size1 = terminals.size();
    int size2 = unTerminals.size();

    String[] colname1 = new String[size1 + size2];
    String[] colname2 = new String[size1 + size2];
    
    String[][] couple1 = new String[size1 + size2 + 1][size1 + size2];
    String[][] couple2 = new String[size2][size1 + size2];
    
    couple1[0] = colname1;
    couple2[0] = colname2;
    
    for (int i = 0; i < size1; i++) {
      String t = terminals.get(i);
      couple1[i][0] = t;
      Set<String> set = firsts.get(t);
      int j = 1;
      for (String string : set) {
        couple1[i][j] = string;
        j++;
      }
    }
    
    couple1[size1][0] = "";
    couple1[size1][1] = "@";
    
    for (int i = 0; i < size2; i++) {
      String u = unTerminals.get(i);
      couple1[i + size1 + 1][0] = u;
      couple2[i][0] = u;
      
      Set<String> set1 = firsts.get(u);
      Set<String> set2 = follows.get(u);
      
      int j = 1, k = 1;
      for (String string : set1) {
        couple1[i][j] = string;
        j++;
      }
      for (String string : set2) {
        couple2[i][k] = string;
        k++;
      }
    }
    
    TableModel tableModel1 = new DefaultTableModel(couple1, colname1);
    TableModel tableModel2 = new DefaultTableModel(couple2, colname2);
    JTable table1 = new JTable(tableModel1);
    JTable table2 = new JTable(tableModel2);
    table1.setFont(l);
    table2.setFont(l);
    
    table1.setEnabled(false);
    table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    table1.setLayout(null);
    table1.setSize(100, 34);
    
    JPanel p1 = new JPanel();
    JPanel p2 = new JPanel();
    p1.setLayout(new BorderLayout());
    p2.setLayout(new BorderLayout());
    p1.add(table1);
    p2.add(table2);

    JScrollPane jsp1 = new JScrollPane(p1);
    JScrollPane jsp2 = new JScrollPane(p2);
    panel.add(jsp1);
    panel.add(jsp2);
    
    jsp1.setBounds(0, 70, 980, 300);
    jsp2.setBounds(0, 420, 980, 300);
  }
  
  /**
   * SLR分析表
   */
  public void showSLR() {
    // 创建新框架对象
    JFrame frame = new JFrame("SLR分析表");
    // 调用框架初始化方法
    FrameUtil.initFrame(frame, 1000, 700, false);
    // 创建新的面
    JPanel panel = new JPanel();
    panel.setBackground(new Color(255,250,205));
    frame.add(panel);
    // 不使用布局管理
    panel.setLayout(null);
    Font b = new Font("微软雅黑", Font.BOLD, 14);
    
    List<String> terminals = parser.getTerminals();
    List<String> unTerminals = parser.getUnTerminals();
    List<Set<Production>> closures = parser.getClosures();
    
    String[][] actions = parser.getActions();
    int[][] gotos = parser.getGotos();
    
    int size1 = terminals.size();
    int size2 = unTerminals.size();
    int size3 = closures.size();
    
    String[][] data = new String[size3 + 1][size1 + size2 + 1];
    String[] colnames = new String[size1 + size2];
    
    for (int i = 0; i < size1; i++) {
      data[0][i + 1] = terminals.get(i);
    }
    
    for (int i = 0; i < size2; i++) {
      data[0][i + size1 + 1] = unTerminals.get(i);
    }
    
    for (int i = 1; i < size3 + 1; i++) {
      data[i][0] = String.valueOf(i - 1);
      for (int j = 0; j < size1; j++) {
        data[i][j + 1] = (actions[i - 1][j].equals("error")) ? "" : actions[i - 1][j];
      }
      for (int j = 0; j < size2; j++) {
        data[i][j + size1 + 1] = (gotos[i - 1][j] != -1) ? String.valueOf(gotos[i - 1][j]) : "";
      }
    }
    
    TableModel tableModel = new DefaultTableModel(data, colnames);
    JTable table = new JTable(tableModel);
    table.setFont(b);

    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    p.add(table);
    
    JScrollPane jsp = new JScrollPane(p);
    panel.add(jsp);
    
    jsp.setBounds(0, 0, 980, 650);
  }
  
  public static void main(String[] args) {
    ParserGUI gui = new ParserGUI();
    gui.mainWindow();
  }
}
