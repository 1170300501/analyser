package lexergui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import lexer.Lexer;
import unit.Token;
import utils.FrameUtil;
import utils.ReadFileUtil;

public class LexerGUI {

  //private File file;       // 被读取文件
  private String content;  // 文本
  private Lexer lexer;     // 词法分析器
  
  public LexerGUI() {
    lexer = new Lexer();
  }
  
  public void mainWindow() {
    // 创建新框架对象
    JFrame frame = new JFrame("词法分析器");
    // 调用框架初始化方法
    FrameUtil.initFrame(frame, 1400, 950);
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
    codeArea.setLineWrap(true);
    codeArea.setOpaque(false);
    panel.add(codeArea);
    codeArea.setBounds(100, 260, 500, 500);
    
    // 输出标签
    JLabel out = new JLabel("输出:");
    out.setFont(l);
    panel.add(out);
    out.setBounds(680, 170, 60, 30);
    
    // 词法分析结果内容
    JTextArea tokenArea = new JTextArea();
    tokenArea.setFont(b);
    tokenArea.setBorder(new LineBorder(new java.awt.Color(127,157,185), 1, false));
    tokenArea.setLineWrap(true);
    tokenArea.setOpaque(false);
    panel.add(tokenArea);
    tokenArea.setBounds(680, 210, 600, 600);

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
        
        content = ReadFileUtil.read(filepath);
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
        content = codeArea.getText();
        System.out.println(content);
        lexer.init();
        
        String result = lexer.tokensToString(lexer.discriminate(content));
        tokenArea.setText(result);
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
