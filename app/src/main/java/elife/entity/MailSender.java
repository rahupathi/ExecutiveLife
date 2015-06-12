package elife.entity;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.os.StrictMode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.ArrayList;
import java.util.Properties;

public class MailSender extends javax.mail.Authenticator {
	private String mailhost = "smtp.gmail.com";
	private String user;
	private String password;
	private Session session;
	private Multipart _multipart;
	static {
		Security.addProvider(new JSSEProvider());
	}

	public MailSender(String user, String password) {
		this.user = user;
		this.password = password;

		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", mailhost);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.quitwait", "false");

		session = Session.getDefaultInstance(props, this);
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user, password);
	}

	public void sendMail(String subject, String body, String sender,
			String recipients, ArrayList<String> fileName) throws Exception {
		try {

			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);

			MimeMessage message = new MimeMessage(session);
			DataHandler handler = new DataHandler(new ByteArrayDataSource(
					body.getBytes(), "text/html"));
			message.setSender(new InternetAddress(sender));
			message.setSubject(subject);
			message.setDataHandler(handler);

			if (recipients.indexOf(',') > 0)
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(recipients));
			else
				message.setRecipient(Message.RecipientType.TO,
						new InternetAddress(recipients));

			// *Attachments
			if (fileName != null) {
				if (fileName.size() > 0) {
					Multipart mp = new MimeMultipart();

					MimeBodyPart mbp1 = new MimeBodyPart();
					mbp1.setContent(body, "text/html");
					mp.addBodyPart(mbp1);
					if (fileName != null) {
						MimeBodyPart mbp2 = null;
						FileDataSource fds = null;
						for (int counter = 0; counter < fileName.size(); counter++) {
							mbp2 = null;
							fds = null;
							mbp2 = new MimeBodyPart();
							fds = new FileDataSource(fileName.get(counter)
									.toString());
							mbp2.setDataHandler(new DataHandler(fds));
							mbp2.setFileName(fds.getName());
							mp.addBodyPart(mbp2);
						}
					}
					message.setContent(mp);
				}
			}

			Transport.send(message);

		} catch (Exception e) {
			throw e;
		}
	}

	public void addAttachment(String filename, String subject) throws Exception {

		_multipart = new MimeMultipart();

		BodyPart messageBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(filename);
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(filename);
		_multipart.addBodyPart(messageBodyPart);

		// BodyPart messageBodyPart2 = new MimeBodyPart();
		// messageBodyPart2.setText(subject);
		// _multipart.addBodyPart(messageBodyPart2);
	}

	public class ByteArrayDataSource implements DataSource {
		private byte[] data;
		private String type;

		public ByteArrayDataSource(byte[] data, String type) {
			super();
			this.data = data;
			this.type = type;
		}

		public ByteArrayDataSource(byte[] data) {
			super();
			this.data = data;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getContentType() {
			if (type == null)
				return "application/octet-stream";
			else
				return type;
		}

		public InputStream getInputStream() throws IOException {
			return new ByteArrayInputStream(data);
		}

		public String getName() {
			return "ByteArrayDataSource";
		}

		public OutputStream getOutputStream() throws IOException {
			throw new IOException("Not Supported");
		}
	}
}
