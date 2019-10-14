package subwayMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class SubwayMap2_0 {
	
	public String[][] str = new String[50][100];
	
	private class Station{
		String stationName;
		boolean visited;
		
		
		int dist;
		Station path;
		String trackname;
		
		public Station(String stationName) {
			super();
			this.stationName = stationName;
			this.visited = false;
			this.dist = Integer.MAX_VALUE;
			this.path = null;
			this.trackname = null;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((stationName == null) ? 0 : stationName.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Station other = (Station) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (stationName == null) {
				if (other.stationName != null)
					return false;
			} else if (!stationName.equals(other.stationName))
				return false;
			return true;
		}

		public String toString() {
			return "Station [stationName=" + stationName + "]";
		}

		private SubwayMap2_0 getOuterType() {
			return SubwayMap2_0.this;
		}
	}
	
	private class Edge{
		Station station;
		String lineName;
		int distance;
		
		public Edge(Station station, String lineName, int distance) {
			super();
			this.station = station;
			this.lineName = lineName;
			this.distance = distance;
		}
		
		@Override
		public String toString() {
			return "Edge [station=" + station + ", lineName=" + lineName + ", distance=" + distance + "]";
		}
	}
	
	private Map<Station,List<Edge>> map = new HashMap<>();
	
	
	public SubwayMap2_0(){
		fileReader();
	}
	
	
	private void fileReader(){
		File f = new File("maptxt/subway.txt");
		if(f.exists()){
			
			try {
				FileReader out = new FileReader(f);
				BufferedReader br = new BufferedReader(out);
				
				String line = null;
				int nums = Integer.parseInt(br.readLine());
				
				for(int i = 0;i < nums;i++){
					br.readLine();
					
					String trackInfo = br.readLine();

					String[] track = trackInfo.split("[\\s]");
					String trackName = track[0];

					int trackStationsNum = Integer.parseInt(track[1]);
					str[i][0]=trackName;
					
					String[] station;
					String stationName;
					int nextDistance;
					
					Station head = null;
					Station preSta = null;
					Edge pre = null;
					
					Station s;
					
					for (int j = 0; j < trackStationsNum; j++) {
						line = br.readLine();
						
						station = line.split("[\\s]");
						stationName = station[0];
						nextDistance = Integer.parseInt(station[1]);
						
				        
						s = new Station(stationName);
						s.trackname = trackName;
						str[i][j+1]   = stationName;
						
						if(!map.containsKey(s)){
							map.put(s, new ArrayList<Edge>());
						}else{
							
							
							for (Station sta : map.keySet()) {
								if(s.stationName.equals(sta.stationName)){
									s = sta;
									break;
								}
							}
							
						}
						
						
						if(pre != null)
							map.get(s).add(pre);
						
						
						if(preSta != null)
							map.get(preSta).add(new Edge(s,trackName,pre.distance));
						
					
						preSta = s;
					
						pre = new Edge(s,trackName, nextDistance);
						
					
						if(j == 0){
							head = s;
						}
						
						
						if(j == trackStationsNum-1 && nextDistance > 0){
						
							map.get(head).add(pre);
						
							map.get(s).add(new Edge(head,trackName,nextDistance));
						}
					}
					
				}
				br.close();
				out.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	
		}
		
	}
	
	public void shortestPath(String s1,String s2){
		
		Station sta1 = new Station(s1);
		Station sta2 = new Station(s2);
		if(!map.containsKey(sta1))
			System.out.println("起点不存在");
		if(!map.containsKey(sta2))
			System.out.println("终点不存在");
		if(s1.equals(s2))
			System.out.println("起点和终点重合");
		
		for (Station s : map.keySet()) {
			if(s.stationName.equals(s1))
				sta1 = s;
			if(s.stationName.equals(s2))
				sta2 = s;
		}
		dijkstraTravel(sta1);
		
		shortestPath(sta2);
		
		System.out.println("\n" + "总站数为" + (sta2.dist+1));
		
	}
	
	private void shortestPath(Station end){
		
		if(end.path != null){
			shortestPath(end.path);
			System.out.print(" -> ");
		}
		if(end.trackname!=null)
			{
			
			System.out.print("乘坐"+end.trackname+"  ");
			}
		
		System.out.print(end.stationName);
	}
	
	public void ShowStation(String stationname) {
		int index = 0;
		int flag = 0;
		for(int i = 0; i < str.length; i++){ 
				if(str[i][0]!=null){
				if(str[i][0].equals(stationname)){
					 index = i;
					 flag = 1;
					 break;
				}

			}
				}
		if (flag == 1 ) {
			for (int j = 1; j < str.length; j++) {
				if(str[index][j]==null)  
					break;
				if( j == 1)
				System.out.print( str[index][1]);	
				else
				System.out.print( " -> " + str[index][j]);
			}
		}
		else if (flag == 0){
			System.out.println("不存在该线路");
		}
	}
	
	private void dijkstraTravel(Station s){
		Set<Station> set = map.keySet();
		
		for (Station station : set) {
			
			station.visited = false;
			station.dist = Integer.MAX_VALUE;
		}
		
		s.dist = 0;
		
		boolean flag = true;
		while(flag){
			Station v = null;
			Station w;
			for (Station station : map.keySet()) {
				if(station.visited == true)
					continue;
				if(v == null || station.dist < v.dist)
					v = station;
			}
			
			
			v.visited = true;
			
			
			List<Edge> list = map.get(v);
			for (int i = 0; i < list.size(); i++) {
				w = list.get(i).station;
				if(!w.visited){
					int d = list.get(i).distance;
					
					
					if(v.dist + d < w.dist){
						w.dist = v.dist + d;
						w.path = v;
					}
				}
			}
			
			
			Iterator<Station> iterator = map.keySet().iterator();
			while(iterator.hasNext()){
				if(!iterator.next().visited){
					flag = true;
					break;
				}
			}
			if(!iterator.hasNext())
				flag = false;
		}
	}
	
	
	public static void main(String[] args) {
		
		SubwayMap2_0 m = new SubwayMap2_0();
		
		Scanner s = new Scanner(System.in);
		String input="";
		while(true) {
			input=s.next();
		  
		if(input.equals("-a")) {
		System.out.println("请输入线路名称:");
		String  line = s.next();
		m.ShowStation(line);
		}
		else if(input.equals("-b")) {
		System.out.println("请输入起点:");
		String start = s.next();
		System.out.println("请输入终点:");
		String end = s.next();
		System.out.println("最少站数路线生成:");
		m.shortestPath(start, end);
		}
		else if(input.equals("-q")) {
		System.out.println("退出程序");
		break;
		}
		else 
		System.out.println("请输入正确命令:");
		
		
	}
	}
	
}
