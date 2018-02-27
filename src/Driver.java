import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.Instances;

public class Driver {

	public static void main(String[] args) {

		BufferedReader reader;
		Instances fall2012, fall2011, fall2010;
		try {
			reader = new BufferedReader(new FileReader("Fall2010CAS.arff"));
			fall2010 = new Instances(reader);
			reader.close();
			reader = new BufferedReader(new FileReader("Fall2011CAS.arff"));
			fall2011 = new Instances(reader);
			reader.close();
			reader = new BufferedReader(new FileReader("Fall2012CAS.arff"));
			fall2012 = new Instances(reader);
			reader.close();
			
			fall2010.insertAttributeAt(new Attribute("Year"), 0);
			fall2011.insertAttributeAt(new Attribute("Year"), 0);
			fall2012.insertAttributeAt(new Attribute("Year"), 0);
			
			
			setYears(fall2010, 2010);
			setYears(fall2011, 2011);
			setYears(fall2012, 2012);
			Instances combined = mergeDatasets(fall2010,fall2011,fall2012);
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Method which sets all the year column of every instance in the provided data set to the year.
	 * @param data The data set to have the 0th column modified for
	 * @param year The year that the column will be set to
	 */
	private static void setYears(Instances data, int year) {
		for(int i = 0; i < data.numInstances();i++)
			data.instance(i).setValue(0, year);
	}
	
	/**
	 * Method which combines three similar data sets that have the same attributes.
	 * @param first The first data set to be merged with the others
	 * @param second The second data set which is to be merged with the others
	 * @param third The third data set which is to be merged with the others
	 * @return The combined data set
	 */
	private static Instances mergeDatasets(Instances first, Instances second, Instances third) {
		int capacity = first.numInstances() + second.numInstances() + third.numInstances();
		Instances combined = new Instances(first,capacity);
		System.out.println(first.firstInstance()+ "\n" + second.firstInstance() + "\n" + third.firstInstance() );
		System.out.println(first.numInstances() +" " + first.numAttributes());
		System.out.println(second.numInstances() + " " + second.numAttributes());
		System.out.println(third.numInstances() + " " + third.numAttributes());
		System.out.println(combined.numInstances() + " " + combined.numAttributes());
		for(int i = 0; i < first.numInstances();i++)
			combined.add(first.get(i));
		System.out.println(combined.numInstances() + " " + combined.numAttributes());

		for(int i = 0; i < second.numInstances();i++)
			combined.add(second.get(i));
		System.out.println(combined.numInstances() + " " + combined.numAttributes());
		
		for(int i = 0; i < third.numInstances();i++)
			combined.add(third.get(i));
		System.out.println(combined.numInstances() + " " + combined.numAttributes());
		
		System.out.println(combined.get(0));
		System.out.println(combined.instance(combined.numInstances()-2));
		ArrayList<Integer> brokenIndexes = new ArrayList<Integer>();
		
		for(int i = 0; i < combined.numInstances();i++) {
			try {
				combined.get(i).toString();
			}catch(IndexOutOfBoundsException e) {
				brokenIndexes.add(i);
			}
		}
		
		System.out.println();
		for(int i = 0; i < brokenIndexes.size();i++) {
			if( i %10 == 0)
				System.out.println();
			System.out.print(brokenIndexes.get(i) +",\t");
		}
		return combined;
	}

}
