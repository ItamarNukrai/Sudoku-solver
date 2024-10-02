import java.util.Objects;

public class Box {//המחלקה הזאת היא הריבוע שמכיל 9 TILES

    public final static int SUDOKU_UNITS_IN_BOX = 9;
    public final static int SUDOKU_UNITS_IN_BOX_IN_X = 3;
    public final static int SUDOKU_UNITS_IN_BOX_IN_Y = 3;


    private final Tile[][] tiles;
    //המיקום של הקופסה
    private final int xPosBox;
    private final int yPosBox;
    private  final Sudoku parentSudoku;
    private int solvedCount;

    public Box(int xPosBox, int yPosBox, Sudoku parentSudoku) {
        this.xPosBox = xPosBox;
        this.yPosBox = yPosBox;
        this.parentSudoku = parentSudoku;
        this.solvedCount = 0;

        tiles = new Tile[SUDOKU_UNITS_IN_BOX_IN_X][SUDOKU_UNITS_IN_BOX_IN_Y];

        generateTiles();
    }


    private void updateTiles() {
        // הפעולה הזאת מעדכנת את כל הTILES בBOX
        for (int x = 0; x < Box.SUDOKU_UNITS_IN_BOX_IN_X; x++) {

            for (int y = 0; y < Box.SUDOKU_UNITS_IN_BOX_IN_Y; y++) {

                Tile tile = tiles[x][y];
                // isBug();

                if (!tile.isTileSolved())
                    tile.updateTile();
            }
        }
    }

    public void increaseSolvedCount() {
        solvedCount++;
        Sudoku sudoku = getParentSudoku();
        sudoku.increaseTotalNumberOfSolved();
    }


    public void decreaseSolvedCount() {
        solvedCount--;
        Sudoku sudoku = getParentSudoku();
        sudoku.decreaseTotalNumberOfSolved();
    }


    public void generateTiles() {
        // >>>>>
        // >>>>
        for (int x = 0; x < SUDOKU_UNITS_IN_BOX_IN_X; x++) {
            for (int y = 0; y < SUDOKU_UNITS_IN_BOX_IN_Y; y++) {
                tiles[x][y] = new Tile(0, x, y, this);
            }
        }
    }

    public boolean isSolved() {
        return this.solvedCount == SUDOKU_UNITS_IN_BOX;
    }

    ////////////////////////
    public boolean isValueInBox(int value) {
//פעולה שבודקת האם קיים ערך כבר בBOX עם הערך VALUE
        for (int x = 0; x < SUDOKU_UNITS_IN_BOX_IN_X; x++) {

            for (int y = 0; y < SUDOKU_UNITS_IN_BOX_IN_Y; y++) {
                Tile tile = tiles[x][y];
                if (tile.getValue() == value)
                    return true;
            }
        }
        return false;
    }

    public boolean isValueInX(int x, int value) {
//פעולה שמקבלת Y וערך ובודקת האם קיים בX את הערך
        for (int y = 0; y < Box.SUDOKU_UNITS_IN_BOX_IN_Y; y++)
            if (this.getTiles()[x][y].getValue() == value)
                return true;

        return false;
    }

    public boolean isValueInY(int y, int value) {
//פעולה שמקבלת X וערך ובודקת האם קיים בY את הערך
        for (int x = 0; x < Box.SUDOKU_UNITS_IN_BOX_IN_X; x++)
            if (this.getTiles()[x][y].getValue() == value)
                return true;

        return false;
    }
    ////////////////////////////////

    public boolean isPossibilityHasOnePlaceInBoxAndItIsParameterPlace2(Tile childTile, int possibility) {
        int count = 0;

        for (int x = 0; x < Box.SUDOKU_UNITS_IN_BOX_IN_X; x++) {

            for (int y = 0; y < Box.SUDOKU_UNITS_IN_BOX_IN_Y; y++) {
                Tile tile = tiles[x][y];

                if (tile.isTilePossibility(possibility))
                    count++;
            }
        }
        // אם יש רק ערך אחד פנוי וזה במקרה במיקום של הTILE שהבאתי
        return count == 1 && childTile.isTilePossibility(possibility);
    }


    public boolean isPossibleInX(int x, int possibility) {
        //פעולה שמקבלת  Y ו BOX (כלומר 3 ערכים שצריך לבדוק) ומחזירה האם יכול להיות הערך

        for (int y1 = 0; y1 < SUDOKU_UNITS_IN_BOX_IN_X; y1++)
            if (this.tiles[x][y1].isTilePossibility(possibility))
                return true;

        return false;
    }
////////////////////////////////////

