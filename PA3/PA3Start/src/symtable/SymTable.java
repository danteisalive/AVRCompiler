package symtable;
import java.util.*;
import java.io.PrintWriter;
import ast.node.*;
import java.util.Stack;
import java.util.Hashtable;
import exceptions.InternalException;
import exceptions.SemanticException;
/**
 * SymTable
 * ....
 * The symbol table also keeps a mapping of expression nodes to
 * types because that information is needed elsewhere especially
 * when looking up method call information.
 *
 * @author mstrout
 * WB: Simplified to only expression types
 */
public class SymTable {
    private final HashMap<Node,Type> mExpType = new HashMap<Node,Type>();
    private final Stack<Scope> mScopeStack = new Stack<Scope>();
    private final Scope mGlobalScope = new Scope("Global", null);

    public SymTable()
    {
      mScopeStack.push(mGlobalScope);
    }


    public STE lookupInnermost(String sym){

      Scope currentScope = mScopeStack.peek();
      return currentScope.lookupInnermost(sym);

    }

    public void insert(STE ste) {

    		if(mScopeStack.peek() != null){
    			   mScopeStack.peek().insert(ste);
    		} else {
    			   throw new SemanticException("Scope stack null.");
    		}

    }

    public STE lookup(String id){

    	Stack<Scope> copyStack = (Stack<Scope>) mScopeStack.clone();
        STE ste = null;
        while (!copyStack.isEmpty()){
            ste = lookupInnermost(id);
            if (ste != null){
            	String t = "";
            	if(ste instanceof MethodSTE){
            		t = "method";
            	} else if (ste instanceof ClassSTE){
            		t = "class";
            	} else {
            		t = "var";
            	}
                break;
            }
            mScopeStack.pop();
        }
        return ste;
    }
    /**
    * Lookup the given method scope and make it the innermost
    * scope.  That is, make it the top of the scope stack.
    */
    public void pushScope(String id) {

       STE ste = lookupInnermost(id);

       if(ste instanceof ClassSTE){
         mScopeStack.push(((ClassSTE)ste).getScope());

       } else if (ste instanceof MethodSTE){
         mScopeStack.push(((MethodSTE)ste).getScope());

       } else {
         throw new SemanticException("Did not find scope with id: " + id + " in SymTable.pushScope");
       }
    }

    public void setExpType(Node exp, Type t)
    {
    	this.mExpType.put(exp, t);
    }

    public Type getExpType(Node exp)
    {
    	return this.mExpType.get(exp);
    }

    public void popScope() {
    		mScopeStack.pop();
    }

    public Scope peekScopeStack(){
    		return mScopeStack.peek();
    }
    public Stack<Scope> getStackScope(){
        return mScopeStack;
    }

    public Scope getGlobalScope(){
        return mGlobalScope;
    }

    public void printSymTable(PrintWriter out, Scope sc){

       boolean flag = false;
       HashMap<String,STE> tempHashMap = new HashMap(sc.getHashMap());
       List<String> keyList = new ArrayList(tempHashMap.keySet());
       Collections.sort(keyList);

       for (String scopeName : keyList) {
 		      STE s = tempHashMap.get(scopeName);
       		if(s instanceof ClassSTE) {
       			if(!flag) {
       				out.print("Classes in global scope:");
       				flag = true;
       			}
       			out.print(" "+ s.getSTEName());
       		}
 	    }

     	if(flag) {
     		out.print("\n");
     		flag = false;
     	}

     	for (String scopeName : keyList) {
     		STE s = tempHashMap.get(scopeName);
     		if(s instanceof VarSTE) {
     			if(!flag) {
     				out.print("Vars in current scope:");
     				flag = true;
     			}
     			out.print(" "+ s.getSTEName() + ":"+ ((VarSTE)s).getSTEType().toString());
     		}
     	}

     	if(flag) {
     		out.print("\n");
     		flag = false;
     	}

     	for (String scopeName : keyList) {
       		STE s = tempHashMap.get(scopeName);
       		if(s instanceof MethodSTE) {
       			if(!flag) {
       				out.print("Methods in current scope:");
       				flag = true;
       			}
       			out.print(" "+ s.getSTEName());
       		}
     	}

     	if(flag) {
     		out.print("\n");
     	}

     	for (String scopeName : keyList) {
       		STE s = tempHashMap.get(scopeName);
       		if(s instanceof ClassSTE)
          {
       			out.println("\nIn class "+ s.getSTEName() +" scope");
       			this.printSymTable(out, ((ClassSTE)s).getScope());
       		}
          else if(s instanceof MethodSTE)
          {
       			out.println("In method "+ s.getSTEName() +" scope");
       			//print method signature here
       			//String formals; //add logic to populate formals
       			//String retType; //add logic to populate return type
       			out.println("Method Signature: " + ((MethodSTE)s).getSignature().toString());
       			this.printSymTable(out, ((MethodSTE)s).getScope());
       		}
     	}

    }
}
