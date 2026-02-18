package com.example.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

public class Handler implements RequestHandler<SQSEvent, Void> {

    private static final String TOPIC_ARN =
            System.getenv("SNS_TOPIC_ARN") != null
                    ? System.getenv("SNS_TOPIC_ARN")
                    : "arn:aws:sns:ap-south-1:536816796922:VibSqsApp-UploadsNotificationTopic";

    private static final SnsClient snsClient = SnsClient.create();

    @Override
    public Void handleRequest(SQSEvent event, Context context) {

        for (SQSEvent.SQSMessage message : event.getRecords()) {
            String shortMessage = String.format(
                    "New file uploaded\nBucket: %s\nKey: %s",
                    "vaibhav-mahajan-infoapp-bucket",
                    "image"
            );
            PublishRequest request = PublishRequest.builder()
                    .topicArn(TOPIC_ARN)
                    .message(shortMessage)
                    .build();

            snsClient.publish(request);

            context.getLogger().log("Published message to SNS\n");
        }

        return null;
    }
}

