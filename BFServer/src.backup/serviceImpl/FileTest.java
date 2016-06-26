package serviceImpl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//wating to be deleted
public class FileTest {

	public static void main(String[] args) {
		File folder = new File("yuanshi"+"/");
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

	}

}
