package com.zzz.interest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by zhang on 2018/1/7.
 */
public class BuildFontLib {

    private Map<String, String> library = new HashMap<>();

    private BuildFontLib() throws IOException {
        library.put("1001100110021<0213041088905070201050108813041<021002100110010000", "文");
    }


    public String getFont(String code) {
        return library.get(code);
    }

    public static void main(String[] args) throws IOException, AWTException {
        FontUtil fontUtil = new FontUtil("H:\\workspaces\\html\\wudi\\resources\\fonts2.properties");
        BufferedImage image = ImageIO.read(new File("C:\\Users\\zhang\\Desktop\\222.png"));
        Point first = fontUtil.findFirstPoint(image);
        int step = 16;
        int fondIndex = 0;
        BufferedReader reader = new BufferedReader(new FileReader("H:\\workspaces\\html\\wudi\\resources\\font.txt"));
        String line = reader.readLine();
        for (int y = first.y; y < image.getHeight()-step; y += step) {
            for (int x = first.x; x < image.getWidth()-step; x += step) {
                BufferedImage bufferedImage = image.getSubimage(x, y, 16, 16);
                String code = FontUtil.fondImageToCode(bufferedImage);
                if(fondIndex>=line.length()){
                    break;
                }

                fontUtil.setLibrary(code,line.charAt(fondIndex)+"");
                fondIndex ++;
            }
        }

        fontUtil.save();
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
