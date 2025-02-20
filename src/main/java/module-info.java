module com.gentrifiedapps.heatseekersimulatorjar {
    requires kotlin.stdlib;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.logging;
    requires com.opencsv;

    exports com.gentrifiedapps.heatseekersimulatorjar;
    exports com.gentrifiedapps.heatseekersimulatorjar.util;
    exports com.gentrifiedapps.heatseekersimulatorjar.drawers;
}