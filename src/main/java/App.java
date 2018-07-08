import org.springframework.context.support.FileSystemXmlApplicationContext;

import xyf.frpc.config.Application;
import xyf.frpc.config.ProtocolConfig;
import xyf.frpc.config.Service;
import xyf.frpc.config.Reference;
import xyf.frpc.config.RegistryConfig;
import xyf.frpc.config.util.ExtensionLoader;
import xyf.frpc.dev.test.interfaces.NoExist;
import xyf.frpc.dev.test.interfaces.TestImpl;
import xyf.frpc.remoting.server.ProviderServer;




/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
    	FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("D:\\developer\\java\\myeclipse\\frpc-dev-test\\spring-provider.xml");
        context.start();
        
        
        ProtocolConfig p = (ProtocolConfig) context.getBean("protocol");
        System.out.println("App: " + p.getId() + " " + p.getName() + " " + p.getPort());
        
        RegistryConfig r = (RegistryConfig)context.getBean("registry");
        System.out.println("App: " + r.getAddresses());
        
        Application app = (Application)context.getBean("application");
        System.out.println("App: " + app.getName());
        
        Service provider = (Service)context.getBean("providertestImpl");
        System.out.println("Provider-" + provider.getInterface() + " " + provider.getRef());
        ((TestImpl)provider.getRef()).fun("abc");
        
        
        Service provider1 = (Service)context.getBean("providernoExist");
        System.out.println("App: " + "Provider-" + provider1.getInterface() + " " + provider1.getRef());
        ((NoExist)provider1.getRef()).nomethod(1);
        
        ProviderServer server = ExtensionLoader.getExtensionLoader(ProviderServer.class).getExtension("netty");

        
    }
}

