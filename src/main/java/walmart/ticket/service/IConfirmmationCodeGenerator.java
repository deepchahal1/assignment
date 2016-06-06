package walmart.ticket.service;

/**
 * Interface to generate confirmation code.
 * @author deep
 *
 */
public interface IConfirmmationCodeGenerator {
    /**
     * function to generate code.
     * @return
     */
	String generateCode();
	
}
