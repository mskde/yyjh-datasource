package com.mdq.tools;

import org.junit.Test;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.*;

public class YYJHToolsTest {

    @Test
    public void parseStrToDate() throws ParseException {
        System.out.println(YYJHTools.parseStrToDate("2018-8-24 05:55:55",""));
    }

    @Test
    public void formatDateToStr() throws ParseException {
        System.out.println(YYJHTools.formatDateToStr(new Date(),null));
    }

    @Test
    public void formatURL() throws MalformedURLException {
        System.out.println(YYJHTools.formatURL("http://m.natee.cc"));
    }

    @Test
    public void isEmail() {
        System.out.println(YYJHTools.isEmail("304137776@qq.com"));
    }

    @Test
    public void filterInnerBlank() {
        System.out.println(YYJHTools.filterInnerBlank("zxc  z12v56avz    32zx1cz       "));
    }

    @Test
    public void get32UUID(){
        System.out.println(YYJHTools.get32UUID());
    }
}