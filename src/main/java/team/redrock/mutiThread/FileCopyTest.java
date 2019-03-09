package team.redrock.mutiThread;

import java.io.File;
import java.util.concurrent.*;

public class FileCopyTest {

    private static Executor executor = Executors.newCachedThreadPool();
    private static void MutiCopy(String src,String dest,int count) {

        File file  = new File(src);
        long len= file.length();
        int oneNum = (int) (len/count);
        System.out.println(oneNum);
        int num = 0;
        CountDownLatch latch = null;
        if((num= (int) (len%count))!=0){
            System.out.println("剩余："+(len%count));
           latch = new CountDownLatch(count+1);
            executor.execute(new FileCopy2(src,dest,(int)(len-len%count),(int)len,num,latch));
        }else{
            System.out.println("整除");
            latch = new CountDownLatch(count);
        }

        for(int i =0;i <count;i++){
            executor.execute(new FileCopy2(src,dest,oneNum*i,oneNum*(i+1),i,latch));
            System.out.println("线程"+i+"启动");
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        FileCopy fileCopy1 = new FileCopy("D:\\JAVA学习\\nginx\\Nginx.md","D:\\temp\\1.md");
//        FileCopy fileCopy2 = new FileCopy("D:\\JAVA学习\\nginx\\20150702114658957.png","D:\\temp\\2.png");
//        FileCopy fileCopy3 = new FileCopy("D:\\ICT\\通信ict战队—机器解码.xlsx","D:\\temp\\3.xlsx");
//        FileCopy fileCopy4 = new FileCopy("E:\\CloudMusic\\Butter-Fly.mp3","D:\\temp\\4.mp3");
//        fileCopy1.start();
//        fileCopy2.start();
//        fileCopy3.start();
//        fileCopy4.start();
//
         MutiCopy("E:\\CloudMusic\\Butter-Fly.mp3","D:\\temp\\5.mp3",1);
        MutiCopy("E:\\CloudMusic\\Butter-Fly.mp3","D:\\temp\\6.mp3",20);


    }
}
