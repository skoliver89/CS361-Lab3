package Labs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/*
 * LAB 3 PART 1 - Bin Sort and Radix Sort
 */
public class Lab3 {	
	public static void main (String[] args)
	{
		Lab3 run = new Lab3();
		int size = 9999097;	//File size for this lab (Per investigation of the data)
		int[] data = null;		//the array to store our data in
		//Variables used for metrics gathering
		long startTime = 0;
		long endTime = 0;
		long time = 0;
		for (int i = 1; i <= 10; i++)	//run three times with different (descending) data sizes
		{
			data = run.getData(".//lab1_data.txt", size);	//Get the integers from our file
			startTime = System.nanoTime();		//record the time of sort start
			run.binSort(data);	//start bin sort
			endTime = System.nanoTime();		//record the time of sort complete
			time = run.runTime(startTime, endTime);	//calculate the run time in milliseconds
			//print out success and time elapsed or failure of sort
			if (run.flgIsSorted(data))
			{
				System.out.println("BinSort: Verified Sorted in " + time + " milliseconds!");
			}
			else
			{
				System.out.println("BinSort: NOT SORTED!");
			}
			
			data = run.getData(".//lab1_data.txt", size);	//Get the ints again to unsort.
			int max = run.maxValue(data);		//	get the biggest number
			String m = Integer.toString(max);	//	convert that number to a string
			int d = m.length();					//	get the length of the string (number) to get max number of digits
			startTime = System.nanoTime();		//record the time of sort start
			run.radixSort(data, d);				//	start radix sort
			endTime = System.nanoTime();		//record the time of sort complete
			time = run.runTime(startTime, endTime);	//calculate the run time in milliseconds
			//print out success and time elapsed or failure of sort
			if (run.flgIsSorted(data))
			{
				System.out.println("RadixSort: Verified Sorted in " + time + " milliseconds!");
			}
			else
			{
				System.out.println("RadixSort: NOT SORTED!");
			}
			data = run.getData(".//lab1_data.txt", size);	//Get the ints again to unsort.
			startTime = System.nanoTime();		//record the time of sort start
			run.auxQuickSort(data, 0, size-1);
			endTime = System.nanoTime();		//record the time of sort complete
			time = run.runTime(startTime, endTime);	//calculate the run time in milliseconds
			//print out success and time elapsed or failure of sort
			if (run.flgIsSorted(data))
			{
				System.out.println("QuickSort: Verified Sorted in " + time + " milliseconds!");
			}
			else
			{
				System.out.println("QuickSort: NOT SORTED!");
			}
			size /= 2;	//divide our data size by 2 to get a new size for next run
			System.out.println("-----------------------------------------------");
		}	
	}
	
	
	public void binSort (int[] array)
	{
		int n = maxValue(array);		//max bin key
		int l = array.length - 1;		//max numbers in a bin
		int[] bin = new int[n+1];		//set-up the bins
		
		for (int i = 0; i <= n; i++) 	//Init bin values to 0 (empty)
		{								// |
			bin[i] = 0;					// v
		}								//-----------------------------
		
		for (int i = 0; i <= l; i++)	//Add 1 to each bin which has a value in given array
		{								// |
			bin[array[i]]++;			// v
		}								//--------------------------------------------------
		
		int outIndex = 0;
		for (int i = 0; i <= n; i++)			//move from bin to bin in acsending order
		{
			for (int j = 0; j < bin[i]; j++)	//figure out how many numbers are in the bin
			{
				array[outIndex] = i;			//place the numbers from the bin back in the array one by one
				outIndex++;
			}
		}
	}
	
	
	public void radixSort (int[] array, int d)
	{
		
		for (int i = d; i > 0; i--)
		{
			int e = (d-i) * 10;							//get division number for digit extraction
			if (e == 0)									//if on the last digit (e == 0) e is set to 1
			{											//	to solve for divide by zero senario.
				e = 1;
			}
			radixBinSort(array, e, d);
		}
	}
	private void radixBinSort(int[] array, int digit, int d)	//modified binSort to work with radixSort
	{
		int n = array.length;
		int[][] bins = new int[10][n];					//array of bins to store numbers of a check digit
		int[] count = new int[10];						//array to keep track of how many numbers in each bin
		
		for (int i = 0; i < n; i++)
		{
			int num = Integer.numberOfLeadingZeros(d-1);	//padd numbers with max digits - 1 zeros to handle variable integer length
			int e = num/digit % 10;				//get the desired digit
			bins[e][count[e]] = num;				//place the number from array into the correct bin by digit
			count[e]++;									//increment the amount of numbers in the digit's bin
		}
		
		int outIndex = 0;								//index used to reconstruct the sorted array
		for (int bin = 0; bin < 10; bin ++)				//take the numbers out of the bins in order
		{	
			for (int i = 0; i < count[bin]; i++)
			{
				array[outIndex] = bins[bin][i];			//put the number into the array
				outIndex++;								//increase reconstruction index
			}
		}
	}
	/*
	 * Get the largest value stored in an array
	 */
	public int maxValue(int[] array)
	{
		int max = 0;
		for (int num : array)
		{
			if (num > max)
			{
				max = num;
			}
		}
		return max;
	}
	
