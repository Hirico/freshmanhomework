//请不要修改本文件名
package serviceImpl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

import service.ExecuteService;
import service.UserService;

public class ExecuteServiceImpl implements ExecuteService {

	/**
	 * 请实现该方法
	 */
	@Override
	public String execute(String code, String param) throws RemoteException {
		for(int i = 0; i < code.length(); i++) {
			if(code.charAt(i) != '[' && code.charAt(i) != ']' && code.charAt(i) != '>' && code.charAt(i) != '<' 
					&& code.charAt(i) != '+' && code.charAt(i) != '-' && code.charAt(i) != ',' && code.charAt(i) != '.'
					&& code.charAt(i) != ' ' && code.charAt(i) != '\n') {
				return "invalid";
			}
		}
		
		try {
		//initialize
		char[] codeChars = code.toCharArray();
		String result = "";
		char[] array = new char[30000]; // use char instead of byte
		for(int i = 0; i < array.length; i++) {
			array[i] = 0;
		}
		int codePointer = 0;
		int arrayPointer = 0;
		int paramPointer = 0;
		int beginPointer = 0; // for loop
		int endPointer = 0;
		
		//get all the parameters
		char[] paraChars = param.toCharArray();
		ArrayList<Character> parameters = new ArrayList<Character>();
		for(int i = 0; i < paraChars.length; i++) {
			parameters.add(paraChars[i]);
		}
		
		//scan all begins and ends to make them combined
		int numOfLoop = 0;
		for(int i = 0; i < codeChars.length; i++) {
			if(codeChars[i] == '[') {
				numOfLoop += 1;
			}
		}
		int [][] loops = new int[numOfLoop][2]; //begin index, end index
		int loopId = 0;
		for(int i = 0; i < codeChars.length; i++) {
			if(codeChars[i] == '[') {
				loops[loopId][0] = i;
				int obstacles = 0;
				for(int j = 1; j < codeChars.length-i; j++) {
					if(codeChars[i+j] == '[') {
						obstacles += 1;
						continue;
					}
					if(codeChars[i+j] == ']' && obstacles != 0) {
						obstacles -= 1;
						continue;
					}
					if(codeChars[i+j] == ']' && obstacles == 0) {
						loops[loopId][1] = i+j;
						break;
					}
				}
				loopId += 1;
			}
		}
		
		//read and execute code one by one
		while(codePointer < codeChars.length) {
			switch(codeChars[codePointer]) {
			case '>':
				arrayPointer += 1;
				codePointer += 1;
				break;
			case '<':
				arrayPointer -= 1;
				codePointer += 1;
				break;
			case '+':
				array[arrayPointer] += 1;
				codePointer += 1;
				break;
			case '-':
				array[arrayPointer] -= 1;
				codePointer += 1;
				break;
			case '.':
				result = result + array[arrayPointer];
				codePointer += 1;
				break;
			case ',':
				array[arrayPointer] = parameters.get(paramPointer);
				paramPointer += 1;
				codePointer += 1;
				break;
			case '[':
				int loopIndex = 0;
				for(int i = 0; i < loops.length; i++) {
					if(loops[i][0] == codePointer) {
						loopIndex = i;
						break;
					}
				}
				endPointer = loops[loopIndex][1];
				if(array[arrayPointer] == 0) {
					codePointer = endPointer;
				} else {
					codePointer += 1;
				}
				break;
			case ']':
				int loopIndex2 = 0;
				for(int i = 0; i < loops.length; i++) {
					if(loops[i][1] == codePointer) {
						loopIndex2 = i;
						break;
					}
				}
				beginPointer = loops[loopIndex2][0];
				if(array[arrayPointer] != 0) {
					codePointer = beginPointer;
				} else {
					codePointer += 1;
				}
				break;				
			}
		}
		return result;
		} catch (Exception e) {
			return "invalid";
		}
	}

}
