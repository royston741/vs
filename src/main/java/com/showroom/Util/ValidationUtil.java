package com.showroom.Util;

import lombok.extern.slf4j.Slf4j;


import java.util.InputMismatchException;
import java.util.Scanner;

@Slf4j
public class ValidationUtil {
//    private static final Logger log = Logger.getLogger(ValidationUtil.class);
    private static Scanner sc = new Scanner(System.in);

    public static String inputString(String entryFor) {
        String str = "";
        sc = new Scanner(System.in);
        while (str.length() == 0) {
            log.info("Enter " + entryFor + " :");
            str = sc.nextLine();
            if (str.length() == 0) {
                log.info("Please enter valid " + entryFor + " !!!!");
            }
        }


        return str;
    }

    public static String inputString(String entryFor, int length) {
        String str = "";
        while (str.length() != length) {

            try {
                log.info("Enter " + entryFor + " :");
                str = sc.next();
            } catch (InputMismatchException e) {
                log.error("Please enter valid " + entryFor + " !!!!");
                continue;
            }

            if (str.length() != length) {
                log.info("Please enter valid " + entryFor + " !!!!");
            }
        }
        return str;
    }

    public static int inputNum(String entryFor) {
        int num = 0;
        while (num == 0) {
            try {
                log.info("Enter " + entryFor + " :");
                num = sc.nextInt();
            } catch (InputMismatchException e) {
                sc.next();
                log.error("Please enter valid " + entryFor + " !!!!");
                continue;
            }
        }
        return num;
    }

}
