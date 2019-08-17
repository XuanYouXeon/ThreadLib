/*  
    名称：图书馆借书与还书模拟
    需求：多线程设计两个线程：借书线程与还书线程
    关系：现实生活中，借书与还书存在因果逻辑关系。但在此代码中，线程是否产生是相互独立，互不影响的。
    功能：
          1、借书线程功能：每执行一个借书线程，图书馆内书本库存数目减1，并且借书完成后在控制台打印图书馆剩余书本库存；
             如果发现图书馆书本库存为0，则会在控制台提示暂时无书本可以借出，并且等待一个还书线程正常执行完成后再继续执行借书过程

          2、还书线程功能：每执行一个还书线程，图书馆内书本库存数目加1，并且还书完成后在控制台打印图书馆剩余书本库存；
             如果发现图书馆书本库存已满，则会在控制台提示无需还书，并且提前终止线程

          3、假定程序初始化时图书馆书本库存为1，库存上限为3（即有2本处于已经借出的状态）

*/

/*
    名称：图书馆借书与还书模拟
    需求：多线程设计两个线程：借书线程与还书线程
    关系：现实生活中，借书与还书存在因果逻辑关系。但在此代码中，线程是否产生是相互独立，互不影响的。
    功能：
          1、借书线程功能：每执行一个借书线程，图书馆内书本库存数目减1，并且借书完成后在控制台打印图书馆剩余书本库存；
             如果发现图书馆书本库存为0，则会在控制台提示暂时无书本可以借出，并且等待一个还书线程正常执行完成后再继续执行借书过程

          2、还书线程功能：每执行一个还书线程，图书馆内书本库存数目加1，并且还书完成后在控制台打印图书馆剩余书本库存；
             如果发现图书馆书本库存已满，则会在控制台提示无需还书，并且提前终止线程

          3、假定程序初始化时图书馆书本库存为1，库存上限为3（即有2本处于已经借出的状态）

*/
import java.util.concurrent.locks.*;

class ThreadLib
{
    public static void main(String[] args)
    {
        Resource r = new Resource();
        Return ret = new Return(r);
        Borrow bor = new Borrow(r);

        Thread b1 = new Thread(bor);
        Thread b2 = new Thread(bor);
        Thread b3 = new Thread(bor);
        Thread b4 = new Thread(bor);
        Thread b5 = new Thread(bor);

        Thread r1 = new Thread(ret);
        Thread r2 = new Thread(ret);
        Thread r3 = new Thread(ret);
        Thread r4 = new Thread(ret);
        Thread r5 = new Thread(ret);

        b1.start();
        b2.start();
        b3.start();
        b4.start();
        b5.start();

        r1.start();
        r2.start();
        r3.start();
        r4.start();
        r5.start();
    }
}

public class Resource //源
{
    private Lock lock = new ReentrantLock();
    public static String book;
    public static int count = 3;
    public static boolean flag = false;

    private Condition condition = lock.newCondition();

    public void set(String book)throws InterruptedException
    {
        lock.lock();
        try
        {
            while(flag)
                condition.await();
            System.out.println(Thread.currentThread().getName() + "图书馆剩余书本库存：" + this.count++);
            flag = true;
            condition.signalAll();
            if(count==0)
            {
                System.out.println("暂无书本可以借出");
                condition.await();
            }
        }
        finally
        {
            lock.unlock();
        }
    }

    public void out()throws InterruptedException//extends Thread
    {
        lock.lock();
        try
        {
            while (!flag)
                condition.await();
            System.out.println(Thread.currentThread().getName() + "图书馆剩余书本库存：" + this.count--);
            flag = false;
            condition.signalAll();
            if(count>3)
            {
                flag = false;
                while(!flag)
                    condition.signalAll();
                System.out.println("无需还书");
            }
        }
        finally
        {
            lock.unlock();
        }
    }

}

public class Return implements Runnable
{
    public Resource res;
    Return(Resource res)
    {
        this.res = res;
    }
    public void run()
    {
        while(true)
        {
            try
            {
                res.out();
            }
            catch (InterruptedException e)
            {

            }
        }
    }
}

public class Borrow implements Runnable//extends Thread
{
    public Resource res;
    Borrow(Resource res)
    {
        this.res = res;
    }
    public void run()
    {
        while(true)
        {
            try
            {
                res.set("图书");
            }
            catch (InterruptedException e)
            {

            }

        }
    }

}


