#!/bin/bash

# 創建輸出目錄
mkdir -p out/main/resources

# 編譯 Java 文件
javac -d out -cp .:lib/javafx-sdk-17.0.15/lib/javafx.controls.jar:lib/javafx-sdk-17.0.15/lib/javafx.fxml.jar:lib/javafx-sdk-17.0.15/lib/javafx.base.jar:lib/javafx-sdk-17.0.15/lib/javafx.graphics.jar src/main/*.java

# 複製資源文件
cp -r src/main/resources/* out/main/resources/

# 運行應用程式
java --module-path lib/javafx-sdk-17.0.15/lib --add-modules javafx.controls,javafx.fxml -cp out main.Main