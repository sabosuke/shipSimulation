package exec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.model.Demand;
import org.model.Fleet;
import org.model.Market;
import org.model.Freight;
import org.model.FuelPrice;
import org.model.OilPrice;
import org.model.Port;
import org.model.SimpleShip;
import org.model.SimpleShipOperator;
import org.model.Status.FuelType;
import org.model.Status.LoadingType;
import org.model.PortNetwork;
import org.model.Ship;
import org.model.ShipOperator;
import org.model.SimpleDemand;


import org.model.SimplePort;
import org.simulation.SimpleSimulation;
import org.simulation.Simulation;
import org.util.CSVReader;

public class Main {

	public static void main(String[] args){
		
		loadInitialFleet("../../data/ship_config.csv");
		loadInitialPorts("../../data/port_config.csv");
		loadMarketInfo("../../data/market_config.csv");
		
		Simulation simulation = new SimpleSimulation(365);
		System.out.println("Simulation Start");
		simulation.execute();
		System.out.println("Simulation End");
		
	}
	
	private static void loadInitialFleet(String filePath){
		//List<String[]> data =CSVReader.forGeneral(filePath);
		double speed = 28;
		LoadingType cargoType = LoadingType.HFO;
		double cargoAmount = 300000;
		double foc = 1.24;
		double fuelCapacity = 5000;
		FuelType fuelType = FuelType.OIL;
		
		Ship ship = new SimpleShip(speed, cargoType, cargoAmount, foc, fuelCapacity, fuelType);
		ShipOperator operator = new SimpleShipOperator("NYK");
		ship.setOwner(operator);
		Fleet.add(ship);
	}
	
	private static void loadInitialPorts(String configFilePath){
		List<String[]> data = CSVReader.forGeneral(configFilePath);
		List<Port> ports = new ArrayList<Port>();
		double[][] routeMatrix = null;
		int portCount = 0;
		for (int i=0;i<data.size();i++){
			if (data.get(i)[0].equals( "PortName")){
				String name = data.get(i+1)[0];
				String fuelType = data.get(i+3)[0];
				String loadingType = data.get(i+3)[1];
				String bunkeringCapacity = data.get(i+3)[2];
				String loadingCapacity = data.get(i+3)[3];
				int numOfPorts = Integer.parseInt(data.get(i+4)[0]);
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("FuelType", fuelType);
				param.put("LoadingType", loadingType);
				param.put("BunkeringCapacity", bunkeringCapacity);
				param.put("LoadingCapacity", loadingCapacity);
				Port port = new SimplePort(name);
				port.addPortFacilities(param,numOfPorts);
				i = i + 5;
				portCount ++;
			}
			if (data.get(i)[0].equals("RouteMatrix")){
				routeMatrix = new double[portCount][portCount];
				for (int j=1;j<1+portCount;j++){
					for(int k=1;k<1+portCount;k++){
						routeMatrix[j-1][k-1] = Double.parseDouble(data.get(i+1+j)[k]);
					}	
				}
			}
		}
		PortNetwork.setPortSettings(ports,routeMatrix);
	}
	
	private static void loadMarketInfo(String filePath){
		double upforStandard = 0;
		double downforStandard = 0;
		double pforStandard = 0;
		double upforRate = 0;
		double downforRate = 0;
		double pforRate = 0;
		double initialStandard = 0;
		double initialRate = 0;
		Freight freight = new Freight(upforStandard,downforStandard,pforStandard,upforRate,downforRate,pforRate,initialStandard,initialRate);
		//List<String[]> data = CSVReader.forGeneral(filePath);
		double initialPrice = 0;
		double upFactor = 0;
		double downFactor = 0;
		double probability = 0;
		FuelPrice oilprice = new OilPrice(initialPrice,upFactor,downFactor,probability);
		
		Demand demand = new SimpleDemand();
		Market.addDemand(demand);
		Market.addFuelPrice(oilprice);
		Market.addFreight(freight);
	}
	
	
}
