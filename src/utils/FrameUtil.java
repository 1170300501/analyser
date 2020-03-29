package utils;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class FrameUtil {
  public static void initFrame(JFrame frame,int width,int height ) {
    //获取默认系统工具包
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    //获取屏幕的分辨率
    Dimension dimension = toolkit.getScreenSize();
    int x = (int)dimension.getWidth();
    int y = (int)dimension.getHeight();
    frame.setBounds((x-width)/2, (y-height)/2, width, height);
    //设置窗体的可见性
    frame.setVisible(true);
    //设置窗体关闭
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
}
