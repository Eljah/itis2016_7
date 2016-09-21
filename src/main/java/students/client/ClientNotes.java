package students.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;


public class ClientNotes implements EntryPoint {

    private VerticalPanel mainPanel = new VerticalPanel();
    private FlexTable notesFlexTable = new FlexTable();
    private HorizontalPanel addPanel = new HorizontalPanel();
    private TextArea newSymbolTextBox = new TextArea();
    private HTML widget = new HTML();


    private Button addNoteButton = new Button("Add");
    private Label lastUpdatedLabel = new Label();
    private ArrayList<String> NotesNames = new ArrayList<String>();


    private VerticalPanel loginPanel = new VerticalPanel();

    public void onModuleLoad() {

        // Create table for Note data.
        notesFlexTable.setText(0, 0, "Note");

        // set button's style
        //addNoteButton.addStyleName("addButton");

        // Assemble Add Note panel.
        addPanel.add(newSymbolTextBox);
        addPanel.add(addNoteButton);

        // Assemble Main panel.
        mainPanel.add(notesFlexTable);
        mainPanel.add(addPanel);
        mainPanel.add(lastUpdatedLabel);
        mainPanel.add(widget);

        // Associate the Main panel with the HTML host page.
        RootPanel.get().add(mainPanel);

        // Move cursor focus to the input box.
        newSymbolTextBox.setWidth("300px");
        newSymbolTextBox.setFocus(true);


        // Listen for mouse events on the Add button.
        addNoteButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                Window.alert("Button Clicked");
                String postUrl = "http://localhost:9966/studentsApp/note";
                String requestData = "text="+newSymbolTextBox.getValue();
                RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, postUrl+"?"+requestData);
                builder.setHeader("Accept-Charset","utf-8");
                try {
                    //builder.sendRequest(requestData.toString(), new RequestCallback() {
                    builder.sendRequest(requestData, new RequestCallback() {
                    public void onError(Request request, Throwable e) {
                            Window.alert(e.getMessage());
                        }

                        public void onResponseReceived(Request request, Response response) {
                            if (200 == response.getStatusCode()) {
                                Window.alert(response.getText());
                                lastUpdatedLabel.setText("Comments are listed below:");
                                widget.setHTML(response.getText());
                            } else {
                                Window.alert("Received HTTP status code other than 200 : " + response.getStatusText());
                            }
                        }
                    });
                } catch (RequestException e) {
                    // Couldn't connect to server
                    Window.alert(e.getMessage());
                }
            }
        });

        // Listen for keyboard events in the input box.
        newSymbolTextBox.addKeyPressHandler(new KeyPressHandler() {
            public void onKeyPress(KeyPressEvent event) {
                if (event.getCharCode() == KeyCodes.KEY_ENTER) {
                    Window.alert("Key Pressed");
                }
            }
        });
    }
}