import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Spreadsheets {

	Scanner input = new Scanner(System.in);
	private int row, col;
	private SheetCell[][] cells;
	private LinkedList<SheetCell> formulaList;
	private static Scanner input2;
	
	private String Interpreter(String value) {
		if (value.charAt(0) == '=') {
			String newValue = "";
			Pattern pattern = Pattern.compile("([=\\+\\-*])([A-F])([\\d+])");
			Matcher matcher = pattern.matcher(value);
			while (matcher.find()) {
				int row = Integer.parseInt(matcher.group(3));
				int col = matcher.group(2).charAt(0) - 'A';
				SheetCell cell = getCells(row, col);
				if (cell == null) return null;
				if (cell.getFormula() != null && cell.getValue() == null) return null;
				if (matcher.group(1) != "=") {
					newValue += matcher.group(1);
				}
				newValue += cell.getValue();
			}
			return "" + new Infix().infix(newValue);
		}
		return null;
	}
	
	public Spreadsheets(int col, int row) {
		// TODO Auto-generated constructor stub
		cells = new SheetCell[row][col];
		formulaList = new LinkedList<SheetCell>();
		for (int r = 0; r < cells.length; ++r) {
			for (int c = 0; c < cells[r].length; ++c) {
				String value = input.next().toUpperCase();
				if (value.charAt(0) == '=') {
					String interprete = Interpreter(value);
					cells[r][c] = new SheetCell(r, c, interprete, value);
					if (interprete == null) {
						formulaList.add(cells[r][c]);
					}
				}
				else {
					cells[r][c] = new SheetCell(r, c, value, null);			
				}
				
			}
			
		}
		// Interprets all
		while (!formulaList.isEmpty()) {
			String value = Interpreter(formulaList.peek().getFormula());
			if (value != null) {
				formulaList.poll().setValue(value);
			}
			else {
				formulaList.offerLast(formulaList.pollFirst());
			}
		}
		
	}
	
	public void printSpreadsheets() {
		for (SheetCell[] r : cells) {
			for (SheetCell c : r) {
				System.out.print(c.value + " ");
				
			}
			System.out.println();
			
		}
		
	}
	
	public static void main(String[] args) {
		input2 = new Scanner(System.in);
		int col = input2.nextInt();
		int row = input2.nextInt();
		Spreadsheets spreadsheets = new Spreadsheets(col, row);
		spreadsheets.printSpreadsheets();
		
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public SheetCell getCells(int row, int col) {
		if (row > getRow() || col > getCol()) throw new RuntimeException("Out of range");
		return cells[row][col];
	}
	
}
