package gds.application.test;
 
import java.util.Random;
 
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

//(name="BitCoinDataCenter",urlPatterns = "/BitCoinDataCenter",loadOnStartup=1) //标记为Servlet不是为了其被访问，而是为了便于伴随Tomcat一起启动
@WebServlet
public class BitCoinDataCenter extends HttpServlet implements Runnable {
 
	
    /**
	 * 生成唯一序列号
	 */
	private static final long serialVersionUID = -4886544031232864785L;

	public void init(ServletConfig config){
        startup();
    }
     
    public  void startup(){
        new Thread(this).start();
    }
    
    public void run() {
        int bitPrice = 100000;
        while(true){
             
            //每隔1-3秒就产生一个新价格
            int duration = 1000+new Random().nextInt(2000);
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            //新价格围绕100000左右50%波动
            float random = 1+(float) (Math.random()-0.5);
            int newPrice = (int) (bitPrice*random);
             
            //查看的人越多，价格越高
            int total = ServerManager.getTotal();
            newPrice = newPrice*total;
             
            String messageFormat = "{\"price\":\"%d\",\"total\":%d}";
            String message = String.format(messageFormat, newPrice,total);

            //广播出去
            ServerManager.broadCast(message);
        }
    }
}