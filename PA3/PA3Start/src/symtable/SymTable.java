package symtable;
import java.util.*;
import ast.node.*;
import java.util.Stack;
import java.util.Hashtable;
import exceptions.InternalException;

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
    private Stack<Scope> mScopeStack = new Stack<Scope>();

    public SymTable() {
    }


    public STE lookup(String sym) {
        return null;
    }

    public STE lookupInnermost(String sym){

      Scope currentScope = mScopeStack.peek();
      return currentScope.lookupInnermost(sym);

    }

    public void insert(STE ste) {

    }

    public void pushScope(String id){

    }

    public void popScope(){

    }

    public void setExpType(Node exp, Type t)
    {
    	this.mExpType.put(exp, t);
    }

    public Type getExpType(Node exp)
    {
    	return this.mExpType.get(exp);
    }

/*
 */

}
