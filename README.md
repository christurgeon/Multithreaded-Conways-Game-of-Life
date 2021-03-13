# Multithreading Application for Conway's Game of Life

## Overview
This version of Conway's Game of Life handles boards up to 30,000 x 30,000 grids. For boards in which the number of cells is larger than the number of pixels, the program utilizes a packing algorithm to pack multiple cells into a pixel, and that pixel is alive if more than half of the packed pixels within it are alive. 
* Threads: Use the menu bar to select the number of threads to use
* Board: Generate a random board of a specific size or load in your own

## Development Information
* Operating System: Windows 10
* Java Version: 1.8.0_201
* Script File: Windows Command Prompt (.cmd)

## How to Run
* If in Windows, double click the ```run_program.cmd``` file.
* If not, then open up a shell of choice within the ```src``` folder, compile both the ```/src/tools``` and ```/src/driver``` folder with the respective commands and then run: 
```
javac code/tools/*.java
javac code/driver/*.java
java -Xms8g -Xmx15g code.driver.GUI
```
The ```-Xms8g and -Xmx15g``` allow the JVM to utilize more of your computer's RAM in order to handle massive boards.
