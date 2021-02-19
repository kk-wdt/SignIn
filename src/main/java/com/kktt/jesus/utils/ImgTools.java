package com.kktt.jesus.utils;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * 图片处理工具类
 * @author hongqinggong
 * @since 2020年11月5日
 */
public class ImgTools {
    private final static int TARGET_WIDTH = 1000;
    private final static int TARGET_HEIGHT = 500;
    private final static int MAX_WIDTH = 10000;

    public static void formatImage(String srcFile,String disFile) throws Exception {
        BufferedImage bufImg = ImageIO.read(new File(srcFile));
        BufferedImage img = reSize(bufImg);
        img = reMark(img);
        img = rotateImg(img,0.2f);
        OutputStream out = new FileOutputStream(disFile);
        ImageIO.write(img,"jpg",out);
        out.flush();
        out.close();
    }

    public static void scaleImage(String netPath,String disFile)throws Exception{
        SslUtils.ignoreSsl();
        BufferedImage bufImg = ImageIO.read(new URL(netPath));
        BufferedImage img = reSize(bufImg);
        img = reMark(img);
        img = rotateImg(img,0.2f);
        OutputStream out = new FileOutputStream(disFile);
        ImageIO.write(img,"jpg",out);
        out.flush();
        out.close();
    }

    private static BufferedImage reMark(BufferedImage srcImg){
        Graphics2D graphics2D = srcImg.createGraphics();
        graphics2D.setColor(new Color(50,50,50));
        int w = srcImg.getWidth();
        int h = srcImg.getHeight();
        graphics2D.drawRect(w/2,h/2,2,2);
        graphics2D.dispose();
        return srcImg;
    }

    private static BufferedImage rotateImg(BufferedImage bufferedImage,float angel){
        if (bufferedImage == null) {
            return null;
        }
        if (angel < 0) {
            // 将负数角度，纠正为正数角度
            angel = angel + 360;
        }
        int imageWidth = bufferedImage.getWidth(null);
        int imageHeight = bufferedImage.getHeight(null);
        // 计算重新绘制图片的尺寸
        Rectangle rectangle = calculatorRotatedSize(new Rectangle(new Dimension(imageWidth, imageHeight)), angel);
        System.out.println(rectangle.getWidth()+"-"+rectangle.getHeight());
        // 获取原始图片的透明度
        int type = bufferedImage.getColorModel().getTransparency();
        BufferedImage newImage = null;
        newImage = new BufferedImage(rectangle.width, rectangle.height, type);

        Graphics g = newImage.getGraphics();
        g.setColor(new Color(255,255,255));
        g.fillRect(-10,-10,(int)rectangle.getWidth()+10,(int)rectangle.getHeight()+10);
        g.dispose();

        Graphics2D graphics = newImage.createGraphics();
        // 平移位置
        graphics.translate((rectangle.width - imageWidth) / 2, (rectangle.height - imageHeight) / 2);
        // 旋转角度
        graphics.rotate(Math.toRadians(angel), imageWidth / 2, imageHeight / 2);

        // 绘图
        graphics.drawImage(bufferedImage, null, null);
        graphics.dispose();


        return newImage;
    }

    private static Rectangle calculatorRotatedSize(Rectangle src, float angel) {
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }
        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new Rectangle(new Dimension(des_width, des_height));
    }

    private static BufferedImage reSize(BufferedImage bufImg)  {
//        BufferedImage bufImg = ImageIO.read(new File(picPath));
        int w = bufImg.getWidth();
        int h = bufImg.getHeight();
        float scale = getScale(w,h);
        if(w*scale > MAX_WIDTH || h*scale > MAX_WIDTH){
            throw new IllegalArgumentException("图片大小异常");
        }
        if(scale == 1){
//            System.out.println(w+" "+h+" -- "+scale+" -- "+w*scale+" "+h*scale);
//            File file = new File(picPath);
            return bufImg;
        }

        int newW = (int)(w*scale);
        int newH = (int)(h*scale);

        Image img = bufImg.getScaledInstance(newW,newH, Image.SCALE_DEFAULT);
        BufferedImage image = new BufferedImage(newW,newH,BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(new Color(255,255,255));
        graphics.drawRect(0, 0, newW, newH);
        graphics.drawImage(img,0,0,null);
        graphics.dispose();

        return image;
//        OutputStream out = new FileOutputStream(outPath);
//        ImageIO.write(image,"png",out);
//        out.close();


    }

    private static float getScale(int w,int h){
        boolean isWlong = w > h;
        int longSize = w > h ? w:h;
        int shortSize = w > h ? h:w;
        if(longSize >= TARGET_WIDTH && longSize <= MAX_WIDTH && shortSize >= TARGET_HEIGHT){
            return 1;
        }

        if(isWlong){
            float scaleW = TARGET_WIDTH*1.0f/w;
            float scaleH = TARGET_HEIGHT*1.0f/h;
            return scaleW > scaleH ? scaleW : scaleH;
        }else{
            float scaleW = TARGET_HEIGHT*1.0f/w;
            float scaleH = TARGET_WIDTH*1.0f/h;
            return scaleW > scaleH ? scaleW : scaleH;
        }
    }
}
