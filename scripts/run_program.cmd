cd ../src
javac code/tools/*.java
javac code/driver/*.java
java -Xms8g -Xmx15g code.driver.GUI
cd ../scripts
