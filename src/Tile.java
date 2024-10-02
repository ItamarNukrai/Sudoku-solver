import java.awt.*;
import java.util.Arrays;

//מחלקה המתארת משבצת סודוקו
public class Tile {

    public static final int TILE_POSSIBILITIES = 9;
    public static final int POSSIBILITIES = 9;

    private final Box parentBox;

    //המיקום ביחס לקופסה
    private final int xInBoxPosTile;
    private final int yInBoxPosTile;

    //שמירת ה - POSSIBILITIES כאשר אנחנו במצב RAND
    int[] savePossibilities;

    private int value;
    private int[] possibleValue;
    private WhoGenerated whoGenerated;

    public Tile(int value, int xInBoxPosTile, int yInBoxPosTile, Box parentBox) {
        this.value = value;

        this.xInBoxPosTile = xInBoxPosTile;
        this.yInBoxPosTile = yInBoxPosTile;

        possibleValue = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};

        this.parentBox = parentBox;
        savePossibilities = new int[9];
    }

    public void returnToBeforeRandom() {
        if (whoGenerated == WhoGenerated.RAND) {
            value = 0;
            decreaseNumberOfSolved();
            whoGenerated = null;

        }
        setRightPossibilities();
        paintTile();
    }

    public boolean isPossibilitiesEmpty() {
        //פעולה שעוברת על כל ה - POSSIBILITY ומחזירה האם כולם שווים ל0
        return (int) Arrays.stream(possibleValue).filter(possibility -> possibility == 0).count() == 9;
    }

    public void delPossibility(int delValue) {
        //הפעולה מוחקת מספר אחד מהאופציות
        //DELVALUE הוא המספר עצמו ולא האינדק
        if (delValue == 0 || this.possibleValue[delValue - 1] == 0)
            return;

        this.possibleValue[delValue - 1] = 0;
        Sudoku parentSudoku = parentBox.getParentSudoku();
        parentSudoku.updateSudoku();

        paintTile();
    }

    public void del8Possibilities(int valueNotDel) {
        // פעולה שמוחקת את כל האפשריות חוץ מאחת

        for (int i = 0; i < TILE_POSSIBILITIES; i++) {

            if (this.possibleValue[i] != valueNotDel)
                delPossibility(this.possibleValue[i]);
        }
    }

    public void delAllPossibilities() {
// פעולה שמקבלת משבצת ומנקה את מערך האפשרויות כך שהכל יהיה  - 0
        Arrays.fill(this.possibleValue, 0);
        Sudoku sudoku = parentBox.getParentSudoku();
        sudoku.updateSudoku();
    }

    public boolean isTilePossibility(int value) {
        // פעולה שפועלת על משבצת, הפעולה מקבלת ערך ובודקת האם הערך אפשרי
        // ערך true אומר שיש אפשרות לערך value במשבצת
        return this.possibleValue[value - 1] != 0;
    }

    public boolean isTileSolved() {
        // פעולה שמחזירה האם המשבצת כבר פתורה ולכן לא צריך לעבור עליה שוב
        // שווה 0 - לא פתורה
        //שונה מ - 0 פתורה
        return this.value != 0;
    }

    public boolean isTileHaveOnePossibilityAndReadyToSolve() {
        //הפעולה בודקת האם הערך שצריך להיות ידוע כבר, כלומר במערך של ה - POSSIBILITY יש רק ערך אחד השונה מ - 0
        return getNeededValueToInsert() != 0;
    }

    public int getNeededValueToInsert() {
        // הפעולה מחזירה את הערך שבוודאות צריך להיות בו ב - value כלומר כאשר יש ב possibleValue
        //  רק מספר אחד ששונה מ 0
        //הפעולה תחזיר את המסר הזה
        //במידה ואין מספר כזה הפעולה תחזיר 0
        if (isTileSolved())
            return 0;

        int count = 0;
        int rightValue = 0;

        for (int j : this.possibleValue)
            if (j != 0)
                count++;

        if (count == 1) {
            for (int j : this.possibleValue)
                if (j != 0)
                    rightValue = j;
        }
        return rightValue;
    }

    public void setRightValue(int value, WhoGenerated whoGenerated) {
        //פעולה ערך ומכניסה את הערך ל - VALUE של TILE ומרוקנת את ה - POSSIBILITIES
        this.value = value;
        this.whoGenerated = whoGenerated;

        delAllPossibilities();

        //החלק הזה מעדכן את השורות ואת ה - BOX כדי להימנע מבאג
        Sudoku sudoku = parentBox.getParentSudoku();
        parentBox.delPossibilityInBox(value);
        sudoku.delPossibilityInAllTilesInX(this);
        sudoku.delPossibilityInAllTilesInY(this);


        increaseNumberOfSolved();

        paintTile();
    }

    public void setRandValue(int value) {
        Sudoku parentSudoku = parentBox.getParentSudoku();
        parentSudoku.saveAllTilesPossibilities();

        setRightValue(value, WhoGenerated.RAND);
    }

    //אלו 3 הפעולות רמה 1
