Steps to run:

1. Unzip state-server.tar.gz file and navigate inside "state-server" folder 
2. make. This compiles the java files to bin folder
3. ./state-server.sh - This starts the server on port 8080.


Project Findings and approach:

I have approached this problem as finding "Point inside polygon problem". All USA states are on negative longitude and positive latitude 
which helps me to simplify this problem. 

I observed that my implementation returns multiple states for some of the border points. This happens because given points are of arbitary precision 
For example: curl -d "longitude=-90.305448&latitude=35.000788" http://localhost:8080/ returns [Mississippi, Tennessee, Arkansas]
Also, Mississippi and Tennessee share same longitude and latitude in the above case as per given input.




