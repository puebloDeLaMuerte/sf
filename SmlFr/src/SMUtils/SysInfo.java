package SMUtils;

import javax.swing.JOptionPane;

public class SysInfo {

	public static void displayMessage() {

		String mssg = 	"java version:  "+System.getProperty("java.version");
		mssg += 	  "\njava.home:     " + System.getProperty("java.home");
		mssg += 	  "\nos.arch:       " + System.getProperty("os.arch");
		mssg += 	  "\nos.aname:      " + System.getProperty("os.name");



		int mb = 1024*1024;

		//Getting the runtime reference from system
		Runtime runtime = Runtime.getRuntime();

		mssg += "\n\n" +"##### Heap utilization statistics [MB] #####";

		//Print used memory
		mssg += "\n   -   " +"Used Memory:    " + (runtime.totalMemory() - runtime.freeMemory()) / mb;

		//Print free memory
		mssg += "\n   -   " +"Free Memory:    " + runtime.freeMemory() / mb;

		//Print total available memory
		mssg += "\n   -   " + "Total Memory:  " + runtime.totalMemory() / mb;

		//Print Maximum available memory
		mssg += "\n   -   " + "Max Memory:    " + runtime.maxMemory() / mb;



		JOptionPane.showMessageDialog(null, mssg);
	}

	public static void printHeapStats() {

		String mssg = "";

		int mb = 1024*1024;

		//Getting the runtime reference from system
		Runtime runtime = Runtime.getRuntime();

		mssg += "" +"##### Heap utilization statistics [MB] #####";

		//Print used memory
		mssg += "\n   -   " +"Used Memory:    " + (runtime.totalMemory() - runtime.freeMemory()) / mb;

		//Print free memory
		mssg += "\n   -   " +"Free Memory:    " + runtime.freeMemory() / mb;

		//Print total available memory
		mssg += "\n   -   " + "Total Memory:  " + runtime.totalMemory() / mb;

		//Print Maximum available memory
		mssg += "\n   -   " + "Max Memory:    " + runtime.maxMemory() / mb;

		System.out.println(mssg);
		System.out.println();
	}

	public static void printSysStats() {

		try {
			//		String mssg = 	"java version:  "+System.getProperty("java.version");
			//		mssg += 	  "\njava.home:     " + System.getProperty("java.home");
			//		mssg += 	  "\nos.arch:       " + System.getProperty("os.arch");
			//		mssg += 	  "\nos.aname:      " + System.getProperty("os.name");
			System.getProperties().store(System.out, "listing System Properties:");
		} catch (Exception e) {
			System.out.println("exception listing properties!");
		}
		System.out.println();
	}
}
