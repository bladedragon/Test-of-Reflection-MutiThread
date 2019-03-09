package team.redrock.mutiThread;

import java.io.*;
import java.text.DecimalFormat;
import java.util.concurrent.CountDownLatch;

public class FileCopy2 implements Runnable {

    private int startPos;
    private int endPos;
    private File src;
    private File dest;
    private int j;
    CountDownLatch countDownLatch;

    public FileCopy2(String src,String dest,int startPos,int endPos,int j,CountDownLatch countDownLatch){
        this.startPos = startPos;
        this.endPos = endPos;
        this.src = new File(src);
        this.dest = new File(dest);
        this.j = j;
        this.countDownLatch = countDownLatch;
    }

    public  void run() {
        //可以从任意位置进行读写文件
        RandomAccessFile in =null;
        RandomAccessFile out = null;
        try {

             in =   new RandomAccessFile(src, "r");
             out = new RandomAccessFile(dest, "rw");

            in.seek(startPos);
            out.seek(endPos);
            int size = endPos-startPos;
            int lenth=0;
            double temp =0;
//            判断剩余长度是否大于1024
            int bufLen = (int) (size < 1024 ? src.length() : 1024);
//            int bufLen = 1024;
            byte[] b = new byte[bufLen];
            //数字格式化，显示百分比
            DecimalFormat df = new DecimalFormat("##.00%");
                while ((lenth = in.read(b)) != -1) {
                    out.write(b, 0, lenth);
                    temp += lenth;
                    double d = temp / size;
                    System.out.println("线程" + j + "复制进度" + df.format(d));
                    if (size - temp < bufLen) {
                        bufLen = (int) (size - temp);
                        if (bufLen == 0) {
                            break;
                        }
                        b = new byte[bufLen];
                    }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("运行到这里");
            countDownLatch.countDown();        //计数器减1
        }
        }

    }

