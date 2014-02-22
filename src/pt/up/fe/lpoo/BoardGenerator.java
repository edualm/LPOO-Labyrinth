/**
 * Labyrinth
 *
 * Created by Eduardo Almeida and João Almeida.
 */

package pt.up.fe.lpoo;

import java.util.Random;
import java.util.Stack;

public class BoardGenerator {
    private int _width = 0;  //  x size
    private int _height = 0; //  y size

    Board.Type boardRep[][] = {
            {Board.Type.LOCKED_WALL, Board.Type.LOCKED_WALL, Board.Type.LOCKED_WALL, Board.Type.LOCKED_WALL, Board.Type.LOCKED_WALL, Board.Type.LOCKED_WALL, Board.Type.LOCKED_WALL, Board.Type.LOCKED_WALL, Board.Type.LOCKED_WALL, Board.Type.LOCKED_WALL},
            {Board.Type.LOCKED_WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.LOCKED_WALL},
            {Board.Type.LOCKED_WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.LOCKED_WALL},
            {Board.Type.LOCKED_WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.LOCKED_WALL},
            {Board.Type.LOCKED_WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.LOCKED_WALL},
            {Board.Type.LOCKED_WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.LOCKED_WALL},
            {Board.Type.LOCKED_WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.LOCKED_WALL},
            {Board.Type.LOCKED_WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.LOCKED_WALL},
            {Board.Type.LOCKED_WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.WALL, Board.Type.LOCKED_WALL},
            {Board.Type.LOCKED_WALL, Board.Type.LOCKED_WALL, Board.Type.LOCKED_WALL, Board.Type.LOCKED_WALL, Board.Type.LOCKED_WALL, Board.Type.LOCKED_WALL, Board.Type.LOCKED_WALL, Board.Type.LOCKED_WALL, Board.Type.LOCKED_WALL, Board.Type.LOCKED_WALL}
    };

    private BoardGenerator() {
        //  Calling this method directly is unsupported.
    }

