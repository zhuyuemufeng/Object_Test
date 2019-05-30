import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * CompanyService的服务启动类
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
public class CompanyProvider_system {

    public static void main(String[] args) throws Exception{
        //1.读取配置文件
        ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:Spring/application_*.xml");
        //2.启动容器
        ac.start();
        //3.任意键退出
        System.in.read();

    }
}
