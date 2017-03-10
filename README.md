# Dijkstra-Scholten-algorithm
# Termination Detection algorithm
 
 It is a Tree-based algorithm.
	
	Algorithm : 
	
	 The INITIATOR is the root of tree.
	 On receiving first message: process joins tree by becoming child of message sender.
	 On receiving subsequent messages: process sends ACK to message sender.
	 When a process becomes idle and has no outstanding ACKs: 
		Process sends ACK (for first message) to its parent.
		Process detaches itself from tree.
	 Termination: When the initiator is idle and has no outstanding ACKs
	 
	 
	 You are given a file that contains a series of events that should be simulated on the nodes. 
	 The program reads these events from the file and executes them on corresponding nodes.Each line 
	 of the file represents an event that is denoted by a tuple<Nodeid,Event,parameter> ; 
	 where: nodeid is the node where the event should occu, Event is the type of event that should
	 occur and parameter specifies to which node it sends the message to. The types of the event could 
	 be one of the following:

| Event | Description |
| --- | --- |
| INITIATOR | Initiates the termination detection algorithm |
| SEND| Send a message to node given in param. |
| IDLE | The node becomes idle and sends an ACK to its parent | 

Once the initiator detects the termination of the computation, it displays proper messages and quits
