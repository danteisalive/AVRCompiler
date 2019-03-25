package symtable;
import java.util.*;

import exceptions.SemanticException;
import exceptions.InternalException;
import java.io.PrintWriter;

public class Scope {

	public final HashMap<String,STE> mDict = new HashMap<String,STE>();
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

		System.out.println("in Scope(" + scopeType + ")" + ".lookup(" + steName + ") ...");

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
		System.out.println("in Scope(" + scopeType + ")" + "insert(" + ste.getSTEName() + ") ...");
		mDict.put(ste.getSTEName(), ste);
	}

	public String getScopeType(){
		return scopeType;
	}



	public String toString(){
		String s = scopeType;
		return s;
	}

	public int numOfSTEs(){
		return mDict.size();
	}

	public void printSTEs(PrintWriter STout, Integer lv){


		Iterator it = mDict.entrySet().iterator();
		String scopeString = new String();
		scopeString += "  " + lv.toString();
		scopeString += " [label=\" <f0> Scope";
		Integer f = 1;
		while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				scopeString += " | <f" + f.toString() + "> mDict\\[" + pair.getKey() + "\\]";
				f += 1;
		}
		scopeString += " \"];";
		STout.println(scopeString);
		STout.flush();


		/////////////////////////////////////////////////////////////////////
		it = mDict.entrySet().iterator();
		f = 1;
		while (it.hasNext()){

				Map.Entry pair = (Map.Entry)it.next();

				if(pair.getValue() instanceof ClassSTE)
				{
					scopeString = "  ";
					scopeString += lv.toString() + ":";
					blocks += 1;
					scopeString += "<f" + f.toString() + "> -> " + blocks.toString() + ":<f0>;";
					STout.println(scopeString);
					STout.flush();

					scopeString = "  ";
					scopeString += blocks.toString();
					scopeString += " [label=\"" + pair.getValue().toString() + "\"];";
					STout.println(scopeString);
					STout.flush();

					blocks += 1;
					Integer newLv = lv + 1;

					scopeString = "  ";
					scopeString += newLv.toString();
					scopeString += ":<f4> -> " + blocks.toString() + ":<f0>;";
					STout.println(scopeString);
					STout.flush();

					((ClassSTE)pair.getValue()).getScope().printSTEs(STout, newLv + 1);
				}
				else if (pair.getValue() instanceof MethodSTE)
				{
					scopeString = "  ";
					scopeString += lv.toString() + ":";
					blocks += 1;
					scopeString += "<f" + f.toString() + "> -> " + blocks.toString() + ":<f0>;";
					STout.println(scopeString);
					STout.flush();

					scopeString = "  ";
					scopeString += blocks.toString();
					scopeString += " [label=\"" + pair.getValue().toString() + "\"];";
					STout.println(scopeString);
					STout.flush();

					blocks += 1;
					Integer newLv = lv + 1;

					scopeString = "  ";
					scopeString += newLv.toString();
					scopeString += ":<f3> -> " + blocks.toString() + ":<f0>;";
					STout.println(scopeString);
					STout.flush();

					((MethodSTE)pair.getValue()).getScope().printSTEs(STout, newLv + 1);
				}
				else if (pair.getValue() instanceof VarSTE)
				{

					blocks += 1;

					scopeString = "  ";
					scopeString += lv.toString() + ":<f" + f.toString() + "> -> " + blocks.toString() + ":<f0>;";
					STout.println(scopeString);
					STout.flush();

					scopeString = "  ";
					scopeString += blocks.toString();
					scopeString += " [label=\"" + pair.getValue().toString() + "\"];";
					STout.println(scopeString);
					STout.flush();

				}
				else
				{
					throw new InternalException("Illegal STE!");
				}

				f += 1;
		}



	}

}
