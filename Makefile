all: clean
	mkdir bin
	javac -d bin src/HTTPServer.java src/UnitedStates.java
	cp src/states.json bin

clean:
	rm -rf bin
