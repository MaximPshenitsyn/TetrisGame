module com.example.pshenitsyn_maxim_203_hw5_tetris {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pshenitsyn_maxim_203_hw5_tetris to javafx.fxml;
    exports com.example.pshenitsyn_maxim_203_hw5_tetris;
    exports engine;
}