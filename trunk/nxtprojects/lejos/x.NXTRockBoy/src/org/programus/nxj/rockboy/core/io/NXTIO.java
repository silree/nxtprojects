package org.programus.nxj.rockboy.core.io;

import java.awt.Point;

import javax.microedition.lcdui.Image;

import lejos.nxt.Button;
import lejos.nxt.ColorLightSensor;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;

import org.programus.nxj.util.DisplayUtil;
import org.programus.nxj.util.txtimg.TextImage;
import org.programus.nxj.util.txtimg.TextImage3x5;

/**
 * NXTIO class is an IO module for real NXT environment. 
 * @author Programus
 * @see IOModule
 */
public class NXTIO extends IOModule {
	// ==================== port setup ====================
	// if you want to connect the sensors or motors with 
	//    port set different from below setup, 
	//    please change the code here. 
	private static Motor R_MOTOR = Motor.A; 
	private static SensorPort R_TOUCH_P = SensorPort.S1; 
	private static SensorPort TOUCH_P = SensorPort.S4; 
	private static SensorPort COLOR_P = SensorPort.S2; 
	private static SensorPort SONAR_P = SensorPort.S3; 
	// ====================================================
	
	private static TouchSensor R_TOUCH_S = new TouchSensor(R_TOUCH_P); 
	private static TouchSensor TOUCH_S = new TouchSensor(TOUCH_P); 
	private static ColorLightSensor COLOR_S; 
	private static LightSensor LIGHT_S; 
	private static UltrasonicSensor SONAR_S = new UltrasonicSensor(SONAR_P);
	
	private static Point limit = new Point(LCD.SCREEN_WIDTH, LCD.SCREEN_HEIGHT); 
	
	NXTIO() {
		COLOR_S = new ColorLightSensor(COLOR_P, ColorLightSensor.TYPE_COLORNONE); 
		if (COLOR_S.readValue() < 0) {
			LIGHT_S = new LightSensor(COLOR_P); 
			COLOR_S = null; 
		}
	}

	@Override
	public ColorLightSensor getColorLightSensor() {
		return COLOR_S;
	}
	@Override
	public int getRotationAngle() {
		return R_MOTOR.getTachoCount();
	}
	@Override
	public TouchSensor getTouchSensor() {
		return TOUCH_S;
	}
	@Override
	public UltrasonicSensor getUltrasonicSensor() {
		return SONAR_S;
	}
	@Override
	public boolean isTouchSensorPressed() {
		return TOUCH_S.isPressed();
	}
	
	@Override
	public void resetPosition() {
		TextImage ti = new TextImage3x5(); 
		R_MOTOR.setPower(0); 
		R_MOTOR.setBrakePower(0); 
		if (!R_TOUCH_S.isPressed()) {
			Sound.buzz(); 
		} else {
			Sound.beep(); 
		}
		Image text = ti.getImage("PLS RESET NXT AND PRESS"); 
		int centerY = (LCD.SCREEN_HEIGHT >> 1); 
		DisplayUtil.drawImageCenter(text, centerY - text.getHeight(), LCD.ROP_XOR); 
		DisplayUtil.drawStringCenter("ENTER", centerY + 1, LCD.ROP_XOR); 
		while (!R_TOUCH_S.isPressed()) {
			if (Button.ESCAPE.isPressed()) {
				System.exit(0); 
			}
			Thread.yield(); 
		}
		int pressedButtonId = 0; 
		while (pressedButtonId != Button.ID_ENTER) {
			pressedButtonId = Button.waitForPress(); 
			if (pressedButtonId == Button.ID_ESCAPE) {
				System.exit(0); 
			}
		}
		LCD.clear(); 
		DisplayUtil.drawStringCenter("RESETING...", 3); 
		text = ti.getImage("DON'T TOUCH ANYTHING NOW!"); 
		DisplayUtil.drawImageCenter(text, centerY, LCD.ROP_XOR); 
		
		Delay.msDelay(1000); 
		
		R_MOTOR.resetTachoCount(); 
		R_MOTOR.regulateSpeed(true); 
		R_MOTOR.setSpeed(100); 
		
		R_MOTOR.forward(); 
		while(R_TOUCH_S.isPressed()); 
		int angle = R_MOTOR.getTachoCount(); 
		R_MOTOR.stop(); 
		
		R_MOTOR.backward(); 
		Delay.msDelay(500); 
		while(R_TOUCH_S.isPressed()); 
		angle += R_MOTOR.getTachoCount(); 
		R_MOTOR.stop(); 
		
		angle >>= 1; 
		R_MOTOR.setSpeed(50); 
		R_MOTOR.rotateTo(angle + 2, false); 
		R_MOTOR.stop(); 
		R_MOTOR.regulateSpeed(false); 
		R_MOTOR.flt(); 
		
		Delay.msDelay(100); 
		R_MOTOR.resetTachoCount(); 
		R_MOTOR.setPower(0); 
		R_MOTOR.setBrakePower(0); 
		
		LCD.clear(); 
		Sound.beep(); 
	}
	
	@Override
	public Point getScreenBoundary() {
		return limit; 
	} 
	
	@Override
	public void playTone(int freq, int duration) {
		Sound.playTone(freq, duration); 
	}
	
	@Override
	public int getDistance() {
		return SONAR_S.getDistance(); 
	}
	
	@Override
	public boolean isTouchPressed() {
		return TOUCH_S.isPressed();
	}
	
	@Override
	public int getLightValue() {
		return this.isUsingColorLightSensor() ? COLOR_S.readValue() : LIGHT_S.readValue(); 
	}

	@Override
	public int getRawLightValue() {
		return this.isUsingColorLightSensor() ? COLOR_S.readRawValue() : LIGHT_S.readNormalizedValue(); 
	}	
	
	@Override
	public void setColorLightSensorType(int type) {
		if (this.isUsingColorLightSensor()) {
			COLOR_S.setType(type); 
		}
	}

	@Override
	public boolean isUsingColorLightSensor() {
		return COLOR_S != null; 
	}

}
