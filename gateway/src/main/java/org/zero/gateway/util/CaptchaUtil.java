package org.zero.gateway.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * 验证码工具类
 *
 * @author zero (cnzeropro@qq.com)
 * @date 2021/2/16
 */
@Slf4j
@UtilityClass
public class CaptchaUtil {
    /**
     * 图片大小
     */
    public static final int IMG_WIDTH = 150;
    public static final int IMG_HEIGHT = 30;
    /**
     * 图片格式
     */
    private static final String IMG_FORMAT = "PNG";
    /**
     * 干扰线数量
     */
    public static final int LINE_COUNT = 15;
    /**
     * 麻点数量
     */
    public static final int POINT_COUNT = 150;
    /**
     * 生成运算用的随机数边界
     */
    public static final int NUM_BOUND = 50;
    /**
     * base64图片前缀
     */
    private static final String BASE64_PREFIX = "data:image/%s;base64,";
    /**
     * base64编码器
     */
    public static final Base64.Encoder base64Encoder = Base64.getEncoder();
    /**
     * 随机数生成器，默认采用强随机数生成器，除非该 jvm 没有该 SecureRandom 实现（基本不可能）
     */
    private static final Random random;

    static {
        Random tempRandom;
        try {
            tempRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            log.warn("No Strong SecureRandom", e);
            tempRandom = new Random();
        }
        random = tempRandom;
    }

    @SneakyThrows
    public static Captcha math() {
        Captcha captcha = math(IMG_WIDTH, IMG_HEIGHT, LINE_COUNT, POINT_COUNT, NUM_BOUND);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(captcha.bufferedImage, IMG_FORMAT, outputStream);
        String encoded = base64Encoder.encodeToString(outputStream.toByteArray());
        String base64Prefix = String.format(BASE64_PREFIX, IMG_FORMAT.toLowerCase());
        captcha.setImgBase64(base64Prefix + encoded);
        return captcha;
    }

    public static String math(OutputStream outputStream) {
        return math(IMG_FORMAT, outputStream);
    }

    @SneakyThrows
    public static String math(String format, OutputStream outputStream) {
        Captcha captcha = math(IMG_WIDTH, IMG_HEIGHT, LINE_COUNT, POINT_COUNT, NUM_BOUND);
        ImageIO.write(captcha.bufferedImage, format, outputStream);
        return captcha.code;
    }

    private static Captcha math(int width, int height, int line, int point, int bound) {
        // 生成随机算术式
        int num1 = random.nextInt(bound) + 1;
        int num2 = random.nextInt(bound) + 1;
        int result;
        String[] strings = new String[4];
        strings[0] = String.valueOf(num1);
        strings[2] = String.valueOf(num2);
        strings[3] = " = ";
        if (num1 <= num2) {
            strings[1] = "+";
            result = num1 + num2;
        } else {
            strings[1] = "-";
            result = num1 - num2;
        }

        // 生成缓冲图片
        // 创建图像缓冲区
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 通过缓冲区创建一个画布
        Graphics graphics = bufferedImage.getGraphics();
        // 为画布创建背景颜色
        graphics.setColor(Color.white);
        // 填充指定的矩形
        graphics.fillRect(0, 0, width, height);
        // 设置字体
        graphics.setFont(new Font("微软雅黑", Font.BOLD + Font.ITALIC, height / 2 + 5));
        // 绘制验证码字符串
        for (int i = 0; i < strings.length; i++) {
            graphics.setColor(getRandomColor(2, 22, 222));
            graphics.drawString(strings[i], (i + 1) * width / 6, height / 2 + 5);
        }
        // 生成干扰线
        for (int i = 1; i <= line; i++) {
            graphics.setColor(getRandomColor(255, 255, 255));
            graphics.drawLine(random.nextInt(width), random.nextInt(height), random.nextInt(width), random.nextInt(height));
        }
        // 随机麻点干扰
        for (int i = 1; i <= point; i++) {
            graphics.setColor(getRandomColor(230, 240, 250));
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            // 画点
            graphics.drawLine(x, y, x, y);
        }
        // 关闭资源
        graphics.dispose();

        return Captcha.builder().bufferedImage(bufferedImage).code(String.valueOf(result)).build();
    }

    /**
     * 生成随机颜色
     */
    private static Color getRandomColor(int r, int g, int b) {
        // if (r > 255) {
        //     r = 255;
        // }
        // if (g > 255) {
        //     g = 255;
        // }
        // if (b > 255) {
        //     b = 255;
        // }
        return new Color(random.nextInt(r), random.nextInt(g), random.nextInt(b));
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Captcha {
        private BufferedImage bufferedImage;
        private String imgBase64;
        private String code;
    }
}
