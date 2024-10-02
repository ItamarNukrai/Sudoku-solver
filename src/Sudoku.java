import java.awt.*;

public class Sudoku {//מחלקת הלוח

    public final static int SUDOKU_BOXES_IN_X = 3;
    public final static int SUDOKU_BOXES_IN_Y = 3;

    public final static int SUDOKU_UNITS_IN_X = 9;
    public final static int SUDOKU_UNITS_IN_Y = 9;
    public final static int SUDOKU_UNITS_IN_BOX = 9;

    public final static int SUDOKU_BOXES = SUDOKU_BOXES_IN_X * SUDOKU_BOXES_IN_Y;
    public final static int TOTAL_UNITS = SUDOKU_BOXES * Box.SUDOKU_UNITS_IN_BOX;

    private final MyJPanel myJPanel;

    private final Tile[][] allTiles = new Tile[9][9];
    private int totalSolvedCount;
    private final Box[][] boxes = new Box[SUDOKU_BOXES_IN_X][SUDOKU_BOXES_IN_Y];
    private Tile currentTile;

    //בודקת אחרי כל מעבר על כל הסודוקו על כל הפעולות מחיקה (LEVEL1 LEVEL2 LEVEL3) האם משהו התעדכן
    private boolean isUpdated;

    //האם אנחנו באלגוריתם של הRAND
    private boolean isInRandMode;


    public Sudoku(MyJPanel myJPanel) {
        totalSolvedCount = 0;
        this.myJPanel = myJPanel;

        generateBoxes();
        connectBoxesTilesToArrayTiles();

        this.currentTile = allTiles[5][5];

        isInRandMode = false;
    }


    public static int getRandomNum(int min, int max) {
        //כולל MIN AND MAX
        int range = max - min + 1;

        return (int) (Math.random() * range) + min;
    }

    private Tile getRandUnsolvedTile() {
        //הפעולה מחזירה TILE רנדומלי שלא פתור
        if (isSudokuSolve())
            throw new RuntimeException("sudoku is solved");

        Tile tile;
        do {
            int x = getRandomNum(0, 8);
            int y = getRandomNum(0, 8);
            tile = allTiles[x][y];
        }
        while (tile.isTileSolved());

        return tile;
    }

