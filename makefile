all: clean compile

compile:
	javac DSCoinPackage/*.java
    javac HelperClasses/*.java
	javac DriverCode.java
clean:
	find . -name '*.class' -exec rm -f {} \;