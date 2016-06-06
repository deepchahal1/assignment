
                 ####################################
                 # 	Ticket Service assignment   	#
                 #  Pradeep Kumar (Deep)            #
                 ####################################
                 
  Implementation of TicketService keep available seats in priority queue according to seat proximity 
  to center of the row and row near to stage.
  Seat Hold expiration is done by background ScheduledThreadPoolExecutor thread and expired seats released back to
  priority queue (encapsulated in LevelSeatModel class).
  
  If request has been made for seats more than available, error will be return and no seats will be held.
  Have not implemented REST service, used dummy repository to save state, roll-back mechanism is not applied.
  
  Class TicketServiceCmdServer is used to run command line option, each time we run it will load spring bean
  configuration and previous state will be lost.   
  
  For invalid level sum of seats of level will be return.                

valid program arguments
-demo
-findSeats -minLevel 1 -maxLevel 2
-held -numSeats 10 -minLevel 1 -maxLevel 2 -customerId xyz@abc.com
-holdAndReserve -numSeats 10 -minLevel 1 -maxLevel 2 -customerId xyz@abc.com

#####  To Build:
mvn install

this will generate /target/ticketservice-0.0.1-SNAPSHOT-jar-with-dependencies.jar

#### To Run command line option:

   ====valid program arguments
-demo    # run 3 test case in sequence
-findSeats -minLevel 1 -maxLevel 2
-held -numSeats 10 -minLevel 1 -maxLevel 2 -customerId xyz@abc.com
-holdAndReserve -numSeats 10 -minLevel 1 -maxLevel 2 -customerId xyz@abc.com


\target>java -cp ticketservice-0.0.1-SNAPSHOT-jar-with-dependencies.jar walmart.ticket.TicketServiceCmdServer -demo
\target>java -cp ticketservice-0.0.1-SNAPSHOT-jar-with-dependencies.jar walmart.ticket.TicketServiceCmdServer -findSeats -minLevel 1
\target>java -cp ticketservice-0.0.1-SNAPSHOT-jar-with-dependencies.jar walmart.ticket.TicketServiceCmdServer -held -numSeats 10 -minLevel 1 -maxLevel 2 -customerId xyz@abc.com
\target>java -cp ticketservice-0.0.1-SNAPSHOT-jar-with-dependencies.jar walmart.ticket.TicketServiceCmdServer -holdAndReserve -numSeats 10 -minLevel 1 -maxLevel 2 -customerId xyz@abc.com
