package VMCompliler;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Stack;


public class HelperFunctions {
	public Stack<Integer> stack = new Stack<>();
	public int sp = 256;
	public int staticStart = 16;
	private HashMap<String, String> arithMap = new HashMap<>();
	private HashMap<String, String> savedRegs = new HashMap<>();
	
	public void init(){

		arithMap.put("add", "M=D+M");
		arithMap.put("sub", "M=M-D");
		arithMap.put("and", "M=D&M");
		arithMap.put("or", "M=D|M");
		arithMap.put("neg", "M=-M");
		arithMap.put("not", "M=!M");
		arithMap.put("add", "M=D+M");
		
		savedRegs.put("local", "LCL");
		savedRegs.put("this", "THIS");
		savedRegs.put("that", "THAT");
		savedRegs.put("argument", "ARG");
		
//		arithMap.put("gt", "D;JGT");
//		arithMap.put("eq", "D;JEQ");
//		arithMap.put("", "D;JGE");
//		arithMap.put("lt", "D=M-D");
//		arithMap.put("", "D;JNE");
//		arithMap.put("", "D;JLE");
//		arithMap.put("", "0;JMP");
		
	}
	
	public void parser(String line, PrintStream fileStream) {
		String[] words = line.split(" ");
		System.out.println(words[0]);
		if(words[0].equals("push")) {
			int number = 0;
			try {
			    number = Integer.parseInt(words[2]);

				System.out.println(number);
			} catch (NumberFormatException e) {
			    System.out.println("Invalid string for conversion to integer");
			    e.printStackTrace();
			}

			if(words[1].equals("constant")) {
			this.setConstantAtSP(number, fileStream);
			stack.push(number);
			this.incrementSP(fileStream);	
			}else if(words[1].equals("argument")) {
				fileStream.println("@ARG");
				fileStream.println("D=M");
				fileStream.println("@" + number);
				fileStream.println("D=A+D");
				fileStream.println("@SP");
				fileStream.println("M=D");
			}else{
				pushToKnownRegisters(savedRegs.get(words[1]), number, fileStream);
			}

			
		}else if(words[0].equals("pop")){
			
			int number = 0;
			try {
			    number = Integer.parseInt(words[2].trim());

			} catch (NumberFormatException e) {
			    System.out.println("Invalid string for conversion to integer");
			    e.printStackTrace();
			}
			if(words[0].equals("static")) {
				int temp = this.staticStart + number;
				fileStream.println("@SP");
				fileStream.println("D=M");
				fileStream.println("@" + temp);
				this.staticStart++;
				fileStream.println("M=D");
			}
			stack.pop();
			this.decrementSP(fileStream);
		}else if(words[0].equals("call")){
			String funtionName = words[1].trim();
			int arg = 0;
			try {
			    arg = Integer.parseInt(words[2].trim());

			} catch (NumberFormatException e) {
			    System.out.println("Invalid string for conversion to integer");
			    e.printStackTrace();
			}
			this.setConstantAtSP(arg, fileStream);
			stack.push(arg);
			this.incrementSP(fileStream);
			
			fileStream.println("@" + funtionName);
			fileStream.println("0;JMP");
			
		}else if(words[0].equals("function")){
			String funtionName = words[1].trim();
			int arg = 0;
			try {
			    arg = Integer.parseInt(words[2].trim());

			} catch (NumberFormatException e) {
			    System.out.println("Invalid string for conversion to integer");
			    e.printStackTrace();
			}

			
			fileStream.println(funtionName + ":");
			fileStream.println("@SP");
			fileStream.println("M=M-" + arg);
			
		}else if(words[0].equals("if-goto")){
			fileStream.println("@SP");
			fileStream.println("D=M");
			fileStream.println("D;JNE");
		}else if(words[0].equals("goto")){
			String jumpLabelName = words[1].trim();
			fileStream.println("@" + jumpLabelName);
			fileStream.println("0;JMP");
		}else if(words[0].equals("return")){
			String jumpLabelName = words[1].trim();
			fileStream.println("@" + jumpLabelName);
			fileStream.println("0;JMP");
		} else {
			arithmetic(words[0], fileStream);
		}

	}

