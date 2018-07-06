import org.springframework.context.support.FileSystemXmlApplicationContext;

import xyf.frpc.config.Application;
import xyf.frpc.config.ProtocolConfig;
import xyf.frpc.config.RegistryConfig;
import xyf.frpc.dev.test.interfaces.ITest;


public class ClientSpring {
	public static void main(String[] args) {
		FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("D:\\developer\\java\\myeclipse\\frpc-dev-test\\spring-reference.xml");
        context.start();
        
        
        ProtocolConfig p = (ProtocolConfig) context.getBean("protocol");
        System.out.println("App: " + p.getId() + " " + p.getName() + " " + p.getPort());
        
        RegistryConfig r = (RegistryConfig)context.getBean("registry");
        System.out.println("App: " + r.getAddresses());
        
        Application app = (Application)context.getBean("application");
        System.out.println("App: " + app.getName());
        
        System.out.println("-----------------------------------------------------");
        Object o = context.getBean("referenceTest");
        ITest itest = (ITest)o;
        //System.out.println("App: " + re.getId() + " " + re.getName() + " " + re.getInterface() + " " + re.getHost());
        System.out.println("client: " +itest.fun("client arg"));
        
        
	}
}
