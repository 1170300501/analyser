package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import lexer.Lexer;
import utils.FrameUtil;
import utils.ReadFileUtil;

public class LexerGUI {

  private Lexer lexer;     // 词法分析器

  public void mainWindow() {
    // 创建新框架对象
    JFrame frame = new JFrame("词法分析器");
    // 调用框架初始化方法
    FrameUtil.initFrame(frame, 1400, 950, true);
    // 创建新的面
    JPanel panel = new JPanel();
    panel.setBackground(new Color(255,250,205));
    frame.add(panel);
    // 不使用布局管理
    panel.setLayout(null);
    Font b = new Font("微软雅黑", Font.BOLD, 16);
    Font l = new Font("微软雅黑", Font.BOLD, 18);
    Font t = new Font("宋体", Font.BOLD, 40);
    
    JLabel titlelabel = new JLabel("词   法   分   析   器");
    titlelabel.setFont(t);

    panel.add(titlelabel);
    titlelabel.setBounds(430, 50, 800, 60);
    
    // 文件路径
    JTextField fileField = new JTextField();
    fileField.setFont(b);
    fileField.setOpaque(false);
    panel.add(fileField);
    fileField.setBounds(100, 170, 360, 30);
    
    // 输入标签
    JLabel in = new JLabel("输入:");
    in.setFont(l);
    panel.add(in);
    in.setBounds(100, 220, 60, 30);
    
    // 源码内容
    JTextArea codeArea = new JTextArea();
    codeArea.setFont(b);
    codeArea.setBorder(new LineBorder(new java.awt.Color(127,157,185), 1, false));
    JScrollPane jsp = new JScrollPane(codeArea);
    jsp.setOpaque(false);
    panel.add(jsp);
    jsp.setBounds(100, 260, 500, 500);
    
    // 输出标签
    JLabel outToken = new JLabel("输出TOKEN:");
    outToken.setFont(l);
    panel.add(outToken);
    outToken.setBounds(680, 160, 140, 30);
    
    // 输出标签
    JLabel outErr = new JLabel("输出ERROR:");
    outErr.setFont(l);
    panel.add(outErr);
    outErr.setBounds(680, 510, 140, 30);
    
    // 词法分析结果内容
    JTextArea tokenArea = new JTextArea();
    tokenArea.setFont(b);
    tokenArea.setBorder(new LineBorder(new java.awt.Color(127,157,185), 1, false));
    JScrollPane jsp1 = new JScrollPane(tokenArea);
    jsp1.setOpaque(false);
    panel.add(jsp1);
    jsp1.setBounds(680, 200, 600, 300);
    
    // 词法分析结果内容
    JTextArea errArea = new JTextArea();
    errArea.setFont(b);
    errArea.setBorder(new LineBorder(new java.awt.Color(127,157,185), 1, false));
    JScrollPane jsp2 = new JScrollPane(errArea);
    jsp2.setOpaque(false);
    panel.add(jsp2);
    jsp2.setBounds(680, 560, 600, 300);

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
    read.setBounds(500, 170, 100, 30);
    
    // 分析开始按钮
    JButton action = new JButton("开始分析");
    action.setFont(b);
    action.setBackground(new Color(100,149,237));

    action.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String content = codeArea.getText();
        
        lexer = new Lexer();
        lexer.init();
        lexer.discriminate(String.valueOf(content));
        
        String[] result = lexer.resToString();
        tokenArea.setText(result[0]);
        errArea.setText(result[1]);
      }
    });

    panel.add(action);
    action.setBounds(500, 780, 100, 30);
  }
  
  public static void main(String[] args) {
    LexerGUI gui = new LexerGUI();
    gui.mainWindow();
  }
}