	private void arithmetic(String opp, PrintStream fileStream) {
		System.out.println(opp);
		int temp = 0;
		if(!opp.equals("neg") && !opp.equals("not")) {

			int y = stack.pop();
			this.decrementSP(fileStream);
			fileStream.println("@SP");
			fileStream.println("D=M");

			int x = stack.pop();

			this.decrementSP(fileStream);
			fileStream.println("@SP");
			

			switch (opp) {
		    case "add":
		    	stack.push(x + y); 
		    	this.incrementSP(fileStream);
		        break;
		    case "sub":
		    	stack.push(x - y); 
		    	this.incrementSP(fileStream);
		        break;
		    case "and":
		    	stack.push(x & y); 
		    	this.incrementSP(fileStream);
		        break;
		    case "or":
		    	stack.push(x | y); 
		    	this.incrementSP(fileStream);
		        break;
		    case "eq":
		    	temp = x == y ? -1 : 0;
		    	stack.push(temp); 
		    	this.setConstantAtSP(temp, fileStream);
		    	this.incrementSP(fileStream);
		        break;
		    case "gt":
		    	temp = x > y ? -1 : 0;
		    	stack.push(temp); 
		    	this.setConstantAtSP(temp, fileStream);
		    	this.incrementSP(fileStream);
		        break;
		    case "lt":
		    	temp = x < y ? -1 : 0;
		    	stack.push(temp); 

		    	this.setConstantAtSP(temp, fileStream);
		    	this.incrementSP(fileStream);
		        break;
		    default:
		        break;
			}
			System.out.println("arithmap at opp" + opp + ":");
			System.out.println(arithMap.get(opp));
			if(arithMap.get(opp) != null) fileStream.println(arithMap.get(opp));
			
		}else {
			int y = stack.pop();
			this.decrementSP(fileStream);
			if(opp.equals("neg")) {
				temp = y * (-1);
				stack.push(temp); 
				this.setConstantAtSP(temp, fileStream);
				this.incrementSP(fileStream);
				}
			if(opp.equals("not")) {
				temp = ~y;
				stack.push(temp); 
				this.setConstantAtSP(temp, fileStream);
				this.incrementSP(fileStream);
				}
		}
	}
	
	private void pushToKnownRegisters(String regName, int number, PrintStream fileStream) {

		fileStream.println("@" + regName);
		fileStream.println("D=M");
		fileStream.println("@" + number);
		fileStream.println("A=D+A");
		fileStream.println("D=M");
		fileStream.println("@SP");
		fileStream.println("M=D");
		decrementSP(fileStream);
		fileStream.println("A=M");
		fileStream.println("D=M");
		fileStream.println("@R13");
		fileStream.println("A=M");
		fileStream.println("M=D");
	}
	private void decrementSP(PrintStream fileStream) {
		fileStream.println("@SP");
		fileStream.println("M=M+1");
		this.sp--;
	}
	
	private void incrementSP(PrintStream fileStream) {
		fileStream.println("@SP");
		fileStream.println("M=M-1");
		this.sp++;
	}
	private void jumpCommand(PrintStream fileStream) {
		fileStream.println("D=M-D");
		fileStream.println("@SP");
		fileStream.println("M=D");
	}
	
	private void setConstantAtSP(int number, PrintStream fileStream) {
		boolean isNeg = false;
		if(number < 0) {number = number * (-1); isNeg = true; }
		fileStream.println("@" + number);
		fileStream.println("D=A");
		fileStream.println("@SP");
		fileStream.println("M=D");
		if(isNeg) this.setValueNeg(fileStream);
	}
	
	private void setValueNeg(PrintStream fileStream) {
		fileStream.println("M=-M");
	}

	
	public int[] numbToBinary(Integer number) {
		int[] result = new int[16];
		
		int currNumber = number;
		int prevNumber;
		int placeNumber;
		int currIndex = 0;
		
		for(int currPlace = 15; currPlace >= 0; currPlace--) {
			prevNumber = currNumber;
			placeNumber = (int) Math.pow(2, currPlace);
			currNumber = (int) (currNumber / placeNumber);
			
			if(currNumber != 0) {
				result[currIndex] = 1;
				currNumber = (int) (prevNumber % placeNumber);
				
			}else {
				result[currIndex] = 0;
				currNumber = prevNumber;
			}
			
			currIndex++;
		}
		return result;
	}
	
	public int binaryToNumb(int[] binary) {
		int result = 0;
		int placeNumber = 0;
		int currIndex = 0;
		
		for(int currPlace = 15; currPlace >= 0; currPlace--) {
			placeNumber = (int) Math.pow(2, currPlace);
			if(binary[currIndex] == 1) result += placeNumber;
			currIndex++;
		}
		return result;
	}
	
	public String arrToString(int[] arr) {
		String result = "";
		for(int a: arr) {
			result = result.concat(String.valueOf(a));
		}
		return result;
	}
}
