//需要客户端的Stub
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
public interface IOService extends Remote{
	public boolean writeFile(String file, String userId, String fileName, String fileVersion)throws RemoteException;
	
	/**@return code, filename, fileVersion */
	public String[] readFile(String userId, String fileName)throws RemoteException;
	
	/**@return code, filename, fileVersion */
	public String[] readFile(String userId, String fileName, String fileVersion)throws RemoteException;
	
	/**@return filenames*/
	public String[] readFileList(String userId)throws RemoteException;
	
	/**@return fileVersions */
	public String[] readVersionList(String userId, String fileName) throws RemoteException;
}