    private void generateBoxes() {
        //הפעולה  מייצרת את הBOXES
        for (int x = 0; x < SUDOKU_BOXES_IN_X; x++) {

            for (int y = 0; y < SUDOKU_BOXES_IN_Y; y++) {
                boxes[x][y] = new Box(x, y, this);
            }
        }
    }
//    public void delIfOnlyPossibilityNumsX2(int x, int y) {
//
//        for (int i = 1; i < SUDOKU_UNITS_IN_REC + 1; i++) {
//            if (isTilePossibilityValue(x, y, i))
//                delIfOnlyPossibilityX2(x, y, i);
//        }
//
//    }
//
//    public void delIfOnlyPossibilityX2(int x, int y, int possibilityNum) {
//
//        int[] recNum1 = null;
//        int[] recNum2 = null;
//
//        do {
//            recNum1 = getRandomDifferentRecNumForTileY(x, y);
//            צריך לעשות את שני הריבועים שהמשבצת לא נמצאת בהם...
//            recNum2 = getRandomDifferentRecNumForTileY(x, y);
//        }
//        while (Arrays.equals(recNum2, recNum1));
//
//        if (isOnlyPlaceIsThisXInRec(x, possibilityNum, recNum1))
//            delValPossibility(x, y, possibilityNum);
//
//        else if (isOnlyPlaceIsThisXInRec(x, possibilityNum, recNum2))
//            delValPossibility(x, y, possibilityNum);
//
//    }
//
//    public void delIfOnlyPossibilityNumsY2(int x, int y) {
//   פעולה שמקבלת משבצת ועוברת על כל המספרים האפשריים בריבועים שונים
//   ובודקת האם בריבוע אחר כל מספר יכול להיות רק בשורה ספציפיצ וכך לפסול
//
//        for (int i = 1; i < SUDOKU_UNITS_IN_REC + 1; i++) {
//            if (isTilePossibilityValue(x, y, i))
//                delIfOnlyPossibilityY2(x, y, i);
//        }
//
//    }
//
//    public boolean isPossibilityCanBeSwapY3(int y1, int y2, int[] recNum1, int[] recNum2, int possibility) {
//        //פעולה שמקבלת שתי שורות ובודקת בכל ריבוע (של כל שורה) האם רק בשתי השורות האלו הערך יכול להימצא,
//        // אם כן הפעולה תמחק אפשרות בריבוע השלישי
//
//        // אם ורק אם בשתי שורות יש ערך מסויים, אז תמחק את האפשרות
//
//        int lastY = getLastY(y1, y2);
//
//        return isYPossibilityValue3(y1, recNum1, possibility) && isYPossibilityValue3(y2, recNum1, possibility) && isYPossibilityValue3(y1, recNum2, possibility) && isYPossibilityValue3(y2, recNum2, possibility) && !isYPossibilityValue3(lastY, recNum1, possibility) && !isYPossibilityValue3(lastY, recNum2, possibility);
//    }

//    public void delPossIfPossibilityCanBeSwapY3(int y1, int y2, int possibility, int[] recNum1, int[] recNum2) {
//
//        if (isPossibilityCanBeSwapY3(y1, y2, recNum1, recNum2, possibility)) {
//            int[] lastRecY = getLastRecY(recNum1, recNum2);
//
//            delPossibilityInYInRec(y1, possibility, lastRecY);
//            delPossibilityInYInRec(y2, possibility, lastRecY);
//
//        }
//
//    }// הבאג היה מכיוון שבדקתי האם יש רק משבצת אחת פנוייה, אבל בדקתי לפני שכל המשבצות התעדכנו בEASYDEL
//
//    public void delIfPPossibilityCanBeSwapY3(int y1, int y2, int[] recNum1, int[] recNum2) {
//
//        for (int i = 1; i < SUDOKU_UNITS_IN_REC + 1; i++)
//            delPossIfPossibilityCanBeSwapY3(y1, y2, i, recNum1, recNum2);
//
//    }

//    public void delIfPPossibilityCanBeSwapALLY3(int[] recNum1, int[] recNum2) {
//        int firstIndexY = 3 * recNum1[1];
////        for (int i = firstIndexY; i < firstIndexY+ 3; i++) {
////            int y1 = 3* recNum1[1];
////            int y2 =
//        delIfPPossibilityCanBeSwapY3(firstIndexY, firstIndexY + 1, recNum1, recNum2);
//        delIfPPossibilityCanBeSwapY3(firstIndexY + 1, firstIndexY + 2, recNum1, recNum2);
//        delIfPPossibilityCanBeSwapY3(firstIndexY, firstIndexY + 2, recNum1, recNum2);
//
////        }
//
//
//    }

//    public void delAllPossibilities(int x, int y) {
//// פעולה שמקבלת משבצת ומנקה את מערך האפשרויות כך שהכל יהיה  - 0
//
//        Arrays.fill(tiles[x][y].possibleValue, 0);
//    }

