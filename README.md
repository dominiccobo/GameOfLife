# GameOfLife
This project is a Java implementation of John Conway's Game Of Life. I created this as part of my Level 1 Computer Science Java Programming module assignment, and since it is now over I decided that publishing it would be the best of things.

For those not familiar with the Game of Life, it is a form of Artificial Life simulation (Cellular Automata) that uses a the basic principle of a grid with two states: alive and dead.

From there each iteration each cell is checked and the new state for the next iteration is set based upon:

* A cell can go from dead to alive (be born) if it has 3 alive cells adjacent to it. 
* A cell will remain alive if it has 2 or 3 alive cells adjacent to it. 
* In all other cases a cell will die, from either lonliness (if it has less than 2 alive adjacent cells), or suffocation (if it has more than 3 alive adjacent cells)

For more information see: 
* www.conwaylife.com/
* https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life

## Implementation
The implementation of this project has been carried out using a Java Swing due to the requirements of the specification detailing so. The Swing creation was entirely created manually using Java code rather than utilizing a form, so the creation code is rather long and hefty, however it has been logically separated sufficiently to ease understanding. 

To ease logic and view differentiation an MVP (Model-View-Presenter) approach was observed. This means that the view is simply dumb and knows nothing about the logic of the application. The presenter, a form of controller, acts as a listener for events occuring on the front end and updates the logic appropriately, whilst observing it for any changes and if need be updating the front end with said changes again. 
