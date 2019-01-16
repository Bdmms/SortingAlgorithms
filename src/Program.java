
/*
 * Sean Rannie
 * August 1st, 2018
 * 
 * This program tests the efficiency of multiple sorting algorithms
 * by timing them from start to finish. Some algorithms take advantage
 * of certain attributes of the situation, such as all numbers being
 * integers, the size of the array being sorted, and the min and max
 * values of the array elements. Many algorithms are not represented
 * here. This was meant to practice a specific variety of methods.
 */
public class Program 
{
	private static final int SIZE = 4096;			//Size of array 
	private static final int MIN_VALUE = 0;			//Min value generated for array
	private static final int MAX_VALUE = 9999;		//Max value generated for array
	private int[] unsortedArray = new int[SIZE];	//The original array
	private int[] sortedArray = new int[SIZE];		//The final array written to (some algorithms don't needs this, while others require this)
	
	int[] count = new int[MAX_VALUE];				//An array that counts the elements with a specific value (used by counting sort)
	int[][] pointer = new int[MAX_VALUE][SIZE];		//An array that reports the address of each value in an array (used by heap counting sort)
	
	private long timeStart = 0;						//The time the sort began
	private long timeEnd = 0;						//The time the sort finished
			
	//Main function
	public static void main(String[] args) 
	{
		new Program();
	}
	
	//Constructor used to initialize data
	public Program()
	{
		//Elements are generated
		for(int i = 0; i < unsortedArray.length - 1; i++)
		{
			unsortedArray[i] = (int)(Math.random()*(MAX_VALUE - MIN_VALUE) + MIN_VALUE);
		}
		
		//Elements are sorted
		for(int i = 0; i < 7; i++)
		{
			sortCompare(i);
		}
	}
	
	//Some algorithms don't need the two arrays so the elements are copied ahead of time
	public void copyArray()
	{
		for(int i = 0; i < unsortedArray.length; i++)
		{
			sortedArray[i] = unsortedArray[i];
		}
	}
	
	//An array algorithm is chosen based on index
	public void sortCompare(int i)
	{
		copyArray();
		System.out.println("------------------------");
		
		switch(i)
		{
			case 0:	//Bubble
			System.out.println("Bubble Sort");
			timeStart = System.nanoTime();
			bubbleSort(sortedArray);
			timeEnd = System.nanoTime();
			break;
			
			case 1: //Selection
			System.out.println("Selection Sort");
			timeStart = System.nanoTime();
			selectionSort(sortedArray);
			timeEnd = System.nanoTime();
			break;
		
			case 2: //Double Selection
			System.out.println("Double selection Sort");
			timeStart = System.nanoTime();
			doubleSelectionSort(sortedArray);
			timeEnd = System.nanoTime();
			break;
			
			case 3: //Local Counting
			System.out.println("Local Counting Sort");
			timeStart = System.nanoTime();
			localCountingSort(unsortedArray);
			timeEnd = System.nanoTime();
			System.out.println((count.length/250) + " KB Cache Used");
			break;
			
			case 4:	//Selection Counting
			System.out.println("Selection Counting Sort");
			timeStart = System.nanoTime();
			selectionCountingSort(sortedArray);
			timeEnd = System.nanoTime();
			System.out.println((count.length/250) + " KB Cache Used");
			break;
			
			case 5: //Heap Counting
			System.out.println("Heap Counting Sort");
			timeStart = System.nanoTime();
			heapCountingSort(unsortedArray);
			timeEnd = System.nanoTime();
			System.out.println((pointer.length*pointer[0].length/250) + " KB Cache Used");
			break;
			
			case 6:	//Merge
			System.out.println("Merge Sort");
			timeStart = System.nanoTime();
			mergeSort(sortedArray, 0, sortedArray.length - 1);
			timeEnd = System.nanoTime();
			break;
			
			default:	//Note: Bogo sort is not actually used, it is a joke if invalid index is chosen
			System.out.println("Bogo Sort");
			timeStart = System.nanoTime();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			System.out.println("- Sort Timeout -");
			timeEnd = System.nanoTime();
			
			break;
		}
		
		System.out.println("Sort Completed");
		System.out.println("Time: " + (double)(timeEnd - timeStart)/1000000 + " ms");
		
		//The array is checked to make sure it is sorted
		if(checkSortedArray(sortedArray))
			System.out.println("Array is correctly sorted");
		else
			System.out.println("Array is incorrectly sorted");
	}
	
