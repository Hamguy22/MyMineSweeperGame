package com.company;

public class Cell {
    private boolean isOpen = false;
    private int numberOfBombsAround = 0;
    private boolean isFlag = false;
    private boolean isBomb = false;

    public void open() {
        isOpen = true;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean isFlag() {
        return isFlag;
    }

    public boolean isZero() {
        return numberOfBombsAround == 0;
    }

    public void setNumberOfBombsAround(int numberOfBombsAround) {
        this.numberOfBombsAround = numberOfBombsAround;
    }

    public void toggleFlag() {
        isFlag = !isFlag;
    }

    public void placeBomb() {
        isBomb = true;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public String getFileName() {
        if (isOpen) {
            if (isBomb) {
                return "bomb.png";
            }
            return "open" + numberOfBombsAround + ".png";
        }
        if (isFlag) {
            return "flag.png";
        }
        return "closed.png";
    }


}