    public boolean isPossibleInY(int y, int possibility) {
        //פעולה שמקבלת  X ו BOX (כלומר 3 ערכים שצריך לבדוק) ומחזירה האם יכול להיות הערך


        for (int x1 = 0; x1 < SUDOKU_UNITS_IN_BOX_IN_X; x1++)
            if (this.tiles[x1][y].isTilePossibility(possibility))
                return true;

        return false;
    }

//    public void delPossibilitiesInBox() {
//הפעולה עוברת על כל הTILES שקיימים בBOX ועל כל אחד מוחקת אפשרויות

//        for (int x = 0; x < SUDOKU_UNITS_IN_BOX_IN_X; x++) {
//
//            for (int y = 0; y < SUDOKU_UNITS_IN_BOX_IN_Y; y++)
//                tiles[x][y].delBasicPossibilitiesInBox();
//        }
//
//
//    public void delPossibilityIfParentBoxIsFullOfValue1() {
//        הפעולה עוברת על כל הTILES בBOX ולכל אחד מהם מפעילה את הפעולה שמעדכנת את הPOSSIBILITIES
//
//        for (int x = 0; x < Box.SUDOKU_UNITS_IN_BOX_IN_X; x++) {
//
//            for (int y = 0; y < Box.SUDOKU_UNITS_IN_BOX_IN_Y; y++) {
//                Tile tile = tiles[x][y];
//                tile.delPossibilityIfParentBoxIsFullOfValues();
//            }
//        }
//
//    }

    public boolean isOnlyPossibilityPlaceIsThisX(int x, int possibility) {
        // פעולה שמקבלת מספר, כמו כן מקבלת ערך POSSIBILITY
        // ובודקת האם הX שהתקבל כפרמטר הוא הטור היחיד שבו הערך יכול להימצא
        //..."אם חייב להיות איזה שהוא ערך"
        //פעולה שעוברת על 3 המספרים בX ב - BOX ובודקת האם באחד מהם ורק בהם יכול להיות הערך NUM

        int countPossibilityInX = 0;
        int countPossibilitiesInBox = 0;

        for (int y1 = 0; y1 < SUDOKU_UNITS_IN_BOX_IN_X; y1++) {
            Tile tile = tiles[x][y1];
            if (tile.isTilePossibility(possibility))
                countPossibilityInX++;
        }

        for (int x1 = 0; x1 < SUDOKU_UNITS_IN_BOX_IN_X; x1++) {

            for (int y1 = 0; y1 < SUDOKU_UNITS_IN_BOX_IN_Y; y1++) {
                Tile tile = tiles[x1][y1];
                if (tile.isTilePossibility(possibility))
                    countPossibilitiesInBox++;
            }
        }

        return countPossibilityInX == countPossibilitiesInBox && countPossibilityInX != 0;
    }

    public boolean isOnlyPossibilityPlaceIsThisY(int y, int possibility) {
        // פעולה שמקבלת מספר, כמו כן מקבלת ערך POSSIBILITY
        // ובודקת האם השורה שהתקבלה כפרמטר היא השורה היחידה שבה הערך יכול להימצא
        //..."אם חייב להיות איזה שהוא ערך"
        //פעולה שעוברת על 3 המספרים בY ובריבוע ובודקת האם באחד מהם יכול להיות הערך NUM

        int countPossibilityInY = 0;
        int countPossibilitiesInRec = 0;

        for (int x1 = 0; x1 < SUDOKU_UNITS_IN_BOX_IN_X; x1++) {
            Tile tile = tiles[x1][y];
            if (tile.isTilePossibility(possibility))
                countPossibilityInY++;
        }

        for (int x1 = 0; x1 < SUDOKU_UNITS_IN_BOX_IN_X; x1++) {

            for (int y1 = 0; y1 < SUDOKU_UNITS_IN_BOX_IN_Y; y1++) {
                Tile tile = tiles[x1][y1];
                if (tile.isTilePossibility(possibility))
                    countPossibilitiesInRec++;
            }
        }

        return countPossibilityInY == countPossibilitiesInRec && countPossibilityInY != 0;
    }


    public void delPossibilityIfOnlyThisXInBoxMustHaveValue3(int x, int possibility) {

        if (parentSudoku.isOnlyThisXInBoxMustHaveValue3(x, this, possibility))
            for (int xToRemoveFrom = 0; xToRemoveFrom < SUDOKU_UNITS_IN_BOX_IN_X; xToRemoveFrom++) {
                if (xToRemoveFrom != x)
                    delPossibilityInX(xToRemoveFrom, possibility);
            }


    }

