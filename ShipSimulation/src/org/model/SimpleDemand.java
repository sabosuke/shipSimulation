package org.model;

import org.model.Status.LoadingType;

/**
 * Continer demand that is constant and happens at regular timing
 * @author Shinnosuke Wanaka
 * 
 */
public class SimpleDemand extends Demand{
	
	private int counter;
	private int limit;
	private double amount;
	private int duration;
	private String departure;
	private String destination;
	
	public SimpleDemand(){
		super();
		setCargoType(LoadingType.Container);
		this.counter = 0;
		this.limit = 30;
		amount = 6600;
		duration = 720;
		departure = "Japan";
		destination = "Los Angels";
	}
	
	public SimpleDemand(LoadingType cargoType, int interval, double amount, int duration, String departure, String destination){
		super();
		setCargoType(cargoType);
		this.counter = 0;
		this.limit = interval;
		this.amount = amount;
		this.duration = duration;
		this.departure = departure;
		this.destination = destination;
	}

	@Override
	public void timeNext(int now) {
		if (counter > this.limit){
			super.setAmountOfCargo(amount);
			super.setStartTime(now);
			super.setEndTime(now + duration);
			super.setDeparture(departure);
			super.setDestination(destination);
			counter = 0;
		}
		counter ++;	
	}
	
}