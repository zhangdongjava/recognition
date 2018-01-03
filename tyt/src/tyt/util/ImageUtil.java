package tyt.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhang on 2018/1/3.
 */
public class ImageUtil {

    private BufferedImage image;

    private Robot robot;

    private int bgx = 14;
    private int bgy1 = 56;
    private int bgy2 = 939;

    private Map<Point,Integer> rgbMap = new HashMap<Point, Integer>();

    public ImageUtil(String calssPath) throws IOException, AWTException {
        image = ImageIO.read(ImageUtil.class.getResourceAsStream(calssPath));
        robot = new Robot();
    }

    public int getWidht() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public int getRgb(int x, int y) {
        Point p = new Point(x, y);
        Integer i = rgbMap.get(p);
        if(i != null)return i;
        BufferedImage screenshot = robot.createScreenCapture(
                new Rectangle(x, y, 1, 1));
        BufferedImage grayImage = hdImage(screenshot);
        int r = grayImage.getRGB(0, 0);
        rgbMap.put(p,r);
        return r;
    }

    public BufferedImage hdImage(BufferedImage screenshot) {
//        BufferedImage grayImage = new BufferedImage(screenshot.getWidth(), screenshot.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
//        for(int i= 0 ; i < screenshot.getWidth() ; i++){
//            for(int j = 0 ; j < screenshot.getHeight(); j++){
//                int rgb1 = screenshot.getRGB(i, j);
//                grayImage.setRGB(i, j, rgb1);
//            }
//        }
        return screenshot;
    }

    public Point findImg(int x, int y, int w, int h) {
        BufferedImage screenshot = robot.createScreenCapture(
                new Rectangle(x, y, w, h));
        int xc = w - getWidht();
        int yc = h - getHeight();
        for (int xi = 0; xi < xc; xi++) {
            for (int yi = 0; yi < yc; yi++) {
                boolean b = equals(screenshot.getSubimage(xi, yi, getWidht(), getHeight()), 5);
                if (b) {
                    return new Point(x + xi, y + yi);
                }

            }

        }
        return null;
    }

    /**
     * 从上到下从左到右 查找第一次不是rgb出现的点
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @param rgb
     * @return
     */
    public Point findFirstNotBgColorUpLeftToDownRight(int x, int y, int w, int h) {
        BufferedImage screenshot = robot.createScreenCapture(
                new Rectangle(x, y, w, h));
        BufferedImage grayImage = hdImage(screenshot);
        for (int yi = 0; yi < grayImage.getHeight(); yi++) {
            for (int xi = 0; xi < grayImage.getWidth(); xi++) {

                int trgb = grayImage.getRGB(xi, yi);
                boolean bgr = false;
                for (int i = bgy1; i < bgy2; i+=30) {
                     int rgb = getRgb(bgx,i);
                    bgr = bgr || rgbPy(trgb, rgb, 10);
                }
                //System.out.println("比较完毕-->"+bgr);
                boolean notSelf = rgbPy(trgb, image.getRGB(0, 0), 10);
                if (!notSelf && !bgr) {
                    System.out.println(trgb + "-->");
                    rgbMap.clear();
                    return new Point(x + xi, y + yi);
                }

            }

        }
        rgbMap.clear();
        return null;
    }

    /**
     * 从上到下从左到右 查找第一次是rgb出现的点
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @param rgb
     * @return
     */
    public Point findFirstColorDownLeftToUpRight(int x, int y, int w, int h, int rgb) throws IOException {
        BufferedImage screenshot = robot.createScreenCapture(
                new Rectangle(x, y, w, h));
        saveImage(screenshot);
        BufferedImage grayImage = hdImage(screenshot);
        for (int yi = grayImage.getHeight() - 1; yi >= 0; yi--) {
            for (int xi = 0; xi < grayImage.getWidth(); xi++) {

                int trgb = grayImage.getRGB(xi, yi);
                boolean b = rgbPy(trgb, rgb, 2);
                if (b) {
                    System.out.println(trgb + "-->" + rgb);
                    return new Point(x + xi, y + yi);
                }

            }

        }
        return null;
    }

    public void saveImage(BufferedImage screenshot) throws IOException {
        ImageIO.write(screenshot, "png", new File("d:/a.png"));
    }

    public boolean equals(BufferedImage image, int py) {
        if (image.getHeight() != this.getHeight() || image.getWidth() != this.getWidht()) {
            return false;
        }
        for (int x = 0; x < getWidht(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (!rgbPy(this.image.getRGB(x, y), image.getRGB(x, y), py)) {
                    return false;
                }

            }

        }

        return true;
    }

    private boolean rgbPy(int rgb1, int rgb2, int py) {
        int r1 = (rgb1 & 0xff0000) >> 16;
        int g1 = (rgb1 & 0xff00) >> 8;
        int b1 = (rgb1 & 0xff);
        int r2 = (rgb2 & 0xff0000) >> 16;
        int g2 = (rgb2 & 0xff00) >> 8;
        int b2 = (rgb2 & 0xff);
        if (Math.abs(r1 - r2) > py) {
            return false;
        }
        if (Math.abs(g1 - g2) > py) {
            return false;
        }
        if (Math.abs(b1 - b2) > py) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws IOException, AWTException {

        ImageUtil image = new ImageUtil("/tyt/wo.png");

        Robot robot = new Robot();
        while (true){
            try {
                run(image,robot);
            } catch (Exception e) {
                e.printStackTrace();
            }
            robot.mouseMove(275, 788);
            robot.mousePress(KeyEvent.BUTTON1_MASK);
            robot.delay(200);
            robot.mouseRelease(KeyEvent.BUTTON1_MASK);
            robot.delay(500);
        }

    }


    public static void run(ImageUtil image,Robot robot) throws IOException {
        int bgx = 30;
        int bgy = 450;
        while (true) {
            //int bgrgb = image.getRgb(bgx, bgy);
            robot.delay(1500);
            Point woPoint = image.findImg(20, 57, 500, 900);
            //背景色

            Point notBgPoint1 = image.findFirstNotBgColorUpLeftToDownRight(20, 300, 470, 400);
            int turgb = image.getRgb(notBgPoint1.x, notBgPoint1.y + 20);
            Point notBgPoint2 = null;

            notBgPoint2 = image.findFirstColorDownLeftToUpRight(Math.max(notBgPoint1.x - 30, 20), notBgPoint1.y, 40, 125, turgb);

            robot.mouseMove(woPoint.x + 15, woPoint.y + 10);
            Point mb = center(notBgPoint2, notBgPoint1);
            double jl = getJlcenter(woPoint, mb);
            robot.mouseMove(notBgPoint1.x, notBgPoint1.y);
            robot.delay(500);
            robot.mouseMove(notBgPoint2.x, notBgPoint2.y);
            robot.delay(500);
            System.out.println(jl);
            robot.mouseMove(bgx, bgy);
            robot.mousePress(KeyEvent.BUTTON1_MASK);
            int d = (int) (jl / 330 * 1000);
            System.out.println(d);
            robot.delay(d);
            robot.mouseRelease(KeyEvent.BUTTON1_MASK);
            //移动鼠标好取背景色
            robot.delay(100);
            robot.mouseMove(woPoint.x, woPoint.y);
        }
    }


    public static Point center(Point p, Point p2) {
        return new Point(p.x / 2 + p2.x / 2, p2.y / 2 + p.y / 2);
    }

    public static double getJlcenter(Point p, Point p2) {
        int x = (p.x - p2.x) * (p.x - p2.x) + (p.y - p2.y) * (p.y - p2.y);
        return (Math.sqrt(x));
    }
}
