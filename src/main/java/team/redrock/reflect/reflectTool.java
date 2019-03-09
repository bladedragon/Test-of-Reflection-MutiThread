package team.redrock.reflect;

import team.redrock.annotation.GoodBoy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.ClosedSelectorException;

public class reflectTool {

    private static void test1() throws ClassNotFoundException {
        String path = "team.redrock.reflect.Student";
        Class cls = Class.forName(path);
        Class cls2 = Student.class;
        System.out.println(cls);
        System.out.println(cls2.getSimpleName());

    }


    private  static void test2(Class cls){
        //获取公有属性
        Field[] fields = cls.getFields();
        for(Field f : fields) {
//            System.out.println(f.getName());

//            try {
//                System.out.println(f.get(cls.getDeclaredConstructor().newInstance()));
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            }
        }

        //获取全部属性
        Field[] fieldss = cls.getDeclaredFields();
        for(Field f : fieldss){

            //如果字段是私有的，不管是读值还是写值，都必须先调用setAccessible（true）方法
            f.setAccessible(true);
            try {
                System.out.println(f.get(cls.getDeclaredConstructor().newInstance()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    private static void test3(Class cls){
//        Method[] methods = cls.getMethods();
//        for(Method method:methods){
//            System.out.println(method.getName());
//
//        }
//        System.out.println("------");

//        Method[] methods1 = cls.getDeclaredMethods();
//        for(Method method:methods1){
//            System.out.println(method.getName());
//        }
//        System.out.println("------");

        Method method = null;
        try {

//            method = cls.getMethod("studySecretly",null);    //和无参获取method一样
            method = cls.getDeclaredMethod("play",String.class);
            Object instance = cls.getDeclaredConstructor().newInstance();
            method.setAccessible(true);  //调用位置注意一下
            method.invoke(instance,"lol");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

   private static void test4(Class cls){

        try {
            Object object = cls.getDeclaredConstructor().newInstance();
            Annotation annotation = (Annotation) cls.getAnnotation(GoodBoy.class);
            if(annotation !=null){
                if(annotation instanceof GoodBoy){
                    GoodBoy goodBoy = (GoodBoy) annotation;
                    System.out.println(goodBoy.age());
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static void test5(Class cls){
        Constructor<Student>[] constructors = cls.getConstructors();
        for (Constructor<Student> constructor1 :constructors){
            System.out.println(constructor1);
            try {
                Student student = constructor1.newInstance();
                System.out.println(student.play("dota"));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }


    }


    public static void main(String[] args) {
        try {
            String path = "team.redrock.reflect.Student";
            Class cls = Class.forName(path);
            test3(cls);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
