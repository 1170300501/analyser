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
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import parser.Parser;
import semanticer.Semanticer;
import unit.Production;
import utils.FrameUtil;
import utils.LineUtil;
import utils.ReadFileUtil;

public class SemGUI {
 private Semanticer semer;
  
  public SemGUI() {
    semer = new Semanticer();
  }
  
  public void mainWindow() {
    // 创建新框架对象
    JFrame frame = new JFrame("语义分析器");
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
    jsp.setBounds(100, 160, 500, 280);
    
    // 词法分析结果内容
    JTextArea tokenArea = new JTextArea();
    tokenArea.setFont(b);
    tokenArea.setBorder(new LineBorder(new java.awt.Color(127,157,185), 1, false));
    JScrollPane jsp1 = new JScrollPane(tokenArea);
    jsp1.setOpaque(false);
    jsp1.setRowHeaderView(new LineUtil());
    panel.add(jsp1);
    jsp1.setBounds(100, 520, 900, 180);
    
    // 语法分析树内容
    JTextArea treeArea = new JTextArea();
    treeArea.setFont(b);
    treeArea.setBorder(new LineBorder(new java.awt.Color(127,157,185), 1, false));
    JScrollPane jsp3 = new JScrollPane(treeArea);
    jsp3.setOpaque(false);
    jsp3.setRowHeaderView(new LineUtil());
    panel.add(jsp3);
    jsp3.setBounds(680, 90, 600, 350);
    
    // 输出TOKEN标签
    JLabel outToken = new JLabel("输出ERROR:");
    outToken.setFont(l);
    panel.add(outToken);
    outToken.setBounds(100, 480, 140, 30);
    
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
        semer = new Semanticer();
        semer.init();
        semer.analyse(String.valueOf(content));
        
        tokenArea.setText(semer.getSErrors());
        treeArea.setText(semer.treePreToString());
      }
    });

    panel.add(action);
    action.setBounds(500, 100, 100, 30);
    
    // FIRST和FOLLOW按钮
    JButton ffset = new JButton("查看三地址码和四元式");
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
    JButton table = new JButton("查看符号表");
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
    JFrame frame = new JFrame("三地址码和四元式");
    // 调用框架初始化方法
    FrameUtil.initFrame(frame, 800, 600, false);
    // 创建新的面
    JPanel panel = new JPanel();
    panel.setBackground(new Color(255,250,205));
    frame.add(panel);
    // 不使用布局管理
    panel.setLayout(null);
    Font b = new Font(Font.MONOSPACED, Font.PLAIN, 15);
    
    JTextPane jtp1 = new JTextPane();
    jtp1.setFont(b);
    jtp1.setText(semer.getctrCodeLines());
    
    JScrollPane jsp1 = new JScrollPane(jtp1);
    jsp1.setOpaque(false);
    panel.add(jsp1);
    jsp1.setBounds(50, 50, 300, 400);
    
    panel.add(jsp1);
    
    JTextPane jtp2 = new JTextPane();
    jtp2.setFont(b);
    jtp2.setText(semer.getQuaternary());
    
    JScrollPane jsp2 = new JScrollPane(jtp2);
    jsp2.setOpaque(false);
    jsp2.setRowHeaderView(new LineUtil());
    panel.add(jsp2);
    jsp2.setBounds(400, 50, 300, 400);
    
    panel.add(jsp2);
  }
  
  /**
   * SLR分析表
   */
  public void showSLR() {
    // 创建新框架对象
    JFrame frame = new JFrame("符号表");
    // 调用框架初始化方法
    FrameUtil.initFrame(frame, 800, 600, false);
    // 创建新的面
    JPanel panel = new JPanel();
    panel.setBackground(new Color(255,250,205));
    frame.add(panel);
    // 不使用布局管理
    panel.setLayout(null);
    Font b = new Font(Font.MONOSPACED, Font.PLAIN, 15);
    
    JTextPane jtp1 = new JTextPane();
    jtp1.setFont(b);
    jtp1.setText(semer.getTblptr());
    
    JScrollPane jsp1 = new JScrollPane(jtp1);
    jsp1.setOpaque(false);
    panel.add(jsp1);
    jsp1.setBounds(50, 50, 700, 400);
    
    panel.add(jsp1);
  }
  
  public static void main(String[] args) {
    SemGUI gui = new SemGUI();
    gui.mainWindow();
  }
}
