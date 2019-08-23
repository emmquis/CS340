import java.io.*;
import java.util.ArrayList;


public class Simp_Shell
{
    public static void main(String[] args) throws IOException
    {
        
        //String OSName = System.getProperty("os.name");
        //System.out.println(OSName);
        
        
        String commandLine;
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        
        ProcessBuilder processBuilder = new ProcessBuilder();
        
        // array to store history
        ArrayList<String> historyList = new ArrayList<String>();
     
        // file directory used
        File dir = new File(System.getProperty("user.home"));
   
        // we break out with <control><C>
        while (true)
        {
  
            // read what the user entered
            System.out.print("jsh>");

            commandLine = console.readLine();
            String lastElement;
            // if the user entered a return, just loop again
            if (commandLine.equals("")) continue;
            
            // if user wants to exit shell
            if(commandLine.contains("quit") || commandLine.contains("exit"))
                System.exit(0);
            
            /** The steps are:
            (1) parse the input to obtain the command and any parameters
            (2) create a ProcessBuilder object
            (3) start the process
            (4) obtain the output stream
            (5) output the contents returned by the command */
            
            // get commands from user separated by a space 
            String[] userCommands = commandLine.split(" ");
            
            // input user commands into list
            ArrayList<String> commandList = new ArrayList<String>();
            ArrayList<String> commandList2 = new ArrayList<String>();          
            for(String userCommand : userCommands) 
            {
                commandList.add(userCommand);
                commandList2.add(commandLine);
            }
            
            // records all commands entered from start to end of program
            historyList.addAll(commandList2);

            // case cd
            // if user wanted to change directories
            // user input cd will show current directory
            if(commandList.contains("cd"))
            {  
                // home directory 
                if(commandList.size() == 1)
                {
                    processBuilder.directory(dir);
                    System.out.println("Current Directory: " + dir);
                    continue;
                }
                
                // new directory check
                else
                {
                    String newPath = commandList.get(1);
                    File newDir = new File(newPath);
                    // new directory does not exist
                    if(!newDir.exists())
                    {
                        System.out.println("Invalid Directory \n");
                        continue;
                    }
                    // changes directory to new directory
                    else
                    {
                        dir = newDir;
                        System.out.println("New directory: " + dir);
                        processBuilder.directory(dir); 
                        continue;
                    }
                }
            }
            
            // case history
            // if user wanted to see past history
            else if(commandList.contains("history"))
            {
                String hist = "History List: \n";
                int i = 1;
                for(String c : historyList)
                {
                    hist += i + " " + c + "\n";
                    i++;
                }
                System.out.println(hist);
                continue;
            }
            
            // case !!
            // if user wanted to run last command
            else if(commandList.contains("!!"))
            {
                
                // check if history list is empty
                if(historyList.size() == 1)
                {
                    System.out.println("History List is Empty! \n");
                    continue;
                }  
                
                // process last command from history
                else
                {
                 
                    //System.out.println(historyList);    //debugging purposes
                    lastElement = historyList.get(historyList.size() - 2);
                    System.out.println(lastElement);    //debugging purposes

                    continue;
                }
            }
            
            // case !
            // if user wanted to run nth command
            else if(commandList.contains("!"))
            {

                String s = commandList.get(1);
                int i = Integer.parseInt(s);
                
                //System.out.println(i);    //debugging purposes
                if(i > historyList.size() - 1 || i < 0)
                {
                    System.out.println("Argument out of bounds");
                    continue;
                }

                else if((i == (int)i))
                {
                    
                    System.out.println(historyList.get(i - 1));   //debugging purposes
                    continue;
                }
                else
                {
                    System.out.println("Not a valid argument");
                    continue;
                }
            }
            
            try
            {
                processBuilder = new ProcessBuilder(commandList);
                processBuilder.directory(dir);
                Process process = processBuilder.start();                
                // obtain the input stream
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                
                // read the output of the process
                String line;
                while ( (line = br.readLine()) != null)
                System.out.println(line);
                br.close();

            }
            catch(IOException e)
            {
                System.out.println("Invalid command!!!");
            }
        }

    }
}
