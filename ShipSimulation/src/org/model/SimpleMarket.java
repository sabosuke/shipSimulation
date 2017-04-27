package org.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.model.Status.LoadingType;
import org.simulation.Simulation;

public class SimpleMarket extends Market {

	@Override
	public List<Demand> checkDemand() {
		List<Demand> returnList = new ArrayList<Demand>();
		for (Demand demand : super.demands){
			if (demand.isDemand()) returnList.add(demand);
		}
		
		if (returnList.size() == 0) return null;
		else return returnList;
	}

	@Override
	public void addContract(Fleet fleet, PortNetwork portNetwork) {
		
		for(Demand demand : super.demands){
			//1.まずはdemandをほどく
			int startTime = demand.getStartTime();
			int endTime = demand.getEndTime();
			LoadingType cargoType = demand.getCargoType();
			double amount = demand.getAmountOfCargo();
			String departure = demand.getDeparture();
			String destination = demand.getDestination();
			Port dep = portNetwork.getPort(departure);
			Port des = portNetwork.getPort(destination);
			//2. 間に合う船がいるかどうか調べる
			List<Ship> ships = fleet.getShips();
			double tmpFuel = 0;
			Ship tmpShip = null;
			List<Ship> assignedShip = new ArrayList<Ship>();
			while(amount >0){
				for (Ship ship : ships){
					if (cargoType == ship.getCargoType()){
						if (canTransport(ship,startTime,endTime,dep,des)){
							//単位貨物あたりの燃料はいくらか?
							double estimateFuel = estimateFuelCost(ship,startTime,endTime,dep,des,amount);
							if (tmpFuel > estimateFuel){
								tmpFuel = estimateFuel;
								tmpShip = ship;
							}
						}
					}
					
				}
				assignedShip.add(tmpShip);
				double done = getAvailableCargoAmount(tmpShip,startTime,endTime,dep,des,amount);
				amount = amount -done;
			}
			//3. 運賃を決める
			double freight = decideFreight(ships,assignedShip,super.fuels,demand);
			//4. スケジュールを入れる
			makeContract(assignedShip,freight);
			
		}

	}
	private boolean canTransport(Ship ship, int startTime, int endTime, Port departure, Port desitination){
		//TO-DO actual behavior
		//対象の船の最終予定時間と場所を取得
		//そこから出発地点まで来て、目的地まで最大船速で行く時間を計算(Loading、Bunkeringの時間を忘れない)
		//その時間とendTimeとを比較する
		return true;
	}
	private double estimateFuelCost(Ship ship, int startTime, int endTime, Port departure, Port destination, double amount){
		//TO-DO actual behavior
		
		return 0;
	}
	private double getAvailableCargoAmount(Ship ship, int startTime, int endTime, Port departure, Port destination, double amount){
		//TO-DO
		return 0;
	}
	private double decideFreight(List<Ship> ships, List<Ship> assignedShip,List<FuelPrice> fuels, Demand demand){
		//TO-DO
		return 0;
	}
	private void makeContract(List<Ship> ships, double freight){
		//TO-DO
	}
	
	
	public class ContainerDemand extends Demand{
		
		private int counter;
		private int limit;
		
		public ContainerDemand(){
			super();
			setCargoType(LoadingType.Container);
			this.counter = 0;
			this.limit = 30;
		}

		@Override
		public void timeNext() {
			if (counter > this.limit){
				super.setAmountOfCargo(6600);
				super.setStartTime(Simulation.getCurrentTime());
				super.setEndTime(Simulation.getCurrentTime()+720);
				super.setDeparture("Japan");
				super.setDestination("Los Angels");
				counter = 0;
			}
			counter ++;	
		}
		
	}
	
	public class OilPrice extends FuelPrice{
		private double upFactor;
		private double downFactor;
		private double upProbability;

		@Override
		public void timeNext() {
			double n = Math.random();
			if (n >= upProbability){
				setPrice(super.price * upFactor);
			}else{
				setPrice(super.price * downFactor);
			}
			
		}
		
	}

}