    //    public void del8Possibilities(int x, int y, int valueNotDel) {
//        // פעולה שמקבלת משבצת ומוחקת את כל האפשריות חוץ מאחת
//
//        for (int i = 0; i < SUDOKU_UNITS_IN_REC; i++) {
//
//            if (tiles[x][y].possibleValue[i] != valueNotDel)
//                delValPossibility(x, y, tiles[x][y].possibleValue[i]);
//
//        }
//    }
//    public boolean isYPossibilityValue3(int y, int[] recNum, int possibility) {
//        פעולה שמקבלת שורה וריבוע (כלומר 3 ערכים שצריך לבדוק) ומחזירה האם יכול להיות הערך
//        int firstIndexX = 3 * recNum[0];
//
//        for (int x1 = firstIndexX; x1 < firstIndexX + 3; x1++)
//            if (isTilePossibilityValue(x1, y, possibility))
//                return true;
//
//        return false;
//    }
    //    public void delPossibilitiesIfParentBoxIsFullOfValue1() {
//פעולה שעוברת על כל הBOXES ובודקת לכל אחד בנפרד האם ניתן למחוק אצלו
    //אם כן הפעולה תעדכן את הערך של המספר
//
//        for (int x = 0; x < Box.SUDOKU_UNITS_IN_BOX_IN_X; x++) {
//
//            for (int y = 0; y < Box.SUDOKU_UNITS_IN_BOX_IN_Y; y++) {
//                Box box = boxes[x][y];
//                box.delPossibilityIfParentBoxIsFullOfValue1();
//            }
//        }
//    }
//
//
//    public int getLastX(int x1, int x2) {
//
//        int lastX = -1;
//
//        if (x1 % 3 == 0 && x2 % 3 == 1)
//            lastX = Math.max(x1, x2) + 1;
//
//        else if (x1 % 3 == 1 && x2 % 3 == 2)
//            lastX = Math.min(x1, x2) - 1;
//
//        else if (x1 % 3 == 0 && x2 % 3 == 2)
//            lastX = Math.min(x1, x2) + 1;
//
//        return lastX;
//    }
//
//    public int getLastY(int y1, int y2) {
//
//        int lastY = -1;
//
//        if (y1 % 3 == 0 && y2 % 3 == 1)
//            lastY = Math.max(y1, y2) + 1;
//
//        else if (y1 % 3 == 1 && y2 % 3 == 2)
//            lastY = Math.min(y1, y2) - 1;
//
//        else if (y1 % 3 == 0 && y2 % 3 == 2)
//            lastY = Math.min(y1, y2) + 1;
//
//        return lastY;
//    }
//
//    public int[] getLastRecX(int[] recNum1, int[] recNum2) {
//        //פעולה שמחזירה את הריבוע האחרון בטור, כלומר ידועים שניים כפרמטרים ואנו רוצים את השלישי
//
//        int[] lastRecNum = new int[2];
//        lastRecNum[0] = recNum1[0];
//
//        if (recNum1[0] == 0 && recNum2[0] == 1)
//            lastRecNum[1] = 2;
//
//        if (recNum1[0] == 1 && recNum2[0] == 2)
//            lastRecNum[1] = 0;
//
//        if (recNum1[0] == 0 && recNum2[0] == 2)
//            lastRecNum[1] = 1;
//
//        return lastRecNum;
//    }
//
//    public int[] getLastRecY(int[] recNum1, int[] recNum2) {
//        //פעולה שמחזירה את הריבוע האחרון בשורה, כלומר ידועים שניים כפרמטרים ואנו רוצים את השלישי
//
//        int[] lastRecNum = new int[2];
//        lastRecNum[1] = recNum1[1];
//
//        if (recNum1[1] == 0 && recNum2[1] == 1)
//            lastRecNum[0] = 2;
//
//        if (recNum1[1] == 1 && recNum2[1] == 2)
//            lastRecNum[0] = 0;
//
//        if (recNum1[1] == 0 && recNum2[1] == 2)
//            lastRecNum[0] = 1;
//
//
//        return lastRecNum;
//    }
/*
    public int[] getRandomDifferentRecNumForTileX(int x, int y) {
        //הפעולה קשורה למחיקה החכמה שלי delIfOnlyPossibilityY
        // הפעולה תחזיר ברנדומליות ערך של ריבוע אחר באותו ערך Y כלומר RECNUM[1]
        int[] actualRecNum = getRectNumForTile(x, y);

        int[] retRecNum = {actualRecNum[0], actualRecNum[1]};

        do {
            Random random = new Random();
            int rand = random.nextInt(3);
            switch (rand) {

                case 0:
                    retRecNum[0] = 0;
                    break;

                case 1:
                    retRecNum[0] = 1;
                    break;

                case 2:
                    retRecNum[0] = 2;
                    break;
            }
        }
        while (retRecNum[0] == actualRecNum[0]);


        return retRecNum;
    }

    public int[] getRandomDifferentRecNumForTileY(int x, int y) {

        int[] actualRecNum = getRectNumForTile(x, y);

        int[] retRecNum = {actualRecNum[0], actualRecNum[1]};

        do {
            Random random = new Random();
            int rand = random.nextInt(3);
            switch (rand) {

                case 0:
                    retRecNum[1] = 0;
                    break;

                case 1:
                    retRecNum[1] = 1;
                    break;

                case 2:
                    retRecNum[1] = 2;
                    break;
            }
        }
        while (retRecNum[1] == actualRecNum[1]);


        return retRecNum;
    }

    public int[][] getOtherRecNumsForTileY(int x, int y) {
        // הפעולה תחזיר מערך דו ממדי, כאשר האינדקס הראשון יחזיר את הריבוע הרישון, והאינדקס השני את הריבוע השני
        int[][] ret = new int[2][2];
        int[] thisRec = getRectNumForTile(x, y);

        if (thisRec[0] == 0) {
            ret[0] = new int[]{1, thisRec[1]};
            ret[1] = new int[]{2, thisRec[1]};
        } else if (thisRec[0] == 1) {
            ret[0] = new int[]{0, thisRec[1]};
            ret[1] = new int[]{2, thisRec[1]};
        } else if (thisRec[0] == 2) {
            ret[0] = new int[]{0, thisRec[1]};
            ret[1] = new int[]{1, thisRec[1]};
        }
        return ret;
    }

    public int[][] getOtherRecNumsForTileX(int x, int y) {
        // הפעולה תחזיר מערך דו ממדי, כאשר האינדקס הראשון יחזיר את הריבוע הרישון, והאינדקס השני את הריבוע השני

        // הפעולה לא נכונה כי העתקתי אותה
        int[][] ret = new int[2][2];
        int[] thisRec = getRectNumForTile(x, y);

        if (thisRec[0] == 0) {
            ret[0] = new int[]{1, thisRec[1]};
            ret[1] = new int[]{2, thisRec[1]};
        } else if (thisRec[0] == 1) {
            ret[0] = new int[]{0, thisRec[1]};
            ret[1] = new int[]{2, thisRec[1]};
        } else if (thisRec[0] == 2) {
            ret[0] = new int[]{0, thisRec[1]};
            ret[1] = new int[]{1, thisRec[1]};
        }
        return ret;
    }
*/
    public void connectBoxesTilesToArrayTiles() {
//        הפעולה מקשרת בין הBOX לבין המערך.שני הלופים הראשונים הם לעבור על הBOXES
//        שני הלולאות הפנימיות הן כדי להשים את המשבצות
        for (int xBox = 0; xBox < SUDOKU_BOXES_IN_X; xBox++) {

            for (int yBox = 0; yBox < SUDOKU_BOXES_IN_X; yBox++) {
                Box box = boxes[xBox][yBox];

                for (int xTile = 0; xTile < Box.SUDOKU_UNITS_IN_BOX_IN_X; xTile++) {

                    for (int yTile = 0; yTile < Box.SUDOKU_UNITS_IN_BOX_IN_Y; yTile++) {
                        Tile tile = box.getTiles()[xTile][yTile];

                        int sudokuXCoordinate = tile.getSudokuCoordinateX();
                        int sudokuYCoordinate = tile.getSudokuCoordinateY();

                        allTiles[sudokuXCoordinate][sudokuYCoordinate] = tile;
                    }
                }
            }
        }

    }

