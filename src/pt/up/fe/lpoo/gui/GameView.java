/**
 * Labyrinth
 *
 * Created by Eduardo Almeida and João Almeida.
 */

package pt.up.fe.lpoo.gui;

import pt.up.fe.lpoo.logic.Board;
import pt.up.fe.lpoo.logic.BoardGenerator;
import pt.up.fe.lpoo.logic.Coordinate;
import pt.up.fe.lpoo.logic.piece.Piece;
import pt.up.fe.lpoo.logic.piece.itemizable.Dragon;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;

public class GameView {
    static private JFrame frame;

    static private GamePanel gp;

    static private int boardX = 10;
    static private int boardY = 10;
    static private int dragonsNr = 1;
    static private boolean dragonsSleep = true;

    static private String upBinding = "up";
    static private String leftBinding = "left";
    static private String downBinding = "down";
    static private String rightBinding = "right";

    public static void main(String[] args) {
        Board brd;

        try {
            brd = new Board();

            BoardGenerator gen = new BoardGenerator(10, 10, 1);

            Vector<Piece> ooBoard = gen.generateBoard();

            for (Piece pc : ooBoard)
                pc.setBoard(brd);

            brd.setBoardPieces(ooBoard);
            brd.setWidth(10);
            brd.setHeight(10);

            Dragon drag = (Dragon) brd.getPiecesWithType(Board.Type.DRAGON).get(0);

            drag.setBehavior(Dragon.Behavior.NO_SLEEP);

            frame = new JFrame("Labyrinth Game View");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setPreferredSize(new Dimension(500, 600));
            frame.getContentPane().setLayout(new BorderLayout());

            gp = new GamePanel(brd, new Coordinate(500, 500));

            frame.getContentPane().add(gp, BorderLayout.CENTER);

            JPanel newPanel = new JPanel(new FlowLayout());

            JButton restartGameButton = new JButton("Restart Game");
            restartGameButton.setPreferredSize(new Dimension(150, 35));
            restartGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    newGame();
                }
            });

            restartGameButton.setFocusable(false);

            newPanel.add(restartGameButton);

            JButton settingsButton = new JButton("Game Settings");
            settingsButton.setPreferredSize(new Dimension(150, 35));
            settingsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    labyrinthConfigurationDialog();
                }
            });

            settingsButton.setFocusable(false);

            newPanel.add(settingsButton);

            JButton finishButton = new JButton("Exit Game");
            finishButton.setPreferredSize(new Dimension(150, 35));
            finishButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            finishButton.setFocusable(false);

            newPanel.add(finishButton);

            frame.getContentPane().add(newPanel, BorderLayout.SOUTH);

            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {

        }
    }

    public static void newGame() {
        try {
            Board newBoard = new Board();

            BoardGenerator gen = new BoardGenerator(boardX, boardY, dragonsNr);

            Vector<Piece> ooBoard = gen.generateBoard();

            for (Piece pc : ooBoard)
                pc.setBoard(newBoard);

            newBoard.setBoardPieces(ooBoard);
            newBoard.setWidth(boardX);
            newBoard.setHeight(boardY);

            Vector<Piece> drag = newBoard.getPiecesWithType(Board.Type.DRAGON);

            for (Piece d : drag)
                ((Dragon) d).setBehavior(dragonsSleep ? Dragon.Behavior.SLEEP : Dragon.Behavior.NO_SLEEP);

            frame.getContentPane().remove(gp);

            gp = new GamePanel(newBoard, new Coordinate(50 * boardX, 50 * boardY));

            frame.getContentPane().add(gp, BorderLayout.CENTER);

            frame.repaint();
            frame.revalidate();

            gp.setFocusable(true);

            frame.resize(50 * boardX, 50 * boardY + 100);
        } catch (Exception exc) {

        }
    }

    public static void labyrinthConfigurationDialog() {
        JTextField xField = new JTextField(Integer.toString(boardX));
        JTextField yField = new JTextField(Integer.toString(boardY));

        JTextField dragField = new JTextField(Integer.toString(dragonsNr));

        JCheckBox shouldSleep = new JCheckBox("Yes", dragonsSleep);

        JTextField upKeyBinding = new JTextField(upBinding);
        JTextField downKeyBinding = new JTextField(downBinding);
        JTextField leftKeyBinding = new JTextField(leftBinding);
        JTextField rightKeyBinding = new JTextField(rightBinding);

        JPanel myPanel = new JPanel();

        myPanel.setLayout(new GridLayout(13, 2));

        myPanel.add(new JLabel("Board Settings"));
        myPanel.add(Box.createHorizontalStrut(1));

        myPanel.add(Box.createHorizontalStrut(1));
        myPanel.add(Box.createHorizontalStrut(1));

        myPanel.add(new JLabel("Board X:"));
        myPanel.add(xField);

        myPanel.add(new JLabel("Board Y:"));
        myPanel.add(yField);

        myPanel.add(new JLabel("Dragons Nr:"));
        myPanel.add(dragField);

        myPanel.add(new JLabel("Dragons Sleep?"));
        myPanel.add(shouldSleep);

        myPanel.add(Box.createHorizontalStrut(1));
        myPanel.add(Box.createHorizontalStrut(1));

        myPanel.add(new JLabel("Key Bindings"));
        myPanel.add(Box.createHorizontalStrut(1));

        myPanel.add(Box.createHorizontalStrut(1));
        myPanel.add(Box.createHorizontalStrut(1));

        myPanel.add(new JLabel("Up:"));
        myPanel.add(upKeyBinding);

        myPanel.add(new JLabel("Left:"));
        myPanel.add(leftKeyBinding);

        myPanel.add(new JLabel("Down:"));
        myPanel.add(downKeyBinding);

        myPanel.add(new JLabel("Right:"));
        myPanel.add(rightKeyBinding);

        int result = JOptionPane.showConfirmDialog(null, myPanel, "Labyrinth Configuration", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            boardX = Integer.parseInt(xField.getText());
            boardY = Integer.parseInt(yField.getText());
            dragonsNr = Integer.parseInt(dragField.getText());
            dragonsSleep = shouldSleep.isSelected();

            upBinding = upKeyBinding.getText();
            leftBinding = leftKeyBinding.getText();
            downBinding = downKeyBinding.getText();
            rightBinding = rightKeyBinding.getText();

            gp.setKeyBindings(upBinding, leftBinding, downBinding, rightBinding);
        }
    }
}