/////////////////////////////////////////////////////////////////////////////
    public void delBasicPossibilityX1(int possibility) {
        //הפעולה הזאת תופעל רק על tile שה - value שלו שווה ל0
        //הפעולה  מקבלת מיקום של tile ולפי הX שלה מוחקת את ה - POSSIBILITY
        if (isTileSolved())
            return;
//        Box[] boxes = parentBox.getBoxesInX();
//
//        for (Box box : boxes) {
//
//            if (box.isValueInX(yBoxPosTile, possibility))
//                delPossibility(possibility);
//        }
        Sudoku sudoku = parentBox.getParentSudoku();

        if (sudoku.isValueInX(parentBox, xInBoxPosTile, possibility))
            delPossibility(possibility);
    }

    public void delBasicPossibilityY1(int possibility) {
        //הפעולה הזאת תופעל רק על tile שה - value שלו שווה ל0
        //הפעולה  מקבלת מיקום של tile ולפי הטור שלה מוחקת את ה - POSSIBILITY
        if (isTileSolved())
            return;
//        Box[] boxes = parentBox.getBoxesInX();
//
//        for (Box box : boxes) {
//
//            if (box.isValueInX(yBoxPosTile, possibility))
//                delPossibility(possibility);
//        }
        Sudoku sudoku = parentBox.getParentSudoku();

        if (sudoku.isValueInY(parentBox, yInBoxPosTile, possibility))
            delPossibility(possibility);
    }

    public void delBasicPossibilityInBox1(int possibility) {
        // הפעולה מוחקת חלק מהאפשרויות של ה - BOX  שה - tile ממוקם בו
        //הפעולה עוברת על כל המספרים האפשריים ומוחקת אפשרויות
        if (isTileSolved())
            return;

        if (parentBox.isValueInBox(possibility))
            delPossibility(possibility);
    }
/////////////////////////////////////////////////////////////////////////////


    //אלו הפעולות רמה 2
/////////////////////////////////////////////////////////////////////////////////////////
    public void setValueIfItHasOnePlaceInBox2(int possibility) {
// פעולה שמקבלת ערך ובודקת לגבי ערך האם יש רק מקום אחד שמתאים לו ב - BOX שלו,
// אם אכן יש משימה את הערך
        // הגדרת מקום מתאים הוא מקום שבכל 8 המשבצות האחרות בריבוע
        // ה - POSSIBILITY של המספר לא קיים לכן יש רק מקום אחד שנשאר
//        int count = 0;
//        // int[] recNum = getRectNumForTile(xPosTile, yPosTile);
////        int xPosBox = parentBox.getXPosBox();
////        int yPosBox = parentBox.getYPosBox();
//
//        for (int x = 0; x < Box.SUDOKU_UNITS_IN_X; x++) {
//
//            for (int y = 0; y < Box.SUDOKU_UNITS_IN_BOX_IN_Y; y++) {
//                Tile tile = parentBox.getTiles()[x][y];
//
//                if (tile.isTilePossibility(possibility))
//                    count++;
//            }
//        }
//        // אם יש רק ערך אחד פנוי וזה במקרה במיקום של ה - TILE שהבאתי
//        if (count == 1 && isTilePossibility(possibility))
//            setRightValue(possibility, WhoGenerated.COMPUTER);
//
//        else if (count > 8)
//            throw new RuntimeException();
        if (isTileHaveOnePossibilityAndReadyToSolve())
            return;
        if (parentBox.isPossibilityHasOnePlaceInBoxAndItIsParameterPlace2(this, possibility))
            del8Possibilities(possibility);
    }

    public void delPossibilityIfSudokuXMustHaveValue2(int possibility) {
        // פעולה שמקבלת מספר
        // ובודקת האם ב - X  שלה ב - BOX כלשהו חייב להיות מספר, אם כן היא מוחקת את ה - POSSIBILITY במשבצת של ה - TILE הנוכחי
        //אם חייב להיות איזה שהוא ערך (שעדיין לא נקבע מיקום ספציפי) אז תמחק את האפשרות...
        if (isTileHaveOnePossibilityAndReadyToSolve())
            return;

        Sudoku sudoku = parentBox.getParentSudoku();

        if (sudoku.isXInDifferentBoxesMustHaveValue2(this, possibility))
            delPossibility(possibility);
    }

    public void delPossibilityIfSudokuYMustHaveValue2(int possibility) {
        // פעולה שמקבלת מספר
        // ובודקת האם ב - Y שלה ב - BOX אחר חייב להיות מספר, אם כן היא מוחקת את ה - POSSIBILITY במשבצת של ה - TILE הנוכחי
        //אם חייב להיות איזה שהוא ערך (שעדיין לא נקבע מיקום ספציפי) אז תמחק את האפשרות...

        if (isTileHaveOnePossibilityAndReadyToSolve())
            return;

        Sudoku sudoku = parentBox.getParentSudoku();

        if (sudoku.isYInDifferentBoxesMustHaveValue2(this, possibility))
            delPossibility(possibility);

    }
