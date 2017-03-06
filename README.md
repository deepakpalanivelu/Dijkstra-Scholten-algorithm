# Dijkstra-Scholten-algorithm
Termination Detection algorithm
	It is a Tree-based algorithm.
	Initiator: root of tree.
	On receiving first message: process joins tree by becoming child of message sender.
	On receiving subsequent messages: process sends ACK to message sender.
	When a process becomes idle and has no outstanding ACKs: 
		Process sends ACK (for first message) to its parent.
		Process detaches itself from tree.
	Termination: When the initiator is idle and has no outstanding ACKs
