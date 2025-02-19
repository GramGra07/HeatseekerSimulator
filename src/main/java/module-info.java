module com.gentrifiedapps.heatseekersimulatorjar {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires com.opencsv;


    opens com.gentrifiedapps.heatseekersimulatorjar to javafx.fxml;
    exports com.gentrifiedapps.heatseekersimulatorjar;
}