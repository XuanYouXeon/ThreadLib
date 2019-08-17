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
