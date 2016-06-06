package walmart.ticket;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import walmart.ticket.service.TicketService;
import walmart.ticket.service.data.VenueConfig;


/**
 * 
 * This class load the bean config for TicketService.
 * @author deep
 *
 */
public class TicketServiceCmdServer {
	private static final Logger logger = LoggerFactory.getLogger(TicketServiceCmdServer.class);

	/**
	 * 
	 * @param args
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	public static void main(String[] args) throws InterruptedException, ParseException {
		/*
		 * Valid program arguments combination
		 -demo
		 -findSeats -minLevel 1 -maxLevel 2
		 -held -numSeats 10 -minLevel 1 -maxLevel 2 -customerId xyz@abc.com
		 -holdAndReserve -numSeats 10 -minLevel 1 -maxLevel 2 -customerId xyz@abc.com
		 */
		// create the parser
	    CommandLineParser parser = new DefaultParser();
	    try {
	        // parse the command line arguments
	        CommandLine line = parser.parse( constructGnuOptions(), args );
	        ApplicationContext context =  new ClassPathXmlApplicationContext("/walmart/ticket/config/ticketService.xml");
    	    TicketService ticketService=(TicketService)context.getBean("ticketService");
    	    VenueConfig venueConfig = (VenueConfig )context.getBean("venueConfig");
    	    processArguments(ticketService, venueConfig, line);
    	    System.exit(0);
	        
	    } catch( ParseException exp ) {
	        logger.error( "Parsing failed.  Reason: {}" , exp.getMessage() );
	    	
	    }catch(RuntimeException ex){
	        logger.error( "error" , ex );

	    }
	    System.exit(0);

	}
	
	public static void processArguments(TicketService ticketService, VenueConfig venueConfig, CommandLine line) throws InterruptedException{
		
		if(line.hasOption(CmdLineOption.demo.name())){
				DemoUtils.runDemo(ticketService,venueConfig );
		}
		if(line.hasOption(CmdLineOption.findSeats.name())){
			DemoUtils.findSeats(ticketService, venueConfig, line);
		}
		if(line.hasOption(CmdLineOption.held.name())){
			DemoUtils.heldSeats(ticketService, venueConfig, line);
		}
		
		if(line.hasOption(CmdLineOption.holdAndReserve.name())){
			DemoUtils.holdAndReserveSeats(ticketService, venueConfig, line);
		}
	}
	
	
	 public static Options constructGnuOptions()
	   {
	      final Options gnuOptions = new Options();
	      gnuOptions.addOption(CmdLineOption.demo.name(), false, "Run demo.")
					.addOption(CmdLineOption.findSeats.name(), false, "num setas to be held.")
	                .addOption(CmdLineOption.minLevel.name(), true, "minLevel to  find seats.")
	                .addOption(CmdLineOption.maxLevel.name(),  true, "maxLevel to find seats")
	      			.addOption(CmdLineOption.held.name(),  false, "hold seats to find seats")
	      			.addOption(CmdLineOption.numSeats.name(), true, "num setas to be held.")
	      			.addOption(CmdLineOption.holdAndReserve.name(),  false, "hold and reserve seats")
	                .addOption(CmdLineOption.customerId.name(),  true, "customer id to reserve seats");
	               
	      return gnuOptions;
	   }
	
	
}
