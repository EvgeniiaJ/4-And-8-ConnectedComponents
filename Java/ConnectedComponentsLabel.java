import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ConnectedComponentsLabel {

	private int numRows;
	private int numCols;
	private int minValue;
	private int maxValue;
	private int newMin;
	private int newMax;
	private int newLabel;
	private int trueLabel;
	
	public int trueNumberCC;
	public int[][] zeroFramedArray;
	public int[][] labelArray;
	public int neighborsCount;
	public int[] nonZeroNeighorArray;
	public int[] equivalentArray;
	public Property[] propertyCC;
	
	
	public ConnectedComponentsLabel(Scanner input) {
		if (input.hasNext()) {
			this.numRows = input.nextInt();
		}
		if (input.hasNext()) {
			this.numCols = input.nextInt();
		}
		if (input.hasNext()) {
			this.minValue = input.nextInt();
		}
		if (input.hasNext()) {
			this.maxValue = input.nextInt();
		}
		
		newLabel = 0;
		neighborsCount = 5; // compatible for both 4 and 8 connected components

		initializeArrays();
	}

	public void initializeArrays() {
		zeroFramedArray = new int[numRows + 2][numCols + 2];
		labelArray = new int[numRows + 2][numCols + 2];

		for (int i = 0; i < numRows + 2; i++) {
			for (int j = 0; j < numCols + 2; j++) {
				zeroFramedArray[i][j] = 0;
				labelArray[i][j] = 0;
			}
		}

		nonZeroNeighorArray = new int[neighborsCount];

		for (int i = 0; i < neighborsCount; i++) {
			nonZeroNeighorArray[i] = 0;
		}

		equivalentArray = new int[(numRows * numCols) / 2];

		for (int i = 0; i < ((numRows * numCols) / 2); i++) {
			equivalentArray[i] = i;
		}
	}

	public void loadImage(Scanner input, int[][] array) {

		while (input.hasNext()) {
			for (int i = 1; i < numRows + 1; i++) {
				for (int j = 1; j < numCols + 1; j++) {
					array[i][j] = input.nextInt();
				}
			}
		}

	}
	
	public void prettyPrint(FileWriter output) {

		try {
			for (int i = 0; i < numRows + 2; i++) {
				for (int j = 0; j < numCols + 2; j++) {
					if (zeroFramedArray[i][j] > 0) {

						output.write(zeroFramedArray[i][j] + " ");

						if (zeroFramedArray[i][j] < 10) {
							output.write(" ");
						}

					} 
					else {
						output.write("   ");
					}
				}
				output.write("\n");
			}
			output.write("\n");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void printEquivalentArray(int newLabel, FileWriter output) {
		try {
			output.write("index   value\n");
			for (int i = 1; i <= newLabel; i++) {
				output.write(i + " " + equivalentArray[i] + "\n");
			}
			output.write("\n");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}	

	public int connectPass1(int connectedness, int[][] array, int[] neighbors) {
		newLabel = 0;
		for (int i = 1; i < numRows + 1; i++) {
			for (int j = 1; j < numCols + 1; j++) {
				if (array[i][j] > 0) {
					
					loadNeighbors(i, j, connectedness, neighbors, 1);
					
					if (connectedness == 4) {
						
						int a = neighbors[0];
						int b = neighbors[1];
						
						if (neighbors[0] == 0 && neighbors[1] == 0) {
							newLabel++;
							array[i][j] = newLabel;
						} 
						else if ((a != 0 || b != 0) && (a != 0 && a == b)) {
							int minLabel = findMinLabel(connectedness, neighbors);
							array[i][j] = minLabel;
							updateEquivalentArray(minLabel);
						} 
						else {
							int minLabel = findMinLabel(connectedness, neighbors);
							array[i][j] = minLabel;
							updateEquivalentArray(minLabel);
						}
					} 
					else if (connectedness == 8) {
						int a = neighbors[0];
						int b = neighbors[1];
						int c = neighbors[2];
						int d = neighbors[3];

						if (a == 0 && b == 0 && c == 0 && d == 0) {
							newLabel++;
							array[i][j] = newLabel;
						}
						else if ((a != 0 || b != 0 || c != 0 || d != 0)
								&& ((a != 0 && a == b) || (a != 0 && a == c) || (a != 0 && a == d) || (b != 0 && b == c)
										|| (b != 0 && b == d) || (c != 0 && c == d))) {
							int minLabel = findMinLabel(connectedness, neighbors);
							array[i][j] = minLabel;
						}
						else {
							int minLabel = findMinLabel(connectedness, neighbors);
							array[i][j] = minLabel;
							updateEquivalentArray(minLabel);
						}
					}
				}
			}
		}
		return newLabel;
	}

	public void loadNeighbors(int i, int j, int connectedness, int[] neighbors, int passId) {
		
		// a b c
		// d x e
		// f g h 
		
		// indexes i and j refer to the location of x,
		// therefore consider neighbors regards this location
		
		if (passId == 1) {
			if (connectedness == 4) {
				
				// consider b and d neighbors 
				
				neighbors[0] = zeroFramedArray[i - 1][j];
				neighbors[1] = zeroFramedArray[i][j - 1];
			}
			
			else if (connectedness == 8) {
				
				// consider a, b, c, and d neighbors
				neighbors[0] = zeroFramedArray[i - 1][j - 1];
				neighbors[1] = zeroFramedArray[i - 1][j];
				neighbors[2] = zeroFramedArray[i - 1][j + 1];
				neighbors[3] = zeroFramedArray[i][j - 1];
			}
		}

		else if (passId == 2) {
			if (connectedness == 4) {
				// consider e and g neighbors as well as x location itself
				neighbors[0] = zeroFramedArray[i][j + 1];
				neighbors[1] = zeroFramedArray[i + 1][j];
				neighbors[2] = zeroFramedArray[i][j];
			} 
			else if (connectedness == 8) {
				
				// consider e, f, g, and h neighbors as well as x location itself
				neighbors[0] = zeroFramedArray[i][j + 1];
				neighbors[1] = zeroFramedArray[i + 1][j - 1];
				neighbors[1] = zeroFramedArray[i + 1][j];
				neighbors[2] = zeroFramedArray[i + 1][j + 1];
				neighbors[3] = zeroFramedArray[i][j];
			}
		}
	}
	
	public void connectPass2(int connectedness, int[][] array, int[] neighbors) {
		
		for (int i = numRows; i > 0; i--) {
			for (int j = numCols; j > 0; j--) {
				if (array[i][j] > 0) {
					loadNeighbors(i, j, connectedness, neighbors, 2);
					if (connectedness == 4) {
						
						// a b c
						// d x e
						// f g h
						
						int c = neighbors[0];
						int d = neighbors[1];
						int x = neighbors[2];
						if (x > 0) {
							if (c == 0 && d == 0) {

							}
							else if ((c == 0 || c == x) && (d == 0 || d == x)) {

							}
							else if ((c != 0 && c != x) || (d != 0 || d != x)) {
								int minLabel = findMinLabel(connectedness, neighbors);
								int maxLabel = findMaxLabel(connectedness, neighbors);
								array[i][j] = minLabel;
								equivalentArray[maxLabel] = minLabel;
								updateEquivalentArray(minLabel);
							}
						}
					}
					else if (connectedness == 8) {
						
						// a b c
						// d x e
						// f g h
						
						int e = neighbors[0];
						int f = neighbors[1];
						int g = neighbors[2];
						int h = neighbors[3];
						int x = neighbors[4];
						
						if (neighbors[4] > 0) {
							if (neighbors[0] == 0 && neighbors[1] == 0 &&
									neighbors[2] == 0) {

							}
							else if ((e == 0 || e == x) && (f == 0 || f == x) &&
								     (g == 0 || g == x) && (h == 0 || h == x)) {

							}
							else if ((e != x && e != 0) || (f != x && f != 0) |
								     (g != x && g != 0) || (h != x && h != 0)) {
								int minLabel = findMinLabel(connectedness, neighbors);
								int maxLabel = findMaxLabel(connectedness, neighbors);								
								array[i][j] = minLabel;
								equivalentArray[maxLabel] = minLabel;
							}
						}
					}
				}
			}
		}
	}
	
	public void connectPass3(int[][] array, int[] neighbors) {
		propertyCC = new Property[trueLabel + 1];
		
		for (int i = 1; i <= trueLabel; i++) {
			propertyCC[i] = new Property(i);
		}
		
		newMin = 1;
		newMax = trueLabel;
		
		for (int i = 1; i < numRows + 1; i++) {
			for (int j = 1; j < numCols + 1; j++) {
				if (array[i][j] > 0) {
					array[i][j] = equivalentArray[array[i][j]];
					propertyCC[array[i][j]].setPixelsNumber((propertyCC[array[i][j]].getPixelsNumber()) + 1);
					if (propertyCC[array[i][j]].getUpperLeftRow() == -1) { 
						propertyCC[array[i][j]].setUpperLeftRow(i);
					}
					else if (propertyCC[array[i][j]].getUpperLeftRow() > i) {
						propertyCC[array[i][j]].setUpperLeftRow(i);
					}
					if (propertyCC[array[i][j]].getUpperLeftCol() == -1) {
						propertyCC[array[i][j]].setUpperLeftCol(j);
					}
					else if (propertyCC[array[i][j]].getUpperLeftCol() > i) {
						propertyCC[array[i][j]].setUpperLeftCol(j);
					}
					if (propertyCC[array[i][j]].getLowerRightRow() == -1) {
						propertyCC[array[i][j]].setLowerRightRow(i);
					}
					else if (propertyCC[array[i][j]].getLowerRightRow() < i) {
						propertyCC[array[i][j]].setLowerRightRow(i);
					}
					if (propertyCC[array[i][j]].getLowerRightCol() == -1) {
						propertyCC[array[i][j]].setLowerRightCol(j);
					}
					else if (propertyCC[array[i][j]].getLowerRightCol() < j) {
						propertyCC[array[i][j]].setLowerRightCol(j);
					}
				}
			}
		}
	}
	
	public void updateEquivalentArray(int min) {
		for (int i = 0; i < 4; i++) {
			if (nonZeroNeighorArray[i] != 0 && nonZeroNeighorArray[i] > min) {
				equivalentArray[nonZeroNeighorArray[i]] = min;
			}
		}
	}
	
	public int findMinLabel(int connectedness, int[] neighbors) {
		int min = 99999;
		
		for(int i = 0; i < neighborsCount; i++) {
			if(neighbors[i] != 0 && min > neighbors[i]) {
				min = neighbors[i];
			}
		}
		
		return min;		
	}
	
	public int findMaxLabel(int connectedness, int[] neighbors) {
		int max = 0;
		
		if(connectedness == 4) {
			max = neighbors[2];
		}
		else if (connectedness == 8) {
			max = neighbors[4];
		}
		
		for(int i = 0; i < neighborsCount; i++) {
			if(neighbors[i] != 0 && max < neighbors[i]) {
				max = neighbors[i];
			}
		}
		
		return max;		
	}
	
	public void manageEquivalentArray(int[] array, int newLabel) {
		int realLabel = 0;
		int index = 1;
		
		while(index <= newLabel) {
			if(index != array[index]) {
				array[index] = array[array[index]];
			}
			else {
				realLabel++;
				array[index] = realLabel;
			}
			index++;
		}
		trueLabel = realLabel;
	}
	
	public void printImage(FileWriter output) {
		try {
			output.write(numRows + " " + numCols + " " + newMin + " " + newMax + "\n");
			for (int i = 1; i < (numRows + 1); i++) {
				for (int j = 1; j < (numCols + 1); j++) {
					
					output.write(zeroFramedArray[i][j] + " ");
					
					if (zeroFramedArray[i][j] < 10) {
						output.write(" ");
					}
				}
				output.write("\n");
			}
			output.write("\n");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printProperty(FileWriter output) {
		try {
			output.write(numRows + " " + numCols + " " + newMin + " " + newMax + "\n");
			output.write(trueLabel + "\n");
			
			for(int i = 1; i <= trueLabel; i++) {
				if(propertyCC[i].getPixelsNumber() > 0) {
					output.write(i + "\n");
					output.write(propertyCC[i].getPixelsNumber() + "\n");
					output.write(propertyCC[i].getUpperLeftRow() +  " " + propertyCC[i].getUpperLeftCol()+ "\n");
					output.write(propertyCC[i].getLowerRightRow() +  " " + propertyCC[i].getLowerRightCol()+ "\n");
					
				}
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void drawBoxes(int[][] array, Property[] property) {
		int index = 1;
		int minRow, minCol, maxRow, maxCol, label;
		
		while(index <= trueLabel) {
			minRow = property[index].getUpperLeftRow();
			minCol = property[index].getUpperLeftCol();
			maxRow = property[index].getLowerRightRow();
			maxCol = property[index].getLowerRightCol();
			label = property[index].getLabel();
			
			for(int i = minCol; i <= maxCol; i++) {
				array[minRow][i] = label;
				array[maxRow][i] = label;
			}
			
			for(int i = minRow; i <= maxRow; i++) {
				array[i][minCol] = label;
				array[i][maxCol] = label;
			}
			index++;			
		}
	}


	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public int getNewMin() {
		return newMin;
	}

	public void setNewMin(int newMin) {
		this.newMin = newMin;
	}

	public int getNewMax() {
		return newMax;
	}

	public void setNewMax(int newMax) {
		this.newMax = newMax;
	}

	public int getNewLabel() {
		return newLabel;
	}

	public void setNewLabel(int newLabel) {
		this.newLabel = newLabel;
	}
	
	public int getTrueLabel() {
		return newLabel;
	}

	public void setTrueLabel(int newLabel) {
		this.newLabel = newLabel;
	}
	
}
