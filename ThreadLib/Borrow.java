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