	/*
	 * Method to populate an array with integers from a text file
	 * (Version imported from lab 2)
	 */
	public int[] getData (String path, int n)
	{
		int[] data = new int[n];
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = null;
			int i = 0;
			while ((line = reader.readLine()) != null && i < n)
			{
				data[i] = Integer.parseInt(line);
				i++;
			}
			reader.close();
			System.out.println("Data (" + n + ") read into the array!");
		}
		catch(IOException e)
		{
			System.out.println("Cannot find file: " + path);
		}
		return data;
	}
	
	/*
	 * Check if a given array is sorted in increasing order. (recursive)
	 * STACKOVERFLOW on 100,000+ sized arrays (fix if time allows).
	 * (Taken from lab 1)
	 * @param array The given array to check. 
	 * @return true if and only if the array is sorted in increasing order.
	 */
	public boolean flgIsSorted (int[] array)
	{
		int n = array.length;
		if (n <= 5000)
		{
			return flgIsSortedRecursion(array, n);
		}
		else
		{
			if (flgIsSortedRecursion(Arrays.copyOfRange(array, 0, 5000), 5000) == true)
			{
				return flgIsSortedLooper(Arrays.copyOfRange(array, 4999, n));
			}
			return false;
		}
	}
	private boolean flgIsSortedRecursion (int[] array, int n)
	{
		if (n == 1)
		{
			return true;
		}
		else if (array[n-2] > array[n-1])
		{
			return false;
		}
		return flgIsSortedRecursion (array, n-1);
	}
	private boolean flgIsSortedLooper(int[] array)
	{
		int prevNum = 0;
		for (int num : array)
		{
			if (num < prevNum)
			{
				return false;
			}
			prevNum = num;
		}
		return true;
	}
	
	/*
	 * Sort the elements between the startIndex and endIndex using Quick Sort with 
	 * the pivot to be the average of the values at startIndex, endIndex, and the 
	 * middle element between startIndex and endIndex.
	 * (From lab 1)
	 * @param array The given array to be sorted
	 * @param startIndex Index to start sorting at
	 * @param endIndex Index to end sorting at
	 */
	public void auxQuickSort (int[] array, int startIndex, int endIndex)
	{
		if (startIndex < endIndex)
		{
			int q = partition(array, startIndex, endIndex);
			auxQuickSort(array, startIndex, q-1);
			auxQuickSort(array, q+1, endIndex);
		}
	}
	private int partition (int[] array, int startIndex, int endIndex)
	{
		int mid = (startIndex + endIndex)/2;
		int pivot = (startIndex + mid + endIndex)/3; //Get pivot from average of startIndex, mid, and endIndex
		exchange(array, pivot, endIndex);	//Switch the pivot element with the last element to get pivot in place
		int x = array[endIndex];
		int i = startIndex - 1;
		for (int j = startIndex; j <= (endIndex -1); j++)
		{
			if (array[j] <= x)
			{
				i++;
				exchange(array, i, j);
			}
		}
		exchange(array, (i+1), endIndex);
		return i + 1;
	}
	public void exchange(int[] array, int i, int j)
	{
		int temp = array[j];
		array[j] = array[i];
		array[i] = temp;	
	}
	
	/*
	 * Method to determine the running time for a method
	 * Given start and end times in nano-seconds
	 * Returns a run-time in milliseconds
	 */
	public long runTime(long start, long end)
	{
		long time = 0;
		time = (end - start)/1000000;
		return time;
	}
}
