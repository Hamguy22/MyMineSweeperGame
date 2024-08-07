package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Controller {
    private static final int SQUARE_SIZE = 50;
    private static final int WIDTH = 9;
    private static final int HEIGHT = 9;
    private static final int NUMBER_OF_MINES = 10;
    private static final int FRAME_WIDTH = WIDTH * SQUARE_SIZE;
    private static final int FRAME_HEIGHT = HEIGHT * SQUARE_SIZE;
    private static final File FOLDER_FILE = new File("images");

    private View view;
    private Graphics graphics;
    private Cell[][] cells = new Cell[WIDTH][HEIGHT];
    private boolean isGameOver = false;

    public void setView(View view) {
        this.view = view;
    }

    public void start() {
        view.create(FRAME_WIDTH, FRAME_HEIGHT);
        fillTheFieldWithCells();
        generateBombsOnField();
        placeNumbers();
        render();
    }

    private void render() {
        BufferedImage image = new BufferedImage(FRAME_WIDTH, FRAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
        graphics = image.getGraphics();
        drawField();
        view.setImageLabel(image);
    }

//    private void redrawTheBoard() {
//        for (int x = 0; x < WIDTH; x++) {
//            for (int y = 0; y < HEIGHT; y++) {
//
//            }
//        }
//    }

    private void draw(BufferedImage bufferedImage, int x, int y) {
        graphics.drawImage(bufferedImage, x * SQUARE_SIZE, y * SQUARE_SIZE, null);
    }

    private BufferedImage loadImage(String fileName) {
        try {
            return ImageIO.read(new File(FOLDER_FILE, fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void drawField() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                draw(loadImage(cells[x][y].getFileName()), x, y);
            }
        }
    }

    public void handleMouseClick(int mouseX, int mouseY, int mouseButtonCode) {
        if (isGameOver) {
            return;
        }
        int x = mouseX / SQUARE_SIZE;
        int y = mouseY / SQUARE_SIZE;
        if (mouseButtonCode == 1) {
            if (cells[x][y].isFlag()) {
                return;
            }
            open(x, y);
            endOfTheGame(x, y);

        } else {
            cells[x][y].toggleFlag();
        }
        if (checkWinner()){
            markBombs();
            isGameOver = true;
        }
        render();
    }

    private void open(int centerX, int centerY) {
        if (cells[centerX][centerY].isOpen()) {
            return;
        }
        cells[centerX][centerY].open();
        if (!cells[centerX][centerY].isZero()) {
            return;
        }
        for (int x = centerX - 1; x <= centerX + 1; x++) {
            for (int y = centerY - 1; y <= centerY + 1; y++) {
                if (isLocationCorrect(x, y)) {
                    open(x, y);
                }
            }
        }
    }

    private void endOfTheGame(int centerX, int centerY) {
        if (cells[centerX][centerY].isBomb()) {
//            for (int x = 0; x < WIDTH; x++) {
//                for (int y = 0; y < HEIGHT; y++) {
//                    if (cells[x][y].isBomb()) {
//                        cells[x][y].open();
//                    }
//                }
//            }
            processField(Cell::open);
            isGameOver = true;
        }

    }

    private void processField(Consumer<Cell> consumer) {
        getFieldStream()
                .filter(Cell::isBomb)
                .forEach(consumer);
    }

    private void markBombs() {
//        for (int x = 0; x < WIDTH; x++) {
//            for (int y = 0; y < HEIGHT; y++) {
//                if (cells[x][y].isBomb()) {
//                    cells[x][y].toggleFlag();
//                }
//            }
//        }
        processField(Cell::toggleFlag);
    }

    private boolean checkWinner() {
//        for (int x = 0; x < WIDTH; x++) {
//            for (int y = 0; y < HEIGHT; y++) {
//                if (!cells[x][y].isBomb() && !cells[x][y].isOpen()) {
//                    return false;
//                }
//            }
//        }
//        return true;

        return getFieldStream().allMatch(c -> c.isBomb() || c.isOpen());
    }

    private void fillTheFieldWithCells() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                cells[x][y] = new Cell();
            }
        }
    }

    private Stream<Cell> getFieldStream() {
        return Arrays.stream(cells)
                .flatMap(Arrays::stream);
    }

//    private void generateBombOnField() {
//        Cell cell;
//        do {
//            cell = cells[randomNumber(WIDTH)][randomNumber(HEIGHT)];
//        } while (cell.isBomb());
//        cell.placeBomb();
//    }
//
//    private void generateBombsOnField() {
//        for (int i = 0; i < NUMBER_OF_MINES; i++) {
//            generateBombOnField();
//        }
//    }

    private void generateBombsOnField() {
        Stream.generate(() -> cells[randomNumber(WIDTH)][randomNumber(HEIGHT)])
                .distinct()
                .limit(NUMBER_OF_MINES)
                .forEach(Cell::placeBomb);
    }

    private void placeNumbers() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (!cells[x][y].isBomb()) {
                    cells[x][y].setNumberOfBombsAround(numberOfBombsAround(x, y));
                }
            }
        }
    }

    private int numberOfBombsAround(int centerX, int centerY) {
        int countBombs = 0;
        for (int x = centerX - 1; x <= centerX + 1; x++) {
            for (int y = centerY - 1; y <= centerY + 1; y++) {
                if (isLocationCorrect(x, y) && cells[x][y].isBomb()) {
                    countBombs++;
                }
            }
        }
        return countBombs;
    }

    private boolean isLocationCorrect(int x, int y) {
        return x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT;
    }

    private int randomNumber(int max) {
        return (int) (Math.random() * max);
    }

//    private void drawField() {
//        for (int x = 0; x < WIDTH; x++) {
//            for (int y = 0; y < HEIGHT; y++) {
//            }
//        }
//    }
}
