package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class ReadFileUtil {

  public static String read(String filename) throws UnsupportedEncodingException {
    InputStream is = null;
    try {
      is = new FileInputStream(new File(filename));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    
    int size = 0;
    byte[] buf = null;
    try {
      size = is.available();
      buf = new byte[size];
      is.read(buf);
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    return new String(buf, 0, size, "GBK");
  }
  
  public static void main(String[] args) {
    try {
      System.out.println(read("example/hello.c"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }
}
