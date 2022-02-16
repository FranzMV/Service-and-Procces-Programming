module com.fran.bingofxclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.fran.bingofxclient to javafx.fxml;
    exports com.fran.bingofxclient;
}