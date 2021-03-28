# Maps 3 + 4 project! #


###Dijkstra and REPL code

REPL and commands structure: Benjamin Goff\
Dijkstra and graph classes: Suyash Kothari

###Division of Labor
Initially, Suyash worked on combining our codebases and Benjamin worked on setting up our front-end files and canvas. Ever since then, we have coded synchronously together, from the back-end handlers to the front-end route and map rendering, scrolling, panning, zooming. 


###Known bugs
Sometimes there is a problem with our backend Dijkstra algorithm (it seems to take a long time for very long routes, but this is purely because of the back-end and not a problem with our Maps 3+4 code). The REPL commands all work and the GUI commands all work. 

###Design details
Design details specific to your code, including how you fit each of the prior project’s codebases together

To use Suyash's graph classes with Benjamin's REPL and code base, we had Suyash's graph classes implement interfaces that already existed in Benjamin's code. For example, we made Suyash's MapNode class implement Benjamin's HasCoordinates interface, allowing it to be used in Benjamin's classes without too much editing.

We organized our classes into packages based on what they are used for. 

<ins>driver package</ins>  
Stores Main and SparkHandlers (where we have kept all of our front-end handlers, including RouteHandler for finding routes to pass to the front-end, UserHandler to select all records of a user to return to the front-end, WayHandler to return to the front-end ways within a bounded box and CheckinHandler to get the latest checkins from the checkin server.)  

<ins>graph package</ins>  
Stores classes used to construct, store and use graphs (DirectedGraph, Dijkstra, MapCalculations etc)
Note that in the Dijkstra code, we store our distances in a HashMap that maps (node -> currently-known-distance).  
Also note that our A* implementation is done implicitly through our DirectedGraph class inside addEdge()

<ins>kdtree package</ins>  
Files relating to tree structure (KDTree, TreeNode, BoundedPriorityQueue):
The classes within the kdtree package provide the infrastructure to create and store k-d trees. In order to implement KDTrees, we defined a Node, Left, and Right
  for each kdtree. Node is of any type that extends the HasCoordinates interface. Left and Right are Optional kdtrees, which empty optionals denoting the bottom
  of

<ins>maps package</ins>  
Files that are unique to the Maps project: - The MapTriggerAction, NearestTriggerAction, RouteTriggerAction, and WaysTriggerAction each implement the TriggerAction interface.
Thus, they each have an execute() method that is run when the execute() method of the TriggerAction interface is called.
Each of their execute() methods runs an action with the given parameters, and returns the output as a string.
One thing to note is that the four trigger actions are extremely similar in the code they implement.
However, since each class is still fundamentally different in its functionality, we chose not to use an abstract class or interface to remove the shared code.
The use of this type of command pattern separates the responsibilities of the requester of the action from the object performing the action, allowing for extensibility of the REPL.
In order to college data from the database, this command loads all traverable nodes into the static data in the Maps class. For Nearest and Route commands,
some extra data is loaded in from the database when those commands are called. This is possible because the Maps class also contains an SQL connetcion object to the
most recently loaded database.  

<ins>mockaroo package</ins>  
Mockaroo assignment from Stars 1,2  

<ins>points package</ins>  
Classes and Interfaces used to define the functionality and structure of a point in both Stars and Maps: - We created an interface called HasCoordinates. The purpose of this interface was to allow extensibility of commands to any object that had a position associated with it.
This interface defines a default euclidean distance method. To implement the functionality of the methods requiring coordinates, we chose to create a separate Point class that creates a point in 3D Cartesian space.
To compare these points to each other, we defined a Point3DComparator class that compares the distances of each point to a reference point and sorts with the closest point to the reference first and the farthest point last.
The CoordinateObjects class essentially stores a List of HasCoordinates objects and implements commands for any set of objects with a position field.  

<ins>repl package</ins>  
CSV parser and REPL class

