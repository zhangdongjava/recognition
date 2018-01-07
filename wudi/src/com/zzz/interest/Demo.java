package com.zzz.interest;

import java.awt.*;
import java.io.IOException;

/**
 * Created by zhang on 2018/1/7.
 */
public class Demo {
    public static void main(String[] args) throws IOException, AWTException {
//        System.out.println(fondImageToCode(ImageIO.read(new File("C:\\Users\\zhang\\Desktop\\zi.png"))));
       FontUtil fontUtil = new FontUtil("H:\\workspaces\\html\\wudi\\resources\\fonts2.properties");
        String str = fontUtil.raceFindFont(0,0,500,500,null);
        System.out.println(str);
    }


}
