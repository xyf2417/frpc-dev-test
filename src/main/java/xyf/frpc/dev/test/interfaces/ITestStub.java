package xyf.frpc.dev.test.interfaces;

public class ITestStub implements ITest {
	private ITest itest;
	public ITestStub(ITest itest) {
		this.itest = itest;
	}
	public String fun(String str) {
		System.out.println("stub before");
		String res = itest.fun(str);
		System.out.println("stub after");
		return res;
	}

}
