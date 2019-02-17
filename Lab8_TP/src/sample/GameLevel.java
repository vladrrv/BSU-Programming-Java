package sample;

import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class GameLevel {
    private int m, n;
    private int[] blockMatrix;

    GameLevel(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        m = scanner.nextInt();
        n = scanner.nextInt();
        blockMatrix = new int[m*n];
        for (int i = 0; i < m*n; ++i) {
            blockMatrix[i] = scanner.nextInt();
        }
    }
    List<Block> getBlocks(double width, double height, double boundThickness) {
        List<Block> blocks = new ArrayList<>();
        double maxHeight = (height-boundThickness)*0.6, maxWidth = width-boundThickness*2;
        double blockWidth = maxWidth / n, blockHeight = maxHeight / m;
        double startX = (width - blockWidth*m) / 2, y = boundThickness;

        for (int i = 0; i < m; ++i) {
            double x = startX;
            for (int j = 0; j < n; ++j) {
                if (blockMatrix[i*n+j] == 1) {
                    blocks.add(new Block(x, y, blockWidth, blockHeight, Color.GREEN));
                }
                x += blockWidth;
            }
            y += blockHeight;
        }
        return blocks;
    }
}
