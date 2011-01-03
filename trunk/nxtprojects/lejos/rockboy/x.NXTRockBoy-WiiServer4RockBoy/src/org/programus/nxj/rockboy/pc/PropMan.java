package org.programus.nxj.rockboy.pc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropMan {
	public static final String NXT_NAME = "NXT.Name"; 
	public static final String NXT_PIN	= "NXT.PIN"; 
	public static final String NXT_CONN_TIMEOUT = "NXT.ConnectTimtOut"; 
	public static final String WII_CONN_TIMEOUT = "Wii.ConnectTimeOut"; 
	public static final String WII_ALPHASMOOTHVALUE = "Wii.AlphaSmoothingValue"; 
	public static final String WII_ANGLEOFFSET = "Wii.AngleOffset"; 
	
	private static final String PROP_FILENAME = "NXTWiiWay.properties"; 
	
	private Properties prop;
	
	private static PropMan pm = new PropMan(); 
	
	public static PropMan getInstance() {
		return pm; 
	}
	
	private PropMan() {
		Properties defaultProps = new Properties(); 
		defaultProps.setProperty(NXT_NAME, "NXT"); 
		defaultProps.setProperty(NXT_PIN, "0000"); 
		defaultProps.setProperty(NXT_CONN_TIMEOUT, "0"); 
		defaultProps.setProperty(WII_CONN_TIMEOUT, "0"); 
		defaultProps.setProperty(WII_ALPHASMOOTHVALUE, "0.5"); 
		defaultProps.setProperty(WII_ANGLEOFFSET, "75"); 
		prop = new Properties(defaultProps); 
		InputStream in = ClassLoader.getSystemResourceAsStream(PROP_FILENAME); 
		try {
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace(); 
		} 
		
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace(); 
		} 
	}
	
	public String getProperty(String key) {
		return prop.getProperty(key); 
	}
	
	@Deprecated
	public Properties getProperties() {
		return this.prop; 
	}
}