    public void delPossibilityIfOnlyThisYInBoxMustHaveValue3(int y, int possibility) {

        if (parentSudoku.isOnlyThisYInBoxMustHaveValue3(y, this, possibility)) {
            for (int yToRemoveFrom = 0; yToRemoveFrom < SUDOKU_UNITS_IN_BOX_IN_Y; yToRemoveFrom++) {
                if (yToRemoveFrom != y)
                    delPossibilityInY(yToRemoveFrom, possibility);
            }
        }

    }


    public Box[] getBoxesInX() {
        //הפעולה מחזירה את הBOXes  באותו הטור

        Sudoku sudoku = getParentSudoku();
        Box[] retBox = new Box[3];

        retBox[0] = sudoku.getBoxes()[xPosBox][0];
        retBox[1] = sudoku.getBoxes()[xPosBox][1];
        retBox[2] = sudoku.getBoxes()[xPosBox][2];

        return retBox;
    }

    public Box[] getBoxesInY() {
        //הפעולה מחזירה את הBOX האחרים באותו שורה

        Sudoku sudoku = getParentSudoku();
        Box[] retBox = new Box[3];

        retBox[0] = sudoku.getBoxes()[0][yPosBox];
        retBox[1] = sudoku.getBoxes()[1][yPosBox];
        retBox[2] = sudoku.getBoxes()[2][yPosBox];

        return retBox;
    }

    public void updateBox() {
        updateTiles();
    }

    public void returnToBeforeRandom() {

        for (int x = 0; x < Box.SUDOKU_UNITS_IN_BOX_IN_X; x++) {

            for (int y = 0; y < Box.SUDOKU_UNITS_IN_BOX_IN_Y; y++) {
                Tile tile = tiles[x][y];

//                if (tile.getWhoGenerated() == WhoGenerated.RAND)
                tile.returnToBeforeRandom();
            }
        }

    }

//    public void returnPossibility(int possibility) {
//
//        for (int x = 0; x < Box.SUDOKU_UNITS_IN_BOX_IN_X; x++) {
//
//            for (int y = 0; y < Box.SUDOKU_UNITS_IN_BOX_IN_Y; y++) {
//                Tile tile = tiles[x][y];
//                tile.returnPossibility(possibility);
//            }
//        }
//    }

    public void delPossibilityInBox(int possibility) {
        //פעולה הנקראת כאשר מעדכנים TILE באותו הBOX מטרת הפעולה היא למנוע מצב של באג כאשר הסמנו ערך לTILE מסויים אבל לא מחקנו את האפשרויות לTILES בשאר הBOX ולכן פעולות אחרות יושפעו מזה לרעה

        for (int x = 0; x < Box.SUDOKU_UNITS_IN_BOX_IN_X; x++) {

            for (int y = 0; y < Box.SUDOKU_UNITS_IN_BOX_IN_Y; y++) {

                Tile tile = tiles[x][y];
                tile.delBasicPossibilityInBox1(possibility);
            }
        }
    }

    public void delPossibilityInX(int x, int possibility) {
        //הפעולה מוחקת את הPOSSIBILITY מכל הTILES הנמצאים בX המתקבל כפרמטר
        for (int y = 0; y < SUDOKU_UNITS_IN_BOX_IN_Y; y++) {
            Tile tile = tiles[x][y];
            tile.delPossibility(possibility);
        }
    }

    public void delPossibilityInY(int y, int possibility) {
        //הפעולה מוחקת את הPOSSIBILITY מכל הTILES הנמצאים בY המתקבל כפרמטר

        for (int x = 0; x < SUDOKU_UNITS_IN_BOX_IN_X; x++) {
            Tile tile = tiles[x][y];
            tile.delPossibility(possibility);
        }
    }

    public int getXPosBox() {
        return xPosBox;
    }

    public int getYPosBox() {
        return yPosBox;
    }


    public Tile[][] getTiles() {
        return tiles;
    }


    public Sudoku getParentSudoku() {
        return this.parentSudoku;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Box box = (Box) o;
        return xPosBox == box.xPosBox && yPosBox == box.yPosBox && Objects.deepEquals(tiles, box.tiles) && Objects.equals(parentSudoku, box.parentSudoku);
    }

    @Override
    public String toString() {
        return "Box{" + "xPosBox=" + xPosBox + ", yPosBox=" + yPosBox + '}';
    }
}
