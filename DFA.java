/**
 * LAB 3 Part 2 - DFA Simulator
 * 
 * Simulate a DFA given (Q,E,S,q0,F)
 *  Q is a finite set called the states
 *  E is a finite set called the alphabet
 *  S : Q x E -> Q is the transition function
 *  q0 Element of Q is the start state, and
 *  F, a subset of Q, is the set of accept states
 *  
 * NOTE: You must input strings.
 * @author Stephen K Oliver
 * @version 13 MAR 2017 (VER 1)
 */
package Labs;

import java.util.ArrayList;
import java.util.Scanner;

public class DFA {
	
	public ArrayList<String> states;
	public String[] alphabet;
	public String[][] transitions;
	public String startState;
	public ArrayList<String> finalStates;
	
	//Default Constructor - Does nothing but ask you for input.
	public DFA ()
	{
		System.out.println("Please Specify a DFA start state and alphabet.");
	}
	
	/**
	 * Constructor for the DFA Class
	 * @param startState The desired DFA start state
	 * @param alphabet The alphabet accepted by the DFA
	 */
	public DFA (String startState, String[] alphabet)
	{
		states = new ArrayList<String>();
		this.alphabet = alphabet;
		initTransitionsTable();
		this.startState = startState;
		addState(startState);
		finalStates = new ArrayList<String>();
	}
	private void initTransitionsTable()
	{
		int k = alphabet.length;
		transitions = new String[1][k];
		for (int i = 0; i < k; i++)
		{
			transitions[0][i] = null;
		}
	}
	
	//---------- The Sets ----------//
	public void addState(String state)
	{
		states.add(state);
	}
	
	public void addFinalState(String state)
	{
		finalStates.add(state);
	}
	
	public void addTransition(String from,String letter, String to)
	{
		String[][] old = transitions.clone(); //storage for old transitions table state
		int n = states.size();		// n x k of the new table
		int k = alphabet.length;	// n x k of the new table
		transitions = new String[n][n];		//re-initialize the table with new n x n dimensions
		for (int i = 0; i < old.length; i++)
		{
			for (int j = 0; j < k; j++)
			{
				transitions[i][j] = old[i][j];		//copy the old transitions to the new table
			}
		}
		//Finally add in the new transition
		int i = getIndex("state", from);
		int j = getIndex("letter", letter);
		transitions[i][j] = to;
	}
	
	//--------- The Gets ----------//
	/**
	 * Get the index for a state in the states or a letter in the alphabet
	 * @param item	specify if looking for a state or and letter in the alphabet
	 * @param value the value of the item getting the index for
	 * @return
	 */
	public int getIndex(String item, String value)
	{
		int index = 0;
		if (item == "state")	//get the index for a state
		{
			for (String s : states)
			{
				if (s.equals(value))
				{
					return index;
				}
				index++;
			}
		}
		else if (item == "letter")		//get the index for a letter
		{
			for (String s : alphabet)
			{
				if (s.equals(value))
				{
					return index;
				}
				index++;
			}
		}
		else
		{
			return -1; //invalid item
		}
		index = -1;	//didn't find the state or letter
		return index;
	}
	
	public String getInput()
	{
		String input = "";
		@SuppressWarnings("resource")
		Scanner reader = new Scanner(System.in); 	//Read from System.in
		System.out.println("Enter A String to put into DFA: "); 	//Prompt for input
		input = reader.nextLine();	//get the line of input to return
		return input;
	}
	
