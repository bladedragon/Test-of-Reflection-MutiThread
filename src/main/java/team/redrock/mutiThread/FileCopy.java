package team.redrock.mutiThread;

import java.io.*;
import java.text.DecimalFormat;

public class FileCopy extends Thread {
    private File src;
    private File dest;

    public FileCopy(String src,String dest){
        this.src = new File(src);
        this.dest = new File(dest);
    }

    @Override
    public void run(){
        FileInputStream in = null;
        FileOutputStream out  = null;

        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dest);

            int buf = 1024;
            byte[] b =  new byte[buf];
            int lenth = 0;
            long len = src.length();
//            //已复制文件的字节数
            double temp = 0;
            //数字格式化，显示百分比
            DecimalFormat df = new DecimalFormat("##.00%");
            while ((lenth = in.read(b)) != -1) {
                out.write(b,0,lenth);
                //获取已下载的大小，并且转换成百分比
                temp += lenth;
                double d = temp/len;
                System.out.println(src.getName()+"已复制进度"+df.format(d));
                //线程阻塞，实现同步
//                Thread.sleep(1);
                if (len - temp < buf) {
                    buf = (int) (len - temp);
                    if (buf == 0) {
                        break;
                    }
                    b = new byte[buf];
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
                try {
                    if(in !=null){
                        in.close();
                    }
                    if(out != null){
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
