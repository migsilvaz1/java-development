package es.chg.portal.requests;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;

public class RequestsUtils {
	
	public static String getFormatDate(Date date) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("es"));
		return df.format(date);
	}
	
	public static String getFormatStatus(String statusCode) {
   	 	if ("PENDIENTE_FIRMA".equals(statusCode)) {
   	 		return "Pendiente de Firma";
   	 	} else if ("FIRMADO".equals(statusCode)) {
   	 		return "Firmado";
   	 	} else if ("PENDIENTE_APROBACION".equals(statusCode)) {
   	 		return "Pendiente de Aprobación";
   	 	} else if ("APROBADO".equals(statusCode)) {
   	 		return "Aprobado";
   	 	} else if ("RECHAZADO".equals(statusCode)) {
   	 		return "Rechazado";
   	 	}
   	 	return "";
	}
	
	public static String getFormatStatusPf(String statusCode) {
   	 	if ("FIRMADO".equals(statusCode) || "VISTOBUENO".equals(statusCode)) {
   	 		return "Aprobado";
   	 	} else if ("DEVUELTO".equals(statusCode) || "CADUCADO".equals(statusCode) || "RETIRADO".equals(statusCode)) {
   	 		return "Rechazado";
   	 	} else { // EN ESPERA, LEIDO, NUEVO
   	 		return "En Proceso";
   	 	}
	}
	
	public static String encodeBase64(byte[] binaryData) {
		return Base64.encodeBase64String(binaryData);
	}
	
	public static byte[] decodeBase64(String base64Data) {
		return Base64.decodeBase64(base64Data);
	}

}