    private void updateBoxes() {
        //הפעולה עוברת על כל הBOXES בלוח ומעדכנת בתוכם את הTILES
        for (int x = 0; x < SUDOKU_BOXES_IN_X; x++) {

            for (int y = 0; y < SUDOKU_BOXES_IN_Y; y++) {

//                isBug(x, y);

                Box box = boxes[x][y];

                if (!box.isSolved())
                    box.updateBox();
            }
        }
    }

    public boolean isSudokuSolve() {
        // פעולה שבודקת האם הסודוקו פתור
        return totalSolvedCount == TOTAL_UNITS;
    }
    //    public void delPossibilityInXInRec(int x, int possibility, int[] recNum) {
//          פעולה שמקבלת טור וריבוע וערך למחיקה ומוחקת את האפשרות מכל ה3 ערכים בשורה בריבוע
//        int firstIndexY = 3 * recNum[1];
//
//        for (int y1 = firstIndexY; y1 < firstIndexY + 3; y1++)
//            delValPossibility(x, y1, possibility);
//    }
//
//    public void delPossibilityInYInRec(int y, int possibility, int[] recNum) {
//          פעולה שמקבלת שורה וריבוע  וערך למחיקה ומוחקת את אפשרות מכל ה3 ערכים בשורה בריבוע
//        int firstIndexX = 3 * recNum[0];
//
//        for (int x1 = firstIndexX; x1 < firstIndexX + 3; x1++)
//            delValPossibility(x1, y, possibility);
//    }
//
//    public void delBasicValPossibilityInY(int x, int y) {
//        הפעולה הזאת תופעל רק על tile שהvalue שלו שווה ל0
//        הפעולה  מקבלת מיקום של tile ולפי הטור שלה מוחקת את האופציות
//        if (tiles[x][y].value != 0) return;
//
//        for (int i = 1; i < SUDOKU_UNITS_IN_Y + 1; i++) {
//
//            if (isNumInY(x, i)) {
//                delValPossibility(x, y, i);
//            }
//
//        }
//
//    }
    public void solve() {
/*
הפעולה פותרת את הסודוקו, היא משתמשת בשני סוגי אלגורמים, הסוג הראשון הוא טכניקות לפתירת סודוקו כמו שאדם היה פותר.
        הסוג השני הוא השמת ערך רנדומלי בTILE רנדומלי עד שהסודוקו נפתר
 */
        while (!isSudokuSolve()) {
            isUpdated = false;
            updateBoxes();

            if (!isUpdated && !isInRandMode) {
                isInRandMode = true;

                Tile tile = getRandUnsolvedTile();
                int randPossibility = tile.getRandomPossibility();
                tile.setRandValue(randPossibility);

                updateBoxes();
            }
            if (isBug() || !isUpdated && isInRandMode)
                returnToBeforeRandom();
        }

    }