	//Checks the array to see if it is sorted
	public boolean checkSortedArray(int[] array)
	{
		for(int i = 0; i < array.length - 1; i++)
		{
			//If the current element is greater than the next element
			if(array[i] > array[i+1])
			{
				System.out.println("ELEMENT: " + i);
				return false;
			}
		}
		return true;
	}
	
	//Bubble Sort - every pair of elements is swapped until array is sorted
	public void bubbleSort(int[] array)
	{
		int temp = 0;
		int i = 0;
		boolean check = false;
		
		//While array is not sorted
		while(!check)
		{
			check = true;
			for(i = 0; i < array.length - 1; i++)
			{
				//Swaps elements if out of order
				if(array[i] > array[i+1])
				{
					temp = array[i+1];
					array[i+1] = array[i];
					array[i] = temp;
					
					check = false;
				}
			}
		}
	}
	
	//Selection Sort - The array is split into two fields, sorted and unsorted.
	//Elements are swapped into sorted when they are the next lowest value.
	public void selectionSort(int[] array)
	{
		int lowestValue = 0;
		int temp = 0;
		int i = 0;
		int e = 0;
		
		//For loop for each sorted element
		for(i = 0; i < array.length - 1; i++)
		{
			lowestValue = i;
			
			for(e = i + 1; e < array.length; e++)
			{
				//If element is less than lowestValue
				if(array[e] < array[lowestValue])
					lowestValue = e;
			}
			
			//If a different lowest value was found
			if(lowestValue != i)
			{
				temp = array[lowestValue];
				array[lowestValue] = array[i];
				array[i] = temp;
			}
		}
	}
	
	//Double Selection Sort - The same as the selection sort, except there are three fields
	//lower sorted, unsorted, and high sorted. The lowest and highest values are sorted at
	//the same time.
	public void doubleSelectionSort(int[] array)
	{
		int lowestValue = 0;
		int highestValue = -1;
		int temp = 0;
		
		int lowI = 0;
		int highI = array.length - 1;
		int e = 0;
		
		//While the boundaries of the unsorted field do not overlap/break
		while(lowI < highI)
		{
			lowestValue = lowI;
			highestValue = highI;
			
			//Search for the highest and lowest value
			for(e = lowI; e <= highI; e++)
			{
				//Lowest
				if(array[e] <= array[lowestValue])
					lowestValue = e;
				//Highest
				if(array[e] >= array[highestValue])
					highestValue = e;
			}
			
			//If the corresponding lowest and highest are within swap points
			if(lowestValue == highI && highestValue == lowI)
			{
				temp = array[lowestValue];
				array[lowestValue] = array[lowI];
				array[lowI] = temp;
				lowI++;
				highI--;
			}
			//If the resulting low swap will move the element for high swap
			else if(highestValue == lowI) 
			{
				//HIGH
				temp = array[highestValue];
				array[highestValue] = array[highI];
				array[highI] = temp;
				highI--;
				
				//LOW
				temp = array[lowestValue];
				array[lowestValue] = array[lowI];
				array[lowI] = temp;
				lowI++;
			}
			else
			{
				//LOW
				temp = array[lowestValue];
				array[lowestValue] = array[lowI];
				array[lowI] = temp;
				lowI++;
				
				//HIGH
				temp = array[highestValue];
				array[highestValue] = array[highI];
				array[highI] = temp;
				highI--;
			}
		}
	}
	
