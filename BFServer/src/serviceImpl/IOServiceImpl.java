package serviceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.RemoteException;

import service.IOService;

public class IOServiceImpl implements IOService{
	
	/**file is code and status waiting to be stored, userId will be rootfolder
	 * fileName will combine with _fileVersion behind */
	@Override
	public boolean writeFile(String file, String userId, String fileName, String fileVersion) {
		File f = new File(userId + "/" + fileName + "_" + fileVersion);
		try {
			//write a file
			FileWriter fw = new FileWriter(f, false);
			fw.write(file);
			fw.flush();
			fw.close();
			
			//check whether there are over 5 versions, if true, delete the oldest one.
			File folder = new File(userId + "/");
			String[] fileList = folder.list(new SameFileFilter(fileName + "_"));
			File[] files = folder.listFiles(new SameFileFilter(fileName + "_"));
			if(fileList.length > 5) {
				long[] versionList = new long[fileList.length];
				for(int i = 0; i < fileList.length; i++) {
					versionList[i] = Long.valueOf(fileList[i].substring(fileName.length()+1));
				}
				int oldestIndex = 0;
				long minValue = versionList[0];
				for(int i = 0; i < versionList.length; i++) {
					if(versionList[i] <= minValue) {
						minValue = versionList[i];
						oldestIndex = i;
					}
				}
				Files.delete(files[oldestIndex].toPath());
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		
	}

	@Override
	public String[] readFile(String userId, String fileName) {
		//find the latest version
		String fileVersion = "";
		File folder = new File(userId+"/");
		String[] fileList = folder.list(new SameFileFilter(fileName+"_"));
		Long[] fileVersionList = new Long[fileList.length];
		int versionBeginIndex = fileList[0].indexOf("_")+1;
		for(int i = 0; i < fileList.length; i++) {
			fileVersionList[i] = Long.valueOf(fileList[i].substring(versionBeginIndex));
		}
		int latestIndex = 0;;
		long maxValue = fileVersionList[0];
		for(int i = 0; i < fileVersionList.length; i++) {
			if(fileVersionList[i] >= maxValue) {
				maxValue = fileVersionList[i];
				latestIndex = i;
			}
		}
		fileVersion = String.valueOf(fileVersionList[latestIndex]);
				
		//fill the result
		String code = "";
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(userId+"/"+fileName + "_"+fileVersion));
			String str;
			while((str = br.readLine()) != null) {
				code = code + str + "\n";
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		String[] result = new String[3];
		result[0] = code;
		result[1] = fileName;
		result[2] = fileVersion;
		return result;
	}
	
	@Override
	public String[] readFile(String userId, String fileName, String fileVersion) throws RemoteException {
		String code = "";
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(userId+"/"+fileName + "_"+fileVersion));
			String str;
			while((str = br.readLine()) != null) {
				code = code + str + "\n";
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		String[] result = new String[3];
		result[0] = code;
		result[1] = fileName;
		result[2] = fileVersion;
		return result;
	}
	
	@Override
	public String[] readFileList(String userId) {
		File folder = new File(userId+"/");
		File[] fileList = folder.listFiles(); 
		String[] fileListString = new String[fileList.length];
		for(int i = 0; i < fileList.length; i++) {
			if(fileList[i].isFile()) {
				int index = fileList[i].getName().indexOf("_");
				fileListString[i] = fileList[i].getName().substring(0, index);
				for(int j = 0; j < i; j++) {
					if(fileListString[j].equals(fileListString[i])) {
						fileListString[i] = "";
					}
				}
			}
		}
		int standaloneCount = 0;
		for(int i = 0; i < fileList.length; i++) {
			if(fileListString[i].length() > 0) {
				standaloneCount += 1;
			}
		}
		String[] resultList = new String[standaloneCount];
		int j = 0;
		for(int i = 0; i < standaloneCount; i++) {
			while(fileListString[j].length() == 0) {
				j += 1;
			}
			resultList[i] = fileListString[j];
			j += 1;
		}
		return resultList;
	}

	@Override
	public String[] readVersionList(String userId, String fileName) throws RemoteException {
		File folder = new File(userId+"/");
		File[] fileList = folder.listFiles(); 
		String[] fileListString = new String[fileList.length];
		for(int i = 0; i < fileList.length; i++) {
			if(fileList[i].isFile()) {
				int index = fileList[i].getName().indexOf("_");
				fileListString[i] = fileList[i].getName();
				if(!fileListString[i].substring(0, index).equals(fileName)) {
					fileListString[i] = "";
				}
			}
		}
		int standaloneCount = 0;
		for(int i = 0; i < fileList.length; i++) {
			if(fileListString[i].length() > 0) {
				standaloneCount += 1;
			}
		}
		String[] resultList = new String[standaloneCount];
		int j = 0;
		for(int i = 0; i < standaloneCount; i++) {
			while(fileListString[j].length() == 0) {
				j += 1;
			}
			resultList[i] = fileListString[j].substring(fileListString[j].indexOf("_")+1);
			j += 1;
		}
		return resultList;
	}
	
	class SameFileFilter implements FilenameFilter {
		private String type = "";
		public SameFileFilter(String type) {
			this.type = type;
		}

		@Override
		public boolean accept(File arg0, String arg1) {
			return arg1.startsWith(type);
		}
		
	}
	
}
