package org.programus.nxj.rockboy.pc.wii;

import wiiusej.wiiusejevents.physicalevents.ExpansionEvent;
import wiiusej.wiiusejevents.physicalevents.IREvent;
import wiiusej.wiiusejevents.physicalevents.MotionSensingEvent;
import wiiusej.wiiusejevents.physicalevents.WiimoteButtonsEvent;
import wiiusej.wiiusejevents.utils.WiimoteListener;
import wiiusej.wiiusejevents.wiiuseapievents.ClassicControllerInsertedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.ClassicControllerRemovedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.DisconnectionEvent;
import wiiusej.wiiusejevents.wiiuseapievents.GuitarHeroInsertedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.GuitarHeroRemovedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.NunchukInsertedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.NunchukRemovedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.StatusEvent;

public class WiimoteAdapter implements WiimoteListener {

	@Override
	public void onButtonsEvent(WiimoteButtonsEvent e) {
	}

	@Override
	public void onClassicControllerInsertedEvent(
			ClassicControllerInsertedEvent e) {
	}

	@Override
	public void onClassicControllerRemovedEvent(ClassicControllerRemovedEvent e) {
	}

	@Override
	public void onDisconnectionEvent(DisconnectionEvent e) {
	}

	@Override
	public void onExpansionEvent(ExpansionEvent e) {
	}

	@Override
	public void onGuitarHeroInsertedEvent(GuitarHeroInsertedEvent e) {
	}

	@Override
	public void onGuitarHeroRemovedEvent(GuitarHeroRemovedEvent e) {
	}

	@Override
	public void onIrEvent(IREvent e) {
	}

	@Override
	public void onMotionSensingEvent(MotionSensingEvent e) {
	}

	@Override
	public void onNunchukInsertedEvent(NunchukInsertedEvent e) {
	}

	@Override
	public void onNunchukRemovedEvent(NunchukRemovedEvent e) {
	}

	@Override
	public void onStatusEvent(StatusEvent e) {
	}

}
