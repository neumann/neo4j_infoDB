package infoDB;

import java.io.Serializable;
import java.util.HashMap;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class InstanceInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public HashMap<Long, Long> interHopMap;
	private long[] accesses; 
	
	public enum InfoKey{
		InterHop, IntraHop, NumNodes, NumRelas, Traffic, rs_delete, n_delete, rs_create, n_create
	}
	
	public InstanceInfo() {
		this.interHopMap = new HashMap<Long, Long>();
		this.accesses = new long[InfoKey.values().length] ;
		for(int i=0; i<InfoKey.values().length ; i++){
			accesses[i]=0;
		}
	}
	
	public long getValue(InfoKey key){
		return accesses[key.ordinal()]++;
	}
	
	public void log( InfoKey key ){
		accesses[key.ordinal()]++;
		switch (key) {
		case rs_create:
			accesses[InfoKey.Traffic.ordinal()]++;
			accesses[InfoKey.NumRelas.ordinal()]++;
			return;
		case rs_delete:
			accesses[InfoKey.Traffic.ordinal()]++;
			accesses[InfoKey.NumRelas.ordinal()]--;
			return;
		case n_create:
			accesses[InfoKey.Traffic.ordinal()]++;
			accesses[InfoKey.NumNodes.ordinal()]++;
			return;
		case n_delete:
			accesses[InfoKey.Traffic.ordinal()]++;
			accesses[InfoKey.NumNodes.ordinal()]--;
			return;
		default:
			break;
		}
	}

	public void logHop(Relationship rs){
		
		if(rs.hasProperty("_isGhost") || rs.hasProperty("_isHalf")){
			// interhop on partitioned db
			accesses[InfoKey.InterHop.ordinal()]++;
			return;
		}			
		
		Node[] nodes = rs.getNodes();
		if(nodes[0].hasProperty("_color")) {
			byte c1 = (Byte) nodes[0].getProperty("_color");
			byte c2 = (Byte) nodes[1].getProperty("_color");
		
			if(c1!=c2){
				accesses[InfoKey.InterHop.ordinal()]++;
				return;
			}
		}
			
		// normal hop
		accesses[InfoKey.IntraHop.ordinal()]++;
		
	}
	
	public void resetTraffic(){
		accesses[InfoKey.Traffic.ordinal()] = 0;
		accesses[InfoKey.InterHop.ordinal()] = 0;
		accesses[InfoKey.IntraHop.ordinal()] = 0;
		this.interHopMap = new HashMap<Long, Long>();
	}
	
	public String toString(){
		String res = "{";
		for (InfoKey k : InfoKey.values()) {
			res+="("+k.name() +" = " + accesses[k.ordinal()]+ ") ";
		}
		res+="}"+ interHopMap;
		return res;
	}
	
	public InstanceInfo differenceTo(InstanceInfo info){
		InstanceInfo res = info.takeSnapshot();
		for(long id: this.interHopMap.keySet()){
			long val = this.interHopMap.get(id);
			if(res.interHopMap.containsKey(id)){
				val = res.interHopMap.get(id) - val;
			}else{
				val = -val;
			}
			res.interHopMap.put(id, val);
		}
		for(int i = 0; i < accesses.length; i++ ){
			res.accesses[i] -=this.accesses[i];
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	public InstanceInfo takeSnapshot(){
		InstanceInfo clone = new InstanceInfo();
		clone.interHopMap = (HashMap<Long, Long>) this.interHopMap.clone();
		clone.accesses = accesses.clone();
		return clone;
	}
}
