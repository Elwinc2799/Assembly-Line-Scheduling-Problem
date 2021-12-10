import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;

public class main {
    public static int n = 0;    //Counter for Primitive Operation
    public static int min = 0;
    public static boolean trg = false;

    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        System.out.println("\n1. Custom input of number of station\n2. Auto generate a series of number up until max number (with CSV file created, OpenCSV jar needed)");
        System.out.print("\nChoice (1/2): ");
        int choice = sc.nextInt();

        //custom input of number of station
        if (choice == 1){
            System.out.print("Number of stations: ");
            int temp = sc.nextInt();
            customInput(temp);
        }
        //create CSV file for f(n) and n data (require OpenCSV jar)
        else if (choice == 2){
            System.out.print("Number of stations from 1 until : ");
            int temp = sc.nextInt();
            autoGenerateResultCSV(temp);
        }
    }

    public static void customInput(int temp){
        int a[][] = new int[2][temp];       //time taken per station
        int t[][] = new int[2][temp-1];       //time taken to transfer line
        int e[] = new int[2];               //entry time
        int x[] = new int[2];               //exit time
        int route[][] = new int[2][temp-1];

        //randomGenerator() used to generate random number for time cost
        for (int i = 0; i < temp; i++){
            if (i < 2){
                e[i] = randomGenerator();
                x[i] = randomGenerator();
            }
            if (i != temp-1){
                t[0][i] = randomGenerator();
                t[1][i] = randomGenerator();
            }
            a[0][i] = randomGenerator();
            a[1][i] = randomGenerator();
        }

        //Display the entry time, exit time, time taken per station and time taken to transfer line
        System.out.println("\nEntry time: " + Arrays.toString(e));
        System.out.println("Exit time: " + Arrays.toString(x) + "\n");
        int j = 0;
        for (int i = 0; i < 2; i++){
            j = j + 1;
            System.out.println("Time taken per station in Line " + j + ": " + Arrays.toString(a[i]));
            System.out.println("Time taken to transfer from Line " + j + " to another line: " + Arrays.toString(t[i]) + "\n");
        }

        assemblyTime(a, t, e, x, temp, route);

        //trg used for store route taken
        System.out.print("Route taken: ");
        if (trg){
            int k = 0;
            for (int i = 0; i < temp - 1; i++){
                k += 1;
                System.out.print("L" + route[0][i] + "S" + k + " -> ");
            }
            System.out.println("L1S4\n");
        }
        else{
            int k = 0;
            for (int i = 0; i < temp - 1; i++){
                k += 1;
                System.out.print("L" + route[1][i] + "S" + k + " -> ");
            }
            System.out.println("L2S4\n");
        }

        System.out.println("Number of stations: " + temp + "\nTotal number of primitive operations: " + n + "\nMin time: " + min);
    }

    public static void autoGenerateResultCSV(int total){
        File file = new File("./result.csv");   //create CSV file in default directory

        try {
            FileWriter writer = new FileWriter(file);
            CSVWriter csv = new CSVWriter(writer);

            String [] tempArr = {Integer.toString(0),Integer.toString(0)};
            csv.writeNext(tempArr);
            //for-loop to loop different number of stations
            for (int temp = 1; temp <= total; temp++){
                int a[][] = new int[2][temp];       //time taken per station
                int t[][] = new int[2][temp-1];       //time taken to transfer line
                int e[] = new int[2];               //entry time
                int x[] = new int[2];               //exit time
                int route[][] = new int[2][temp-1];

                //randomGenerator() used to generate random number for time cost
                for (int i = 0; i < temp; i++){
                    if (i < 2){
                        e[i] = randomGenerator();
                        x[i] = randomGenerator();
                    }
                    if (i != temp-1){
                        t[0][i] = randomGenerator();
                        t[1][i] = randomGenerator();
                    }
                    a[0][i] = randomGenerator();
                    a[1][i] = randomGenerator();
                }

                n = 0;
                assemblyTime(a, t, e, x, temp, route);

                //write data into CSV file in a row
                String [] data = {Integer.toString(temp),Integer.toString(n)};
                csv.writeNext(data);
            }
            csv.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static int randomGenerator(){
        int upper = 25;

        Random rand = new Random();
        int randomNum = 0;
        while (randomNum == 0){
            randomNum = rand.nextInt(upper);
        }
        return randomNum;
    }

    public static void assemblyTime(int a[][], int t[][], int e[], int x[], int len, int route[][]) {

        // time taken to leave 1st station in line 1
        int first = e[0] + a[0][0];
        n = n + 4;  //Counter: 2 * array index, addition, assignment

        // time taken to leave 1st station in line 2
        int second = e[1] + a[1][0];
        n = n + 4;  //Counter: 2 * array index, addition, assignment

        n++;    //Counter: assignment (int i = 1)
        for (int i = 1; i < len; i++) {
            n = n + 1;  //Counter: comparison (i < len)

            int up = Math.min(first + a[0][i],second + t[1][i-1] + a[0][i]);
            n = n + 8;  //Counter: 3 * array index, 3 * addition, comparison, assignment

            //trg used for store route taken
            if ((first + a[0][i]) == up){
                route[0][i-1] = 1;
            }
            else {
                route[0][i-1] = 2;
            }
            int down = Math.min(second + a[1][i],first + t[0][i-1] + a[1][i]);
            n = n + 8;  //Counter: 3 * array index, 3 * addition, comparison, assignment

            //trg used for store route taken
            if ((second + a[1][i]) == down){
                route[1][i-1] = 2;
            }
            else {
                route[1][i-1] = 1;
            }

            first = up;
            second = down;
            n = n + 2;  //Counter: 2 * assignment

            n = n + 2;  //Counter: addition, assignment (i++)
        }
        n++;    //Counter: last comparison

        first += x[0];
        second += x[1];
        n = n + 6;  //Counter: 2 * array index, 2 * addition, 2 * assignment

        min = Math.min(first, second);
        n = n + 2;  //Counter: comparison + assignment

        //trg used for checking which route taken
        if (first == min){
            trg = true;
        }
        else {
            trg = false;
        }
    }
}