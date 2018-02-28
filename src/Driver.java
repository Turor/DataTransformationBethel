import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import weka.core.Attribute;
import weka.core.Instances;

public class Driver {

	public static void main(String[] args) {

		BufferedReader reader;
		Instances fall2012, fall2011, fall2010, combined;
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
			reader = new BufferedReader(new FileReader("Combined.arff"));
			combined = new Instances(reader);
			reader.close();



			combined.insertAttributeAt(new Attribute("Year"), 0);
			for(int i = 0; i < combined.numInstances();i++) {
				if(i < fall2010.numInstances()) {
					combined.instance(i).setValue(0, 2010);

				}else if( i < fall2010.numInstances()+fall2011.numInstances()) {
					combined.instance(i).setValue(0, 2011);

				}else {
					combined.instance(i).setValue(0, 2012);

				}
			}

			//			for(int i = 0; i < combined.numAttributes();i++) {
			//				if( (i+1) % 5 == 0)
			//					System.out.println();
			//				System.out.print(i+" " + combined.attribute(i).name() + ":\t" + combined.firstInstance().value(i) + "  \t");
			//			}

			
			System.out.println("Before Transformations\n"+combined.firstInstance());
			
			//Major 2
			replaceMissing(combined, 48);
			System.out.println("\nAfter defaulting missing data for second majors\n"+ combined.firstInstance());
			//Department 2
			replaceMissing(combined, 50);
			System.out.println("\nAfter defaulting missing data for second departmets\n"+combined.firstInstance());
			//Sports
			replaceMissing(combined, 53);
			System.out.println("\nAfter defaulting missing data for the sport category to no sport\n" +combined.firstInstance());

			standardizeSports(combined);
			System.out.println("\nAfter transforming sports to a more standard format\n" + combined.firstInstance());
			
			insertNewSportsColumns(combined);
			
			populateSportsColumns(combined);
			
			for(int i = 53; i < combined.numAttributes();i+=27)
				combined.deleteAttributeAt(i);
			
			System.out.println("\nAfter converting the sports column to the new format\n"+combined.firstInstance());
			
			combined.insertAttributeAt(new Attribute("Number of Semesters at Bethel"), 39);
			combined.insertAttributeAt(new Attribute("Cumulative number of Credits"), 40);
			combined.insertAttributeAt(new Attribute("Last Recorded GPA"), 41);
			
			updateCredits(combined);
			updateGPA(combined);
			updateSemesterCount(combined);
			
			System.out.println("\nAfter adding and updating time at Bethel, Total credits, and Cumulative GPA\n"+combined.firstInstance());

			
			PrintWriter out = new PrintWriter("C:/Users/Turor/Documents/Datamining/DataTransformation/result.arff");
			out.println(combined.toString());
			out.close();
			
			


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void updateSemesterCount(Instances combined) {
		for(int index = 0; index < combined.numInstances();index++) {
			int semester = 0;
			for(int hour = 42; hour < combined.numAttributes();hour+= 26) {
				if( combined.instance(index).value(hour)>0)
					semester++;
			}
			combined.instance(index).setValue(39, semester);
		}
		
	}

	private static void updateGPA(Instances combined) {
		for(int index = 0; index < combined.numInstances();index++) {
			double gpa = combined.instance(index).value(44);
			for(int term = 44; term < combined.numAttributes();term++) {
				if(combined.instance(index).hasMissingValue())
					break;
				else
					gpa = combined.instance(index).value(term);
			}
			combined.instance(index).setValue(41, gpa);
		}
		
	}

	private static void updateCredits(Instances combined) {
		for(int index = 0; index < combined.numInstances();index++) {
			double gpa = combined.instance(index).value(45);
			for(int term = 45; term < combined.numAttributes();term++) {
				if(combined.instance(index).hasMissingValue())
					break;
				else
					gpa = combined.instance(index).value(term);
			}
			combined.instance(index).setValue(40, gpa);
		}
		
	}

	private static void populateSportsColumns(Instances combined) {
		for(int i = 0; i < combined.numInstances();i++) {
			for(int root = 53; root<combined.numAttributes();root+=27) {
				for(int j = 0; j < 12; j++)
					combined.instance(i).setValue(root+1+j, 0);
		
				String sports = combined.instance(i).toString(root);
				int arg1 = 1;
				if(sports.contains("Baseball"))
					combined.instance(i).setValue(root+1, 1);
				if(sports.contains("Basketball"))
					combined.instance(i).setValue(root+2, 1);
				if(sports.contains("Crosscountry"))
					combined.instance(i).setValue(root+3, 1);
				if(sports.contains("Football"))
					combined.instance(i).setValue(root+4,1);
				if(sports.contains("Golf"))
					combined.instance(i).setValue(root+5, 1);
				if(sports.contains("Icehockey"))
					combined.instance(i).setValue(root+6, arg1);
				if(sports.contains("Soccer"))
					combined.instance(i).setValue(root+7, arg1);
				if(sports.contains("Softball"))
					combined.instance(i).setValue(root+8, arg1);
				if(sports.contains("Tennis"))
					combined.instance(i).setValue(root+9, arg1);
				if(sports.contains("Trackindoor"))
					combined.instance(i).setValue(root+10, arg1);
				if(sports.contains("Trackoutdoor"))
					combined.instance(i).setValue(root+11, arg1);
				if(sports.contains("Volleyball"))
					combined.instance(i).setValue(root+12, arg1);
				
			}
			
		}
	}

	private static void insertNewSportsColumns(Instances combined) {
		int insertionPoint = 54;
		for(int i = 0; i < 8; i++) {
			String sport ="T" + i+ "Volleyball";
			combined.insertAttributeAt(new Attribute(sport), insertionPoint);
			sport = "T" + i+ "Trackoutdoor";
			combined.insertAttributeAt(new Attribute(sport), insertionPoint);
			sport = "T" + i+"Trackindoor";
			combined.insertAttributeAt(new Attribute(sport), insertionPoint);
			sport = "T" + i+"Tennis";
			combined.insertAttributeAt(new Attribute(sport), insertionPoint);
			sport = "T" + i+"Softball";
			combined.insertAttributeAt(new Attribute(sport), insertionPoint);
			sport = "T" + i+"Soccer";
			combined.insertAttributeAt(new Attribute(sport), insertionPoint);
			sport = "T" + i+"Icehockey";
			combined.insertAttributeAt(new Attribute(sport), insertionPoint);
			sport = "T" + i+"Golf";
			combined.insertAttributeAt(new Attribute(sport), insertionPoint);
			sport = "T" + i+"Football";
			combined.insertAttributeAt(new Attribute(sport), insertionPoint);
			sport = "T" + i+"Crosscountry";
			combined.insertAttributeAt(new Attribute(sport), insertionPoint);
			sport = "T" + i+"Basketball";
			combined.insertAttributeAt(new Attribute(sport), insertionPoint);
			sport = "T" + i+"Baseball";
			combined.insertAttributeAt(new Attribute(sport), insertionPoint);
			insertionPoint +=27;
		}
		
	}

	private static void standardizeSports(Instances combined) {
		for(int i = 0; i < combined.numInstances();i++) {
			for(int column = 53; column < 159; column+=15) {
				if(combined.instance(i).toString(column).equals("0"))
					continue;
				else {
					String sport = combined.instance(i).toString(column);

					sport = sport.replace("Womens", "");
					sport = sport.replace("Mens", "");
					sport = sport.replace("-", "");
					sport = sport.replace("TrackIndoor", "Trackindoor");
					sport = sport.replace("CrossCountry", "Crosscountry");
					sport = sport.replace("TrackOutdoor", "Trackoutdoor");
					sport = sport.replace("IceHockey", "Icehockey");
					combined.get(i).setValue(column, sport);
				}

			}
		}

	}

	private static void replaceMissing(Instances combined, int t1) {
		for(int i = 0; i < combined.numInstances();i++) {
			for(int column = t1; column < 159 ; column = column + 15) {
				if(combined.instance(i).isMissing(column) || combined.instance(i).toString(column).equals("?"))
					combined.instance(i).setValue(column, 0);
			}
		}

	}


}
