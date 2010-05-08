package fastInfoDB;

import java.io.Serializable;
import java.util.HashMap;

import org.neo4j.graphdb.Relationship;

public class InstanceInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public HashMap<Long, Long> interHopMap;
	private long[] accesses; 
	
	public enum InfoKey{
		InterHop, IntraHop, NumNodes, NumRelas, Traffic,TRANSACTION,SHUTDOWN, RS_DELETE, N_DELETE, RS_CREATE, N_CREATE, GETALLNODES, NODE_BYID, REL_BYID, N_GETID, GetRels,HasREL, N_GetProp, RS_GetProp, RS_GetID, N_HasProp, RS_HasProp, N_RemProp, RS_RemProp, N_SetProp, RS_SetProp, RS_GETNODE, RS_GetType, RS_IsType
	}
	
	public InstanceInfo() {
		this.interHopMap = new HashMap<Long, Long>();
		this.accesses = new long[InfoKey.values().length] ;
		for(int i=0; i<InfoKey.values().length ; i++){
			accesses[i]=0;
		}
	}
	
	public long getValue(InfoKey key){
		switch (key) {
		case Traffic:
			long sum = 0;
			for(int i = InfoKey.RS_DELETE.ordinal(); i < accesses.length; i++){
				sum+=accesses[i];
			}
			return sum + accesses[InfoKey.Traffic.ordinal()];

		default:
			break;
		}
		return accesses[key.ordinal()]++;
	}
	
	public void log( InfoKey key ){
		accesses[key.ordinal()]++;
	}
	
	// TODO finish the interrela count
	public void logHop(Relationship rs){
		
		if(rs.hasProperty("_isGhost") || rs.hasProperty("_isHalf")){
			// interhop on partitioned db
			accesses[InfoKey.InterHop.ordinal()]++;
		}else if(true) {
			// interhop on normal database 
			accesses[InfoKey.InterHop.ordinal()]++;
		}else {
			accesses[InfoKey.IntraHop.ordinal()]++;
		}
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
