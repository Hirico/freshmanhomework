package serviceImpl;
//waiting to be deleted

import java.rmi.RemoteException;

public class Test {
	public static void main(String arg[]) throws RemoteException {
		ExecuteServiceImpl executor = new ExecuteServiceImpl();
		String result = executor.execute(",>++++++[<-------->-],,[<+>-],<.>.", "4 3\n");
		System.out.println(result);
		String result1 = executor.execute("++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.", "");
		System.out.println(result1);
		String result2 = executor.execute(",>,,>++++++++[<------<------>>-]<<[>[>+>+<<-]>>[<<+>>-]<<<-]>>>++++++[<++++++++>-],<.>.", "2 3\n");
		System.out.println(result2);
		String result3 = executor.execute(",>,<.>.", "AB");
		System.out.println(result3);
	}
}
