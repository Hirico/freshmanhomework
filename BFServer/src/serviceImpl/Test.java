package serviceImpl;
//waiting to be deleted

import java.rmi.RemoteException;

public class Test {
	public static void main(String arg[]) throws RemoteException {
		ExecuteServiceImpl executeService = new ExecuteServiceImpl();
		String result = executeService.execute(",>++++++[<-------->-],,[<+>-],<.>.", "4 3\n");
		System.out.println(result);
		String result1 = executeService.execute("++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.", "");
		System.out.println(result1);
		String result2 = executeService.execute(",>,,>++++++++[<------<------>>-]<<[>[>+>+<<-]>>[<<+>>-]<<<-]>>>++++++[<++++++++>-],<.>.", "2 3\n");
		System.out.println(result2);
	}
}