////////////////////////////////////////////////////////////////////////////////////////

    public void updateTile() {
        // הפעולה הזאת מעדכנת את הTILE כלומר בודקת האם ניתן
        // למחוק אפשרויות והאם ניתן להכניס ערך ל - VALUE
        // הפעולה נקראת רק כאשר המשבצת לא פתורה
        Sudoku parentSudoku = parentBox.getParentSudoku();
        generalDel();

        int value = getNeededValueToInsert();
        WhoGenerated whoGenerated;

        //רק אם יש ערך אמיתי תעדכן
        if (value != 0) {
            if (parentSudoku.isInRandMode())
                whoGenerated = WhoGenerated.RAND;

            else whoGenerated = WhoGenerated.COMPUTER;

            setRightValue(value, whoGenerated);
        }

    }

    public void generalDel() {
        // פעולה שמזמנת כל כל תתי הפעולות כדי למחוק כל אפשרות
        delLevel1();
        delLevel2();
        delLevel3();
    }

    public void delLevel1() {
//  אלו פעולות שנקראות גם על משבצות שנפתרו חשובבב!!!!

        for (int possibility = 1; possibility <= POSSIBILITIES; possibility++) {

            delBasicPossibilityX1(possibility);
            delBasicPossibilityY1(possibility);
            delBasicPossibilityInBox1(possibility);
        }
    }

    public void delLevel2() {
        if (isTileHaveOnePossibilityAndReadyToSolve())
            return;
        for (int possibility = 1; possibility <= POSSIBILITIES; possibility++) {
            setValueIfItHasOnePlaceInBox2(possibility);
            delPossibilityIfSudokuXMustHaveValue2(possibility);
            delPossibilityIfSudokuYMustHaveValue2(possibility);
        }
    }

    public void delLevel3() {
        if (isTileHaveOnePossibilityAndReadyToSolve())
            return;

        for (int possibility = 1; possibility <= POSSIBILITIES; possibility++) {
            parentBox.delPossibilityIfOnlyThisXInBoxMustHaveValue3(xInBoxPosTile, possibility);
            parentBox.delPossibilityIfOnlyThisYInBoxMustHaveValue3(yInBoxPosTile, possibility);
        }

    }

//    public void delLevel4() {
//        if (isTileHaveOnePossibilityAndReadyToSolve())
//            return;
//
//        for (int possibility = 1; possibility <= POSSIBILITIES; possibility++) {
//
//
//
//        }
//    }
//
//    public void delHumanVal() {
//        returnPossibility(value);
//
//        value = 0;
//        whoGenerated = null;
//    }

