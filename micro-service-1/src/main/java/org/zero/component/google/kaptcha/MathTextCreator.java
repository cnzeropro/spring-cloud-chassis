package org.zero.component.google.kaptcha;

import com.google.code.kaptcha.text.TextProducer;

import java.util.Random;

/**
 * @author zero
 * @since 2021/6/21
 */
public class MathTextCreator implements TextProducer {
    public static final int NUM_BOUND = 10;

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        int num1 = random.nextInt(NUM_BOUND);
        int num2 = random.nextInt(NUM_BOUND);
        int op = random.nextInt(4);

        if (op == 0) {
            sb.append(num1);
            sb.append("+");
            sb.append(num2);
        } else if (op == 1) {
            sb.append(num1);
            sb.append("*");
            sb.append(num2);
        } else if (op == 2) {
            if (num1 >= num2) {
                sb.append(num1);
                sb.append("-");
                sb.append(num2);
            } else {
                sb.append(num2);
                sb.append("-");
                sb.append(num1);
            }
        } else {
            if (num2 != 0 && num1 % num2 == 0) {
                sb.append(num1);
                sb.append("/");
                sb.append(num2);
            } else {
                sb.append(num1);
                sb.append("+");
                sb.append(num2);
            }
        }
        sb.append("=?");
        return sb.toString();
    }
}
