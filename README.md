Rock the Net [![Build Status](http://148.251.5.105/job/Rock%20The%20Net/badge/icon)](http://gserver/job/Rock%20The%20Net/)

Developed by an outstanding team:
* Elias Frantar
* Samuel Schmidt
* Nikolaus Schrack
* Gary Ye

Check the documentation out in the doc/ directory. The CI server can be found under http://148.251.5.105

Also read the javadoc in the javadoc/ directory. 

This project uses ant is the build tool. 
The important tasks of the ant build script are listed here:

* run-gui starts the program with the graphical user interface
* compile compiles the program
* test starts the unit tests of the application; additionally a JaCoCo test report will be created as an .exec file. 


Known problems: If the JAVA_HOME is not set properly then this program will not work. 
