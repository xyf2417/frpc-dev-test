package xyf.frpc.dev.test.interfaces;

public class NoExist implements INoexist {

	public int nomethod(int i) {
		System.out.println("--------NoExist nomethod(" + i + ")");
		return i + 1;
	}

}