	public String getNextState(String currState, String value)
	{
		String nextState = "";
		
		//find the next state
		int i = getIndex("state", currState);
		int j = getIndex("letter", value);
		nextState = transitions[i][j];
		
		return nextState;
	}
	//---------- Printing Methods ----------//
	//Print the transition table
	public void printTransitions()
	{
		int n =  transitions.length;
		int k = alphabet.length;
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < k; j++)
			{
				System.out.print(transitions[i][j] + " | ");
			}
			System.out.print("\n");
		}
	}
	//Prints the contents of an array
	public void printArray(String[] array)
	{
		int i = 0;
		for (String s : array)
		{
			System.out.println(i + ": " + s);
			i++;
		}
	}
	public void printArrayList(ArrayList<String> array)
	{
		int i = 0;
		for (String s : array)
		{
			System.out.println(i + ": " + s);
			i++;
		}
	}
	
	//Message to print if the input string is accepted by the DFA
	public void printSuccessMsg(String input)
	{
		System.out.println(input + " is accepted by the DFA.");
	}
	
	//Message to print if the input string crashes the DFA
	public void printCrashMsg(String input)
	{
		System.out.println(input + " crashed the DFA.");
	}
	
	//---------- Other Methods ----------//
	public void runDFA(String input)
	{
		//Handle empty string entry ----------------------------------------
				if (input.length() == 0  && isFinal(startState)) //if startState is a Final State: OK
				{
					printSuccessMsg("The empty string");
					System.exit(0);
				}
				else if (input.length() == 0 && isFinal(startState) == false)		//if startState NOT a Final State: Crash!
				{
					printCrashMsg("The empty String");
					System.exit(0);
				}
		//------------------------------------------------------------------
				String currState = startState;			//init current state to the start state
				String[] letters = input.split("");		//split the input string into consumeable letters
				for (String letter : letters)			//For all of the inputed letters
				{
					if (isInAlphabet(letter))
					{
						currState = getNextState(currState, letter); //Move to the next state
					}
					else
					{
						printCrashMsg(input);		//Invalid letter consumed, crash.
						System.exit(0);
					}
				}
				if (isFinal(currState))
				{
					printSuccessMsg(input);
					System.exit(0);
				}
				else
				{
					printCrashMsg(input);
					System.exit(0);
				}
	}
	/**
	 * Check if the last state visited by an inputed string is a final state
	 * @param state last state visited by the inputed string
	 * @return if on a final state return true, else false
	 */
	public boolean isFinal(String state)
	{
		if (finalStates.contains(state))
		{
			return true;
		}
		return false;
	}
	/**
	 * Check if an inputed letter is valid
	 * @param letter letter from input
	 * @return true if the input's current letter is in the alphabet, else false
	 */
	public boolean isInAlphabet(String letter)
	{
		for (String a : alphabet)
		{
			if(a.equals(letter))
			{
				return true;
			}
		}
		return false;
	}
	
	//---------- The Main Method ----------//
	public static void main (String[] args)
	{
		//Build a virtual DFA (accepts all even binary numbers and the empty string)
		String[] alpha = {"0", "1"}; 			//the alphabet
		DFA evens = new DFA("q0", alpha);		//initialize the virtual DFA with a start state "q0" and our alphabet above
		evens.addState("q1");					//add state "q1"
		evens.addTransition("q0", "0", "q0");	//add transition from q0 to q0 if consume a "0"
		evens.addTransition("q0", "1", "q1");	//add transition from q0 to q1 if consume a "1"
		evens.addTransition("q1", "1", "q1"); 	//add transition from q1 to q1 if consume a "1"
		evens.addTransition("q1", "0", "q0"); 	//add transition from q1 to q0 if consume a "0"
		evens.addFinalState("q0"); 		 		//add q0 as a final state
		
		//Printouts for the DFA constructed
		System.out.println("States: ");
		evens.printArrayList(evens.states);
		System.out.println("Alphabet: ");
		evens.printArray(evens.alphabet);
		System.out.println("Transitions: ");
		evens.printTransitions();
		System.out.println("Start State: ");
		System.out.println(evens.startState);
		System.out.println("Final States: ");
		evens.printArrayList(evens.finalStates);
		
		//Run String Through the DFA (evens) built above
		String input = evens.getInput();	//get user input to run through the DFA
		evens.runDFA(input);
	}
}
