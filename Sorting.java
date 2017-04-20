/*
 *  Author: Javier Mucia
 *  Description: Counting Sort, Radix Sort, Bucket Sort
 */
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Sorting {
    public static int count;
    public static void main(String[] args) {
        // Name of the files to be processed
        String [] files = { "Num8.txt", "Num16.txt", "Num32.txt", "Num64.txt", "Num128.txt", "Num256.txt"};

        // the column headers of the summary file/table | summary string will be saved onto a *.csv file
        String summary =   "Algorithm, Num8.txt, Num16.txt, Num32.txt, Num64.txt, Num128.txt, Num256.txt";

    /*<<<<<<<<<<<<<<<<<<<<<<<<<  Apply COUNTING Sort    >>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
        summary +="\nCountingSRT";        //new entry for Counting_Sort on the summary file/table

        // get the data from the text files and apply Counting sort one file at a time
        for (String file_name: files) {
            count = 0;                                      // Restart count for this algorithm on the current file
            int number_of_elements = Integer.parseInt( file_name.substring(3, file_name.length() - 4) );    // # of values on this file
            int[] dataA = new int[number_of_elements];       // create array to hold exactly the number of values in current file
            int[] dataB = new int[number_of_elements];
            FromFile_ToArray("./data/"+file_name, dataA);    // Fill up the array with values from the file

            COUNTING_SORT(dataA, dataB, number_of_elements);   // Apply Counting Sort to the data of current file
            summary +=","+count;    // save the value of 'count' for Counting sort on this file
            if(isSorted(dataB))
                System.out.println("SORTED");
            else
                System.out.println("SORTED");


            // Save the sorted array into a new file (one value per line), include size of array and value of 'count'
            FromArray_ToFile(dataB, "ArrayLength: "+dataB.length+"\nCount: "+count, "CountingSRT_"+file_name);
        }



    /*<<<<<<<<<<<<<<<<<<<<<<<<<  Apply RADIX Sort   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
        //new entry for Radix Sort on the summary file/table
        summary +="\nRadixSRT";

        // get the data from the text files and apply Radix sort one file at a time
        for (String file_name: files) {
            count = 0;                  // Restart count for this algorithm on the current file
            int number_of_elements = Integer.parseInt( file_name.substring(3, file_name.length() - 4) ); // # of values on this file
            int[] data = new int[number_of_elements];    // create array to hold exactly the number of values in current file
            FromFile_ToArray("./data/"+file_name, data); // Fill up the array with values from the file

            RADIX_SORT(data);       // Apply Radix Sort to the data of current file
            summary +=","+count;    // save the value of 'count' for Radix sort on this file
            if(isSorted(data))
                System.out.println("SORTED");
            else
                System.out.println("SORTED");
            // Save the sorted array into a new file (one value per line), include size of array and value of 'count'
            FromArray_ToFile(data, "ArrayLength: "+data.length+"\nCount: "+count, "RadixSRT_"+file_name);
        }


    /*<<<<<<<<<<<<<<<<<<<<<<<<   Apply BUCKET Sort     >>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
        //new entry for Bucket sort on the summary file/table
        summary +="\nBucketSRT";

        // get the data from the text files and apply Bucket sort one file at a time
        for (String file_name: files) {
            count = 0;      // Restart count for this algorithm on the current file
            int number_of_elements = Integer.parseInt( file_name.substring(3, file_name.length() - 4) ); // # of values on this file
            int[] data = new int[number_of_elements];   // create array to hold exactly the number of values in current file
            FromFile_ToArray("./data/"+file_name, data); // Fill up the array with values from the file

            BUCKET_SORT( data );      // Apply the Bucket Sort to the data of current file
            summary +=","+count;    // save the value of 'count' for Bucket sort on this file

            if(isSorted(data))
                System.out.println("SORTED");
            else
                System.out.println("SORTED");


            // Save the sorted array into a new file (one value per line), include size of array and value of 'count'
            FromArray_ToFile(data, "ArrayLength: "+data.length+"\nCount: "+count, "BucketSRT_"+file_name);
        }



    /*<<<<<<<<<<<<<<<<<     SAVE THE SUMMARY OF THE RESULTS IN A CSV FILE TO DO A GRAPH     >>>>>>>>>>>>>>>>>>>>>>>*/
        try(  PrintWriter out = new PrintWriter( "SUMMARY.csv" )  ){  out.println(summary); }
        catch ( IOException e){ System.out.println("Something wrong happened while saving the summary."); }
    }

    /************************ Definition of COUNTING Srot ***********************/
    // A: Unsortoed source Array
    // B: Destination Array where the sorted values will be saved
    // k: length of Array A (and B). Assumes the greatest value in A is <= k
    public static void COUNTING_SORT(int [] A, int [] B, int k){
        // C: Counter arra, couts the # of occurrences of each element in A
        // Initialize all the elements to 0 (zero)
        int[] C = new int[k+1];
        for(int i = 0; i<k; ++i) {
            C[i] = 0;
            }
        // Count the frequency of each element in A (and store those frequencies on C)
        for(int i=0; i<k; ++i) {
            C[A[i]] = C[A[i]] + 1;
            }
        // Change C[i] so that each C[i] holds the
        // position of each value in B
        for (int i=1; i<=k; ++i) {
            C[i] = C[i] + C[i - 1];
            }
        // Fill up B, the array that holds the sorted values of A
        for (int i = 0; i<k; ++i){
            ++count;
            B[C[A[i]]-1] = A[i];
            C[A[i]] = C[A[i]] -1;
            }
        }

    /************************ Definition of RADIX Sort*************************/
    public static void RADIX_SORT(int [] A){
        int n = A.length;       // n = length of the list

        // Get Highest value on the list of numbers
        int max_element = A[0];
        for (int i = 1; i < A.length; i++)
            if (A[i] > max_element)
                max_element = A[i];

        // Get the length of the highest value on the list
        int d = String.valueOf(max_element).length();

        // It sorts from least to most significant digits: 10^0, ... , 10^d
        for(int k = 1; max_element/k>0; k *=10 ){
            //int k = (int)Math.pow(10, i); // k = 10^0, 10^1, ... , 10^(d-1)
            int[] B = new int[n];   // B = list where sorted elements will be saved
            // C: Counter arra, couts the # of occurrences of each element in A
            // Initialize all the elements to 0 (zero)
            int[] C = new int[10];
            for(int j = 0; j<10; ++j) {
                C[j] = 0;
                }
            // Count the frequency of each element in A (and store those frequencies on C)
            for(int j=0; j<n; ++j) {
                C[ (A[j]/k)%10 ] += 1;
                }
            // Change C[i] so that each C[i] holds the
            // position of each value in B
            for (int j=1; j<10; ++j) {
                C[j] = C[j] + C[j - 1];
                }
            // Fill up B, the array that holds the sorted values of A
            for (int j = (n-1); j>=0; --j){
                ++count;
                B[C[ (A[j]/k)%10 ]-1 ] = A[j];
                C[ (A[j]/k)%10 ] -= 1;
                }
            for(int j=0; j<n; ++j){
                A[j] = B[j];
                }

            }
    }

    /************************ Definition of BUCKET Sort*************************/
    public static void BUCKET_SORT(int[] A) {
        int n = A.length;       // n = length of the list
        // Convert all elements in the list to the range (0-1]
        int min = A[0];
        int max = A[0];
        double[] C = new double[n];
        // Get minimun and maximum values in the list
        for(int i =0; i<n; ++i){
            if(A[i]>max)
                max = A[i];
            if(A[i]<min)
                min = A[i];
            }
        for(int i=0; i<n; ++i)      // The normalization part
            C[i] = (double)(A[i]-min)/(max+1);

        // At this point B contains all the numbers of A, normalized to the range (0, 1]
    // Apply Bucket Sort
        int n2 = n; // Number of buckets
    // 1)  Place values into bukets
        ArrayList<Double>[] B = new ArrayList[n2];
        for (int i=0;  i<n ; ++i) {
            if(B[ (int)(n2*C[i]) ] ==  null)
                B[ (int)(n2*C[i]) ] = new  ArrayList<Double>();
            B[ (int)(n2*C[i]) ].add( C[i] );
            }
    // 2) Sort each bucket
        for (int i=0; i<B.length; ++i) {
            if(B[i] == null || B[i].isEmpty() ) // Skip empty buckets
                continue;
            for (int j = 1; j < B[i].size(); ++j){
                double key = B[i].get(j);
                int m = j - 1;
                while( m >= 0 && B[i].get(m) > key ){
                    count = count +1;
                    B[i].set(m+1, B[i].get(m));
                    m = m - 1;
                    }
                B[i].set(m+1,  key);
                }
            }
    // 3) Concatenate all the (sorted) buckets into a single list
        ArrayList<Double> result = new ArrayList<Double>();
        for (int i=0;  i<B.length; ++i) {
            if(B[i] == null)
                continue;
            result.addAll(B[i]);
            }

        // Denormalize all the values from the range (0, 1] to the original values
        for(int i=0; i <n ; ++i)
            A[i] = (int)(result.get(i)*(max+1)+min);
    }


    /************************    AUXILIARY FUNCTIONS   **************************/
    // opens a file with the name 'Filename' containing a number per line and fills upn an array with those vales
    public static void FromFile_ToArray(String Filename, int[] anArray){
        try {
            FileReader fileReader = new FileReader(new File(Filename));
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            for(int i=0; i<anArray.length; ++i){
                String aLine = bufferedReader.readLine();
                anArray[i] = Integer.parseInt(aLine);
            }
            fileReader.close();
            } catch (IOException e) { e.printStackTrace(); }

        }
    // saves the values of an array into a new file (one value per line). the given string is at the head of the file
    public static void FromArray_ToFile( int[] anArray, String initialString,  String Filename ){
        try{
            PrintWriter writer = new PrintWriter(Filename);
            writer.println(initialString);
            for(int i = 0; i<anArray.length; ++i)
                writer.println(anArray[i]);
            writer.close();
            } catch (IOException e) { System.out.print("Something happened while saving to a file."); }
        }

    // Function that checks that all the elements in an array are sorted in ascending order
    public static boolean isSorted(int[] A) {
        for (int i = 0; i < A.length - 1; i++) {
            if (A[i] > A[i + 1]) {
                return false; // It is proven that the array is not sorted.
                }
            }
        return true; // If this part has been reached, the array must be sorted.
        }


    }