import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ConnectedComponents {

	public static void main(String[] args) {
		
		String inputFileName;
		String printFileName;
		String labelFileName;
		String propertyFileName;
		String connectednessString;
		
		if(args.length != 5) {
			System.out.println("Invalid Number of Arguments.");
			System.out.println("Please provide input file, print file, label file, property file, and a value for connectedness (4 or 8).");
			System.exit(0);
		}
		
		try {
			
			inputFileName = args[0];
			printFileName = args[1];
			labelFileName = args[2];
			propertyFileName = args[3];
			connectednessString = args[4];
			
			Scanner input = new Scanner(new File(inputFileName));
			FileWriter print = new FileWriter(new File(printFileName));
			FileWriter label = new FileWriter(new File(labelFileName));
			FileWriter property = new FileWriter(new File(propertyFileName));
			int connectedness = Integer.parseInt(connectednessString);
			
			ConnectedComponentsLabel components = new ConnectedComponentsLabel(input);
			components.loadImage(input, components.zeroFramedArray);
			int newLabel = components.connectPass1(connectedness, components.zeroFramedArray, components.nonZeroNeighorArray);
			components.setNewLabel(newLabel);
			print.write("Pretty Print: Result of Pass1:\n");
			components.prettyPrint(print);
			print.write("\nContent of EquivalentArray after Pass1:\n");
			components.printEquivalentArray(components.getNewLabel(), print);
			
			components.connectPass2(connectedness, components.zeroFramedArray, components.nonZeroNeighorArray);
			print.write("Pretty Print: Result of Pass2:\n");
			components.prettyPrint(print);
			print.write("\nContent of EquivalentArray after Pass2:\n");
			components.printEquivalentArray(components.getNewLabel(), print);
			
			components.manageEquivalentArray(components.equivalentArray, components.getNewLabel());
			print.write("\nContent of EquivalentArray after managing the array:\n");
			components.printEquivalentArray(components.getNewLabel(), print);
			
			components.connectPass3(components.zeroFramedArray, components.nonZeroNeighorArray);
			print.write("Pretty Print: Result of Pass3:\n");
			components.prettyPrint(print);
			print.write("\nContent of EquivalentArray after Pass3:\n");
			components.printEquivalentArray(components.getNewLabel(), print);
			
			components.printImage(label);
			components.printProperty(property);
			components.drawBoxes(components.zeroFramedArray, components.propertyCC);
			print.write("\nResult of the Array after DrawBowes:\n");
			components.prettyPrint(print);
			
			input.close();
			print.close();
			label.close();
			property.close();
			
		}
		catch(IOException e) {
			e.printStackTrace();
		}

	}

}