	//Selection Counting Sort - Same as selection sort, but the values used in
	//the array are counted. The counting array is initialized ahead of time,
	//but in some scenarios it would have to be initialized in the function. 
	//The algorithm knows when to stop searching when no more of a particular
	//value exist
	public void selectionCountingSort(int[] array)
	{
		//int[] count = new int[MAX_VALUE];
		int sortedElements = 0;
		int i = 0;
		int e = 0;
		int temp = 0;
		
		//Count
		for(i = 0; i < array.length; i++)
		{
			count[array[i]]++;
		}
		
		//Sort
		for(e = 0; sortedElements < array.length; e++)
		{
			//Search if count is greater than 0 in value
			for(i = sortedElements; count[e] > 0; i++)
			{
				//If element is one of the values
				if(array[i] == e)
				{
					temp = array[sortedElements];
					array[sortedElements] = array[i];
					array[i] = temp;
					
					count[e]--;
					sortedElements++;
				}
			}
		}
	}
	
	//Local Counting Sort - Almost identical to Selection Counting
	//Sort, but swapping is not performed. The searched values are
	//written into an entirely different array.
	public void localCountingSort(int[] array)
	{
		//int[] count = new int[MAX_VALUE];
		int sortedElements = 0;
		int i = 0;
		int e = 0;
		
		//Count
		for(i = 0; i < array.length; i++)
		{
			count[array[i]]++;
		}
		
		//Sort
		for(e = 0; sortedElements < array.length; e++)
		{
			//Search array for value
			for(i = 0; count[e] > 0; i++)
			{
				//If element contains value
				if(array[i] == e)
				{
					sortedArray[sortedElements] = array[i];
					count[e]--;
					sortedElements++;
				}
			}
		}
	}
	
	//Heap Counting Sort - Each value in the array is counted, followed 
	//by each value having the index value written to the array. Very 
	//few comparisons need to be made, however, it requires a lot of memory
	public void heapCountingSort(int[] array)
	{
		//int[][] pointer = new int[MAX_VALUE][SIZE];
		int sortedElements = 0;
		int i = 0;
		int e = 0;
		
		//Count
		for(i = 0; i < array.length; i++)
		{
			pointer[array[i]][0]++;
			pointer[array[i]][pointer[array[i]][0]] = i;
		}
		
		//Sort the elements of the array into a different array
		for(e = 0; e < pointer.length; e++)
		{
			for(i = 1; i <= pointer[e][0]; i++)
			{
				sortedArray[sortedElements] = array[pointer[e][i]];
				sortedElements++;
			}
		}
	}
	
	//Merge Sort - The sorting is broken up into small parts, each
	//part is then merged, which is a more efficient sort.
	public void mergeSort(int[] a, int l, int r)
	{
		//Recursively split array into parts until it compares two elements
		if(l < r)
		{
			int m = (l+r)/2;
			mergeSort(a, l, m);
			mergeSort(a, m + 1, r);
			merge(a, l, m, r);
		}
	}
	
	//The merging algorithm for the array
	public void merge(int[] a, int l, int m, int r)
	{
		int n1 = m - l + 1;
		int n2 = r - m;
		
		int l1 = 0;
		int l2 = 0;
		int[] LA = new int[n1];
		int[] RA = new int[n2];
		
		//Copy array elements over
		for(r = 0; r < n1; r++)
			LA[r] = a[l+r];
		for(r = 0; r < n2; r++)
			RA[r] = a[m+r+1];
		
		//Merge the two arrays into the main array
		while(l1 < n1 && l2 < n2)
		{
			if(LA[l1] <= RA[l2])
			{
				a[l] = LA[l1];
				l1++;
			}
			else
			{
				a[l] = RA[l2];
				l2++;
			}
			l++;
		}
		
		//Fill leftover in 1st array
		while(l1 < n1)
		{
			a[l] = LA[l1];
			l1++;
			l++;
		}
		
		//Fill leftover in 2nd array
		while(l2 < n2)
		{
			a[l] = RA[l2];
			l2++;
			l++;
		}
	}
}