//    public void returnPossibility(int possibility) {
//        possibleValue[possibility - 1] = possibility;
//    }

    public void savePossibilities() {
        this.savePossibilities = this.possibleValue.clone();
    }

    public void setRightPossibilities() {
        possibleValue = savePossibilities.clone();
    }

    private void increaseNumberOfSolved() {
        parentBox.increaseSolvedCount();
    }

    private void decreaseNumberOfSolved() {
        parentBox.decreaseSolvedCount();
    }

    public void setHumanValue(int value) {
        if (isTileSolved())
            throw new RuntimeException("you cant not place 2 values in one tile!");

        setRightValue(value, WhoGenerated.HUMAN);
    }

    public int getSudokuCoordinateX() {
        int boxNumber = parentBox.getXPosBox();
        int x = App.calc(boxNumber, xInBoxPosTile);

        if (x == 9)
            x = App.calc(boxNumber, xInBoxPosTile);

        return x;
    }

    public int getSudokuCoordinateY() {
        int boxNumber = parentBox.getYPosBox();

        int y = App.calc(boxNumber, yInBoxPosTile);

        return y;
    }

    private void paintTile() {
        MyJPanel myJPanel = parentBox.getParentSudoku().getMyJPanel();
        myJPanel.paintImmediately(0, 0, MyJPanel.SCREEN_WIDTH, MyJPanel.SCREEN_HEIGHT);
    }

    public void drawValue(int value, Graphics g) {
        int sudokuXCoordinate = getSudokuCoordinateX();
        int sudokuYCoordinate = getSudokuCoordinateY();

        if (getWhoGenerated() == WhoGenerated.HUMAN) {
            g.setFont(new Font("Ink Free", Font.BOLD, 60));
            g.setColor(Color.BLACK);

            g.drawString(String.valueOf(value), sudokuXCoordinate * MyJPanel.UNIT_SIZE + 50, sudokuYCoordinate * MyJPanel.UNIT_SIZE + 70);
        } else if (getWhoGenerated() == WhoGenerated.COMPUTER) {
            g.setFont(new Font("Ink Free", Font.BOLD, 60));
            g.setColor(Color.RED);
            g.drawString(String.valueOf(value), sudokuXCoordinate * MyJPanel.UNIT_SIZE + 50, sudokuYCoordinate * MyJPanel.UNIT_SIZE + 70);
        } else if (getWhoGenerated() == WhoGenerated.RAND) {
            g.setFont(new Font("Ink Free", Font.BOLD, 60));
            g.setColor(Color.GREEN);

            g.drawString(String.valueOf(value), sudokuXCoordinate * MyJPanel.UNIT_SIZE + 50, sudokuYCoordinate * MyJPanel.UNIT_SIZE + 70);
        }
    }

    public void drawPossibility(int num, Graphics g) {
        int sudokuXCoordinate = getSudokuCoordinateX();
        int sudokuYCoordinate = getSudokuCoordinateY();


        int plusX = ((num - 1) % 3) * (MyJPanel.UNIT_SIZE / 3);
        int plusY = ((num - 1) / 3) * (MyJPanel.UNIT_SIZE / 3);

        int xCord = sudokuXCoordinate * MyJPanel.UNIT_SIZE + plusX + 15;///
        int yCord = sudokuYCoordinate * MyJPanel.UNIT_SIZE + plusY + 30; ///

        g.drawString(String.valueOf(num), xCord, yCord);
    }

    public void drawPossibilities(Graphics g) {
        // הפעולה נקראת רק כאשר המשבצת לא פתורה
        g.setFont(new Font("Ink Free", Font.BOLD, 34));
        g.setColor(Color.BLACK);

        for (int i = 1; i < Sudoku.SUDOKU_UNITS_IN_BOX + 1; i++) {
            if (isTilePossibility(i))
                drawPossibility(i, g);
        }
    }

    public void drawTile(Graphics g) {
        //הפעולה עושה את הריבוע הירוק שמסמל איפה העכבר כעת

        int sudokuXCoordinate = getSudokuCoordinateX();
        int sudokuYCoordinate = getSudokuCoordinateY();

        g.setColor(new Color(89, 195, 89));
        g.fillRect(sudokuXCoordinate * MyJPanel.UNIT_SIZE, sudokuYCoordinate * MyJPanel.UNIT_SIZE, MyJPanel.UNIT_SIZE, MyJPanel.UNIT_SIZE);
    }

    public Tile getUpdateCurrentTile(Direction direction) {
        Tile retTile = this;
        Tile[][] tiles = parentBox.getParentSudoku().getAllTiles();

        int sudokuXCoordinate = getSudokuCoordinateX();
        int sudokuYCoordinate = getSudokuCoordinateY();

        switch (direction) {

            case UP:
                retTile = tiles[sudokuXCoordinate][sudokuYCoordinate - 1];
                break;
            case DOWN:
                retTile = tiles[sudokuXCoordinate][sudokuYCoordinate + 1];
                break;
            case LEFT:
                retTile = tiles[sudokuXCoordinate - 1][sudokuYCoordinate];
                break;
            case RIGHT:
                retTile = tiles[sudokuXCoordinate + 1][sudokuYCoordinate];
                break;
        }
        return retTile;
    }

    public int getRandomPossibility() {
        // הפעולה מחזירה אפשרות ברנדומליות כל עוד היא במערך האפשרויות של ה - TILE
        int max = 9;
        int min = 1;
        int possibility;

        do {
            possibility = Sudoku.getRandomNum(min, max);
        }
        while (!isTilePossibility(possibility));

        return possibility;
    }

    public int getXInBoxPosTile() {
        return xInBoxPosTile;
    }

    public int getYInBoxPosTile() {
        return yInBoxPosTile;
    }

    public int getValue() {
        return value;
    }

    public WhoGenerated getWhoGenerated() {
        return whoGenerated;
    }

    public Box getParentBox() {
        //הפעולה מחזירה את ה - BOX שהוא נמצא בו

//        int[] retPlace = new int[2];
//        retPlace[0] = parentBox.getXPosBox();
//        retPlace[1] = parentBox.getYPosBox();
//
//        return retPlace;
        return parentBox;
    }

    @Override
    public String toString() {
        return "Tile{" + "xInBoxPosTile=" + xInBoxPosTile + ", yInBoxPosTile=" + yInBoxPosTile + '}';
    }

}