    private void returnToBeforeRandom() {
//פעולה שמחזירה את הBOXES לערכים שלפני האלגורתם RAND ויוצאת מRANDMODE
        for (int x = 0; x < SUDOKU_BOXES_IN_X; x++) {

            for (int y = 0; y < SUDOKU_BOXES_IN_Y; y++) {
                Box box = boxes[x][y];
                box.returnToBeforeRandom();
            }
        }
        isInRandMode = false;
    }

    private boolean isBug() {
        //הפעולה בודקת האם הערכים בסודוקו שגויים והסודוקו לא פתיר
        //הפעולה בודקת האם יש TILE שבו כל הPOSSIBILITIES שווים 0 אבל אין ערך
        for (int x = 0; x < SUDOKU_UNITS_IN_X; x++) {

            for (int y = 0; y < SUDOKU_UNITS_IN_Y; y++) {
                Tile tile = allTiles[x][y];

                if (tile.isPossibilitiesEmpty() && !tile.isTileSolved())
                    return true;
            }
        }
        return false;
    }

    public boolean isValueInX(Box parentBox, int x, int possibility) {
//  פעולה שמקבלת X וBOX ובודקת האם המספר num קיים בX
        Box[] boxesInX = parentBox.getBoxesInX();

        for (Box box : boxesInX)
            if (box.isValueInX(x, possibility))
                return true;

        return false;
    }

