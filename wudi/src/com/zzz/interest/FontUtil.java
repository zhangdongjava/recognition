package com.zzz.interest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhang on 2018/1/7.
 */
public class FontUtil {

    private Robot robot;

    private Map<String, String> library = new ConcurrentHashMap<>();
    private String fontLibraryPath;

    FontUtil(String fontLibraryPath) throws IOException, AWTException {
        this.fontLibraryPath = fontLibraryPath;
        robot = new Robot();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fontLibraryPath)));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("#")) {
                System.out.println(line);
                continue;
            }
            String[] lineDatas = line.split("=");
            if (lineDatas[1].startsWith("\\u")) {
                lineDatas[1] = decodeUnicode(lineDatas[1]);
            }
            library.put(lineDatas[0], lineDatas[1]);
        }
    }

    public void setLibrary(String code, String font) {
        if (!exists(code))
            library.put(code, font);
    }

    public boolean exists(String code) {
        return library.containsKey(code);
    }

    public void save() throws IOException {
        FileOutputStream outputStream = new FileOutputStream(fontLibraryPath);
        for (Map.Entry<String, String> entry : library.entrySet()) {
            outputStream.write(entry.getKey().getBytes());
            outputStream.write("=".getBytes());
            outputStream.write(entry.getValue().getBytes());
            outputStream.write("\r\n".getBytes());
        }
        outputStream.flush();
        outputStream.close();
    }

    public String raceFindFont(int x, int y, int w, int h, int[]... rgb) {
        StringBuilder builder = new StringBuilder();
        //拷贝屏幕到一个BufferedImage对象screenshot
        BufferedImage screenshot = (robot).createScreenCapture(
                new Rectangle(x, y, w, h));
        return imageFindFont(screenshot);
    }


    public String imageFindFont(BufferedImage image) {
        StringBuilder builder = new StringBuilder();
        int size = 16;
        int w = image.getWidth();
        int h = image.getHeight();
        Point point = findFirstPoint(image);
        for (int y = point.y; y + size < h; y += size) {
            for (int x = point.x; x + size < w; x++) {
                BufferedImage subImage = image.getSubimage(x, y, size, size);
                String code = fondImageToCode(subImage);
                String font = getFont(code);
                if (font != null) {
                    x += size - 1;
                    builder.append(font);
                }
            }

        }
        return builder.toString();
    }


    public Point findFirstPoint(BufferedImage image) {
        int size = 16;
        int w = image.getWidth();
        int h = image.getHeight();
        int heightStep = 1;
        for (int y = 0; y + size < h; y += heightStep) {
            for (int x = 0; x + size < w; x++) {
                BufferedImage subImage = image.getSubimage(x, y, size, size);
                String code = fondImageToCode(subImage);
                String font = getFont(code);
                if (font != null) {
                    return new Point(x, y);
                }
            }

        }
        return null;
    }


    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }

    public String getFont(String code) {
        return library.get(code);
    }


    public static String fondImageToCode(BufferedImage image) {
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
