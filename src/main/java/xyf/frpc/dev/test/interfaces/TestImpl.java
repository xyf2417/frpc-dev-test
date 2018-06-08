package xyf.frpc.dev.test.interfaces;

public class TestImpl implements ITest {

	public String fun(String str){
		System.out.println("----------------------TestImpl.fun(" + str + ")");
		return "TestImpl.fun " + str;
	}

}
