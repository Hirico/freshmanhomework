package serviceImpl;

import java.rmi.RemoteException;

import service.UserService;

public class UserServiceImpl implements UserService{
	public static String currentUsername = "";

	/**assume there are two users registered:
	 * yuanshi youeryuan
	 * banzang ryogawaga
	 *  */
	@Override
	public boolean login(String username, String password) throws RemoteException {
		if((username.equals("yuanshi") && password.equals("youeryuan"))
				|| (username.equals("banzang") && password.equals("ryogawaga"))) {
			currentUsername = username;
			return true;
		} else {
			return false;
		}
		
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		currentUsername = "";
		return true;
	}

}
