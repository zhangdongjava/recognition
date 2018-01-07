package com.zzz.interest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by zhang on 2018/1/7.
 */
public class Test {

    public static void main(String[] args) throws IOException, AWTException {
        FontUtil fontUtil = new FontUtil("H:\\workspaces\\html\\wudi\\resources\\fonts2.properties");
        BufferedImage image = ImageIO.read(new File("C:\\Users\\zhang\\Desktop\\222.png"));
        int xNum = 87;
        int yNum = 30;
        int step = 1;
        int fondIndex = 0;
        int az = 9;
        int ay = 1;
        //Properties properties = new Properties();
        for (int y = 0; y < yNum; y += step) {
            for (int x = 0; x < xNum; x += step) {
                BufferedImage bufferedImage = image.getSubimage(x * 16 + az, y * 16 + ay, 16, 16);
                String code = fondImageToCode(bufferedImage);
                String font = fontUtil.getFont(code);
                if(font == null){
                    System.out.println(fondIndex+"--->"+code);
                }else{
                    System.out.println(font);
                }
                fondIndex++;
            }
        }


    }

    private static String fondImageToCode(BufferedImage image) {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (image.getRGB(x, y) == -16777216) {
                    sb.append("1");
                } else {
                    sb.append("0");
                }
            }
        }
        return twoTo64(sb.toString());
    }

    private static String twoTo64(String str) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i += 4) {
            builder.append(binaryTo64(str.substring(i, i + 4)));

        }
        return builder.toString();
    }

    private static char binaryTo64(String str) {
        char[] chars = str.toCharArray();
        int i = 0;
        for (int j = 0; j < chars.length; j++) {
            int aChar = chars[j] - 48;
            i += aChar * Math.pow(2, chars.length - 1 - j);
        }
        return (char) (i + 97);
    }




}
