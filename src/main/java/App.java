import org.springframework.context.support.FileSystemXmlApplicationContext;

import xyf.frpc.config.Application;
import xyf.frpc.config.ProtocolConfig;
import xyf.frpc.config.Provider;
import xyf.frpc.config.RegistryConfig;
import xyf.frpc.dev.test.interfaces.NoExist;
import xyf.frpc.dev.test.interfaces.TestImpl;




/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
    	FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("D:\\developer\\java\\myeclipse\\frpc-dev-test\\spring-demo.xml");
        context.start();
        
        
        ProtocolConfig p = (ProtocolConfig) context.getBean("protocol");
        System.out.println(p.getId() + " " + p.getName() + " " + p.getPort());
        
        RegistryConfig r = (RegistryConfig)context.getBean("registry");
        System.out.println(r.getAddresses());
        
        Application app = (Application)context.getBean("application");
        System.out.println(app.getName());
        
        Provider provider = (Provider)context.getBean("providertestImpl");
        System.out.println("Provider-" + provider.getInterface() + " " + provider.getTarget());
        ((TestImpl)provider.getTarget()).fun("abc");
        
        
        Provider provider1 = (Provider)context.getBean("providernoExist");
        System.out.println("Provider-" + provider1.getInterface() + " " + provider1.getTarget());
        ((NoExist)provider1.getTarget()).nomethod(1);
        
        
        
    }
}

