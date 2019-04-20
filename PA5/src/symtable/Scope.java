package symtable;
import java.util.*;

import exceptions.SemanticException;
import exceptions.InternalException;
import java.io.PrintWriter;

public class Scope {

	public HashMap<String,STE> mDict = new HashMap<String,STE>();
	public Scope mEnclosing; // parent scope
	public String scopeType; // for debugging: global, class, method

	public static Integer blocks = 0;

	public Scope(Scope mEnclosing) {
		this.mEnclosing = mEnclosing;
	}
	public Scope(String scopeType, Scope mEnclosing){
		this.scopeType = scopeType;
		this.mEnclosing = mEnclosing;
	}


	public STE lookupInnermost (String steName){

		//System.out.println("in Scope(" + scopeType + ")" + ".lookup(" + steName + ") ...");

		if(mDict.containsKey(steName)){
			return mDict.get(steName);
		}
		else {
			return null;
		}
	}

	public void insert(STE ste){
		// check if if it's var type
		if(ste instanceof VarSTE){
			// if the var is defined in current scope
			if(mDict.containsKey(ste.getSTEName())){
				throw new SemanticException("Variable " + ste.getSTEName() + "has already been defined");
			}
		}
		//System.out.println("in Scope(" + scopeType + ")" + "insert(" + ste.getSTEName() + ") ...");
		mDict.put(ste.getSTEName(), ste);
	}

	public String getScopeType(){
		return scopeType;
	}

	public HashMap<String,STE> getHashMap(){
		return mDict;
	}


	public String toString(){
		String s = scopeType;
		return s;
	}

	public int numOfSTEs(){
		return mDict.size();
	}

}
