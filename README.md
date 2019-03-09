# 多线程/IO

> CPU是以时间片的方式为进程分配CPU处理时间的,为了提高CPU的使用率，采用多线程的方式去同时完成几件事情而互不干扰,从而提高CPU的使用效率

####多线程的使用场景 

+ 多线程同时完成多个任务

  ```java
  //多线程核心代码 
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
                  System.out.println(src.getName()+"以复制进度"+df.format(d));
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
  ```

+ 多线程协同完成单一任务

```java
//多线程核心代码
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
            countDownLatch.countDown();
        }
        }
```

```java
//线程调用方法
private static Executor executor = Executors.newCachedThreadPool();
    private static void MutiCopy(String src,String dest,int count) {
        File file  = new File(src);
        long len= file.length();
        int oneNum = (int) (len/count);
        System.out.println(oneNum);
        int num = 0;
        //
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
```

####sleep()、join()、CountDownLatch类之间的区别  

>Sleep方法是个静态方法，由thread类来调用。**它只能暂停当前线程，不能暂停其他线程**。它接受的参数指名这个线程需要休眠多少时间。

>Join方法是非静态方法，他使得在系统调用此方法时**只有这个线程执行完后** ，才能执行其他线程，包括主线程的终止；或者给它制定时间，即最多过了这么多时间后，如果还是没有执行完，下面的线程可以继续执行而不必再理会当前线程是否执行完。

>CountDownLatch是一个同步工具类，它允许一个或多个线程一直等待，直到其他线程的操作执行完后再执行。调用join方法需要等待thread执行完毕才能继续向下执行,而CountDownLatch只需要检查计数器的值为零就可以继续向下执行

#### Thread和Runnable之间的关系

+ 实现Runnable更加适合资源共享
+ 实现Runnable接口避免单点继承的局限，一个类可以继承多个接口

备注：

- Runnable始终使用一个对象的计数器，Thread则每次创建一个计数器
- Thread通过static属性同样可以实现资源共享
- 注意添加同步锁



####线程池

+ 更好调度和管理线程
+ 种类
  + FixedThreadPool
  + SingleThreadExecutor
  + CachedThreadPool
  + SingleThreadSchduledExecutor
  + ScheduledThreadPool

> 使用线程池的好处是减少在创建和销毁线程上所花的时间以及系统资源的开销，解决系统资源不足的问题。如果不适用线程池，有可能造成系统创建大量同类线程而导致消耗完内存或者"过度切换的问题"。



####单线程和多线程 

> **进程只有一个，所以分配的CPU资源是一定的，多线程只不过是轮流抢占CPU而已**，并不会真正提高处理速度。这意味着，**多线程的作用主要在于提高了并发数量，比如http请求，如果是单线程，一次只能接收一个请求，多线程则可以同时接收多个请求**

> **但是多线程由于轮换使用CPU，会造成单个线程的执行速度变慢**（以前CPU供一个线程使用，现在要供多个线程轮流使用了）。**但是在多CPU的服务器上，多线程就很有优势了，它不但能提高并发数量，而且能提高处理速度**。因为在多CPU的服务器上，CPU调度很灵活，当一个线程占用着一个CPU的时候，其他线程可以被分配给其他CPU去处理，从而实现了“真正意义上地并行”





# 反射



### 反射的概念

> 虚拟机在运行状态中，可以动态加载一个只有名称的类，加载完类之后，在堆内存中，就产生了一个 Class 类型的对象，这个对象就包含了完整的结构信息，通过这个对象我们可以看到类的结构。



反射的用处

+ 动态加载类，在运行期间可以打开和检查.class文件，动态获取类、方法、属性等
+ 广泛应用于各类框架，如spring



### Class类

> 在Java中每个类都有一个Class对象，每当我们编写并且编译一个新创建的类就会产生一个对应Class对象并且这个Class对象会被保存在同名.class文件里(编译后的字节码文件保存的就是Class对象)

> *\*内存中class对象只有一个，只能被JVM创建并加载*



类的生成过程

+ 加载
+ 连接
+ 初始化

![img](assets/20170430160610299.png)



**获取 class类的三种方式** 

+ 通过类名获取 :  类名.class

+ 通过对象获取 : 对象名.getClass()  

  > 获取字面常量的Class引用时，不会触发初始化

+ 通过全类名获取 :  Class.ForName(全类名)



####类加载器 

> 负责将.class文件加载到内存中，并为之生成对应的Class对象



![img](assets/181711178455.jpg)

组成

+ *Bootstrap ClassLoader 根类加载器* 

  负责Java核心类的加载，比如System类，在JDK中JRE的lib目录下rt.jar文件中的类

+ *Extension ClassLoader 扩展类加载器* 

  负责JRE的扩展目录中jar包的加载，在JDK中JRE的lib目录下ext目录

+ *System ClassLoader 系统类加载器* 

  负责在JVM启动时加载来自java命令的class文件，以及classpath环境变量所指定的jar包和类路径

```java
public void testClassLoader() throws IOException {
        InputStream in1 = null;
        in1 = this.getClass().getClassLoader().getResourceAsStream("reflect/1.txt");

        System.out.println(in1.read());
    }
```











### 基本操作

####类

+ 获取类的方法
+ 获取类名以及其它信息

```java
private static void test1() throws ClassNotFoundException {
        String path = "team.redrock.reflect.Student";
        Class cls1 = Class.forName(path);
//        Class cls2 = Student.class;
        System.out.println(cls1.getName());
        System.out.println(cls1.getSimpleName());
    
  //      Student student = new Student();
    //    Class cls3 = student.getClass();
      //  System.out.println(cls3.getName());
    }
```

####属性 

+ 无参获取公有属性
+ 无参获取私有属性
+ 获取公有属性值
+ 获取私有属性值

```java
private  static void test2(Class cls){
        //获取公有属性
        Field[] fields = cls.getFields();
        for(Field f : fields) {
//            System.out.println(f.getName());
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
```

+ new和newInstance的区别
  + new=初始化+实例化
  + newInstance=实例化
+ 必须根据实例获取属性和方法

####方法 

+ 获取公有方法
+ 获取私有方法
+ 获取指定方法
+ 调用方法
+ 调用父类的私有方法

```java
private static void test3(Class cls){
//        Method[] methods = cls.getMethods();  //不能获取私有方法
//        for(Method method:methods){
//            System.out.println(method.getName());
//        }

//        Method[] methods1 = cls.getDeclaredMethods();   //不能获取父类方法
//        for(Method method:methods1){
//            System.out.println(method.getName());
//        }
        Method method = null;
        try {
//            method = cls.getMethod("studySecretly",null);    //和无参获取method一样
            method = cls.getDeclaredMethod("studySecretly");
            Object instance = cls.getDeclaredConstructor().newInstance();
            method.setAccessible(true);  //调用位置注意一下
            method.invoke(instance);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

```



####注解 

+ 利用反射实现注解功能

```java
	private static void test4(Class cls){

        try {
            Object object = cls.getDeclaredConstructor().newInstance();
            Annotation annotation = (Annotation) cls.getAnnotation(GoodBoy.class);
            if(annotation !=null){
                if(annotation instanceof GoodBoy){   //该注解是否是定义注解类的一个实例
                    GoodBoy goodBoy = (GoodBoy) annotation;
                    System.out.println(goodBoy.value());//实现注解逻辑的地方
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
```



> 如果在程序中要获取注解，然后获取注解的值进而判断我们赋值是否合法，那么类对象的创建和方法的创建必须是通过反射而来的

####构造器 

+ 利用构造器创建对象

```java
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
```





