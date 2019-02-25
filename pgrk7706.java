// Aashay Thakkar CS610 7706 prp

import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

class pgrk7706{
	
  public static void main(String[] args) throws IOException {
  
    DecimalFormat dec = new DecimalFormat("0.0000000");
    int init_val = 0;
    int vert = 0;
    int edges = 0;
    int num_iter = 0;
    String Graph_File = "";
    double d = 0.85;
    boolean flag = true;
    int iter_counter = 0;
    double errorrate = 0.0;
    
   
    if (args.length != 3){
      System.out.println("Enter a valid command line arguement");
      return;
    }
   
    for (int i=0;i<args.length;i++) {
      num_iter = Integer.parseInt(args[0]);
      init_val = Integer.parseInt(args[1]);
      Graph_File = args[2];
    }
    
    if (!(init_val >= -2 && init_val <= 1)){
      System.out.println("Wrong initial value. Enter a value between -2 and 1");
      return;
    }
   
    Scanner scanner = new Scanner(new File(Graph_File));
    vert = scanner.nextInt();
    edges = scanner.nextInt();
    
    double graph[][] = new double[vert][vert];		//adjacency matrix
    for(int i = 0; i < vert; i++){
      for(int j = 0; j < vert; j++){
            graph[i][j] = 0.0;
      }
    }
    while(scanner.hasNextInt()){
      graph[scanner.nextInt()][scanner.nextInt()] = 1.0;
    }
    
    if (num_iter < 0){		//error rate if no. of iterations is negative
      errorrate = Math.pow(10, num_iter);
    }
    
    double pgrk[] = new double[vert];
    double sub[] = new double[vert];
    double out_degree[] = new double[vert];		//Initializing
    double initial_pgrk [] = new double[vert];
    
   
    for(int i=0; i<vert; i++){
      out_degree[i] = 0;
      for(int j=0; j<vert; j++){
        out_degree[i] = out_degree[i] + graph[i][j];
      }
    }
  
      if (vert < 10){		//Initial pagerank
        switch(init_val){
          case 0:
          for (int i=0; i<vert;i++){
            initial_pgrk[i] = 0.0;
          }
          break;
          case 1:
          for (int i=0; i<vert;i++){
            initial_pgrk[i] = 1.0;
          }
          break;
          case -1:
          for (int i=0; i<vert;i++){
            initial_pgrk[i] = 1.0/vert;
          }
          break;
          case -2:
          for (int i=0; i<vert;i++){
            initial_pgrk[i] = 1.0/Math.sqrt(vert);
          }
          break;
        }
      }
   
      else{		//Initial pagerank when vertices less than 10
        num_iter = 0;
        init_val = -1;
        errorrate = 0.00001;
        for (int i=0; i<vert;i++){
          initial_pgrk[i] = 1.0/vert;
        }
      }

      System.out.print("Base: " +iter_counter + " : ");
      if (vert > 5){
        System.out.println();
      }
      for(int i=0; i<vert; i++){
          System.out.print(" P [" + i + "] = " + dec.format(initial_pgrk[i]));
          if (vert > 5){
            System.out.println();
        }
      }
      System.out.println();
     
      if(num_iter > 0){
        do{
          for (int j=0; j<vert; j++){
            pgrk[j]=0.0;
          }
          for (int j=0; j<vert; j++){
            for (int k=0; k<vert; k++){
              if(graph[k][j]==1){
                pgrk[j] = pgrk[j]+initial_pgrk[k]/out_degree[k];
              }
            }
          }
         
          System.out.print("Iter: " + (iter_counter+1) + " : ");
          if (vert > 5){
            System.out.println();
          }
          for (int j=0; j<vert; j++){
            pgrk[j] = d*pgrk[j] + (1-d)/vert;
            System.out.print(" P [" + j + "] = " + dec.format(pgrk[j]));
            if (vert > 5){
              System.out.println();
          }
        }
        System.out.println();
        for (int j=0; j<vert; j++){
          initial_pgrk[j]=pgrk[j];
        }
        iter_counter++;
        num_iter--;
      } while(num_iter !=0);
    }
   
    else{
      do{
          if(flag == true)
          {
             flag = false;
          }
          else
          {
            for(int i = 0; i < vert; i++) {
              initial_pgrk[i] = pgrk[i];
            }
          }
          for (int j=0; j<vert; j++){
            pgrk[j]=0.0;
            sub[j]=0.0;
          }
          for (int j=0; j<vert; j++){
            for (int k=0; k<vert; k++){
              if(graph[k][j]==1){
                pgrk[j] = pgrk[j]+initial_pgrk[k]/out_degree[k];
              }
            }
          }
          
          System.out.print("Iter: " + (iter_counter+1) + " : ");
          if (vert > 5){
            System.out.println();
          }
          for (int j=0; j<vert; j++){
            pgrk[j] = d*pgrk[j] + (1-d)/vert;
            System.out.print(" P [" + j + "] = " + dec.format(pgrk[j]));
            if (vert > 5){
              System.out.println();
          }
        }
        System.out.println();
        iter_counter++;
      } while(Convergence7706(pgrk, initial_pgrk, vert, errorrate)!=true);
    }
  }
    
    public static boolean Convergence7706(double start[], double previous[], int k, double error_rate){
       for(int i = 0 ; i < k; i++){
        if (Math.abs(start[i]-previous[i]) > error_rate )
          return false;
        }
       return true;
    }
}
