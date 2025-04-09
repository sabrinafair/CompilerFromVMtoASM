package VMCompliler;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import VMCompliler.HelperFunctions;



public class VMCompiler {
	

	public static void main(String[] args) {
		FilenameFilter filter = new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".vm");
		    }
		};
		HelperFunctions functions = new HelperFunctions();
//        Scanner scanner = new Scanner(System.in);
//		System.out.print("Enter file path: ");
//        String path = scanner.nextLine();
//		System.out.print("Enter file name: ");
//        String name = scanner.nextLine();

//        String inputFile = "C:\\Users\\Sabrina Ferras\\OneDrive - csumb.edu\\Documents\\CPP\\Spring2025\\CS 5250 - Adv Comp Architecture\\software\\nand2tetris\\nand2tetris\\projects\\7\\StackArithmetic\\StackTest\\StackTest.vm";
       // inputFile = "C:\\Users\\Sabrina Ferras\\OneDrive - csumb.edu\\Documents\\CPP\\Spring2025\\CS 5250 - Adv Comp Architecture\\software\\nand2tetris\\nand2tetris\\projects\\8\\FunctionCalls\\StaticsTest\\Sys.vm";
//        String inputFile = path + "\\" + name + ".vm";
//        String outputFile = path + "\\" + name + "1.hack";
//        for(int i = 0; i < 2; i++) {
        
        File folder = new File("C:\\Users\\Sabrina Ferras\\OneDrive - csumb.edu\\Documents\\CPP\\Spring2025\\CS 5250 - Adv Comp Architecture\\software\\nand2tetris\\nand2tetris\\projects\\7\\MemoryAccess\\StaticTest");
        File[] listOfFiles = folder.listFiles(filter);
        
        for (int i = 0; i < listOfFiles.length; i++) {
	    try {
//	        File myObj = new File(inputFile); 
	    	File myObj = listOfFiles[i];
	        PrintStream fileStream = new PrintStream(new File("C:\\Users\\Sabrina Ferras\\OneDrive - csumb.edu\\Documents\\CPP\\Spring2025\\CS 5250 - Adv Comp Architecture\\software\\nand2tetris\\nand2tetris\\projects\\7\\MemoryAccess\\StaticTest\\TEST.asm"));
	        
	        Scanner myReader = new Scanner(myObj);
	        functions.init();
	        while (myReader.hasNextLine()) {
	          String data = myReader.nextLine();
		      
	          // check for comments then skip line
	  		  String line = data.trim();
	  		  line = line.split("//")[0];		
	  		  if(line != "") {
	          functions.parser(line, fileStream);
	  		  

	          System.out.println(line);
	          System.out.println(functions.stack.toString());
//	          if(!functions.isLabel && !functions.firstPass) System.out.print(functions.arrToString(functions.machineCode) + System.lineSeparator());
//	          if(!functions.isLabel && !functions.firstPass) fileStream.println(functions.arrToString(functions.machineCode));
	  		  }
	        }
	        myReader.close();
	      } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }
        }
//	    functions.firstPass = false;
	    System.out.print("Final stack:");
	    System.out.println(functions.stack.toString());

	    System.out.print("Final sp:");
	    System.out.println(functions.sp);
        }
//	}

}
