package team.redrock.reflect;

import team.redrock.annotation.GoodBoy;

@GoodBoy(age = 12)
public class Student {

    static{
        System.out.println("调用一个静态方法");
    }


    private String stuname;
    private String classroom;
    public String Openid = "1324564";
    public int OpenScore = 100;

    public void study(){
        System.out.println("学习使我快乐");
    }

    public int play(String game){
        System.out.println("熬夜玩了"+game);
        System.out.println("然后第二天挂了");
        return 0;
    }
    private void  studySecretly(){
        System.out.println("偷偷学习");
    }
}
