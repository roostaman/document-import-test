package com.pm.test;

import com.pm.util.RequestSender;
import org.testng.annotations.Test;

public class PaymentServiceTest {

    @Test
    public void testSendModifiedFile_updateToNumericValue() {
        RequestSender.sendRequestAndValidate("1");
    }

    @Test
    public void testSendModifiedFile_updateToEmptyValue() {
        RequestSender.sendRequestAndValidate("");
    }

    @Test
    public void testSendModifiedFile_updateToSymbolValue() {
        RequestSender.sendRequestAndValidate("$");
    }

    @Test
    public void testSendModifiedFile_updateToTextValue() {
        RequestSender.sendRequestAndValidate("DUMMY TEXT");
    }

    @Test
    public void testSendModifiedFile_updateToLongTextValue() {
        RequestSender.sendRequestAndValidate("Sentence continues with some random filler text to push beyond 200 characters: Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
    }

}
