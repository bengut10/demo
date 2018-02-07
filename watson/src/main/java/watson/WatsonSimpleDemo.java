/*
 * Copyright 2017 IBM Corp. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package watson;

import com.ibm.watson.developer_cloud.conversation.v1.model.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import watson.services.ConversationService;
import watson.services.DialogService;

/**
 * Example of how to call the {@link ConversationService#message(String, MessageRequest)} method synchronous,
 * asynchronous, and using react.
 *
 * @version v1-experimental
 */
public class WatsonSimpleDemo extends Application {

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        DialogService dialogService = new DialogService();

        Button startButton = new Button("Start the demo!");
        startButton.setOnAction(action -> dialogService.startDialogService());
        primaryStage.setScene(new Scene(new BorderPane(startButton)));
        primaryStage.show();

    }
}





