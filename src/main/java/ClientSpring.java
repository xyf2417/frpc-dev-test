import java.util.concurrent.TimeUnit;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import xyf.frpc.config.Application;
import xyf.frpc.config.ProtocolConfig;
import xyf.frpc.config.RegistryConfig;
import xyf.frpc.dev.test.interfaces.INoexist;
import xyf.frpc.dev.test.interfaces.ITest;


public class ClientSpring {
	public static void main(String[] args) throws InterruptedException {
		FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("D:\\developer\\java\\myeclipse\\frpc-dev-test\\spring-reference.xml");
        context.start();
        
        
        ProtocolConfig p = (ProtocolConfig) context.getBean("protocol");
        System.out.println("client: " + p.getId() + " " + p.getName() + " " + p.getPort());
        
        RegistryConfig r = (RegistryConfig)context.getBean("registry");
        System.out.println("client: " + r.getAddresses());
        
        Application app = (Application)context.getBean("application");
        System.out.println("client: " + app.getName());
        
        System.out.println("-----------------------------------------------------");
        Object o = context.getBean("referenceTest");
        ITest itest = (ITest)o;
        //System.out.println("App: " + re.getId() + " " + re.getName() + " " + re.getInterface() + " " + re.getHost());
        System.out.println("client: " +itest.fun("client arg"));

        TimeUnit.SECONDS.sleep(10);
        
        System.out.println("client: " + itest.fun("arg2"));
        
//        INoexist inoe = (INoexist)context.getBean("noExistTest");
//        
// 
//        System.out.println("client: " + inoe.nomethod(10011));
//        
//        System.out.println("client: " + inoe.toString());
        
	}
}
