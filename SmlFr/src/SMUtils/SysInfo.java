package SMUtils;

import javax.swing.JOptionPane;

public class SysInfo {

	public static void displayMessage() {
		
		String mssg = 	"java version:  "+System.getProperty("java.version");
		mssg += 	  "\njava.home:     " + System.getProperty("java.home");
		mssg += 	  "\nos.arch:       " + System.getProperty("os.arch");
		
		
		
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
}