We used a command design pattern as the inspiration for our repl. In order to make the repl itself as general as possible, we tried to move most of the processing out of repl.java class and into other classes.
Thus, the repl itself only parses the String input by the user into a command String (stars, maps, neighbors, nearest, etc) and a String Array of arguments.
It then instantiates a TriggerActionExecutor object and passes it the command and arguments. The TriggerActionExecutor is the Invoker.
It does some preprocessing to make sure the Command entered is within a List of allowed commands.
This List of allowed commands is not predefined and can be modified by the user. If the command is allowed and it is given the correct number of arguments,
the TriggerActionExecutor gets the TriggerAction interface to execute the command by calling the execute method. The TriggerAction interface acts as the Command interface as described by the command pattern.
For all the commands, it provides a command() method that returns the String version of the command that the user must type and a execute() method that asks the Receiver of the command to carry out an operation.

<ins>stars package</ins>  
Files that are unique to the Stars project: - The StarsTriggerAction, NaiveNeighborsTriggerAction, naiveRadiusTriggerAction, neighborsTriggerAction, and radiusTriggerAction each implement the TriggerAction interface.
Thus, they each have an execute() method that is run when the execute() method of the TriggerAction interface is called.
Each of their execute() methods runs an action in the Receiver class which is StarApplications. When the execute() method of StarsTriggerAction is called, this is loading the CSV file.
For the naiveNeighborsTriggerAction and naiveRadiusTriggerAction, it is calling the correct naiveNeighbors(...) and naiveRadius(...) methods. One thing to note is that the four trigger actions are extremely similar in the code they implement.
However, since each class is still fundamentally different in its functionality, we chose not to use an abstract class or interface to remove the shared code.
The use of this type of command pattern separates the responsibilities of the requester of the action from the object performing the action, allowing for extensibility of the REPL.
To create an object with fields like ID and name that are specific to stars, we created a Star class that inherits the Point class.
Objects of the Star class are created to implement Stars-specific functionality which requires retrieving the Name and ID fields of a star.

###Optimizations
Any runtime/space optimizations you made beyond the minimum requirements

We use caching in the back-end by storing ways of nodes rather than reading in every way in the beginning. Additionally, we cache the front-end when we zoom in (Zooming in doesn't require any post requests; we use the ways we already have to render the canvas)

###Instructions for using the GUI
How to build/run your program with the GUI
####First terminal window
In one terminal window, run the checkin feed by doing `cs032_maps_location_tracking [port] [num] -[flag]`
we used port 8080, num=10 and -m for the flag

####Second terminal window
In another terminal window, from the repository root directory:\
`mvn package`\
`./run --gui`

If you don't have permission to use the run executable,`chmod 777 run`

####Third terminal window
In a third terminal window, from the repository root, cd into:
`src/main/resources/static/maps-app`

Then, do `npm start`, which should open up our GUI in a web browser.

If you get a permission denied error, try cd into `node-modules/.bin` then `chmod 777 react-scripts`. Retry npm start

####Loading in the map
In your second terminal window, load in a map like so: 
`map data/maps/maps.sqlite3`

####Interacting with the GUI
Go to the browser window that opened up after you did `npm start`. You should (soon) see a view of Brown's campus with traversable ways in red and non-traversable ways in blue.  

<ins>Zoom</ins>: you can zoom in and out of the current view by scrolling up to zoom in and scrolling down to zoom out. Alternatively, you can zoom using the `+` and `-` buttons.  

<ins>Pan</ins>: you can pan from the current view by clicking, dragging, then releasing  

<ins>Finding a route</ins>:  
Textboxes: 
You can enter in street, cross street, street2, cross street2 into the four text boxes.  
Extra Feature - Alternatively, you can enter in a pair of coordinates (start latitude, start longitude. end latitude, end longitude).  
Then, press the `Find Path` button. 

Clicking:  
Click on any two points on the map to find the shortest route between them (you do not need to press thr Find Route button if you already clicked on to points). Note that if you click and drag, our program will not recognize that you want to find a route.

A combination of text and clicking:  
You can enter (latitude, longitude) or (street, cross street) in the first two text boxes. Then, click on one point on the map and press `Find Path`.  
You can also do the inverse of this: enter (latitude, longitude) or (street, cross street) in the last two text boxes, and click on the map once, then press `Find Path`.  
Note that you can do the clicking and enter data into the text boxes in either order (i.e. you could click on a point first then enter in data into two of the text boxes before clicking `Find Path`)

Route information & Extra Feature:  
The resulting route will be shown in green on the map. The program will also tell you how long the route is in miles as well as how long it will take to walk it.

###Browser details
We used Chrome to run our GUI and verify that our implementation works.
