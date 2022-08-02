package org.salesforce.demo.auth;

import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Basic authentication helper that uses SOAP login to retrieve an auth token.
 * WARNING: do not use this in production, switch to JWT authentication.
 */
public class AuthenticationHelper {

	private static final Logger logger = Logger.getLogger(AuthenticationHelper.class.getName());

	private static final String SOAP_LOGIN_SERVICE_URI = "/services/Soap/u/55.0/";

	public static SalesforceSession login(String loginUrl, String username, String password, String secretToken)
			throws AuthenticationException {
		String requestBody = getSoapLoginRequestBody(username, password, secretToken);
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			// Prepare authentication request
			HttpPost request = new HttpPost(loginUrl + SOAP_LOGIN_SERVICE_URI);
			request.addHeader("Content-Type", "text/xml; charset=UTF-8");
			request.addHeader("SOAPAction", "login");
			request.setEntity(new StringEntity(requestBody));
			// Execute request
			try (CloseableHttpResponse response = httpclient.execute(request)) {
				// Parse response
				LoginResponseParser parser = parseLoginResponse(response);
				if (response.getCode() == 200) {
					String instanceUrl = "https://" + new URL(parser.serverUrl).getHost();
					logger.info(String.format("Logged as %s on %s", username, instanceUrl));
					return new SalesforceSession(parser.sessionId, parser.organizationId, instanceUrl);
				}
				throw new Exception(
						String.format("Request failed with HTTP %d: %s", response.getCode(), parser.faultstring));
			}
		} catch (Exception e) {
			logger.severe(String.format("Failed to login as %s", username));
			throw new AuthenticationException("Failed to authenticate", e);
		}
	}

	private static String getSoapLoginRequestBody(String username, String password, String secretToken) {
		return "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n"
				+ "<env:Envelope xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
				+ "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
				+ "    xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" + "  <env:Body>\n"
				+ "    <n1:login xmlns:n1=\"urn:partner.soap.sforce.com\">\n" + "      <n1:username><![CDATA["
				+ username + "]]></n1:username>\n" + "      <n1:password><![CDATA[" + password + secretToken
				+ "]]></n1:password>\n" + "    </n1:login>\n" + "  </env:Body>\n" + "</env:Envelope>";
	}

	/**
	 * Reads the authentication response (XML)
	 * 
	 * @param response
	 * @return a parser that contains the authentication info
	 * @throws Exception
	 */
	private static LoginResponseParser parseLoginResponse(CloseableHttpResponse response) throws AuthenticationException {
		try (InputStream responseStream = response.getEntity().getContent()) {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
			spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
			spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			spf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			spf.setNamespaceAware(true);
			SAXParser saxParser = spf.newSAXParser();
			LoginResponseParser parser = new LoginResponseParser();
			saxParser.parse(responseStream, parser);
			return parser;
		} catch (Exception e) {
			throw new AuthenticationException("Unable to parse authentication response", e);
		}
	}

	private static class LoginResponseParser extends DefaultHandler {

		private String faultstring;
		private String serverUrl;
		private String sessionId;
		private String organizationId;

		private String buffer;
		private boolean reading = false;

		@Override
		public void characters(char[] ch, int start, int length) {
			if (reading) {
				buffer = new String(ch, start, length);
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) {
			reading = false;
			switch (localName) {
			case "organizationId":
				organizationId = buffer;
				break;
			case "sessionId":
				sessionId = buffer;
				break;
			case "serverUrl":
				serverUrl = buffer;
				break;
			case "faultstring":
				faultstring = buffer;
				break;
			default:
			}
			buffer = null;
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) {
			switch (localName) {
			case "sessionId":
			case "serverUrl":
			case "faultstring":
			case "organizationId":
				reading = true;
				break;
			default:
			}
		}
	}
	
	public static class AuthenticationException extends Exception {
		private static final long serialVersionUID = 5581749931914191866L;

		public AuthenticationException(String message, Exception causedBy) {
			super(message, causedBy);
		}
	};
}