    public boolean isValueInY(Box parentBox, int y, int possibility) {
//  פעולה שמקבלת Y וBOX ובודקת האם המספר num קיים בY
        Box[] boxesInY = parentBox.getBoxesInY();

        for (Box box : boxesInY)
            if (box.isValueInY(y, possibility))
                return true;

        return false;
    }

    public boolean isXInDifferentBoxesMustHaveValue2(Tile childTile, int possibility) {
        //פעולה שעוברת על כל הX של ה - childTile בסודוקו ואומרת האם חייב להיות הערך, אם כן תמחק בפעולה שבתוך הTILE
        //המחיקה תתקיים רק אם הערך בBOX שונה מהPARENTBOX של הTILE הנוכחי
        Box[] boxesInX = childTile.getParentBox().getBoxesInX();
        Box parentBox = childTile.getParentBox();

        for (Box boxInX : boxesInX) {
            int xInBoxPosTile = childTile.getXInBoxPosTile();

            //תחזיר אמת רק אם חייב להיות ערך בX אבל לא בBOX של הTILE
            if (boxInX.isOnlyPossibilityPlaceIsThisX(xInBoxPosTile, possibility) && !parentBox.equals(boxInX))
                return true;
        }
        return false;
    }

    public boolean isYInDifferentBoxesMustHaveValue2(Tile childTile, int possibility) {
        //פעולה שעוברת על כל הY של ה - childTile בסודוקו ואומרת האם חייב להיות הערך, אם כן תמחק בפעולה שבתוך הTILE
        //המחיקה תתקיים רק אם הערך בBOX שונה מהPARENTBOX של הTILE הנוכחי
        Box[] boxesInY = childTile.getParentBox().getBoxesInY();
        Box parentBox = childTile.getParentBox();

        for (Box boxInY : boxesInY) {
            int yInBoxPosTile = childTile.getYInBoxPosTile();

            if (boxInY.isOnlyPossibilityPlaceIsThisY(yInBoxPosTile, possibility) && !parentBox.equals(boxInY))
                return true;
        }
        return false;

    }

    public boolean isOnlyThisXInBoxMustHaveValue3(int x, Box parentBox, int possibility) {
        //פעולה שעוברת על הX של הSUDOKU ובודקת האם הערך חייב להיות בBOX הזה, אם כן תמחק בשאר השורות בBOX
        if (!parentBox.isPossibleInX(x, possibility) || isValueInX(parentBox, x, possibility))
            return false;

        Box[] boxesInX = parentBox.getBoxesInX();


        for (Box box : boxesInX)
            if (!box.equals(parentBox) && box.isPossibleInX(x, possibility))
                return false;

        return true;
    }

    //    public boolean isOnlyPlaceIsThisXInRec(int x, int possibility, int[] recNum) {
//
//        int firstIndexX = 3 * recNum[0];
//        int firstIndexY = 3 * recNum[1];
//
//        int countPossibilityInX = 0;
//        int countPossibilitiesInRec = 0;
//
//        for (int y1 = firstIndexY; y1 < firstIndexY + 3; y1++)
//            if (isTilePossibilityValue(x, y1, possibility))
//                countPossibilityInX++;
//
//        for (int x1 = firstIndexX; x1 < firstIndexX + 3; x1++) {
//
//            for (int y1 = firstIndexY; y1 < firstIndexY + 3; y1++)
//                if (isTilePossibilityValue(x1, y1, possibility))
//                    countPossibilitiesInRec++;
//        }
//        return countPossibilityInX == countPossibilitiesInRec && countPossibilityInX != 0;
//
//
//    }
    public boolean isOnlyThisYInBoxMustHaveValue3(int y, Box parentBox, int possibility) {
        //הפעולה עוברת על הY של הסודוקו ובודקת האם רק בBOX שהועבר כפרמטר יכול להיות הPOSSIBILITY
        if (!parentBox.isPossibleInY(y, possibility) || isValueInY(parentBox, y, possibility)) // מה קורה אם יש רק אפשרות אחת
            return false;

        Box[] boxesInY = parentBox.getBoxesInY();


        for (Box box : boxesInY)
            if (!box.equals(parentBox) && box.isPossibleInY(y, possibility))
                return false;

        return true;
    }

