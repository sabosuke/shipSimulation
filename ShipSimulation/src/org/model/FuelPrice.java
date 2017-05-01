package org.model;

import org.model.Status.FuelType;

public abstract class FuelPrice {
	protected FuelType fuelType;
	protected double price;
	
	public abstract void timeNext(int now);
	public abstract double getPastPrice(int past);
	
	public FuelType getFuelType() {
		return fuelType;
	}
	public void setFuelType(FuelType fuelType) {
		this.fuelType = fuelType;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}
