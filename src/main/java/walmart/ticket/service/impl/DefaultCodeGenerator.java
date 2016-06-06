package walmart.ticket.service.impl;

import java.util.Random;

import com.google.common.io.BaseEncoding;

import walmart.ticket.service.IConfirmmationCodeGenerator;

/**
 * confirmation code generator.
 * @author deep
 *
 */
public class DefaultCodeGenerator implements IConfirmmationCodeGenerator {
	private final Random random = new Random(); 

	
	@Override
	public String generateCode() {
		final byte[] buffer = new byte[9];
	    random.nextBytes(buffer);
	    return BaseEncoding.base64Url().omitPadding().encode(buffer); 
	}
}