    public void updateCurrentTile(Direction direction) {
        //פעולה המעדכנת את המיקום של המשתמש במסך, הכוונה לריבוע הירוק שמציין איפה המספר יושם במידה וילחץ מספר בין 1 - 9
        currentTile = currentTile.getUpdateCurrentTile(direction);
    }
    //    public void drawValues(Graphics g) {
//
//        for (int x = 0; x < Sudoku.SUDOKU_UNITS_IN_X; x++) {
//
//            for (int y = 0; y < Sudoku.SUDOKU_UNITS_IN_Y; y++) {
//                Box box = boxes[x][y];
//                box.drawNumbers(g);
//            }
//        }
//    }

    public void delPossibilityInAllTilesInX(Tile solvedTiled) {
        //הפעולה נקראת כאשר מסימים ערך לTILE מסויים
        //הפעולה עוברת על כל הTILES בX של הTILE שהועבר כפרמטר ומעדכנת את הPOSSIBILITIY שהועבר
        int delPossibility = solvedTiled.getValue();
        int x = solvedTiled.getXInBoxPosTile();

        for (int y = 0; y < SUDOKU_UNITS_IN_X; y++) {
            Tile tile = allTiles[x][y];
            tile.delBasicPossibilityX1(delPossibility);
        }
    }

    public void delPossibilityInAllTilesInY(Tile solvedTiled) {
        //הפעולה נקראת כאשר מסימים ערך לTILE מסויים

        int delPossibility = solvedTiled.getValue();
        int y = solvedTiled.getYInBoxPosTile();

        for (int x = 0; x < SUDOKU_UNITS_IN_X; x++) {
            Tile tile = allTiles[x][y];
            tile.delBasicPossibilityY1(delPossibility);
        }
    }

//    public boolean isXWingInY4(Tile tile, int possibility) {
//        //פעולה שמקבלת TILE ובודקת האם יש אלחסון וככה אפשר למחוק אופציות
//
//        int count = 0;
//
//
//    }

    public void saveAllTilesPossibilities() {
//פעולה שעוברת על כל הTILES ושומרת את כל הPOSSIBILITIES האמיתיים, כלומר לפני האלגוריתם הRAND

        for (int x = 0; x < SUDOKU_UNITS_IN_X; x++) {

            for (int y = 0; y < SUDOKU_UNITS_IN_Y; y++) {
                Tile tile = allTiles[x][y];

                tile.savePossibilities();
            }
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    public void drawNumbers(Graphics g) {
        //הפעולה צובעת את המספרים, כלומר את הVALUES אם יש ואם אין אז את הPOSSIBILITIES
        for (int x = 0; x < SUDOKU_UNITS_IN_X; x++) {

            for (int y = 0; y < SUDOKU_UNITS_IN_Y; y++) {
                Tile tile = allTiles[x][y];
                if (tile.isTileSolved())
                    tile.drawValue(tile.getValue(), g);

                else tile.drawPossibilities(g);
            }
        }
    }

    public void updateSudoku() {
        isUpdated = true;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public MyJPanel getMyJPanel() {
        return myJPanel;
    }

    public Box[][] getBoxes() {
        return boxes;
    }

    public Tile[][] getAllTiles() {
        return allTiles;
    }

    public void increaseTotalNumberOfSolved() {
        totalSolvedCount++;
        updateSudoku();
    }

    public void decreaseTotalNumberOfSolved() {
        totalSolvedCount--;
    }

    public boolean isInRandMode() {
        return isInRandMode;
    }
}