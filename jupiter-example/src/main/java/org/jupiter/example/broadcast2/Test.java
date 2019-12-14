package org.jupiter.example.broadcast2;

import javax.xml.stream.events.StartElement;

/**
 * @author Ray
 * @version 2019/12/12-10:11
 **/
public class Test {
    public static void main(String[] args) {
        try {
            int i = 1;

            System.out.println(i / 0);
        }catch (Exception ex){
            StackTraceElement[] stackTrace = ex.getStackTrace();

            for(StackTraceElement se : stackTrace){
                System.out.println(se.toString());
            }
        }
    }
}
