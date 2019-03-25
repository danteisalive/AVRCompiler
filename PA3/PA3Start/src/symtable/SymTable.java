package symtable;
import java.util.*;
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
      System.out.println("Creating a symbol table. Initiating a global scope...");
      mScopeStack.push(mGlobalScope);
    }


    public STE lookupInnermost(String sym){

      Scope currentScope = mScopeStack.peek();
      return currentScope.lookupInnermost(sym);

    }

    public void insert(STE ste) {

        System.out.println("in SymTable.insert(" + ste.mName + ") ...");
    	//	System.out.println("insert " + ste.mName);
    		if(mScopeStack.peek() != null){
    			   mScopeStack.peek().insert(ste);
    		} else {
    			   throw new SemanticException("Scope stack null.");
    		}

    }

    public STE lookup(String id){
    	System.out.println("in SymTable.lookup(" + id + ") ...");
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
            	System.out.println("found " + t + " ste " + id);
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
       // actually only need to look up the top of the scope since we just inserted the STE
       System.out.println("in SymTable.pushScope(" + id + ") ...");
       STE ste = lookupInnermost(id);

       if(ste instanceof ClassSTE){
         mScopeStack.push(((ClassSTE)ste).getScope());
         System.out.println("pushed a class scope onto stack");
       } else if (ste instanceof MethodSTE){
         mScopeStack.push(((MethodSTE)ste).getScope());
         System.out.println("pushed a method scope onto stack");
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

}
