package com.example.myntra;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;

public class Test {

    public static double[][] multiplyMatrices(double[][] m1, double[][] m2) {
        int m1ColLength = m1[0].length;
        int m2RowLength = m2.length;
        if (m1ColLength != m2RowLength) {
            return null;
        } else {
            int mRRowLength = m1.length;
            int mRColLength = m2[0].length;
            double[][] mResult = new double[mRRowLength][mRColLength];

            for(int i = 0; i < mRRowLength; ++i) {
                for(int j = 0; j < mRColLength; ++j) {
                    for(int k = 0; k < m1ColLength; ++k) {
                        mResult[i][j] += m1[i][k] * m2[k][j];
                    }
                }
            }

            return mResult;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Bitmap daltonize(int type, Bitmap img) {
        //Bitmap img = Bitmap.createScaledBitmap(image, image.getWidth(), image.getHeight(), true);
        int width = img.getWidth();
        int height = img.getHeight();
        double[][] current = new double[3][1];
        double[][] toLMS = new double[][]{{17.8824D, 43.5161D, 4.11935D}, {3.45565D, 27.1554D, 3.86714D}, {0.0299566D, 0.184309D, 1.46709D}};
        double[][] protanope = new double[][]{{0.0D, 2.02344D, -2.52581D}, {0.0D, 1.0D, 0.0D}, {0.0D, 0.0D, 1.0D}};
        double[][] deutranope = new double[][]{{1.0D, 0.0D, 0.0D}, {0.494207D, 0.0D, 1.24827D}, {0.0D, 0.0D, 1.0D}};
        double[][] tritanope = new double[][]{{1.0D, 0.0D, 0.0D}, {0.0D, 1.0D, 0.0D}, {-0.395913D, 0.801109D, 0.0D}};
        double[][] toRGB = new double[][]{{0.08094444D, -0.1305044D, 0.116721066D}, {-0.010248533514D, 0.05401932663599884D, -0.11361470821404349D}, {-3.652969378610491E-4D, -0.004121614685876285D, 0.6935114048608589D}};

        for(int i = 1; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                Color myColor = img.getColor(j, i);
                double temp = (float) myColor.red();
                current[0][0] = temp;
                temp = (double)myColor.green();
                current[1][0] = temp;
                temp = (double)myColor.blue();
                current[2][0] = temp;
                System.out.print(current[0][0] + "," + current[1][0] + "," + current[2][0]);
                current = multiplyMatrices(toLMS, current);
                if (type == 1) {
                    current = multiplyMatrices(protanope, current);
                } else if (type == 2) {
                    current = multiplyMatrices(deutranope, current);
                } else if (type == 3) {
                    current = multiplyMatrices(tritanope, current);
                } else {
                    System.out.print("Invalid Colorblindness Parameter");
                }

                current = multiplyMatrices(toRGB, current);
                if (current[0][0] < 0.0D) {
                    current[0][0] = 0.0D;
                } else if (current[0][0] > 252.0D) {
                    current[0][0] = 252.0D;
                }

                if (current[1][0] < 0.0D) {
                    current[1][0] = 0.0D;
                } else if (current[1][0] > 252.0D) {
                    current[1][0] = 252.0D;
                }

                if (current[2][0] < 0.0D) {
                    current[2][0] = 0.0D;
                } else if (current[2][0] > 252.0D) {
                    current[2][0] = 252.0D;
                }

                img.setPixel(j,i,Color.rgb((int)current[0][0], (int)current[1][0], (int)current[2][0]));
            }
        }
        return img;
    }

/*
    public static void rgbContrast(Bitmap img, double factor) {
        int width = img.getWidth();
        int height = img.getHeight();

        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                Color myColor = img.getColor(j, i);
                double temp = (double)myColor.getRed();
                double newRed = (1.0D - temp / 252.0D) * factor + temp / 252.0D;
                temp = (double)myColor.getGreen();
                double newGreen = (1.0D - temp / 252.0D) * factor + temp / 252.0D;
                temp = (double)myColor.getBlue();
                double newBlue;
                if (newRed > newGreen) {
                    newBlue = temp / 252.0D - temp / 252.0D * factor;
                } else {
                    newBlue = (1.0D - temp / 252.0D) * factor + temp / 252.0D;
                }

                Color newColor = new Color((int)(newRed * 252.0D), (int)(newGreen * 252.0D), (int)(newBlue * 252.0D));
                int rgb = newColor.getRGB();
                img.setRGB(j, i, rgb);
            }
        }

        saveImage(savePath, img);
        System.out.print("Saved Successfully");
    }
*/

}
