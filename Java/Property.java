
public class Property {
	
	private int label;
	private int pixelsNumber;
	
	/*	Representing rectangular box in Cartesian Coordinate system:
	 * 	tow points define the borders of the box: upper left corner and
	 * 	lower right corner .
	 * 	Two points represent the smallest rectangular box that the 
	 * 	connected component can fit inside the box
	 * */
	
	private int upperLeftRow;
	private int upperLeftCol;
	private int lowerRightRow;
	private int lowerRightCol;
	
	public Property() {

		this.label = -1;
		this.pixelsNumber = -1;
		this.upperLeftRow = -1;
		this.upperLeftCol = -1;
		this.lowerRightRow = -1;
		this.lowerRightCol = -1;
	}
	
		
	public Property(int label) {

		this.label = label;
		this.pixelsNumber = 0;
		this.upperLeftRow = -1;
		this.upperLeftCol = -1;
		this.lowerRightRow = -1;
		this.lowerRightCol = -1;
	}
	
	public int getLabel() {
		return label;
	}
	public void setLabel(int label) {
		this.label = label;
	}
	public int getPixelsNumber() {
		return pixelsNumber;
	}
	public void setPixelsNumber(int pixelsNumber) {
		this.pixelsNumber = pixelsNumber;
	}
	public int getUpperLeftRow() {
		return upperLeftRow;
	}
	public void setUpperLeftRow(int upperLeftRow) {
		this.upperLeftRow = upperLeftRow;
	}
	public int getUpperLeftCol() {
		return upperLeftCol;
	}
	public void setUpperLeftCol(int upperLeftCol) {
		this.upperLeftCol = upperLeftCol;
	}
	public int getLowerRightRow() {
		return lowerRightRow;
	}
	public void setLowerRightRow(int lowerRightRow) {
		this.lowerRightRow = lowerRightRow;
	}
	public int getLowerRightCol() {
		return lowerRightCol;
	}
	public void setLowerRightCol(int lowerRightCol) {
		this.lowerRightCol = lowerRightCol;
	}

}
