import java.util.concurrent.locks.*;

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