    public BoardGenerator(int w, int h) throws Exception {
        if (w < 3 || h < 3)
            throw new Exception("Both dimensions need to be >= 3.");

        boardRep = new Board.Type[h][w];

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                boardRep[i][j] = ((i == 0 || i == (h - 1)) ? Board.Type.LOCKED_WALL :
                        ((j == 0 || j == (w - 1)) ? Board.Type.LOCKED_WALL :
                                Board.Type.WALL));
            }
        }

        _width = w;
        _height = h;
    }

    public Board.Type[][] generateBoard() throws Exception {
        Coordinate stp;

        try {
            stp = _startingPoint();
        } catch (Exception exc) {
            throw exc;
        }

        boardRep[stp.y][stp.x] = Board.Type.EXIT;

        Coordinate position = new Coordinate(stp.x, stp.y);

        Stack<Coordinate> crdHistory = new Stack<Coordinate>();

        while (true) {
            Coordinate[] moves = _possibleMoves(position);

            Random rand = new Random();

            if (moves.length == 0) {
                if (crdHistory.size() == 0)
                    break;

                position = crdHistory.pop();

                continue;
            }

            int val = rand.nextInt(moves.length);

            try {
                boardRep[moves[val].y][moves[val].x] = Board.Type.BLANK;
            } catch (Exception exc) {
                break;
            }

            position = moves[val];

            crdHistory.push(position);
        }

        for (int i = 0; i < _height; i++)
            for (int j = 0; j < _width; j++)
                if (boardRep[i][j] == Board.Type.LOCKED_WALL)
                    boardRep[i][j] = Board.Type.WALL;

        _fillWithCharacters();

        return boardRep;
    }

    private void _fillWithCharacters() {
        Coordinate[] whiteSpaces = _getWhitespaces();

        Random rand = new Random();

        /*  Seems dirty, but it's optimized. So, profit! ^^ */

        int val = rand.nextInt(whiteSpaces.length);
        int val2 = rand.nextInt(whiteSpaces.length);
        int val3 = rand.nextInt(whiteSpaces.length);

        while (val == val2)
            val2 = rand.nextInt(whiteSpaces.length);

        while (val == val3 || val2 == val3)
            val3 = rand.nextInt(whiteSpaces.length);

        boardRep[whiteSpaces[val].y][whiteSpaces[val].x] = Board.Type.DRAGON;
        boardRep[whiteSpaces[val2].y][whiteSpaces[val2].x] = Board.Type.HERO;
        boardRep[whiteSpaces[val3].y][whiteSpaces[val3].x] = Board.Type.SWORD;
    }

    private Coordinate[] _getWhitespaces() {
        Coordinate coords[] = new Coordinate[_width * _height];

        int currIndex = 0;

        for (int i = 0; i < _height; i++)
            for (int j = 0; j < _width; j++)
                if (boardRep[i][j] == Board.Type.BLANK)
                    coords[currIndex++] = new Coordinate(j, i);

        Coordinate crdRet[] = new Coordinate[currIndex];

        for (int i = 0; i < currIndex; i++)
            crdRet[i] = coords[i];

        return crdRet;
    }

    private Coordinate[] _possibleMoves(Coordinate pos) {
        /*
         *  Th'arr be monst'arrs below, lad!
         *  Don't dive too deep without the help of your crew, capt'n!
         */

        Coordinate[] crds = new Coordinate[4];

        int ccIndex = 0;

        //  Move Up

        try {
            if (boardRep[pos.y - 1][pos.x] == Board.Type.WALL)
                if (!(boardRep[pos.y - 1][pos.x - 1] == Board.Type.BLANK &&
                        boardRep[pos.y - 2][pos.x - 1] == Board.Type.BLANK &&
                        boardRep[pos.y - 2][pos.x] == Board.Type.BLANK))
                    if (!(boardRep[pos.y - 2][pos.x] == Board.Type.BLANK &&
                            boardRep[pos.y - 2][pos.x + 1] == Board.Type.BLANK &&
                            boardRep[pos.y - 1][pos.x + 1] == Board.Type.BLANK))
                        crds[ccIndex++] = new Coordinate(pos.x, pos.y - 1);
        } catch (Exception exc) {

        }

        //  Move Down

        try {
            if (boardRep[pos.y + 1][pos.x] == Board.Type.WALL)
                if (!(boardRep[pos.y + 1][pos.x - 1] == Board.Type.BLANK &&
                        boardRep[pos.y + 2][pos.x - 1] == Board.Type.BLANK &&
                        boardRep[pos.y + 2][pos.x] == Board.Type.BLANK))
                    if (!(boardRep[pos.y + 2][pos.x] == Board.Type.BLANK &&
                            boardRep[pos.y + 2][pos.x + 1] == Board.Type.BLANK &&
                            boardRep[pos.y + 1][pos.x + 1] == Board.Type.BLANK))
                        crds[ccIndex++] = new Coordinate(pos.x, pos.y + 1);
        } catch (Exception exc) {

        }

        //  Move Right

        try {
            if (boardRep[pos.y][pos.x + 1] == Board.Type.WALL)
                if (!(boardRep[pos.y - 1][pos.x + 1] == Board.Type.BLANK &&
                        boardRep[pos.y - 1][pos.x + 2] == Board.Type.BLANK &&
                        boardRep[pos.y][pos.x + 2] == Board.Type.BLANK))
                    if (!(boardRep[pos.y][pos.x + 2] == Board.Type.BLANK &&
                            boardRep[pos.y + 1][pos.x + 2] == Board.Type.BLANK &&
                            boardRep[pos.y + 1][pos.x + 1] == Board.Type.BLANK))
                        crds[ccIndex++] = new Coordinate(pos.x + 1, pos.y);
        } catch (Exception exc) {

        }

        //  Move Left

        try {
            if (boardRep[pos.y][pos.x - 1] == Board.Type.WALL)
                if (!(boardRep[pos.y - 1][pos.x - 1] == Board.Type.BLANK &&
                        boardRep[pos.y - 1][pos.x - 2] == Board.Type.BLANK &&
                        boardRep[pos.y][pos.x - 2] == Board.Type.BLANK))
                    if (!(boardRep[pos.y][pos.x - 2] == Board.Type.BLANK &&
                            boardRep[pos.y + 1][pos.x - 2] == Board.Type.BLANK &&
                            boardRep[pos.y + 1][pos.x - 1] == Board.Type.BLANK))
                        crds[ccIndex++] = new Coordinate(pos.x - 1, pos.y);
        } catch (Exception exc) {

        }

        Coordinate[] crdRet = new Coordinate[ccIndex];

        for (int i = 0; i < ccIndex; crdRet[i] = crds[i++]) {

        }

        return crdRet;
    }

    private Coordinate _startingPoint() throws Exception {
        //  Choosing the Column...

        Random rand = new Random();

        int val = rand.nextInt(4);
        int index = rand.nextInt(8) + 1;

        switch (val) {
            case 0:  // Top-Most Line

                return new Coordinate(val, 0);

            case 1: //  Right-Most Line

                return new Coordinate(9, val);

            case 2: //  Bottom-Most Line

                return new Coordinate(val, 9);

            case 3: //  Left-Most Line

                return new Coordinate(0, val);

            default:    //  ...

                break;
        }

        throw new Exception("Say What?!");
    }
}
