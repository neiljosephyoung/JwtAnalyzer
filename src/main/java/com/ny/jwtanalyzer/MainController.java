package com.ny.jwtanalyzer;

import atlantafx.base.theme.Styles;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MainController {


    @FXML
    private TextArea jwtInputArea;
    @FXML
    private TextField secretInputField;
    @FXML
    private ComboBox<String> algorithmChoiceBox;
    @FXML
    private TextArea headerArea;
    @FXML
    private TextArea payloadArea;
    @FXML
    private Label statusLabel;

    @FXML
    public Label timeLabel;
    @FXML
    public Label cpuUsageLabel;
    @FXML
    public Label memoryUsageLabel;
    @FXML
    public Button decodeButton;
    @FXML
    public Button verifyButton;
    @FXML
    public Button clearButton;

    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final JsonMapper mapper = new JsonMapper();

    private final DecimalFormat df = new DecimalFormat("#0.00");
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @FXML
    private void initialize() {
        startClock();
        startSystemMonitor();
//        decodeButton.getStyleClass().add(Styles.SUCCESS);
//        verifyButton.getStyleClass().add(Styles.ACCENT);
//        clearButton.getStyleClass().add(Styles.DANGER);
        algorithmChoiceBox.setValue("HS256");
        statusLabel.setText("Ready to decode JWT");

        // ctrl + enter to action decode
        jwtInputArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.ENTER) {
                onDecodeClick();
                event.consume();
            }
        });

        secretInputField.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.isControlDown() && keyEvent.getCode().equals(KeyCode.ENTER)) {
                onVerifyClick();
                keyEvent.consume();
            }
        });
    }

    private void startSystemMonitor() {
        scheduler.scheduleAtFixedRate(() -> {
            double cpuLoad;
            String memoryInUse;
            try {
                cpuLoad = OshiHelper.getCpuLoad();
                double finalCpuLoad = cpuLoad;
                Platform.runLater(() -> cpuUsageLabel.setText("CPU: " + df.format(finalCpuLoad) + " %"));

                memoryInUse = OshiHelper.getMemoryUsage();
                String finalMemInUse = memoryInUse;
                Platform.runLater(() -> memoryUsageLabel.setText("MEM: " + finalMemInUse));

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // refresh every 2 seconds
        }, 0, 2, TimeUnit.SECONDS);
    }

    private void startClock() {
        Timeline clock = new Timeline(
                new KeyFrame(Duration.ZERO, e ->
                        timeLabel.setText(LocalTime.now().format(timeFormat))
                ),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    @FXML
    private void onDecodeClick() {
        String token = jwtInputArea.getText().trim();
        if (token.isEmpty()) {
            statusLabel.setText("Please enter a JWT token.");
            return;
        }

        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            JsonNode header = mapper.readTree(decodeB64(decodedJWT.getHeader()));
            JsonNode payload = mapper.readTree(decodeB64(decodedJWT.getPayload()));

            headerArea.setText(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(header));
            payloadArea.setText(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload));
            statusLabel.setText("Token decoded successfully!");

        } catch (Exception e) {
            statusLabel.setText("Failed to decode JWT: " + e.getMessage());
            headerArea.clear();
            payloadArea.clear();
        }
    }

    private static String decodeB64(String b64) {
        byte[] decoded = Base64.getUrlDecoder().decode(b64);
        return new String(decoded, StandardCharsets.UTF_8);
    }

    @FXML
    private void onVerifyClick() {
        String token = jwtInputArea.getText().trim();
        String secret = secretInputField.getText().trim();
        String targetAlgo = algorithmChoiceBox.getValue();

        if (token.isEmpty()) {
            statusLabel.setText("Please enter a JWT token.");
            return;
        }

        if (secret.isEmpty()) {
            statusLabel.setText("Please enter the secret or public key.");
            return;
        }

        try {
            Algorithm algorithm = switch (targetAlgo) {
                case "HS256" -> Algorithm.HMAC256(secret.trim());
                default -> throw new UnsupportedOperationException("Unsupported Algo type");
            };

            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token.trim());

            log.info("Signature valid");
            statusLabel.setText("Signature valid");
        } catch (UnsupportedOperationException | JWTVerificationException e) {
            log.error("Signature invalid: {}", e.getMessage());
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onClearClick() {
        jwtInputArea.clear();
        headerArea.clear();
        payloadArea.clear();
        statusLabel.setText("Cleared.");
    }
}

