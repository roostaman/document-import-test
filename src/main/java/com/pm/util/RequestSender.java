package com.pm.util;

import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class RequestSender {

    //    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceTest.class);
    private static final Path DATA_FILE_PATH = Path.of(GetConfig.get("DATA_FILE_PATH"));
    private static final Path UPDATED_DATA_FILE_PATH = Path.of(GetConfig.get("UPDATED_DATA_FILE_PATH"));

    private static final String ENDPOINT = GetConfig.get("ENDPOINT");
    private static final String ACCESS_TOKEN = GetConfig.get("ACCESS_TOKEN");


    public static void sendRequestAndValidate(String theValue) {
        SoftAssert softAssert = new SoftAssert();

        // read original file
        List<String> lines = null;
        try {
            lines = FileUtil.readLines(DATA_FILE_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // iterate over every line
        for (int i = 0; i < lines.size(); i++) {
            List<String> modified = new ArrayList<>(lines);
            modified.set(i, theValue);  // replace line i with new value

            String requestBody = String.join("\n", modified);

            // write modified content to temporary .txt file
            try {
                Files.writeString(
                        UPDATED_DATA_FILE_PATH,
                        requestBody,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // attach modified file content to report
            Allure.addAttachment(String.format("Modified file (line-%d)", i), requestBody);

            // log for debugging
            System.out.println("**** Iteration " + i);
            System.out.println("**** Request Body:\n" + requestBody + "\n");
//            logger.info("Iteration: {}", i);
//            logger.info("Request body: {}", requestBody);

            // send request with updated file
            Response response = RestAssured
                    .given()
                    .header("Authorization", "Bearer " + ACCESS_TOKEN)
                    .multiPart("files", UPDATED_DATA_FILE_PATH.toFile())
                    .when()
                    .post(ENDPOINT);

            // attach response to report
            Allure.addAttachment(
                    String.format("Response (line-%d)", i),
                    String.format("%s\nStatus code: %s", response.asPrettyString(), response.statusCode())
            );

            // log for debugging
            System.out.println("Response:\n" + response.asPrettyString());
            System.out.println("Status code: " + response.statusCode());
            System.out.println("========================================\n");

            // validate response code
            softAssert.assertNotEquals(500, response.statusCode());
        }
        // collect and report all
        softAssert.assertAll();
    }

}
