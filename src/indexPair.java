public class indexPair {


    int rowIndex;
    int columnIndex;

    public indexPair(int row, int column) {
        this.rowIndex = row;
        this.columnIndex = column;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void putRowIndex(int row) { this.rowIndex = row; }

    public void putColumnIndex(int column) { this.columnIndex = column; }

    public indexPair adjacent (int direction) {
        // System.out.println(" --------- method adjacent ------------");
        indexPair newIndexPair = new indexPair (this.getRowIndex(),this.getColumnIndex());
        switch (direction) {
            case 0:
                newIndexPair.putRowIndex(this.getRowIndex()-1);
                break;
            case 1:
                newIndexPair.putColumnIndex(this.getColumnIndex()+1);
                break;
            case 2:
                newIndexPair.putRowIndex(this.getRowIndex()+1);
                break;
            case 3:
                newIndexPair.putColumnIndex(this.getColumnIndex()-1);
                break;
            default:
                System.out.println("! UNEXPECTED direction VALUE in indexPair.adjacent = " + direction);
                break;
        }
        // System.out.println(" --------- EXIT method adjacent ------------");
        return newIndexPair;
    }



}
